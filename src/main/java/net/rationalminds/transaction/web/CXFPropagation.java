package net.rationalminds.transaction.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.transport.http.AbstractHTTPDestination;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class CXFPropagation {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(CXFPropagation.class);

	public static EndpointModel read(Object obj,EndpointModel epm) {
		if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.CXF_WS)) {
			String guid = null;
			try {
				if (obj instanceof org.apache.cxf.message.Message) {
					org.apache.cxf.message.Message message = (org.apache.cxf.message.Message) obj;
					HttpServletRequest request = (HttpServletRequest) message
							.get(AbstractHTTPDestination.HTTP_REQUEST);
					if (request != null) {
						guid = request.getHeader(CONSTANTS.GUID);
						epm.setRemoteMachine(request.getRemoteHost());
						epm.setRemotePort(String.valueOf(request.getRemotePort()));
						epm.setLocalPort(String.valueOf((request.getLocalPort())));
					}
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}
			if(guid!=null){
				GUIDSelectionFactory.selectMedia(guid);
			}
		}
		return epm;
	}
}
