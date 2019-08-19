package net.rationalminds.Aspects.discovery.web;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.web.ApacheHttpPropagation;
import net.rationalminds.Aspects.JSnooperAspectCore;

public abstract aspect ApacheClientDiscovery extends JSnooperAspectCore {

	public pointcut capturePoint():execution(public 
			org.apache.http.HttpResponse 
			org.apache.http.protocol.HttpRequestExecutor.execute(
					org.apache.http.HttpRequest, 
					org.apache.http.HttpClientConnection, 
					org.apache.http.protocol.HttpContext));

	/**
	 * Before statement
	 */
	before():capturePoint(){
		enter();
		ApacheHttpPropagation _prop = new ApacheHttpPropagation();
		Object req = thisJoinPoint.getArgs()[0];
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm=_prop.propagate(req,epm);		
		startWithSend(epm);
	}

	/**
	 * 
	 * @param returnObject
	 */
	after() returning(Object returnObject):capturePoint(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):capturePoint(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
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
