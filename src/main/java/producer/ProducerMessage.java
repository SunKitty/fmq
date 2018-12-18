package producer;

import com.google.common.collect.Maps;
import common.IProperty;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author sunding
 *  发送数据封装对象
 */
public class ProducerMessage {

	private String partitionKey;
	private Integer partition;
	private Object content;
	private transient byte[] serializedContent;
	private transient Map<String, Object> properties = Maps.newHashMapWithExpectedSize(2);

	public ProducerMessage(byte[] serializedContent) {
		this.serializedContent = serializedContent;
	}

	public ProducerMessage(Object content) {
		this.content = content;
	}

	public ProducerMessage(Integer partition, Object content) {
		this.partition = partition;
		this.content = content;
	}

	public ProducerMessage(Integer partition, byte[] serializedContent) {
		this.partition = partition;
		this.serializedContent = serializedContent;
	}

	public ProducerMessage(String partitionKey, Object content) {
		this.partitionKey = partitionKey;
		this.content = content;
	}

	public ProducerMessage(String partitionKey, byte[] serializedContent) {
		this.partitionKey = partitionKey;
		this.serializedContent = serializedContent;
	}

	public ProducerMessage(String partitionKey, Integer partition, Object content) {
		this.partitionKey = partitionKey;
		this.partition = partition;
		this.content = content;
	}

	public ProducerMessage(String partitionKey, Integer partition, byte[] serializedContent) {
		this.partitionKey = partitionKey;
		this.partition = partition;
		this.serializedContent = serializedContent;
	}

	public Integer getPartition() {
		return partition;
	}

	public void setPartition(Integer partition) {
		this.partition = partition;
	}

	public String getPartitionKey() {
		return this.partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

	public Object getContent() {
		return this.content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void addProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	public <V> void addProperty(IProperty<V> property, V value) {
		this.properties.put(property.name(), value);
	}

	public void addAllProperty(Map<String, Object> pros) {
		this.properties.putAll(pros);
	}

	public void addAllOrignalProperty(Map<String, byte[]> pros) {
		this.properties.putAll(pros);
	}

	public boolean hasProperty() {
		return !CollectionUtils.isEmpty(properties);
	}

	public boolean hasProperty(IProperty property) {
		return this.properties.containsKey(property.name());
	}

	public boolean hasProperty(String property) {
		return this.properties.containsKey(property);
	}

	public byte[] getSerializedContent() {
		return this.serializedContent;
	}

	public void setSerializedContent(byte[] serializedContent) {
		this.serializedContent = serializedContent;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ProducerMessage{");
		sb.append("partitionKey=\'").append(this.partitionKey).append('\'');
		sb.append(", partion=").append(this.partition);
		sb.append(", content=").append(this.content);
		sb.append(", serializedContent=").append(this.serializedContent);
		sb.append(", properties=").append(this.properties);
		sb.append('}');
		return sb.toString();
	}
}
