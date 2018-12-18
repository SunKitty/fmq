package consumer;

import consumer.normal.config.NormalConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author sunding
 */
public interface IMessageFilter {

	boolean shouldIgnore(NormalConsumerConfig config, ConsumerRecord<String, byte[]> record);
}
