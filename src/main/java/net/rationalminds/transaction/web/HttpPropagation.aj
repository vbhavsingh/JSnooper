package net.rationalminds.transaction.web;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transaction.guid.UID;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.ERROR_MSG;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;
import net.rationalminds.util.PropertyManager;

public abstract aspect HttpPropagation {
	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(HttpPropagation.class);
	private static List<String> exclusions = new ArrayList<String>();

	/*
	 * public pointcut connect(Object
	 * connection):within(java.net.URLConnection+) && execution(public *
	 * connect(..)) && this(connection);
	 */

	public pointcut connect(): call(* java.net.URL.openConnection(..));

	public pointcut write(Object connection):within(java.net.URLConnection+)
	&& execution(public * getOutputStream(..)) && this(connection);

	public pointcut read(Object connection):within(java.net.URLConnection+)
	&& execution(public * getInputStream(..)) && this(connection);

	/**
	 * 
	 */
	after() returning(java.net.URLConnection conn): connect()
	&& if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.HTTP)){
		String clazz = conn.getClass().getName();
		System.out.println(thisJoinPoint.getSignature().toLongString());
		if (!exclusions.contains(clazz)) {
			java.net.URLConnection c = null;
			try {
				c = (java.net.URLConnection) conn;
			} catch (ClassCastException e) {
				LOGGER.error("ClassCastException, " + clazz, e);
			}
			if (c == null) {
				LOGGER.error("Not able to set connection property");
			} else {
				c.addRequestProperty(CONSTANTS.GUID, "");
			}
		}

	}

	/**
	 * 
	 * @param connection
	 */
	before(Object connection):write(connection)
	&& if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.HTTP)){
		String clazz = connection.getClass().getName();
		if (!exclusions.contains(clazz)) {
			HttpURLConnection c = null;
			try {
				c = (HttpURLConnection) connection;
			} catch (ClassCastException e) {
				LOGGER.error("ClassCastException, " + clazz, e);
			}
			if (c == null) {
				LOGGER.error(ERROR_MSG.HTTP.NULL_CONNECTION_W);
			} else {
				String uid = GUIDSelectionFactory.selectMedia().getCorrelator();
				if (uid == null) {
					uid = UID.get();
				}
				c.setRequestProperty(CONSTANTS.GUID, uid);
				LOGGER.info("GUID set: " + uid);
			}
		}

	}

	/**
	 * 
	 * @param connection
	 */
	before(Object connection):read(connection)
	&& if (PropertyManager.CORRELATION_MODES.contains(CONSTANTS.TECH.HTTP)){
		String clazz = connection.getClass().getName();
		if (!exclusions.contains(clazz)) {
			HttpURLConnection c = null;
			try {
				c = (HttpURLConnection) connection;
			} catch (ClassCastException e) {
				LOGGER.error("ClassCastException, " + clazz, e);
			}
			if (c == null) {
				LOGGER.warn(ERROR_MSG.HTTP.NULL_CONNECTION_R);
			} else {
				String uid = c.getRequestProperty(CONSTANTS.GUID);
				LOGGER.info("GUID fetched: " + uid);
				GUIDSelectionFactory.selectMedia().setCorrelator(uid);
			}
		}

	}

	static {
		exclusions.add("weblogic.utils.zip.ZipURLConnection");
	}
}
