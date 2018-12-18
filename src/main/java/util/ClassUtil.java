package util;

import java.lang.reflect.Method;

/**
 * @author sunding
 */
public class ClassUtil {

	public ClassUtil() {
	}

	public static <T> T castFrom(Object o) {
		return (T) o;
	}

	public static String mangleMethodName(Method method) {
		Class[] argTypes = method.getParameterTypes();
		String[] argTypeStrings = new String[argTypes.length];

		for(int i = 0; i < argTypeStrings.length; ++i) {
			argTypeStrings[i] = argTypes[i].getName();
		}

		return mangleMethodName(method.getName(), argTypeStrings);
	}

	public static String mangleMethodName(String methodName, String[] argTypeStrings) {
		StringBuilder sb = new StringBuilder();
		sb.append(methodName);

		for(int i = 0; i < argTypeStrings.length; ++i) {
			sb.append("_");
			sb.append(argTypeStrings[i]);
		}

		return sb.toString();
	}
}
