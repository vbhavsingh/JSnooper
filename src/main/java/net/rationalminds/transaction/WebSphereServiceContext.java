package net.rationalminds.transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.rationalminds.transaction.guid.GUIDPropgation;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.ERROR_MSG;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class WebSphereServiceContext implements GUIDPropgation {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(WebSphereServiceContext.class);

	String partitionName = CONSTANTS.WS_PARTITION;

	String workArea = CONSTANTS.WS_WORKAREA;

	String jndiName = "java:comp/websphere/UserWorkArea";

	private String passiveId = null;

	private static Class[] clazz1 = { java.lang.String.class };

	private static Class[] clazz2 = { java.lang.String.class,
			java.lang.String.class };

	private static Class[] clazz3 = { java.lang.String.class,
			java.io.Serializable.class };

	public String getGuid(String action) {
		if (action == null) {
			action = "NA";
		}
		if (action.equals(CONSTANTS.START)) {
			int counter = 0;
			LOGGER.info("Fetching Workarea for action: " + action);
			Object wa = getArea();
			if (wa != null) {
				Object guid = invokeMethod("get", wa, clazz1, CONSTANTS.GUID,
						null);
				String uid = null;
				if (guid != null) {
					uid = guid.toString();
				}
				if (uid == null) {
					uid = UID.get();
					invokeMethod("begin", wa, clazz1, CONSTANTS.WS_WORKAREA,
							null);
					LOGGER.info("Starting workarea");
					try {
						counter = 0;
						invokeMethod("set", wa, clazz3, CONSTANTS.GUID, uid);
						Object cObj = invokeMethod("get", wa, clazz1,
								"E_AGLE_C_OUNTER", null);
						String c = null;
						if (cObj != null) {
							c = cObj.toString();
						}
						if (c != null) {
							try {
								counter = Integer.parseInt(c);
							} catch (Exception e) {
								LOGGER.error("Parse Exception", e);
								LOGGER.error("Parse Exception "
										+ e.getLocalizedMessage());

							}
						}
						counter++;
						invokeMethod("set", wa, clazz3, "E_AGLE_C_OUNTER",
								String.valueOf(counter));
						LOGGER.info("Set Workarea uid=" + uid + " @counter="
								+ counter);
					} catch (Exception e) {
						LOGGER.error("", e);
					}
				}
				passiveId = uid;
				return uid;
			}
		}
		if (action.equals(CONSTANTS.STOP)) {
			int counter = 0;
			LOGGER.info("Fetching Workarea for action: " + action);
			Object wa = getArea();
			if (wa != null) {
				Object guid = invokeMethod("get", wa, clazz1, CONSTANTS.GUID,
						null);
				String uid = null;
				if (guid != null) {
					uid = guid.toString();
				}
				if (uid == null) {
					uid = passiveId;
				}
				try {
					Object cObj = invokeMethod("get", wa, clazz1,
							"E_AGLE_C_OUNTER", null);
					String c = null;
					if (cObj != null) {
						c = cObj.toString();
					}
					if (c != null) {
						try {
							counter = Integer.parseInt(c);
						} catch (Exception e) {
							LOGGER.error("Parse Exception", e);
						}
					}
					if (counter == 0)
						invokeMethod("complete", wa, null, null, null);
					else
						counter--;
				} catch (Exception e) {
					LOGGER.error("", e);
				}
				LOGGER.info("Seek uid=" + uid + " @Cnt=" + counter);
				return uid;
			}
		}
		/*
		 * if (wa != null) { return wa.get(CONSTANTS.GUID).toString(); }
		 */
		return null;

	}

	private Object getArea() {
		InitialContext ic;
		try {
			ic = new InitialContext();
			Object obj = ic.lookup(jndiName);
			if (obj == null) {
				return null;
			} else {
				return obj;
			}
		} catch (Exception e) {
			if (e instanceof NamingException) {
				LOGGER.error(ERROR_MSG.IBM.JNDI_FAIL, e);
			} else {
				LOGGER.error("Exception", e);
			}
		}
		return null;
	}

	public String getCorrelator() {
		return getGuid(null);
	}

	public void setCorrelator(String value) {
		Object wa = getArea();
		try {
			if (wa != null)
				invokeMethod("set", wa, clazz3, CONSTANTS.GUID, value);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private Object invokeMethod(String methodName, Object obj, Class[] clazz,
			String a1, String a2) {
		try {
			Method m = obj.getClass().getDeclaredMethod(methodName, clazz);
			m.setAccessible(true);
			Object o = null;
			if (a1 == null && a2 == null) {
				o = m.invoke(obj);
			}
			if (a1 != null && a2 == null) {
				o = m.invoke(obj, a1);
			}
			if (a1 != null && a2 != null) {
				o = m.invoke(obj, a1, a2);
			}

			return o;
		} catch (InvocationTargetException e) {
			LOGGER.warn("InvocationTargetException: " + methodName);
			return null;
		} catch (IllegalAccessException e) {
			LOGGER.warn("IllegalAccessException: " + methodName);
			return null;
		} catch (NoSuchMethodException e) {
			LOGGER.warn("NoSuchMethodException: " + methodName);
			return null;
		} catch (Exception e) {
			LOGGER
					.warn("Unknown Exception on method invocation: "
							+ methodName);
			e.printStackTrace();
			return null;
		}
	}
}
