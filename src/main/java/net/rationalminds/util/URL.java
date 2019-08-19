package net.rationalminds.util;

import java.util.Date;

public class URL {
	
	private String uri;
	
	private boolean active;
	
	private Date inactiveSince;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getInactiveSince() {
		return inactiveSince;
	}

	public void setInactiveSince(Date inactiveSince) {
		this.inactiveSince = inactiveSince;
	} 

}
