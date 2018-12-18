package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author sunding
 */
public interface IFailedMessageHandler {

	String TOPIC_FAILED_MESSAGE = "__failed_message";

	void report(String var1, ConsumerRecord<String, ?> var2, String var3);
}
