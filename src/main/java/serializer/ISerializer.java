package serializer;

import exception.SerializationException;

/**
 * @author sunding
 *  序列化
 */
public interface ISerializer {

	/**
	 * 序列化
	 * @param o
	 * @return
	 * @throws SerializationException
	 */
	byte[] serialize(Object o) throws SerializationException;
}
