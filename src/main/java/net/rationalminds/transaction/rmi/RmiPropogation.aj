package net.rationalminds.transaction.rmi;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import java.rmi.RemoteException;
import java.rmi.Remote;

import net.rationalminds.transaction.guid.UID;

public abstract aspect RmiPropogation {

	declare parents: javax.ejb.EJBObject+ implements net.rationalminds.transaction.guid.JSnooperGuid;

	public void net.rationalminds.transaction.guid.JSnooperGuid.propogateGuid(String guid)
			throws java.rmi.RemoteException {

		System.out.println(guid);

	}

	public pointcut remoteExecution() : within(Remote+) && execution(public * *(..) throws RemoteException);

	public pointcut ejbRemoteMethod(Object targetObj) : within(javax.ejb.EJBObject+) && !within(javax.ejb.EJBHome+) && remoteExecution() && target(targetObj);

	before(Object targetObj):
		ejbRemoteMethod(targetObj) {
		if (!targetObj.getClass().getName().startsWith("_WL")) {
			Class targetClass = targetObj.getClass();
			Signature signature = thisJoinPointStaticPart.getSignature();
			Object thisObj = thisJoinPoint.getThis();
			Object[] arguments = thisJoinPoint.getArgs();
			Class[] argTypes = ((CodeSignature) signature).getParameterTypes();
			Object[] newArgs = new Object[arguments.length + 1];
			Class[] newArgTypes = new Class[0];
			newArgs[0] = UID.get();
			Method method = null;
			try {
				Class clazz = Class.forName("java.rmi.Remote");
				Method mc[] = clazz.getMethods();
				for (int i = 0; i < mc.length; i++) {
					System.out.println(mc[i].getName());
				}
				System.out.println("******************************");
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Method m[] = targetClass.getMethods();
				for (int i = 0; i < m.length; i++) {
					System.out.println(m[i].getName());
				}
				method = targetClass.getMethod("propogateGuid",
						java.lang.String.class);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// perform the call to the extra method
			ArrayList returnArray = null;
			try {
				returnArray = (ArrayList) method.invoke(targetObj, UID.get());
			} catch (Exception e) {
			}
		}
	}
}
