package net.rationalminds.Aspects.discovery.outgoing.jersey;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;

public abstract aspect JerseyRequestCapture extends JSnooperAspectCore {

	public pointcut pointCut():within(com.sun.jersey.api.client.UniformInterface+)
							&& (execution(* *.get(..)) 
							||  execution(* *.put(..))
							||  execution(* *.post(..))
							||  execution(* *.delete(..)));
	/**
	 * Operations before beginPoint Set the start time for execution
	 */
	before(): pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		Object thisObj=thisJoinPoint.getThis();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
	}

	/**
	 * Operations before beginPoint Set the start time for execution
	 */
	after() returning(java.net.URLConnection conn): outBoundCall() && cflow(pointCut()) && if(CONSTANTS.CONTROL.PROBE_ON){
		forwardGuid(conn);
	}
	
	/**
	 * Operations after endPoint set end time for complete transaction
	 */
	after() returning(Object returnObject): pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}
	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):pointCut(){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

}
