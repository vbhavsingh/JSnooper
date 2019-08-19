/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rationalminds.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

/**
 *
 * @author Vaibhav Singh
 */
public class ReflectionUtils {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(ReflectionUtils.class);

	/**
	 * Invoke reflective methods
	 *
	 * @param methodName
	 * @param obj
	 * @return
	 */
	public static String invokeMethod(String methodName, Object obj) {
		if (obj != null
				&& (obj.getClass().isPrimitive() || obj instanceof String)) {
			return obj.toString();
		}
		try {
			if (methodName.contains(".")) {
				String level[] = methodName.split("\\.", 2);
				Method m = obj.getClass().getDeclaredMethod(level[0],
						(Class[]) null);
				m.setAccessible(true);
				Object o = m.invoke(obj, (Object[]) null);
				return invokeMethod(level[1], o);

			} else {

				Method m = getDeclaredMethod(obj, methodName, (Class[]) null);
				if (m != null) {
					Object o = m.invoke(obj, (Object[]) null);
					if (o != null && o.getClass().isPrimitive()
							|| o instanceof String) {
						return o.toString();
					}
				}
			}

			return null;
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
			LOGGER.warn("Unknown Exception on method invocation: " + methodName);
			return null;
		}
	}

	/**
	 * Invoke reflective methods
	 *
	 * @param methodName
	 * @param obj
	 * @return
	 */
	public static Object invokeMethodObject(String methodName, Object obj) {
		if (obj != null
				&& (obj.getClass().isPrimitive() || obj instanceof String)) {
			return obj.toString();
		}
		try {
			if (methodName.contains(".")) {
				String level[] = methodName.split("\\.", 2);
				Method m = obj.getClass().getDeclaredMethod(level[0],
						(Class[]) null);
				m.setAccessible(true);
				Object o = m.invoke(obj, (Object[]) null);
				return invokeMethod(level[1], o);

			} else {
				Method m = getDeclaredMethod(obj, methodName, (Class[]) null);
				if (m != null) {
					Object o = m.invoke(obj, (Object[]) null);
					return o;
				}
			}
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
			LOGGER.warn("Unknown Exception on method invocation: " + methodName);
			return null;
		}
		return null;
	}

	/**
	 * Invoke reflective methods
	 *
	 * @param methodName
	 * @param obj
	 * @return
	 */
	public static String invokeMethod(String methodName, Object obj,
			Object[] args) {
		if (obj != null
				&& (obj.getClass().isPrimitive() || obj instanceof String)) {
			return obj.toString();
		}
		try {
			if (methodName.contains(".")) {
				String level[] = methodName.split("\\.", 2);
				Method m = obj.getClass().getDeclaredMethod(level[0],
						(Class[]) null);
				m.setAccessible(true);
				Object o = m.invoke(obj, args);
				return invokeMethod(level[1], o);

			} else {
				Class cl[] = new Class[args.length];
				for (int i = 0; i < args.length; i++) {
					cl[i] = args[i].getClass();
				}
				Method m = getDeclaredMethod(obj, methodName, cl);
				if (m != null) {
					Object o = m.invoke(obj, args);
					if (o != null && o.getClass().isPrimitive()
							|| o instanceof String) {
						return o.toString();
					}
				}

			}

			return null;
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
			LOGGER.warn("Unknown Exception on method invocation: " + methodName);
			return null;
		}
	}

	/**
	 * Invoke reflective methods
	 *
	 * @param methodName
	 * @param obj
	 * @return
	 */
	public static Object invokeMethodObject(String methodName, Object obj,
			Object[] args) {
		if (obj != null
				&& (obj.getClass().isPrimitive() || obj instanceof String)) {
			return obj.toString();
		}
		try {
			if (methodName.contains(".")) {
				String level[] = methodName.split("\\.", 2);
				Method m = obj.getClass().getDeclaredMethod(level[0],
						(Class[]) null);
				m.setAccessible(true);
				Object o = m.invoke(obj, args);
				return invokeMethod(level[1], o);

			} else {
				Class cl[] = new Class[args.length];
				for (int i = 0; i < args.length; i++) {
					cl[i] = args[i].getClass();
				}
				Method m = getDeclaredMethod(obj, methodName, cl);
				if (m != null) {
					Object o = m.invoke(obj, args);
					return o;
				}
			}

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
			LOGGER.warn("Unknown Exception on method invocation: " + methodName);
			return null;
		}
		return null;
	}

	/**
	 *
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static String readField(String fieldName, Object obj,
			String methodName) {
		try {
			Field f = obj.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			if (f.getType().isPrimitive()
					|| f.getType().getName()
							.equalsIgnoreCase("java.lang.String")) {
				return f.get(obj).toString();
			} else {
				if (methodName != null && !methodName.trim().equals("")) {
					return invokeMethod(methodName, f.get(obj));
				}
			}
			return null;
		} catch (NoSuchFieldException e) {
			LOGGER.warn("NoSuchFieldException: " + fieldName);
			return null;
		} catch (IllegalAccessException e) {
			LOGGER.warn("IllegalAccessException: " + fieldName);
			return null;
		} catch (Exception e) {
			LOGGER.warn("Unknown Exception at reading field: " + methodName);
			return null;
		}
	}

	public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
		if (cls == null) {
			return null;
		}

		final LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
		getAllInterfaces(cls, interfacesFound);

		return new ArrayList<Class<?>>(interfacesFound);
	}

	/**
	 * Get the interfaces for the specified class.
	 *
	 * @param cls
	 *            the class to look up, may be {@code null}
	 * @param interfacesFound
	 *            the {@code Set} of interfaces for the class
	 */
	private static void getAllInterfaces(Class<?> cls,
			final HashSet<Class<?>> interfacesFound) {
		while (cls != null) {
			final Class<?>[] interfaces = cls.getInterfaces();

			for (final Class<?> i : interfaces) {
				if (interfacesFound.add(i)) {
					getAllInterfaces(i, interfacesFound);
				}
			}

			cls = cls.getSuperclass();
		}
	}

	public static boolean instanceOf(String clazz, Object obj) {
		if (obj == null) {
			return false;
		}

		if (clazz.equals(obj.getClass().getName())) {
			return true;
		}
		List<Class<?>> cList = getAllInterfaces(obj.getClass());
		if (cList != null) {
			for (Class c : cList) {
				if (clazz.equals(c.getName())) {
					return true;
				}
			}
		}

		// Class cList[] = obj.getClass().getInterfaces();
		// for (Class c : cList) {
		// if (clazz.equals(c.getName())) {
		// return true;
		// }
		// }
		return false;
	}

	public static Method getDeclaredMethod(Object obj, String methodName,
			Class[] cl) {
		Class current = obj.getClass();
		Method method = null;
		while (current != Object.class) {
			try {
				method = current.getDeclaredMethod(methodName, cl);
				method.setAccessible(true);
				break;
			} catch (NoSuchMethodException ex) {
				current = current.getSuperclass();
			}
		}
		return method;
	}

	public static boolean isPrimitive(Object object) {
		if (object == null)
			return false;
		if (object.getClass().isPrimitive())
			return true;
		if (object instanceof String)
			return true;
		return false;
	}

}
