package net.rationalminds.Aspects.discovery.outgoing.apachehttp3;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;

public abstract aspect ApacheHttp3 extends JSnooperAspectCore {

	public pointcut pointCut();
	
	before() : pointCut() && if(CONSTANTS.CONTROL.PROBE_ON)
	{
		enter();
		//check for HttpMessage Object
		Object[] allArgs = thisJoinPoint.getArgs();
		if(allArgs != null && allArgs.length > 0 )
		{
			//look for HttpMessage object
			for(int i =0;i<allArgs.length;i++)
			{
				if(ReflectionUtils.instanceOf("org.apache.commons.httpclient.HttpMethod", allArgs[i]) && allArgs[i] !=null)
				{
					if(ReflectionUtils.invokeMethodObject("getRequestHeader", allArgs[i], new Object[]{CONSTANTS.GUID}) == null)
					{
						String guid = getThreadLocal().get().getGuid();
						ReflectionUtils.invokeMethod("setRequestHeader", allArgs[i], new Object[]{CONSTANTS.GUID, guid});
						break;
					}
				}
			}
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
