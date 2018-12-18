package producer.normal.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import common.DefaultConfig;
import common.IProperty;
import common.ProduceAckType;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import producer.FutureSendResult;
import producer.ISendCallBack;
import producer.ProducerMessage;
import producer.SendResult;
import producer.normal.IProducer;
import producer.normal.config.NormalProducerConfig;
import serializer.ISerializer;
import util.ClassUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author sunding
 * 普通生产者，不支持事务
 */
public class NormalProducer implements IProducer {

	protected KafkaProducer        kafkaProducer;
	protected ISerializer          serializer;
	protected Properties           properties;

	public NormalProducer(NormalProducerConfig producerConfig) {
		//1、参数检验
		this.checkProducerConfig(producerConfig);
		//2、初始化生产者配置文件
		this.convertConfigToProperties(producerConfig);
		//3、实例化生产者
		this.buildKafkaProducer();
	}

	public NormalProducer() {}

	/**
	 * 检验配置文件信息
	 * @param producerConfig
	 */
	protected void checkProducerConfig(NormalProducerConfig producerConfig) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(producerConfig.getServerAdds()), "kafka server must not be null !");
		Preconditions.checkArgument(producerConfig.getSerializer() != null, "serializer must not be null !");
	}

	/**
	 * 将配置对象转换为配置文件
	 * @param producerConfig
	 */
	protected void convertConfigToProperties(NormalProducerConfig producerConfig) {

		if (producerConfig.getMoreConfig() != null) {
			this.properties = producerConfig.getMoreConfig();
		} else {
			this.properties = new Properties();
		}
		this.serializer = producerConfig.getSerializer();
		this.properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		this.properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		this.properties.setProperty("bootstrap.servers", producerConfig.getServerAdds());
		this.properties.setProperty("client.id", producerConfig.getClientId());
		this.properties.setProperty("max.block.ms", producerConfig.getMaxBlockMs() == 0 ?
				DefaultConfig.maxBlockMs : String.valueOf(producerConfig.getMaxBlockMs()));
		this.properties.setProperty("acks", producerConfig.getAck() == null ?
				DefaultConfig.acks : producerConfig.getAck().acks());
		this.properties.setProperty("max.in.flight.requests.per.connection",
				producerConfig.getMaxInFlightRequestsPerConnection() == 0 ?
						DefaultConfig.maxInFlightRequestsPerConnection :
						String.valueOf(producerConfig.getMaxInFlightRequestsPerConnection()));
		//生产者具有幂等性
		if (producerConfig.isIdempotent()) {
			this.properties.setProperty("enable.idempotence", "true");
			this.properties.setProperty("max.in.flight.requests.per.connection", "1");
			this.properties.setProperty("acks", ProduceAckType.ALL.acks());
		}
	}

	/**
	 * 根据配置文件实例化生产者
	 */
	protected void buildKafkaProducer() {
		this.kafkaProducer = new KafkaProducer(properties);
	}

	@Override
	public SendResult sendSync(String topic, ProducerMessage message) {
		try {
			FutureSendResult future = this.sendAsync(topic, message);
			return future.get();
		} catch (Exception e) {
			return SendResult.newFailResult(e);
		}
	}

	@Override
	public FutureSendResult sendAsync(String topic, ProducerMessage message) {
		ProducerRecord record = this.convertToRecord(topic, message);
		return new FutureSendResult(this.kafkaProducer.send(record));
	}

	@Override
	public void sendAsync(String topic, final ProducerMessage message, final ISendCallBack callBack) {
		ProducerRecord record = this.convertToRecord(topic, message);
		this.kafkaProducer.send(record, new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if (exception != null) {
					callBack.onCompletion(SendResult.newFailResult(exception));
				} else {
					callBack.onCompletion(SendResult.newSuccessResult(metadata.topic(), metadata.partition(),
							metadata.offset(), metadata.serializedValueSize()));
				}
			}
		});
	}

	@Override
	public List<PartitionInfo> partitionsFor(String topic) {
		return this.kafkaProducer.partitionsFor(topic);
	}

	@Override
	public Map<MetricName, ? extends Metric> metrics() {
		return this.kafkaProducer.metrics();
	}

	@Override
	public void close(long times, TimeUnit unit) {
		this.flush();
		this.kafkaProducer.close(times, unit);
	}

	@Override
	public void flush() {
		this.kafkaProducer.flush();
	}

	@Override
	public void close() throws IOException {
		this.flush();
		this.kafkaProducer.close();
	}

	/**
	 * Topic、Message转换为ProducerRecord
	 * @param topic
	 * @param message
	 * @return
	 */
	private ProducerRecord<String, byte[]> convertToRecord(String topic, ProducerMessage message) {
		byte[] content = this.serializeContent(message);
		ProducerRecord record = new ProducerRecord(topic, message.getPartition(),
				Long.valueOf(System.currentTimeMillis()), message.getPartitionKey(), content);
		if(message.hasProperty()) {
			Headers headers = record.headers();
			Map.Entry entry;
			byte[] serializedValue;
			for(Iterator iterator = message.getProperties().entrySet().iterator(); iterator.hasNext();
				headers.add(new RecordHeader((String)entry.getKey(), serializedValue))) {
				entry = (Map.Entry)iterator.next();
				Object value = entry.getValue();
				if(value instanceof byte[]) {
					serializedValue = ClassUtil.castFrom(value);
				} else {
					serializedValue = IProperty.SERIALIZER.serialize(entry.getValue());
				}
			}
		}
		return record;
	}

	/**
	 *  获取序列化内容
	 * @param message
	 * @return
	 */
	private byte[] serializeContent(ProducerMessage message) {
		return message.getSerializedContent() != null ? message.getSerializedContent() :
				(message.getContent() instanceof byte[] ? (byte[]) ClassUtil.castFrom(message.getContent()) :
						this.serializer.serialize(message.getContent()));
	}
}
