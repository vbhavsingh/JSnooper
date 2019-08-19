package net.rationalminds.Aspects;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;

public abstract privileged aspect HttpRequestAbstractAspect extends JSnooperAspectCore {

	// Transaction start point cut
	public abstract pointcut pointCut();

	/**
	 * Operations before beginPoint Set mthe start time for execution
	 */
	
	before(): pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		_read.recieve(thisJoinPoint);
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
	}

	/**
	 * Operations after endPoint set end time for complete transaction
	 */
	after() returning(Object returnObject): pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):pointCut() && if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}
}
