package producer.normal;

import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import producer.FutureSendResult;
import producer.ISendCallBack;
import producer.ProducerMessage;
import producer.SendResult;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sunding
 */
public interface IProducer extends Closeable{

	/**
	 * 同步发送消息
	 * @param topic 主题
	 * @param message 消息体
     * @return 同步结果
     */
	SendResult sendSync(String topic, ProducerMessage message);

	/**
	 * 异步发送消息
	 * @param topic 主题
	 * @param message 消息体
     * @return 异步结果，相对于Future
     */
	FutureSendResult sendAsync(String topic, ProducerMessage message);

	/**
	 * 异步发送消息
	 * @param topic 主题
	 * @param message 消息体
	 * @param callBack 回调函数，可以用来处理发送完成之后的业务逻辑
     */
	void sendAsync(String topic, ProducerMessage message, ISendCallBack callBack);

	/**
	 *	获取该Topic分区信息
	 * @param topic 主题
	 * @return
     */
	List<PartitionInfo> partitionsFor(String topic);

	Map<MetricName, ? extends Metric> metrics();

	void close(long times, TimeUnit unit);

	void flush();

}
