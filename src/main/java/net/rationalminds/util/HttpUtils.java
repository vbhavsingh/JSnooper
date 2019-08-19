package net.rationalminds.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transfer.ReflectionUtils;

public class HttpUtils {
	
	public static List<String> methodNames;
	
	/**
	 * 
	 * @param objectList
	 */
	public static String xxxgetRequestKVMap(Object object) {
		if(object != null){
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(object);
			return getRequestKVMap(objectList);
		}
		return null;
		
	}
	/**
	 * 
	 * @param objectList
	 */
	public static String getRequestKVMap(List<Object> objectList) {
		if (objectList == null) {
			return null;
		}
		StringBuilder builder = null;
		for (Object object : objectList) {
			if (ReflectionUtils.instanceOf("javax.servlet.http.HttpServletRequest", object)) {
				builder = new StringBuilder();
				builder.append("{\"httpRequestObject\":{");
				for (String methodName : methodNames) {
					Object valueObject=ReflectionUtils.invokeMethodObject(methodName, object);
					if(valueObject != null){
						if(ReflectionUtils.isPrimitive(valueObject)){
							String value=String.valueOf(valueObject);
							methodName=methodName.substring(3);
							builder.append("\""+ methodName+"\""+":"+ " \""+value+"\",");
						}
						if(ReflectionUtils.instanceOf("java.util.Map", valueObject)){
							Map<String, String> objectMap = (Map) valueObject;
							methodName=methodName.substring(3);
							builder.append("\""+ methodName+"\""+":{");
							for(Map.Entry<String, String> entry:objectMap.entrySet()){
								String key=entry.getKey()==null?"":String.valueOf(entry.getKey());
								String value=entry.getValue()==null?"":String.valueOf(entry.getValue());
								builder.append("\""+ key+"\""+":"+ " \""+value+"\",");
							}
							builder.append("}");
						}
					}
				}
				builder.append("}}");
				break;
			}
		}
		if(builder != null){
			return builder.toString();
		}
		return null;
	}
	
	public static Map<String,String> insertHttpParamsIntoMap( Object object){
		if(object != null){
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(object);
			return  insertHttpParamsIntoMap(null,objectList);
		}
		return null;
	}
	
	
	public static Map<String,String> insertHttpParamsIntoMap(List<Object> objectList){
		return  insertHttpParamsIntoMap(null,objectList);
	}
	
	private static Map<String,String> insertHttpParamsIntoMap( Map<String,String> thisMap, List<Object> objectList){
		if(objectList==null){
			return null;
		}
		if(thisMap==null){
			thisMap=new HashMap<String, String>();
		}
		for (Object object : objectList) {
			if (ReflectionUtils.instanceOf("javax.servlet.http.HttpServletRequest", object)) {
				for (String methodName : methodNames) {
					Object valueObject=ReflectionUtils.invokeMethodObject(methodName, object);
					if(valueObject != null){
						if(ReflectionUtils.isPrimitive(valueObject)){
							String value=String.valueOf(valueObject);
							methodName=methodName.substring(3);
							thisMap.put(methodName, value);
						}
						if(ReflectionUtils.instanceOf("java.util.Map", valueObject)){
							Map<String, Object> objectMap = (Map) valueObject;
							for(Map.Entry<String, Object> entry:objectMap.entrySet()){
								if(ReflectionUtils.isPrimitive(entry.getValue())){
									String key=entry.getKey()==null?"":String.valueOf(entry.getKey());
									String value=entry.getValue()==null?"":String.valueOf(entry.getValue());
									thisMap.put(key, value);
								}
							}
						}
					}
				}
				break;
			}
		}
		return thisMap;
	}
	
	public static  EndpointModel enrichWithIPPortData(EndpointModel epm, JoinPoint jp){
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(jp.getThis());
		objectList.add(jp.getTarget());
		
		if(jp.getArgs()!=null && jp.getArgs().length>0){
			objectList.addAll(Arrays.asList(jp.getArgs()));
		}
		
		epm=enrichWithIPPortData(epm, objectList);
		return epm;
	}
	
	/**
	 * 
	 * @param epm
	 * @param jp
	 * @return
	 */
	public static  EndpointModel enrichWithIPPortData(EndpointModel epm, Object object)
	{
		if(object != null)
		{
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(object);
			return enrichWithIPPortData(epm, objectList);
		}
		return epm;
		
	}
	/**
	 * 
	 * @param epm
	 * @param jp
	 * @return
	 */
	public static  EndpointModel enrichWithIPPortData(EndpointModel epm, List<Object> objectList){
		if(objectList==null){
			return epm;
		}		
		for (Object object : objectList){
			if(ReflectionUtils.instanceOf("javax.servlet.http.HttpServletRequest", object)){
				Object remoteHost = ReflectionUtils.invokeMethodObject("getRemoteHost", object);
            	Object remoteAddr = ReflectionUtils.invokeMethodObject("getRemoteAddr", object);
				Object remotePort = ReflectionUtils.invokeMethodObject("getRemotePort", object);
				Object localPort = ReflectionUtils.invokeMethodObject("getLocalPort", object);
				Object requestURL = ReflectionUtils.invokeMethodObject("getRequestURL", object);
				
				if(remoteHost != null){
					epm.setRemoteMachine(remoteHost.toString());
				}
				if(remoteAddr != null){
					epm.setRemoteIP(remoteAddr.toString());
				}
				if(remotePort != null){
					epm.setRemotePort(remotePort.toString());
				}
				if(localPort != null){
					epm.setLocalPort(localPort.toString());
				}
				if(requestURL != null){
					epm.setBizName(requestURL.toString());
				}
			}
		}
		
		return epm;
	}

	static {
		methodNames = new ArrayList<String>();
		methodNames.add("getAuthType");
		methodNames.add("getContextPath");
		methodNames.add("getMethod");
		methodNames.add("getPathInfo");
		methodNames.add("getPathTranslated");
		methodNames.add("getQueryString");
		methodNames.add("getRemoteUser");
		methodNames.add("getRequestedSessionId");
		methodNames.add("getRequestURI");
		methodNames.add("getRequestURL");
		methodNames.add("getServletPath");

		methodNames.add("getContentType");
		methodNames.add("getParameterMap");
		methodNames.add("getProtocol");
		
	}

}
