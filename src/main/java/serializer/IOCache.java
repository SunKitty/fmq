package serializer;

import com.caucho.hessian.io.SerializerFactory;

/**
 * @author sunding
 */
public interface IOCache {

	SerializerFactory HESSIAN_SERIALIZERFACTORY = new SerializerFactory();
}
