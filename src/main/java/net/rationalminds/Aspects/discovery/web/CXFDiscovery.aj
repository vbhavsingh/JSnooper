package net.rationalminds.Aspects.discovery.web;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.HttpUtils;
import net.rationalminds.util.PropertyManager;

public abstract privileged aspect CXFDiscovery extends JSnooperAspectCore {
	
	// Transaction start point cut
	public abstract pointcut pointCut();
	
	@Override
	before(): pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		//get request object from message
		Object message = thisJoinPoint.getArgs()[0];
		Object request = null;
		if (message != null && PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.CXF_WS) 
				&& ReflectionUtils.instanceOf("org.apache.cxf.message.Message", message)) 
		{
			request = ReflectionUtils.invokeMethodObject("getContextualProperty", message,new Object[]{"HTTP.REQUEST"});
		}
		_read.recieve(request);
		
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm = HttpUtils.enrichWithIPPortData(epm, request);
		epm.setKvMap(HttpUtils.insertHttpParamsIntoMap(request));
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
	after()throwing(Throwable e):pointCut()&& if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}
}
