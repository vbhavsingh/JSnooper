package net.rationalminds.dto;

import java.util.List;
import java.util.Map;

public class DataCollectionModel {

	private String action;

	private String gmtTime;

	private String methodSignature;

	private String guid;

	private String remoteIP;

	private String remoteAddress;

	private String remotePort;

	private String localPort;

	private String codeStack;

	private String transactionStatus;

	private String errorCode;

	private String errorMessage;

	private List<Object> objectList;

	private String biztransactionName;
	
	private Map<String,String> kvMap;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getGmtTime() {
		return gmtTime;
	}

	public void setGmtTime(String gmtTime) {
		this.gmtTime = gmtTime;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
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

	public String getCodeStack() {
		return codeStack;
	}

	public void setCodeStack(String codeStack) {
		this.codeStack = codeStack;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Object> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	public String getBiztransactionName() {
		return biztransactionName;
	}

	public void setBiztransactionName(String biztransactionName) {
		this.biztransactionName = biztransactionName;
	}

	public Map<String, String> getKvMap() {
		return kvMap;
	}

	public void setKvMap(Map<String, String> kvMap) {
		this.kvMap = kvMap;
	}

	

	
}
