package producer;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author sunding
 * 异步获取发送结果
 */
public class FutureSendResult implements Future<SendResult> {

	private Future<RecordMetadata> recordMetadataFuture;

	public FutureSendResult(Future<RecordMetadata> recordMetadataFuture) {
		this.recordMetadataFuture = recordMetadataFuture;
	}

	SendResult doGet(long watiTime, TimeUnit timeUnit) {
		try {
			RecordMetadata metadata = this.recordMetadataFuture.get(watiTime, timeUnit);
			return SendResult.newSuccessResult(metadata.topic(), metadata.partition(), metadata.offset(),
					metadata.serializedValueSize());
		} catch (Exception e) {
			return SendResult.newFailResult(e);
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return this.recordMetadataFuture.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return this.recordMetadataFuture.isCancelled();
	}

	@Override
	public boolean isDone() {
		return this.recordMetadataFuture.isDone();
	}

	@Override
	public SendResult get() throws InterruptedException, ExecutionException {
		return this.doGet(9223372036854775807L, TimeUnit.DAYS);
	}

	@Override
	public SendResult get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return this.doGet(timeout, unit);
	}
}
