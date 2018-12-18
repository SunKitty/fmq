package common;

/**
 * @author sunding
 * 生产者确认（ACK）类型
 */
public enum ProduceAckType {

	/** 不能保证数据写入 */
	NOT_REQUIRED("0"),
	/** Leader写入成功即可 */
	LEADER("1"),
	/** Leader和所有副本写入成功 */
	ALL("all");

	String acks;

	private ProduceAckType(String acks) {
		this.acks = acks;
	}

	public String acks() {
		return this.acks;
	}

}
