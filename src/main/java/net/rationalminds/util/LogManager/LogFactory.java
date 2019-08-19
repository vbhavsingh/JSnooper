package net.rationalminds.util.LogManager;

import net.rationalminds.util.PropertyManager;

public class LogFactory {

	public static JSnooperLogger getLogger(Class clazz) {
		JSnooperLogger logger = null;
		if (PropertyManager.LOG_APPENDER.equals("sop")) {
			logger = new SOPLogger(clazz);
		}
		if (PropertyManager.LOG_APPENDER.equals("log4j")) {
			try {
				logger = new Log4jLogger(clazz);
			} catch (UndefinedLoggger e) {
				logger = new SOPLogger(clazz);
				logger.error("Log4j initlization failed");
				logger.error("Switching log to server logs");
				logger.error("Cause: " + e.getLocalizedMessage());
			} catch (NoClassDefFoundError e) {
				logger = new SOPLogger(clazz);
				logger.error("Log4j initlization failed");
				logger.error("Switching log to server logs");
				logger.error("Cause: " + e.getLocalizedMessage());
			}
		}
		return logger;
	}
}
