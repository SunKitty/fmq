package producer;

/**
 * @author sunding
 * 发送结果
 */
public class SendResult {
	/**
	 * 是否发送成功标志位
	 */
	private boolean   successful;
	/**
	 * 主题
	 */
	private String    topic;
	/**
	 * 分区
	 */
	private int       partition;
	/**
	 * Offset
	 */
	private long      offset;
	/**
	 * 序列化内容大小
	 */
	private int       serializedContentSize;
	/**
	 * 异常
	 */
	private Exception exception;

	/**
	 * 成功发送结果
	 * @param topic
	 * @param partition
	 * @param offset
	 * @param serializedContentSize
	 * @return
	 */
	public static SendResult newSuccessResult(String topic, int partition, long offset,
			int serializedContentSize) {
		SendResult successResult = new SendResult();
		successResult.setSuccessful(true);
		successResult.setTopic(topic);
		successResult.setPartition(partition);
		successResult.setOffset(offset);
		successResult.setSerializedContentSize(serializedContentSize);
		return successResult;
	}

	/**
	 * 失败发送结果
	 * @param exception
	 * @return
	 */
	public static SendResult newFailResult(Exception exception) {
		SendResult failResult = new SendResult();
		failResult.setSuccessful(false);
		failResult.setException(exception);
		return failResult;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getSerializedContentSize() {
		return serializedContentSize;
	}

	public void setSerializedContentSize(int serializedContentSize) {
		this.serializedContentSize = serializedContentSize;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "SendResult{" + "successful=" + successful + ", topic='" + topic + '\'' + ", partition=" + partition
				+ ", offset=" + offset + ", serializedContentSize=" + serializedContentSize + ", exception=" + exception
				+ '}';
	}
}
