package common;

/**
 * @author sunding
 */
public interface DefaultConfig {

	//生产者默认配置

	/**
	 * 此配置是表明当一次发送消息请求被认为完成时的确认值，即指producer需要多少个broker返回的确认信号。
	 * 0-不需要等待任何确认消息
	 * 1-至少等待leader写入消息
	 * all-leader及所有follower写入消息
	 */
	String acks = ProduceAckType.ALL.acks();

	/**
	 * 在阻塞之前，客户端在单个连接上发送的未确认请求的最大数量,如果大于1，则存在消息重排序的风险。
	 */
	String maxInFlightRequestsPerConnection = "1";

	/**
	 * 缓冲区已满或元数据不可用而被阻塞, producer.send()和partitionsFor()被阻塞的时间
	 */
	String maxBlockMs = "10000";

}
