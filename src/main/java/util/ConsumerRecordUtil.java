package util;

import common.IProperty;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import serializer.IDeserializer;
import serializer.ISerializer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author sunding
 */
public class ConsumerRecordUtil {
	static final Map<String, byte[]> EMPTY_MAP = new HashMap(0);
	static final Set<String>         EMPTY_SET = new HashSet(0);

	private ConsumerRecordUtil() {
	}

	public static Set<String> getAllPropertyKeys(ConsumerRecord<String, ?> message) {
		if (message != null && message.headers() != null) {
			Headers headers = message.headers();
			Header[] headerArray = headers.toArray();
			if (headerArray != null && headerArray.length != 0) {
				HashSet keys = new HashSet();
				Header[] arr$ = headerArray;
				int len$ = headerArray.length;

				for (int i$ = 0; i$ < len$; ++i$) {
					Header header = arr$[i$];
					if (!keys.contains(header.key())) {
						keys.add(header.key());
					}
				}

				return keys;
			} else {
				return EMPTY_SET;
			}
		} else {
			return EMPTY_SET;
		}
	}

	public static Map<String, byte[]> getAllProperty(ConsumerRecord<String, ?> message) {
		if (message != null && message.headers() != null) {
			Headers headers = message.headers();
			Header[] headerArray = headers.toArray();
			if (headerArray != null && headerArray.length != 0) {
				HashMap properties = new HashMap();
				Header[] arr = headerArray;
				int len = headerArray.length;

				for (int i = 0; i < len; ++i) {
					Header header = arr[i];
					properties.put(header.key(), header.value());
				}
				return properties;
			} else {
				return EMPTY_MAP;
			}
		} else {
			return EMPTY_MAP;
		}
	}

	public static <V> V getPropertyValue(ConsumerRecord<String, ?> message, String propName, Class<V> valueType,
			IDeserializer deserializer) {
		Headers headers = message.headers();
		Header header = headers == null ? null : headers.lastHeader(propName);
		return header == null ? null : deserializer.deserialize(header.value(), valueType);
	}

	public static <V> V getPropertyValue(ConsumerRecord<String, ?> message, IProperty<V> property) {
		return getPropertyValue(message, property.name(), property.valueType(), IProperty.DESERIALIZER);
	}

	public static <V> void setPropertyValue(ConsumerRecord<String, ?> message, IProperty<V> property, V value) {
		setPropertyValue(message, property, value, IProperty.SERIALIZER);
	}

	public static <V> void setPropertyValue(ConsumerRecord<String, ?> message, IProperty<V> property, V value,
			ISerializer serializer) {
		if (message != null) {
			Headers headers = message.headers();
			headers.remove(property.name());
			headers.add(new RecordHeader(property.name(), serializer.serialize(value)));
		}
	}

	public static byte[] getPropertyValueByteArray(ConsumerRecord<String, ?> message, String propName) {
		Headers headers = message.headers();
		Header header = headers == null ? null : headers.lastHeader(propName);
		return header == null ? null : header.value();
	}

	public static String descRecord(ConsumerRecord<String, ?> message) {
		return message == null ? null : message.topic() + "-" + message.partition() + "-" + message.offset();
	}

	public static boolean isCompensated(ConsumerRecord<String, ?> message) {
		Boolean flag = getPropertyValue(message, IProperty.IS_COMPENSATED);
		return flag != null && flag.booleanValue();
	}

	public static boolean isSeekCommand(ConsumerRecord<String, ?> message) {
		Boolean flag = getPropertyValue(message, IProperty.IS_SEEK_COMMAND);
		return flag != null && flag.booleanValue();
	}

	public static boolean isNull(ConsumerRecords<String, ?> messages) {
		return messages == null || messages.isEmpty();
	}
}
