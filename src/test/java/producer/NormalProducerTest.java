package producer;

import producer.normal.IProducer;
import producer.normal.config.NormalProducerConfig;
import producer.normal.impl.NormalProducer;
import serializer.HessianSerializer;

/**
 * Created by sundingding on 2018/12/12.
 */
public class NormalProducerTest {

	private static final String TOPIC = "xyz_financial_oa_apply_dev";
	private static final String IP = "192.168.50.75:9092";

	public static void main(String[] args) {
		NormalProducerConfig producerConfig = new NormalProducerConfig()
				.serverAdds(IP)
				.serializer(HessianSerializer.INSTANCE)
				.idempotent(true);
		IProducer producer = new NormalProducer(producerConfig);
//		SendResult result =
				testSync(producer);
//		if (result.isSuccessful()) {
//			System.out.println("success");
//		} else {
//			System.out.println("fail");
//			System.out.println(result.getException());
//			result.getException().printStackTrace();
//		}
	}

	public static void testSync(IProducer producer) {
		int count = 1;
		while(true) {
			for (int i=1; i<6; i++) {
				producer.sendSync(TOPIC, new ProducerMessage(count + " _ hello my fmq _" + i));
			}
			count++;
		}
	}
}
