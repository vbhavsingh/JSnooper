package net.rationalminds.Aspects;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;

public abstract privileged aspect BaseAspectThreePoints extends JSnooperAspectCore {

	private short counter = 0;

	// Transaction start point cut
	abstract pointcut startPoint();

	// Transaction trace point for value
	abstract pointcut trace();

	// Transaction end point cut
	abstract pointcut endPoint();

	// exception point cut
	// abstract pointcut exceptionHandle();

	/**
	 * Operations before beginPoint Set the start time for execution
	 */
	before(): startPoint()&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithNoSend(epm);
		counter = 1;
	}

	/**
	 * Trace the value from underlying method
	 */
	before(): trace() && cflow(startPoint())&& if(CONSTANTS.CONTROL.PROBE_ON){
		if (counter == 1) {
			EndpointModel epm = new EndpointModel(thisJoinPoint, false, true);
			startWithSend(epm);
			counter = 2;
		}
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
