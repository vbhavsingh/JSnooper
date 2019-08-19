package net.rationalminds.Aspects.discovery.outgoing.cxf;

import java.lang.reflect.Method;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;

public abstract aspect Interceptor extends JSnooperAspectCore {

	public pointcut pointCut();

	before() : pointCut() && if(CONSTANTS.CONTROL.PROBE_ON)
	{
		enter();
		try {
			// check for HttpMessage Object
			Object[] allArgs = thisJoinPoint.getArgs();
			if (allArgs != null && allArgs.length > 0) {
				// look for HttpMessage object
				for (int i = 0; i < allArgs.length; i++) {
					if (ReflectionUtils.instanceOf(
							"org.apache.cxf.message.Message", allArgs[i])
							&& allArgs[i] != null) {
						Method getMethod = allArgs[i].getClass().getMethod(
								"get", new Class[] { Object.class });
						if (getMethod != null) {
							Object multiValueMap = getMethod
									.invoke(allArgs[i],
											new Object[] { "org.apache.cxf.message.Message.PROTOCOL_HEADERS" });
							if (multiValueMap != null) {
								// check for J_Snooper_G_uid_Val
								Method getFirst = multiValueMap.getClass()
										.getDeclaredMethod("getFirst",
												new Class[] { Object.class });
								Object obj = getFirst.invoke(multiValueMap,
										new Object[] { CONSTANTS.GUID });
								if (obj == null) {
									String guid = getThreadLocal().get()
											.getGuid();
									Method m = multiValueMap.getClass()
											.getDeclaredMethod(
													"putSingle",
													new Class[] { Object.class,
															Object.class });
									m.invoke(multiValueMap, new Object[] {
											CONSTANTS.GUID, guid });
								} else {
									GUIDSelectionFactory.selectMedia(obj
											.toString());
								}

							}
						}
						break;
					}
				}
			}
		} catch (Exception ex) {
			// ignore Exception
		}
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
