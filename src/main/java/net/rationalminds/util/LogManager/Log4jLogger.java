package net.rationalminds.util.LogManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class Log4jLogger implements net.rationalminds.util.LogManager.JSnooperLogger {

	private static Logger logger;

	public Log4jLogger(Class clazz) throws UndefinedLoggger {
		try {
			this.logger = Logger.getLogger(clazz);
			final JSnooperLog4jAppender appender = new JSnooperLog4jAppender();
			this.logger.addAppender(appender.getRollingFileAppender());
			logger.setLevel(setLevel());

		} catch (Throwable t) {
			throw new UndefinedLoggger(t);
		}
	}

	public void error(Object message) {
		this.logger.error(message);
	}

	public void error(Object message, Throwable t) {
		this.logger.error(message, t);
	}

	public void warn(Object message) {
		this.logger.warn(message);
	}

	public void warn(Object message, Throwable t) {
		this.logger.warn(message, t);
	}

	public void info(Object message) {
		this.logger.info(message);
	}

	public void info(Object message, Throwable t) {
		this.logger.info(message, t);
	}

	public void debug(Object message) {
		this.logger.debug(message);
	}

	public void debug(Object message, Throwable t) {
		this.logger.debug(message, t);
	}

	private Level setLevel() {
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_INFO)) {
			return Level.INFO;
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_FULL)) {
			return Level.ALL;
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_ERROR)) {
			return Level.ERROR;
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_WARN)) {
			return Level.WARN;
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_FATAL)) {
			return Level.FATAL;
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_SEVERE)) {
			return Level.FATAL;
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_NOLOG)) {
			return Level.OFF;
		}
		return Level.ERROR;
	}

	private enum LEVEL {
		nolog, logall, error, warn, others, trace, fatal, servere
	}

}
