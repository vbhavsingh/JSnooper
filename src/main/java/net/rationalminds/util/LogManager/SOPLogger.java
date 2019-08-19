package net.rationalminds.util.LogManager;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class SOPLogger implements JSnooperLogger {

	private Logger logger;

	private static ConsoleHandler handler = new ConsoleHandler();

	private static JSnooperLogFormatter formatter = new JSnooperLogFormatter();

	private String clazzName;

	public SOPLogger(Class clazz) {
		clazzName = clazz.getName();
		logger = Logger.getLogger(clazzName);
		logger.setUseParentHandlers(false);
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		logger.setLevel(Level.OFF);
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_INFO)) {
			logger.setLevel(Level.INFO);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_FULL)) {
			logger.setLevel(Level.ALL);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_ERROR)) {
			logger.setLevel(Level.SEVERE);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_WARN)) {
			logger.setLevel(Level.WARNING);
		}
	}

	public void debug(Object message) {
		this.logger.logp(Level.INFO, this.clazzName, null, (String) message);
	}

	public void debug(Object message, Throwable t) {
		this.logger.logp(Level.INFO, this.clazzName, null, (String) message, t);
	}

	public void error(Object message) {
		this.logger.logp(Level.SEVERE, this.clazzName, null, (String) message);
	}

	public void error(Object message, Throwable t) {
		this.logger.logp(Level.SEVERE, this.clazzName, null, (String) message,
				t);

	}

	public void info(Object message) {
		this.logger.logp(Level.INFO, this.clazzName, null, (String) message);
	}

	public void info(Object message, Throwable t) {
		this.logger.logp(Level.INFO, this.clazzName, null, (String) message, t);
	}

	public void warn(Object message) {
		this.logger.logp(Level.WARNING, this.clazzName, null, (String) message);
	}

	public void warn(Object message, Throwable t) {
		this.logger.logp(Level.WARNING, this.clazzName, null, (String) message,
				t);
	}
	
	/**
	 * Used by external callers to change the logging mode at runtime
	 */
	public void changeLogMode(){
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_INFO)) {
			logger.setLevel(Level.INFO);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_FULL)) {
			logger.setLevel(Level.ALL);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_ERROR)) {
			logger.setLevel(Level.SEVERE);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_WARN)) {
			logger.setLevel(Level.WARNING);
		}
		if (PropertyManager.LOG_MODE.equals(CONSTANTS.LOG.LOG_LEVEL_NOLOG)) {
			logger.setLevel(Level.OFF);
		}
	}

}
