package net.rationalminds.Aspects.discovery.web;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;

public abstract aspect JaxWSWebMetod extends JSnooperAspectCore {

	public pointcut wmExecute():execution(@javax.jws.WebMethod * *.*(..)) || execution(@javax.jws.WebService * *.*(..));
	
	public pointcut wmAnnotated():within(@javax.jws.WebService *) 
	&& execution(public * *.*(..)) 
	&& !within(com.e2e.aspect.annotation.*);
	//&& if(com.e2e.aspect.annotation.AnnotationExtension.implementsAnnotation("@WebMethod()", thisJoinPoint));

	public pointcut neglect(): !execution(* *.equals(..)) 
	&& !execution(* *.toString(..)) 
	&& !execution(* *.hashCode(..)) 
	&& !execution(* *.close(..)) 
	&& !execution(* *.getRequestContext(..)) 
	&& !execution(* *.getBinding(..)) 
	&& !execution(* *.setAddress(..)) 
	&& !execution(* *.setOutboundHeaders(..)) 
	&& !execution(* *.getEndpointReference(..)) 
	&& !execution(* *.getInboundHeaders(..)) 
	&& !execution(* *.getResponseContext(..)) 
	&& !execution(* *.isProxyClass(..)) 
	&& !execution(* *.getProxyClass(..)) 
	&& !execution(* *.getInvocationHandler(..)) 
	&& !execution(* *.newProxyInstance(..)) 
	&& !execution(* *.wait(..)) 
	&& !execution(* *.getClass(..)) 
	&& !execution(* *.notify(..)) 
	&& !execution(* *.notifyAll(..));

	before() : (wmExecute()||wmAnnotated()) && neglect() {
		enter();
		EndpointModel epm=new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
		// System.out.println("Execute " + thisJoinPoint.toString());
	}

	/**
	 * 
	 * @param returnObject
	 */
	after() returning(Object returnObject):(wmExecute()||wmAnnotated()) && neglect(){
		EndpointModel epm=new EndpointModel(thisJoinPoint, true, false);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):(wmExecute()||wmAnnotated()) && neglect(){
		EndpointModel epm=new EndpointModel(thisJoinPoint, true, false);
		epm.setErrorText( e.getMessage());
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
