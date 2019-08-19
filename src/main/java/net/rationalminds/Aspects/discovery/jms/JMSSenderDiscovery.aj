package net.rationalminds.Aspects.discovery.jms;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.jms.JMSPropgation;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public abstract aspect JMSSenderDiscovery extends JSnooperAspectCore {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(JMSSenderDiscovery.class);

	public pointcut producer_1(Object jms) : within(javax.jms.MessageProducer+) && execution(public * send(..) throws javax.jms.JMSException) && this(jms);

	public pointcut producer_2(Object jms) : within(javax.jms.QueueSender+)  && execution(public * send(..)  throws javax.jms.JMSException) && this(jms);

	public pointcut producer_3(Object jms) : within(javax.jms.TopicPublisher+)  && execution(public * publish(..)  throws javax.jms.JMSException) && this(jms);

	public pointcut producer_4(Object jms) : within(weblogic.jms.client.JMSProducer+) && execution(public * send(..) throws javax.jms.JMSException) && this(jms);
	
	private pointcut avoid(): within(javax.jms.MessageProducer+) && execution(public * send(javax.jms.Message));

	before(Object jms) : !cflowbelow(avoid()) && (producer_1(jms) || producer_2(jms) || producer_3(jms) || producer_4(jms)) && if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.JMS)) {
			JMSPropgation _prop = new JMSPropgation();
			Object[] obj = thisJoinPoint.getArgs();
			if (obj != null && obj.length > 0) {
				try {
					for (int i = 0; i < obj.length; i++) {
						if (obj[i] instanceof javax.jms.Message) {
							_prop.propgateGUID(obj[i]);
							epm=_prop.enrich(thisJoinPoint.getThis(), epm);
						}
					}
				} catch (Exception e) {
					LOGGER.error("", e);
					e.printStackTrace();
				}
			}

		}
		startWithSend(epm);
	}

	after(Object jms) returning(Object returnObject) : !cflowbelow(avoid()) &&  (producer_1(jms) || producer_2(jms) || producer_3(jms) || producer_4(jms)) && if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	after(Object jms) throwing(Throwable e) : !cflowbelow(avoid()) &&  (producer_1(jms) || producer_2(jms) || producer_3(jms) || producer_4(jms)) && if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}
}
