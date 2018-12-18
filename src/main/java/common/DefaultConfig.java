package common;

/**
 * @author sunding
 */
public interface DefaultConfig {

	//生产者默认配置
	String acks = ProduceAckType.ALL.acks();

	String maxInFlightRequestsPerConnection = "1";

	String maxBlockMs = "10000";

	//消费者默认配置
}
