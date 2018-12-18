package producer.transaction.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import common.ProduceAckType;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import producer.normal.impl.NormalProducer;
import producer.transaction.ITransactionalProducer;
import producer.transaction.config.TransactionalProducerConfig;

import java.util.Map;

/**
 * @author sunding
 * 	事务生产者
 */
public class TransactionalProducer extends NormalProducer implements ITransactionalProducer {


	public TransactionalProducer(TransactionalProducerConfig config) {
		//1、参数校验
		this.checkProducerConfig(config);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(config.getTransactionalId()),
				"TransactionalId must not be null !");
		//2、初始化生产者配置文件
		this.convertConfigToProperties(config);
		//3、事务生产者特殊配置信息
		this.setTransactionalProperties(config);
		//4、实例化生产者
		this.buildKafkaProducer();

	}

	@Override
	public void initTransactions() {
		this.kafkaProducer.initTransactions();
	}

	@Override
	public void beginTransaction() throws ProducerFencedException {
		this.kafkaProducer.beginTransaction();
	}

	@Override
	public void commitTransaction() throws ProducerFencedException {
		this.kafkaProducer.commitTransaction();
	}

	@Override
	public void abortTransaction() throws ProducerFencedException {
		this.kafkaProducer.abortTransaction();
	}

	@Override
	public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId)
			throws ProducerFencedException {
		this.kafkaProducer.sendOffsetsToTransaction(offsets, consumerGroupId);
	}

	/**
	 * 事务生产者需要的特殊属性值
	 * @param config
	 */
	private void setTransactionalProperties(TransactionalProducerConfig config) {
		this.properties.setProperty("transactional.id", config.getTransactionalId());
		this.properties.setProperty("enable.idempotence", "true");
		this.properties.setProperty("acks", ProduceAckType.ALL.acks());
	}
}
