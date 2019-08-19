package net.rationalminds.Aspects.discovery.web;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.web.JaxWSJava6;
import net.rationalminds.Aspects.JSnooperAspectCore;

public abstract aspect JaxWsWebServiceDiscovery extends JSnooperAspectCore {

	public pointcut wsCapture():within(javax.xml.ws.BindingProvider+)&& execution(* *.*(..)) 
	&& !execution(* *.equals(..)) 
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
	&& !execution(* *.notifyAll(..))
	&& !execution(* *.doProcess(..))
	&& !execution(* *.setResponseContext(..))
	&& !execution(* *.invoke(..))
	&& !execution(* *.process(..))
	&& !execution(* *.invoke(..));

	before() : wsCapture() {
		enter();
		String guid = getThreadLocal().get().getGuid();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		JaxWSJava6 _prop = new JaxWSJava6();
		_prop.publicpropgateGuid(thisJoinPoint.getThis(),epm,guid);
		
		//epm=_prop.enrich(epm, null);
		startWithSend(epm);
	}

	/**
	 * 
	 * @param returnObject
	 */
	after() returning(Object returnObject):wsCapture(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):wsCapture(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

}
