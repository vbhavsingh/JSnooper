package net.rationalminds.transaction.web;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class ApacheHttpPropagation {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(ApacheHttpPropagation.class);

	public EndpointModel propagate(Object obj, EndpointModel epm) {
		if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.APACHE)) {
			try {
				String uid = GUIDSelectionFactory.selectMedia().getCorrelator();
				if (uid == null) {
					uid = UID.get();
				}
				org.apache.http.HttpRequest request;
				if (obj != null) {
					request = (org.apache.http.HttpRequest) obj;
					request.addHeader(CONSTANTS.GUID, uid);
					epm.setRemoteMachine(request.getRequestLine().getUri());
				} else {
					LOGGER.error("Request Object is null");
				}
				GUIDSelectionFactory.selectMedia().setCorrelator(uid);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}
		return epm;
	}
}
