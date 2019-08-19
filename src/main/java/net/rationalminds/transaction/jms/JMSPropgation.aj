package net.rationalminds.transaction.jms;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class JMSPropgation {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(JMSPropgation.class);
	
	public EndpointModel enrich(Object object,EndpointModel epm){
		String val=null;
		try{
			if (ReflectionUtils.instanceOf("javax.jms.QueueSender", object)){
				javax.jms.QueueSender qs= (javax.jms.QueueSender)object;
				val=qs.getQueue().getQueueName();
			}
			else if (ReflectionUtils.instanceOf("javax.jms.TopicPublisher", object)){
				javax.jms.TopicPublisher tp= (javax.jms.TopicPublisher)object;
				val=tp.getTopic().getTopicName();
			}
			else if (ReflectionUtils.instanceOf("javax.jms.MessageProducer", object)){
				javax.jms.MessageProducer mp=(javax.jms.MessageProducer)object;
				val=(mp.getDestination()==null)?null:mp.getDestination().toString();
			} 
			if(val!=null){
				epm.getKvMap().put("JMS.DESNTINATION", val);
			}
		}catch(Exception e){}
		return epm;
	}

	public void propgateGUID(Object jms) {
		try {
			javax.jms.Message m = null;
			try {
				m = (javax.jms.Message) jms;
			} catch (ClassCastException e) {
				LOGGER.error("", e);
			}
			if (m != null) {
				String uid = GUIDSelectionFactory.selectMedia().getCorrelator();
				if (uid == null) {
					uid = UID.get();
				}
				try {
					m.setStringProperty(CONSTANTS.GUID, uid);
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public String readGUID(Object jms) {
		javax.jms.Message m = null;
		String uid = null;
		try {
			m = (javax.jms.Message) jms;
		} catch (ClassCastException e) {
			LOGGER.error("", e);
		}
		if (m != null) {
			try {
				uid = m.getStringProperty(CONSTANTS.GUID);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}
		return uid;
	}
}
