package producer.transaction;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import producer.normal.IProducer;

import java.util.Map;

/**
 * 事务生产者
 */
public interface ITransactionalProducer extends IProducer {

	void initTransactions();

	void beginTransaction() throws ProducerFencedException;

	void commitTransaction() throws ProducerFencedException;

	void abortTransaction() throws ProducerFencedException;

	void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId)
			throws ProducerFencedException;
}
