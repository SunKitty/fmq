package producer;

/**
 * @author sunding
 * 	发送完成回调
 */
public interface ISendCallBack {

	void onCompletion(SendResult result);
}
