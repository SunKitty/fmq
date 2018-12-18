package common;

/**
 * @author sunding
 */
public enum IsolationLevel {
	READ_UNCOMMITTED((byte) 0),
	READ_COMMITTED((byte) 1);

	private final byte id;

	private IsolationLevel(byte id) {
		this.id = id;
	}

	public byte id() {
		return this.id;
	}

	public static IsolationLevel forId(byte id) {
		switch(id) {
		case 0:
			return READ_UNCOMMITTED;
		case 1:
			return READ_COMMITTED;
		default:
			throw new IllegalArgumentException("Unknown isolation level " + id);
		}
	}
}
