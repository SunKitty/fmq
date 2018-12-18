package producer.transaction.config;

import producer.normal.config.NormalProducerConfig;

/**
 * @author sunding
 * 支持事务的生产者配置
 */
public class TransactionalProducerConfig extends NormalProducerConfig {

	/**
	 * 事务ID
	 */
	private String transactionalId;

	public TransactionalProducerConfig transactionalId(String transactionalId) {
		this.transactionalId = transactionalId;
		return this;
	}

	public String getTransactionalId() {
		return transactionalId;
	}

	public void setTransactionalId(String transactionalId) {
		this.transactionalId = transactionalId;
	}
}
