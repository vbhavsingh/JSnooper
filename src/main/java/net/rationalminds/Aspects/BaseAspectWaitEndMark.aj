package net.rationalminds.Aspects;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;

public abstract privileged aspect BaseAspectWaitEndMark extends JSnooperAspectCore {

	// Transaction start point cut
	abstract pointcut startPoint();

	// Transaction trace point for value
	abstract pointcut trace();

	/**
	 * Operations before beginPoint Set the start time for execution
	 */
	before(): startPoint()&& if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithNoSend(epm);
	}

	/**
	 * Set threadlocal decrement
	 */
	after(): startPoint()&& if(CONSTANTS.CONTROL.PROBE_ON){
		exit();
	}
	
	/**
	 * Trace the value from underlying method
	 */
	before(): trace() && cflow(startPoint())&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, false, true);
		exitOnStartAndSend(epm);
	}

	/**
	 * soft exception message capture
	 * 
	 * @param e
	 */
	before(Exception e):exceptionHandler(e) && cflow(trace())&& if(CONSTANTS.CONTROL.PROBE_ON){
		if (e == null) {
			faults.append("exception variable is null");
		} else if (e.getMessage() == null || e.getMessage().trim().equals("")) {
			setSoftError(e);
		} else if (faults.length() <= 200) {
			faults.append(e.getMessage());
		}

	}

}
