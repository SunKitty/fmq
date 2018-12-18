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

	SendResult sendSync(String topic, ProducerMessage message);

	FutureSendResult sendAsync(String topic, ProducerMessage message);

	void sendAsync(String topic, ProducerMessage message, ISendCallBack callBack);

	List<PartitionInfo> partitionsFor(String topic);

	Map<MetricName, ? extends Metric> metrics();

	void close(long times, TimeUnit unit);

	void flush();

}
