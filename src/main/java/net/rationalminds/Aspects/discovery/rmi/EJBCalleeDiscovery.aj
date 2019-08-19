package net.rationalminds.Aspects.discovery.rmi;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.List;
import java.util.ArrayList;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.Aspects.JSnooperAspectCore;

public abstract aspect EJBCalleeDiscovery extends JSnooperAspectCore {

	private static final Class ejbClass = EJBCalleeDiscovery
			.findClassIfPresent("javax.ejb.EnterpriseBean",
					EJBCalleeDiscovery.class.getClassLoader());

	public pointcut ejbMethod(Object ejb) : within(javax.ejb.EnterpriseBean+) && execution(public * *(..)) && this(ejb);

	/**
	 * 
	 */
	before(Object ejb) : ejbMethod(ejb) {
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

	/**
	 * 
	 * @param ejb
	 */
	after(Object ejb) returning(Object returnObject): ejbMethod(ejb){
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
	after(Object ejb)throwing(Throwable e):ejbMethod(ejb){
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
	/*before(Exception e):exceptionHandler(e) && cflow(ejbMethod(Object)){
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
		ignoreMethods.add("ejbCreate");
		ignoreMethods.add("ejbPostCreate");
		ignoreMethods.add("ejbFindByPrimaryKey");
		ignoreMethods.add("setEntityContext");
		ignoreMethods.add("setMessageDrivenContext");
		ignoreMethods.add("getHandle");
		ignoreMethods.add("getHomeHandle");
		ignoreMethods.add("onMessage");// handled by JMSAspect

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

	static String getInterfaceAndClasses(Object ejb) {
		String allParents = "";
		Class[] implementedInterfaces = ejb.getClass().getInterfaces();
		for (int i = 0; i < implementedInterfaces.length; i++) {
			allParents += " " + implementedInterfaces[i].getName();
		}
		return allParents;
	}

	static Class findClassIfPresent(String name, ClassLoader loader) {
		try {
			return Class.forName(name, false, loader);
		} catch (Throwable t) {
			return null;
		}
	}
}
