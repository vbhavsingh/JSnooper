package net.rationalminds.Aspects.discovery.rmi;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.EJBLocalObject;

import org.aspectj.lang.JoinPoint;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.Aspects.JSnooperAspectCore;

public abstract aspect EJBLocalDiscovery extends JSnooperAspectCore {

	private static final Class ejbRemoteClass = findClassIfPresent(
			"javax.ejb.EJBObject", EJBLocalDiscovery.class.getClassLoader());
	private static final Class ejbHomeClass = findClassIfPresent(
			"javax.ejb.EJBHome", EJBLocalDiscovery.class.getClassLoader());

	public pointcut ejbLocalMethod() : within(javax.ejb.EJBLocalObject+);

	before(Object ejb) : ejbLocalMethod() && this(ejb){
		enter();
		String methodName = thisJoinPointStaticPart.getSignature().getName();
		if (methodName == null || ignoreMethods.contains(methodName)
				|| methodName.startsWith("__WL")
				|| methodName.startsWith("_WL")) {

		} else {
			EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
			startWithSend(epm);
		}
	}

	after(Object ejb) returning(Object returnObject): ejbLocalMethod()&& this(ejb){
		String methodName = thisJoinPointStaticPart.getSignature().getName();
		if (methodName == null || ignoreMethods.contains(methodName)
				|| methodName.startsWith("__WL")
				|| methodName.startsWith("_WL")) {

		} else {
			EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
			epm.setReturnObj(returnObject);
			exitWithSuccessAndSend(epm);
		}
		exit();
	}

	/**
	 * Exception Handling
	 */
	after(Object ejb)throwing(Throwable e):ejbLocalMethod()&& this(ejb){
		String methodName = thisJoinPointStaticPart.getSignature().getName();
		if (methodName == null || ignoreMethods.contains(methodName)
				|| methodName.startsWith("__WL")
				|| methodName.startsWith("_WL")) {

		} else {
			EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
			epm.setErrorText(e.getMessage());
			exitWithErrorAndSend(epm);
		}
		exit();
	}

	/**
	 * soft exception message capture
	 * 
	 * @param e
	 */
	/*before(Exception e):exceptionHandler(e) && cflow(ejbLocalMethod()){
		if (e == null) {
			faults.append("exception variable is null");
		} else if (e.getMessage() == null || e.getMessage().trim().equals("")) {
			setSoftError(e);
		} else if (faults.length() <= 200) {
			faults.append(e.getMessage());
		}

	}*/

	static List<String> ignoreMethods = new ArrayList<String>();
	static {
		ignoreMethods.add("setSessionContext");
		ignoreMethods.add("invokeLifecycleMethod");
		ignoreMethods.add("getJeeInterceptorAdvisors");
		ignoreMethods.add("getAdvisors");
		ignoreMethods.add("getTargetSource");
		ignoreMethods.add("ejbActivate");
		ignoreMethods.add("ejbPassivate");
		ignoreMethods.add("ejbLoad");
		ignoreMethods.add("ejbStore");
		ignoreMethods.add("ejbRemove");
		ignoreMethods.add("ejbFindByPrimaryKey");
		ignoreMethods.add("findByPrimaryKey");
		ignoreMethods.add("getPrimaryKey");
		ignoreMethods.add("create");
		ignoreMethods.add("getEJBHome");
		ignoreMethods.add("remove");
		ignoreMethods.add("getHandle");
		ignoreMethods.add("getHomeHandle");

	}

	static Class getMostDerivedInterface(Object ejb, Class interfaze) {
		Class[] implementedInterfaces = ejb.getClass().getInterfaces();
		Class least = interfaze;
		for (int i = 0; i < implementedInterfaces.length; i++) {
			if (least.isAssignableFrom(implementedInterfaces[i])) {
				least = implementedInterfaces[i];
			}
			// System.out.println(implementedInterfaces[i].getName());
		}
		return least;
	}

	static Class findClassIfPresent(String name, ClassLoader loader) {
		try {
			return Class.forName(name, false, loader);
		} catch (Throwable t) {
			return null;
		}
	}

}
