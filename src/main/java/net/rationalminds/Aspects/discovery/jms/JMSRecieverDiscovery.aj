package net.rationalminds.Aspects.discovery.jms;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transaction.jms.JMSPropgation;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public abstract aspect JMSRecieverDiscovery extends JSnooperAspectCore {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(JMSRecieverDiscovery.class);

	public pointcut consumer_1(Object jms) : within(javax.jms.MessageConsumer+) 
	&& execution(public * receive(..)  
	throws javax.jms.JMSException) 
	&& this(jms);

	public pointcut consumer_2(Object jms) : within(javax.jms.MessageConsumer+) 
	&& execution(public * receiveNoWait(..) 
	throws javax.jms.JMSException) 
	&& this(jms);

	public pointcut consumer_3(Object jms) : within(javax.jms.MessageListener+) && execution(public * onMessage(..)) && this(jms);

	after(Object jms)returning(Object returnObject) : (consumer_1(jms) || consumer_2(jms)) && if(CONSTANTS.CONTROL.PROBE_ON) {
		enter();
		if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.JMS)) {
			JMSPropgation _prop = new JMSPropgation();
			Object obj = returnObject;
			String guid = null;
			if (obj != null) {
				try {
					if (obj instanceof javax.jms.Message) {
						guid = _prop.readGUID(obj);
						GUIDSelectionFactory.selectMedia().setCorrelator(guid);
					}
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			LOGGER.info("Guid via JMS: " + guid);
			EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
			startWithSend(epm);
			epm.setReturnObj(returnObject);
			exitWithSuccessAndSend(epm);
			exit();
		}
	}

	after(Object jms) throwing(Throwable e) : (consumer_1(jms) || consumer_2(jms)) && if(CONSTANTS.CONTROL.PROBE_ON) {
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

	before(Object jms) : consumer_3(jms) && if(CONSTANTS.CONTROL.PROBE_ON) {
		if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.JMS)) {
			JMSPropgation _prop = new JMSPropgation();
			Object[] obj = thisJoinPoint.getArgs();
			String guid = null;
			if (obj != null && obj.length > 0) {
				try {
					for (int i = 0; i < obj.length; i++) {
						if (obj[i] instanceof javax.jms.Message) {
							guid = _prop.readGUID(obj[i]);
						}
					}
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			LOGGER.info("Guid via JMS: " + guid);
			GUIDSelectionFactory.selectMedia().setCorrelator(guid);
		}
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
	}

	after(Object jms) returning(Object returnObject) : consumer_3(jms)  && if(CONSTANTS.CONTROL.PROBE_ON) {
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	after(Object jms) throwing(Throwable e) : consumer_3(jms) && if(CONSTANTS.CONTROL.PROBE_ON) {
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}

}
