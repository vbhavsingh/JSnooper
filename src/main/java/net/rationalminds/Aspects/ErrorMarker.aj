package net.rationalminds.Aspects;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;

public abstract aspect ErrorMarker extends JSnooperAspectCore {

	public abstract pointcut errorMarker();

	before():errorMarker()&& if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, false, true);
		startWithSend(epm);
	}

	after() returning(Object returnObject):errorMarker()&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint);
		epm.setReturnObj(returnObject);
		epm.setRecollectData(true);
		errorMarkerWithSucess(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):errorMarker()&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

}
