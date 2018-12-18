package producer;

import producer.normal.IProducer;
import producer.normal.config.NormalProducerConfig;
import producer.normal.impl.NormalProducer;
import serializer.HessianSerializer;

/**
 * 生产者测试类
 * @author sunding
 */
public class NormalProducerTest {

	private static final String TOPIC = "TEST";
	private static final String IP = "192.168.50.75:9092";

	public static void main(String[] args) {
		NormalProducerConfig producerConfig = new NormalProducerConfig()
				.serverAdds(IP)
				.serializer(HessianSerializer.INSTANCE)
				.idempotent(true);
		IProducer producer = new NormalProducer(producerConfig);
		SendResult result = testSync(producer);
		if (result.isSuccessful()) {
			System.out.println("success");
		} else {
			System.out.println("fail");
			System.out.println(result.getException());
			result.getException().printStackTrace();
		}
	}

	public static SendResult testSync(IProducer producer) {

		return producer.sendSync(TOPIC, new ProducerMessage("hello my fmq"));

	}
}
