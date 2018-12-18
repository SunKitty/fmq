package consumer.normal.config;

import common.IsolationLevel;
import common.OffsetResetStrategy;
import consumer.IFailedMessageHandler;
import consumer.IGroupIdTransformer;
import consumer.IMessageFilter;
import serializer.HessianDeserializer;
import serializer.IDeserializer;
import java.util.Properties;

/**
 * @author sunding
 *  消费者配置文件
 */
public class NormalConsumerConfig {

	private String 				  currentSite;
	private String 				  serverAddrs;
	private String 				  clientId = "";
	private String              groupId;
	private IGroupIdTransformer groupIdTransformer;
	private OffsetResetStrategy   autoOffsetReset = OffsetResetStrategy.EARLIEST;
	private IsolationLevel        isolationLevel = IsolationLevel.READ_COMMITTED;
	private boolean               autoCommit = true;
	private long                  autoCommitInterval = 3000L;
	private int                   maxPollRecords = 200;
	private int                   maxPollIntervalMs = 2147483647;
	private boolean               enableInnerFilter = true;
	private IMessageFilter        messageFilter;
	private IFailedMessageHandler failedMessageHandler;
	private IDeserializer         deserializer = HessianDeserializer.INSATANCE;
	private Properties moreConfig = new Properties();

	public NormalConsumerConfig currentSite(String currentSite) {
		this.currentSite = currentSite;
		return this;
	}

	public NormalConsumerConfig serverAddrs(String serverAddrs) {
		this.serverAddrs = serverAddrs;
		return this;
	}

	public NormalConsumerConfig clientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public NormalConsumerConfig groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public NormalConsumerConfig groupIdTransformer(IGroupIdTransformer groupIdTransformer) {
		this.groupIdTransformer = groupIdTransformer;
		return this;
	}

	public NormalConsumerConfig autoOffsetReset(OffsetResetStrategy autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
		return this;
	}

	public NormalConsumerConfig isolationLevel(IsolationLevel isolationLevel) {
		this.isolationLevel = isolationLevel;
		return this;
	}

	public NormalConsumerConfig autoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
		return this;
	}

	public NormalConsumerConfig autoCommitInterval(long autoCommitInterval) {
		this.autoCommitInterval = autoCommitInterval;
		return this;
	}

	public NormalConsumerConfig maxPollRecords(int maxPollRecords) {
		this.maxPollRecords = maxPollRecords;
		return this;
	}

	public NormalConsumerConfig maxPollIntervalMs(int maxPollIntervalMs) {
		this.maxPollIntervalMs = maxPollIntervalMs;
		return this;
	}

	public NormalConsumerConfig enableInnerFilter(boolean enableInnerFilter) {
		this.enableInnerFilter = enableInnerFilter;
		return this;
	}

	public NormalConsumerConfig messageFilter(IMessageFilter messageFilter) {
		this.messageFilter = messageFilter;
		return this;
	}

	public NormalConsumerConfig failedMessageHandler(IFailedMessageHandler failedMessageHandler) {
		this.failedMessageHandler = failedMessageHandler;
		return this;
	}

	public NormalConsumerConfig deserializer(IDeserializer deserializer) {
		this.deserializer = deserializer;
		return this;
	}

	public NormalConsumerConfig moreConfig(Properties moreConfig) {
		this.moreConfig = moreConfig;
		return this;
	}

	public String getCurrentSite() {
		return currentSite;
	}

	public void setCurrentSite(String currentSite) {
		this.currentSite = currentSite;
	}

	public String getServerAddrs() {
		return serverAddrs;
	}

	public void setServerAddrs(String serverAddrs) {
		this.serverAddrs = serverAddrs;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public IGroupIdTransformer getGroupIdTransformer() {
		return groupIdTransformer;
	}

	public void setGroupIdTransformer(IGroupIdTransformer groupIdTransformer) {
		this.groupIdTransformer = groupIdTransformer;
	}

	public OffsetResetStrategy getAutoOffsetReset() {
		return autoOffsetReset;
	}

	public void setAutoOffsetReset(OffsetResetStrategy autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
	}

	public IsolationLevel getIsolationLevel() {
		return isolationLevel;
	}

	public void setIsolationLevel(IsolationLevel isolationLevel) {
		this.isolationLevel = isolationLevel;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public long getAutoCommitInterval() {
		return autoCommitInterval;
	}

	public void setAutoCommitInterval(long autoCommitInterval) {
		this.autoCommitInterval = autoCommitInterval;
	}

	public int getMaxPollRecords() {
		return maxPollRecords;
	}

	public void setMaxPollRecords(int maxPollRecords) {
		this.maxPollRecords = maxPollRecords;
	}

	public int getMaxPollIntervalMs() {
		return maxPollIntervalMs;
	}

	public void setMaxPollIntervalMs(int maxPollIntervalMs) {
		this.maxPollIntervalMs = maxPollIntervalMs;
	}

	public boolean isEnableInnerFilter() {
		return enableInnerFilter;
	}

	public void setEnableInnerFilter(boolean enableInnerFilter) {
		this.enableInnerFilter = enableInnerFilter;
	}

	public IMessageFilter getMessageFilter() {
		return messageFilter;
	}

	public void setMessageFilter(IMessageFilter messageFilter) {
		this.messageFilter = messageFilter;
	}

	public IFailedMessageHandler getFailedMessageHandler() {
		return failedMessageHandler;
	}

	public void setFailedMessageHandler(IFailedMessageHandler failedMessageHandler) {
		this.failedMessageHandler = failedMessageHandler;
	}

	public IDeserializer getDeserializer() {
		return deserializer;
	}

	public void setDeserializer(IDeserializer deserializer) {
		this.deserializer = deserializer;
	}

	public Properties getMoreConfig() {
		return moreConfig;
	}

	public void setMoreConfig(Properties moreConfig) {
		this.moreConfig = moreConfig;
	}

	public boolean hasGroupIdTransformer() {
		return this.groupIdTransformer != null;
	}

	public boolean hasUserMessageFilter() {
		return this.messageFilter != null;
	}
}
