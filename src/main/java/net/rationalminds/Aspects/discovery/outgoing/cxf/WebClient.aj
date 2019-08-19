package net.rationalminds.Aspects.discovery.outgoing.cxf;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;

public abstract aspect WebClient extends JSnooperAspectCore {

	public pointcut pointCut();

	/**
	 * Operations after endPoint set end time for complete transaction
	 */
	after() returning(Object returnObject): pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		try {
			if (ReflectionUtils.instanceOf("javax.ws.rs.core.MultivaluedMap",
					returnObject) && returnObject != null) {
				Method key = returnObject.getClass().getMethod(
						"containsKey", new Class[] { Object.class });
				boolean obj = (boolean)key.invoke(returnObject,
						new Object[] { CONSTANTS.GUID });
				if (!obj) {
					String guid = getThreadLocal().get().getGuid();
					Method m = returnObject.getClass().getMethod("putSingle",
							new Class[] { Object.class, Object.class });
					if (m != null) {
						m.invoke(returnObject, new Object[] { CONSTANTS.GUID,
								guid });
					}
					EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
					startWithSend(epm);
					epm.setReturnObj(returnObject);
					exitWithSuccessAndSend(epm);
					exit();
				} 
			}
		} catch (Exception ex) {
			//ignore
		}
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):pointCut() && if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

}
