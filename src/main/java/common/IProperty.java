package common;

import serializer.HessianDeserializer;
import serializer.HessianSerializer;
import serializer.IDeserializer;
import serializer.ISerializer;

/**
 * @author sunding
 */
public interface IProperty<V> {

	ISerializer SERIALIZER = HessianSerializer.INSTANCE;
	IDeserializer DESERIALIZER = HessianDeserializer.INSATANCE;

	IProperty<String>  TO_SITE      = new IProperty() {
		@Override
		public String name() {
			return "To_Site";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<String>  TOPIC        = new IProperty() {
		@Override
		public String name() {
			return "Topic";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<Integer> PARTITION    = new IProperty() {
		@Override
		public String name() {
			return "Partition";
		}
		@Override
		public Class<Integer> valueType() {
			return Integer.class;
		}
	};
	IProperty<String> TO_GROUP = new IProperty() {
		@Override
		public String name() {
			return "To_Group";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<String> FAIL_CONSUME_REASON = new IProperty() {
		@Override
		public String name() {
			return "Fail_Consume_Reason";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<String> MESSAGE_CONTENT = new IProperty() {
		@Override
		public String name() {
			return "Message_Content";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<String> PRODUCER_TYPE = new IProperty() {
		@Override
		public String name() {
			return "Producer_Type";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<Boolean> IS_COMPENSATED = new IProperty() {
		@Override
		public String name() {
			return "Is_Compensated";
		}
		@Override
		public Class<Boolean> valueType() {
			return Boolean.class;
		}
	};
	IProperty<Boolean> IS_ASYNC_PRODUCE = new IProperty() {
		@Override
		public String name() {
			return "Is_Async";
		}
		@Override
		public Class<Boolean> valueType() {
			return Boolean.class;
		}
	};
	IProperty<String> APP_INFO = new IProperty() {
		@Override
		public String name() {
			return "App_Info";
		}
		@Override
		public Class<String> valueType() {
			return String.class;
		}
	};
	IProperty<Long> CONSUME_TIME = new IProperty() {
		@Override
		public String name() {
			return "CT";
		}
		@Override
		public Class<Long> valueType() {
			return Long.class;
		}
	};
	IProperty<Boolean> IS_SEEK_COMMAND = new IProperty() {
		@Override
		public String name() {
			return "SC";
		}
		@Override
		public Class<Boolean> valueType() {
			return Boolean.class;
		}
	};

	String name();

	Class<V> valueType();
}
