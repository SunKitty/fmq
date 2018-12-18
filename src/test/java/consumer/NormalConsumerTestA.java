package consumer;

import consumer.normal.IConsumer;
import consumer.normal.config.NormalConsumerConfig;
import consumer.normal.impl.NormalConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import serializer.HessianDeserializer;
import serializer.IDeserializer;

/**
 * @author sunding
 */
public class NormalConsumerTestA {

	private static final String        TOPIC        = "xyz_financial_oa_apply_dev";
	private static final String        IP           = "192.168.50.75:9092";
	private static IDeserializer DESERIALIZER = HessianDeserializer.INSATANCE;
	public static void main(String[] args) throws Exception{
		NormalConsumerConfig config = new NormalConsumerConfig().serverAddrs(IP)
																.clientId("xyz")
																.currentSite("XYZ_TEST")
																.groupId("sunding")
																.autoCommit(true);

		IConsumer consumer = new NormalConsumer(config);
		consumer.subscribe(TOPIC);
		while (true) {
			ConsumerRecords<String, byte[]> records = consumer.poll(100);
			for (ConsumerRecord<String, byte[]> record : records) {
				try {
					//获取消息体，不一定是String类型，可以是对象
					String message = DESERIALIZER.deserialize(record.value(), String.class);
					System.out.println("A:" + message);
				}
				catch (Exception e) {
					//以下两种失败处理都需要在config里配置IFailedMessageHandler
					//消息处理失败后，上报服务，以便及时补偿处理失败的消息
					//consumer.reportFailedMessage(record,e);
				}
				//如果autoCommit设为false，在这里手动提交offset，offset必须+1提交
//				consumer.commitSync(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
			}
		}
	}
}
