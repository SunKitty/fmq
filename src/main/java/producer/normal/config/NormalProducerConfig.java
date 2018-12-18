package producer.normal.config;

import common.ProduceAckType;
import serializer.HessianSerializer;
import serializer.ISerializer;

import java.util.Properties;

/**
 * @author sunding
 *
 * 生产者基本配置信息
 */
public class NormalProducerConfig {

	/** Broker地址 ，多个地址用;隔开 */
	private String serverAdds;
	/** 跟踪记录消息的标识，不能重复 */
	private String clientId = "";
	/** 确认状态 */
	private ProduceAckType ack;
	/** 在阻塞之前，客户端在单个连接上发送的未确认请求的最大数量 */
	private long maxInFlightRequestsPerConnection;
	/** 生产者是否具有幂等性,默认具有幂等性 */
	private boolean idempotent = true;
	/** 允许最大的阻塞时间 */
	private long maxBlockMs;
	/** 序列化接口 */
	private ISerializer serializer = HessianSerializer.INSTANCE;
	/** 其他配置信息 */
	private Properties moreConfig;

	public NormalProducerConfig serverAdds(String serverAdds) {
		this.serverAdds = serverAdds;
		return this;
	}

	public NormalProducerConfig clientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public NormalProducerConfig acks(ProduceAckType ack) {
		this.ack = ack;
		return this;
	}

	public NormalProducerConfig maxInFlightRequestsPerConnection(long maxInFlightRequestsPerConnection) {
		this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
		return this;
	}

	public NormalProducerConfig idempotent(boolean idempotent) {
		this.idempotent = idempotent;
		return this;
	}

	public NormalProducerConfig maxBlockMs(long maxBlockMs) {
		this.maxBlockMs = maxBlockMs;
		return this;
	}

	public NormalProducerConfig serializer(ISerializer serializer) {
		this.serializer = serializer;
		return this;
	}

	public NormalProducerConfig moreConfig(Properties moreConfig) {
		this.moreConfig = moreConfig;
		return this;
	}

	public String getServerAdds() {
		return serverAdds;
	}

	public String getClientId() {
		return clientId;
	}

	public ProduceAckType getAck() {
		return ack;
	}

	public long getMaxInFlightRequestsPerConnection() {
		return maxInFlightRequestsPerConnection;
	}

	public boolean isIdempotent() {
		return idempotent;
	}

	public long getMaxBlockMs() {
		return maxBlockMs;
	}

	public ISerializer getSerializer() {
		return serializer;
	}

	public Properties getMoreConfig() {
		return moreConfig;
	}
}
