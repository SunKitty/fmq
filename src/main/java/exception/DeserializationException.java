package exception;

/**
 * @author sunding
 *  反序列化异常
 */
public class DeserializationException extends RuntimeException {

	public DeserializationException(byte[] content, Throwable throwable) {
		super(buildByteArrayString(content), throwable);
	}

	static String buildByteArrayString(byte[] byteArray) {
		if(byteArray == null) {
			return null;
		} else if(byteArray.length > 1024) {
			return byteArray.toString();
		} else {
			StringBuilder sb = new StringBuilder("[");

			for(int i = 0; i < byteArray.length; ++i) {
				sb.append(byteArray[i]);
				if(i != byteArray.length - 1) {
					sb.append(",");
				}
			}

			sb.append("]");
			return sb.toString();
		}
	}
}
