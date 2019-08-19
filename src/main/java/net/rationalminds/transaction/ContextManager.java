package net.rationalminds.transaction;

import weblogic.workarea.PrimitiveContextFactory;
import weblogic.workarea.PropagationMode;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;
import net.rationalminds.transaction.guid.GUIDPropgation;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class ContextManager implements GUIDPropgation {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(ContextManager.class);

	/**
	 * Set GUID into work context
	 */
	public WorkContextMap getContext() {
		WorkContextMap map = null;
		try {
			map = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
			// if context is null set new GUID into context
			if (map != null) {
				WorkContext localwc = map.get(CONSTANTS.GUID);
				if (localwc == null) {
					WorkContext stringContext = PrimitiveContextFactory
							.create(UID.get());
					try {
						map.put(CONSTANTS.GUID, stringContext,
								PropagationMode.ONEWAY | PropagationMode.RMI
										| PropagationMode.JMS_QUEUE
										| PropagationMode.SOAP);
					} catch (Exception e) {
						LOGGER.error("NOT ABLE TO SET UID", e);
					}
				}

			}
			LOGGER.info("Guid seeeked::" + map.get(CONSTANTS.GUID));
			return map;

		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * Remove GUID from context
	 * 
	 * @return
	 */
	public WorkContextMap removeContext() {
		WorkContextMap map = null;
		String guid = "";
		try {
			map = WorkContextHelper.getWorkContextHelper().getWorkContextMap();

			if (map != null) {
				WorkContext localwc = map.get(CONSTANTS.GUID);
				guid = localwc.toString();
				if (localwc != null) {
					map.remove(CONSTANTS.GUID);
				}
			}
			LOGGER.info("Guid removed::" + guid);
			return map;

		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * Overloaded from GUIDPropgation to get guid value
	 */
	public String getCorrelator() {
		ContextManager ctx = new ContextManager();
		WorkContextMap map = ctx.getContext();
		if (map != null) {
			return map.get(CONSTANTS.GUID).toString();
		}
		return null;

	}

	/**
	 * Overloaded from GUIDPropgation to set guid value
	 */
	public void setCorrelator(String value) {
		WorkContextMap map = null;
		try {
			map = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
			// if context is null set new GUID into context
			if (map != null) {
				WorkContext localwc = map.get(CONSTANTS.GUID);
				if (localwc == null) {
					WorkContext stringContext = PrimitiveContextFactory
							.create(value);
					try {
						map.put(CONSTANTS.GUID, stringContext,
								PropagationMode.ONEWAY | PropagationMode.RMI
										| PropagationMode.JMS_QUEUE
										| PropagationMode.SOAP);
					} catch (Exception e) {
						LOGGER.error("NOT ABLE TO SET UID", e);
					}
				}

			}
			LOGGER.info("Guid Set into Context::" + map.get(CONSTANTS.GUID));

		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
}
