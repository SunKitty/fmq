package serializer;

import com.caucho.hessian.io.Hessian2Input;
import exception.DeserializationException;

import java.io.ByteArrayInputStream;

/**
 * @author sunding
 *  反序列化
 */
public class HessianDeserializer implements IDeserializer, IOCache {

	public static HessianDeserializer INSATANCE = new HessianDeserializer();

	public HessianDeserializer() {

	}

	@Override
	public <T> T deserialize(byte[] content, Class<T> tClass) throws DeserializationException {
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(content);
			Hessian2Input hi = new Hessian2Input();
			hi.setSerializerFactory(HESSIAN_SERIALIZERFACTORY);
			hi.init(stream);
			return (T) (tClass != null ? hi.readObject(tClass) : hi.readObject());
		} catch (Exception e) {
			throw new DeserializationException(content, e);
		}
	}
}
