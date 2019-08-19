package net.rationalminds.dto;

public class ConnectionDetails {

	private String remoteMachine;

	private String remoteIP;

	private String remotePort;

	private String localPort;

	private boolean outbound;

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

	public boolean isOutbound() {
		return outbound;
	}

	public void setOutbound(boolean outbound) {
		this.outbound = outbound;
	}

}
