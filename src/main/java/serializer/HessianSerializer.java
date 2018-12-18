package serializer;

import com.caucho.hessian.io.Hessian2Output;
import exception.SerializationException;

import java.io.ByteArrayOutputStream;

/**
 * @author sunding
 *  Hessian序列化
 */
public class HessianSerializer implements ISerializer, IOCache {

	public static HessianSerializer INSTANCE = new HessianSerializer();

	public HessianSerializer() {

	}

	@Override
	public byte[] serialize(Object o) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Hessian2Output ho = new Hessian2Output();
			ho.setSerializerFactory(HESSIAN_SERIALIZERFACTORY);
			ho.init(stream);
			ho.writeObject(o);
			ho.flush();
			return stream.toByteArray();
		} catch (Exception e) {
			throw  new SerializationException(o, e);
		}
	}
}
