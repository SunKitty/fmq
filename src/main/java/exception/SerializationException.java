package exception;

/**
 * @author sunding
 * 序列化异常
 */
public class SerializationException extends RuntimeException{

	public SerializationException(Object o, Throwable e) {
		super("Serializer Exception " + o, e);
	}

}
