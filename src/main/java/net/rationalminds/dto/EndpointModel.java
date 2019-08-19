package net.rationalminds.dto;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;

public class EndpointModel {

	private String remoteMachine;

	private String remoteIP;

	private String remotePort;

	private String localPort;

	private String defferedLocalPort;

	private JoinPoint joinPoint;

	private boolean resetTime;

	private boolean recollectData;

	private Object returnObj;

	private String errorText;

	private String bizName;
	
	private Map<String,String> kvMap;

	public EndpointModel() {
		super();
	}

	public EndpointModel(JoinPoint joinPoint) {
		super();
		this.joinPoint = joinPoint;
	}

	public EndpointModel(JoinPoint joinPoint, boolean resetTime,
			boolean recollectData) {
		super();
		this.joinPoint = joinPoint;
		this.resetTime = resetTime;
		this.recollectData = recollectData;
	}

	public String getRemoteMachine() {
		return remoteMachine;
	}

	public void setRemoteMachine(String remoteMachine) {
		this.remoteMachine = remoteMachine;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	public String getLocalPort() {
		return localPort;
	}

	public void setLocalPort(String localPort) {
		this.localPort = localPort;
	}

	public String getDefferedLocalPort() {
		return defferedLocalPort;
	}

	public void setDefferedLocalPort(String defferedLocalPort) {
		this.defferedLocalPort = defferedLocalPort;
	}

	public JoinPoint getJoinPoint() {
		return joinPoint;
	}

	public void setJoinPoint(JoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	public boolean isResetTime() {
		return resetTime;
	}

	public void setResetTime(boolean resetTime) {
		this.resetTime = resetTime;
	}

	public boolean isRecollectData() {
		return recollectData;
	}

	public void setRecollectData(boolean recollectData) {
		this.recollectData = recollectData;
	}

	public Object getReturnObj() {
		return returnObj;
	}

	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public Map<String, String> getKvMap() {
		if(kvMap == null){
			this.kvMap = new HashMap<String,String>();
		}
		return kvMap;
	}

	public void setKvMap(Map<String, String> kvMap) {
		this.kvMap = kvMap;
	}
	
	
}
