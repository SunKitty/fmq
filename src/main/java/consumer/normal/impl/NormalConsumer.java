package consumer.normal.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import common.IProperty;
import consumer.normal.IConsumer;
import consumer.IMessageFilter;
import consumer.normal.config.NormalConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.util.CollectionUtils;
import serializer.IDeserializer;
import util.CollectionUtil;
import util.ConsumerRecordUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author sunding
 * 普通消费者
 */
public class NormalConsumer implements IConsumer {

	private Properties                    properties;
	private KafkaConsumer<String, byte[]> kafkaConsumer;
	private NormalConsumerConfig 	      config;
	private static final IMessageFilter INNER_FILTER = new IMessageFilter() {
		@Override
		public boolean shouldIgnore(NormalConsumerConfig config, ConsumerRecord<String, byte[]> record) {
			if(record == null) {
				return true;
			} else {
				String targetGroup = ConsumerRecordUtil.getPropertyValue(record, IProperty.TO_GROUP);
				String targetSite = ConsumerRecordUtil.getPropertyValue(record, IProperty.TO_SITE);
				String currentSite = config.getCurrentSite();
				String group = config.getGroupId();
				if(!Strings.isNullOrEmpty(targetSite) && !targetSite.equalsIgnoreCase(currentSite)) {
					return true;
				} else if(!Strings.isNullOrEmpty(targetGroup) && !targetGroup.equalsIgnoreCase(group)) {
					return true;
				} else {
					return false;
				}
			}
		}
	};

	public NormalConsumer(NormalConsumerConfig consumerConfig) {
		//1、参数检验
		this.checkProducerConfig(consumerConfig);
		//2、初始化消费者配置文件
		this.convertConfigToProperties(consumerConfig);
		//2、实例化消费者
		this.buildKafkaConsumer();
	}

	private void checkProducerConfig(NormalConsumerConfig consumerConfig) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(consumerConfig.getServerAddrs()),
				"kafka server must not be null !");
		Preconditions.checkArgument(consumerConfig.getDeserializer() != null, "serializer must not be null !");
	}

	private void convertConfigToProperties(NormalConsumerConfig consumerConfig) {
		this.config = consumerConfig;
		if (consumerConfig.getMoreConfig() != null) {
			this.properties = consumerConfig.getMoreConfig();
		} else {
			this.properties = new Properties();
		}
		this.properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		this.properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		if(config.getAutoOffsetReset() != null) {
			this.properties.setProperty("auto.offset.reset", config.getAutoOffsetReset().toString().toLowerCase());
		}
		this.properties.setProperty("bootstrap.servers", consumerConfig.getServerAddrs());
		this.properties.setProperty("client.id", consumerConfig.getClientId());
		this.properties.setProperty("isolation.level", consumerConfig.getIsolationLevel().name().toLowerCase());
		this.properties.setProperty("enable.auto.commit", Boolean.toString(consumerConfig.isAutoCommit()));
		this.properties.setProperty("auto.commit.interval.ms", Long.toString(consumerConfig.getAutoCommitInterval()));
		this.properties.setProperty("max.poll.records", Integer.toString(consumerConfig.getMaxPollRecords()));
		this.properties.setProperty("max.poll.interval.ms", Integer.toString(consumerConfig.getMaxPollIntervalMs()));
		if(consumerConfig.hasGroupIdTransformer()) {
			this.properties.setProperty("group.id", consumerConfig.getGroupIdTransformer()
					.transform(consumerConfig.getGroupId()));
		} else {
			this.properties.setProperty("group.id", consumerConfig.getGroupId());
		}
	}

	private void buildKafkaConsumer() {
		this.kafkaConsumer = new KafkaConsumer(this.properties);
	}

	@Override
	public void subscribe(String topic) {
		this.kafkaConsumer.subscribe((Collection) Arrays.asList(new String[]{topic}));
	}

	@Override
	public void subscribe(String... topics) {
		this.kafkaConsumer.subscribe((Collection)Arrays.asList(topics));
	}

	@Override
	public void subscribe(Collection<String> topics) {
		this.kafkaConsumer.subscribe(topics);
	}

	@Override
	public void subscribe(String topic, ConsumerRebalanceListener listener) {
		kafkaConsumer.subscribe((Collection) Arrays.asList(new String[]{topic}), listener);
	}

	@Override
	public void subscribe(Collection<String> topics, ConsumerRebalanceListener listener) {
		kafkaConsumer.subscribe(topics, listener);
	}

	@Override
	public void subscribe(Pattern pattern) {
		this.kafkaConsumer.subscribe(pattern, new NoOpConsumerRebalanceListener());
	}

	@Override
	public ConsumerRecords<String, byte[]> poll(long timeout) {
		return this.pollDeserialized(timeout, byte[].class);
	}

	private  <T> ConsumerRecords<String, T> pollDeserialized(long timeout, Class<T> type) {
		IDeserializer deserializer = this.getConsumerConfig().getDeserializer();
		ConsumerRecords records = this.kafkaConsumer.poll(timeout);
		boolean needDeserialize = !byte[].class.equals(type);
		if(ConsumerRecordUtil.isNull(records)) {
			return ConsumerRecords.empty();
		} else {
			IMessageFilter messageFilter = this.config.getMessageFilter();
			Map filteredRecoredMap = Maps.newHashMapWithExpectedSize(8);
			Iterator iterator = records.partitions().iterator();
			while (iterator.hasNext()) {
				TopicPartition partition = (TopicPartition) iterator.next();
				List filteredRecords = Lists.newArrayListWithCapacity(8);
				Iterator i = records.records(partition).iterator();
				while (true) {
					while (i.hasNext()) {
						ConsumerRecord record = (ConsumerRecord) i.next();
						boolean ignore = this.getConsumerConfig().isEnableInnerFilter()
								&& INNER_FILTER.shouldIgnore(this.getConsumerConfig(), record)
								|| this.getConsumerConfig().hasUserMessageFilter()
								&& messageFilter.shouldIgnore(this.getConsumerConfig(), record);
						if (ignore) {
							continue;
						} else if (needDeserialize) {
							filteredRecords.add(new ConsumerRecord(record.topic(), record.partition(),
									record.offset(), record.timestamp(), record.timestampType(),
									null, record.serializedKeySize(), record.serializedValueSize(), record.key(),
									deserializer.deserialize((byte[])record.value(), type), record.headers()));
						} else {
							filteredRecords.add(record);
						}
					}
					if (!CollectionUtils.isEmpty(filteredRecords)) {
						filteredRecoredMap.put(partition, filteredRecords);
					}
					break;
				}
			}
			return new ConsumerRecords(filteredRecoredMap);
		}
	}

	@Override
	public void commitSync(String topic, int partition, OffsetAndMetadata offsetAndMetadata) {
		this.commitSync(new TopicPartition(topic, partition), offsetAndMetadata);
	}

	@Override
	public void commitSync(String topic, int partition, long offset) {
		this.commitSync(new TopicPartition(topic, partition), offset);
	}

	@Override
	public void commitAsync(String topic, int partition, long offset) {
		this.commitAsync(new TopicPartition(topic, partition), offset);
	}

	@Override
	public void commitAsync(String topic, int partition, long offset, OffsetCommitCallback commitCallback) {
		this.commitAsync(new TopicPartition(topic, partition), offset, commitCallback);
	}

	@Override
	public void commitAsync(String topic, int partition, OffsetAndMetadata offsetAndMetadata) {
		this.commitAsync(new TopicPartition(topic, partition), offsetAndMetadata);
	}

	@Override
	public void commitAsync(String topic, int partition, OffsetAndMetadata offsetAndMetadata, OffsetCommitCallback commitCallback) {
		this.commitAsync(new TopicPartition(topic, partition), offsetAndMetadata, commitCallback);
	}

	@Override
	public void commitSync(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata) {
		Map committedOffset = CollectionUtil.newSingleMap(topicPartition, offsetAndMetadata);
		this.kafkaConsumer.commitSync(committedOffset);
	}

	@Override
	public void commitSync(TopicPartition topicPartition, long offset) {
		this.commitSync(topicPartition, new OffsetAndMetadata(offset));
	}

	@Override
	public void commitAsync(TopicPartition topicPartition, long offset) {
		this.commitAsync(topicPartition, offset, (OffsetCommitCallback)null);
	}

	@Override
	public void commitAsync(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata) {
		this.commitAsync(topicPartition, offsetAndMetadata, (OffsetCommitCallback)null);
	}

	@Override
	public void commitAsync(TopicPartition topicPartition, long offset, OffsetCommitCallback commitCallback) {
		this.commitAsync(topicPartition, new OffsetAndMetadata(offset), commitCallback);
	}

	@Override
	public void commitAsync(Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback) {
		this.kafkaConsumer.commitAsync(offsets, callback);
	}

	@Override
	public void commitAsync(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata, OffsetCommitCallback commitCallback) {
		this.commitAsync(CollectionUtil.newSingleMap(topicPartition, offsetAndMetadata), commitCallback);
	}

	@Override
	public void close() {
		this.kafkaConsumer.close();
	}

	@Override
	public void close(long timeout, TimeUnit timeUnit) {
		this.kafkaConsumer.close(timeout, timeUnit);
	}

	private NormalConsumerConfig getConsumerConfig() {
		return this.config;
	}
}
