package net.rationalminds.transaction.web;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class JaxRRCJava45 {

	public EndpointModel propagate(Object obj, EndpointModel epm, String uid) {
		if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.JAX_RPC)) {
			if (obj instanceof javax.xml.rpc.handler.MessageContext) {
				SOAPMessageContext soapContext = (SOAPMessageContext) obj;
				SOAPMessage message = soapContext.getMessage();
				MimeHeaders hd = message.getMimeHeaders();
				hd.addHeader(CONSTANTS.GUID, uid);
			}
		}
		return epm;
	}

}
