package net.rationalminds.Aspects;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;

public abstract privileged aspect BaseAspectWaitStartSingle extends JSnooperAspectCore {
	abstract pointcut pointCut();

	before(): pointCut() && if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
	}

	after() returning(Object returnObject): pointCut() && if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setReturnObj(returnObject);
		startOnExitAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):pointCut() && if(CONSTANTS.CONTROL.PROBE_ON){
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
	before(Exception e):exceptionHandler(e) && cflow(pointCut())&& if(CONSTANTS.CONTROL.PROBE_ON){
		if (e == null) {
			faults.append("exception variable is null");
		} else if (e.getMessage() == null || e.getMessage().trim().equals("")) {
			setSoftError(e);
		} else if (faults.length() <= 200) {
			faults.append(e.getMessage());
		}

	}
}
