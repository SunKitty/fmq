package consumer.normal;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author sunding
 *  消费者API
 */
public interface IConsumer {

	/**
	 * 订阅单个Topic
	 * @param topic
	 */
	void subscribe(String topic);

	/**
	 * 订阅多个Topic
	 * @param topics
	 */
	void subscribe(String... topics);

	/**
	 * 订阅多个Topic
	 * @param topics
	 */
	void subscribe(Collection<String> topics);

	void subscribe(String topic, ConsumerRebalanceListener listener);

	void subscribe(Collection<String> topics, ConsumerRebalanceListener listener);

	void subscribe(Pattern pattern);

	ConsumerRecords<String, byte[]> poll(long timeout);

	void commitSync(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata);

	void commitSync(TopicPartition topicPartition, long offset);
	
	void commitSync(String topic, int partition, OffsetAndMetadata offsetAndMetadata);

	void commitSync(String topic, int partition, long offset);

	void commitAsync(String topic, int partition, long offset);

	void commitAsync(String topic, int partition, long offset, OffsetCommitCallback commitCallback);

	void commitAsync(String topic, int partition, OffsetAndMetadata offsetAndMetadata);

	void commitAsync(String topic, int partition, OffsetAndMetadata offsetAndMetadata, OffsetCommitCallback commitCallback);
	
	void commitAsync(TopicPartition topicPartition, long offset);

	void commitAsync(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata);

	void commitAsync(TopicPartition topicPartition, long offset, OffsetCommitCallback commitCallback);

	void commitAsync(Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback);

	void commitAsync(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata, OffsetCommitCallback commitCallback);

	void close();

	void close(long timeout, TimeUnit timeUnit);
}
