package net.rationalminds.Aspects.discovery.web;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.web.JaxRRCJava45;

public abstract aspect JaxRPCWebServiceDiscovery extends JSnooperAspectCore {

	public pointcut wsCapture();

	before() : wsCapture() {
		enter();
		String guid = getThreadLocal().get().getGuid();
		JaxRRCJava45 _prop = new JaxRRCJava45();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm=_prop.propagate(thisJoinPoint.getThis(),epm,guid);
		startWithSend(epm);
		// System.out.println("Execute " + thisJoinPoint.toString());
	}

	/**
	 * 
	 * @param returnObject
	 */
	after() returning(Object returnObject):wsCapture(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):wsCapture(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

	/**
	 * soft exception message capture
	 * 
	 * @param e
	 */
	/*
	 * before(Exception e):exceptionHandler(e) && cflow(wsCapture()){ if (e ==
	 * null) { faults.append("exception variable is null"); } else if
	 * (e.getMessage() == null || e.getMessage().trim().equals("")) {
	 * setSoftError(e); } else if (faults.length() <= 200) {
	 * faults.append(e.getMessage()); } }
	 */

}
