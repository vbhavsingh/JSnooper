package net.rationalminds.transaction.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class JaxWSJava6 {

	private static JSnooperLogger LOGGER = LogFactory.getLogger(JaxWSJava6.class);

	public void publicpropgateGuid(Object obj, EndpointModel epm, String uid) {
		if (PropertyManager.CORRELATION_MODES
				.contains(CONSTANTS.TECH.JAX_WS_JAVA6)) {
			try {
				Map httpHeader = new HashMap();
				httpHeader.put(CONSTANTS.GUID, Collections.singletonList(uid));
				BindingProvider bp = (BindingProvider) obj;
				//BindingProvider ENDPOINT_ADDRESS_PROPERTY
				bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS,
						httpHeader);
				Object address = bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				if(address !=null ){
					epm.setBizName(address.toString());
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		}
	}

	public EndpointModel enrich(EndpointModel epm, Object obj) {
		
		return epm;
	}

}
