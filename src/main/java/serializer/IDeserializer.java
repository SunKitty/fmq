package serializer;

import exception.DeserializationException;

/**
 * @author sunding
 *  反序列化
 */
public interface IDeserializer {

	/**
	 * 反序列化
	 * @param content
	 * @param tClass
	 * @param <T>
	 * @return
	 * @throws DeserializationException
	 */
	<T> T deserialize(byte[] content, Class<T> tClass) throws DeserializationException;
}
