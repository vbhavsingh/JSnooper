/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rationalminds.dto;

/**
 *
 * @author Vaibhav Singh
 */
public class CommonTransactionModel {

	private String action;

	private String gmtTime;

	private String methodSignature;

	private String localMachineName;

	private String timeZone;

	private String agentName;

	private String applicationName;

	private String bizTransactionName;

	private String guid;

	private String remoteAddress;

	private String remoteIP;

	private String remotePort;

	private String localPort;

	private String codeStack;

	private String kvJasonMap;

	private String transactionStatus;

	private String errorCode;

	private String errorMessage;

	private String aspectName;

	private String agentBufferSize;

	private String agentBufferLimit;

	public CommonTransactionModel(String guid) {
		this.guid = guid;
	}

	public CommonTransactionModel() {
	}

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

	public String getLocalMachineName() {
		return localMachineName;
	}

	public void setLocalMachineName(String localMachineName) {
		this.localMachineName = localMachineName;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
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

	public String getCodeStack() {
		return codeStack;
	}

	public void setCodeStack(String codeStack) {
		this.codeStack = codeStack;
	}

	public String getBizTransactionName() {
		return bizTransactionName;
	}

	public void setBizTransactionName(String bizTransactionName) {
		this.bizTransactionName = bizTransactionName;
	}

	public String getKvJasonMap() {
		return kvJasonMap;
	}

	public void setKvJasonMap(String kvJasonMap) {
		this.kvJasonMap = kvJasonMap;
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

	public String getAspectName() {
		return aspectName;
	}

	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}

	public String getAgentBufferSize() {
		return agentBufferSize;
	}

	public void setAgentBufferSize(String agentBufferSize) {
		this.agentBufferSize = agentBufferSize;
	}

	public String getAgentBufferLimit() {
		return agentBufferLimit;
	}

	public void setAgentBufferLimit(String agentBufferLimit) {
		this.agentBufferLimit = agentBufferLimit;
	}

	@Override
	public String toString() {
		return "CommonTransactionModel [action=" + action + ", gmtTime="
				+ gmtTime + ", methodSignature=" + methodSignature
				+ ", localMachineName=" + localMachineName + ", timeZone="
				+ timeZone + ", agentName=" + agentName + ", applicationName="
				+ applicationName + ", bizTransactionName="
				+ bizTransactionName + ", guid=" + guid + ", remoteAddress="
				+ remoteAddress + ", remoteIP=" + remoteIP + ", remotePort="
				+ remotePort + ", localPort=" + localPort + ", codeStack="
				+ codeStack + ", kvJasonMap=" + kvJasonMap
				+ ", transactionStatus=" + transactionStatus + ", errorCode="
				+ errorCode + ", errorMessage=" + errorMessage
				+ ", aspectName=" + aspectName + ", agentBufferSize="
				+ agentBufferSize + ", agentBufferLimit=" + agentBufferLimit
				+ "]";
	}

}
