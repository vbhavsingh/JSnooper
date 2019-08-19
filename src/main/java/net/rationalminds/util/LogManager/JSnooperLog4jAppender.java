package net.rationalminds.util.LogManager;

import java.io.IOException;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class JSnooperLog4jAppender {

	private static RollingFileAppender rolingAppender;

	public JSnooperLog4jAppender() throws UndefinedLoggger {
		super();
		setDefaults();
	}

	public RollingFileAppender getRollingFileAppender() {
		return rolingAppender;
	}

	private static void setDefaults() throws UndefinedLoggger {
		String logFileName;
		PatternLayout pattern;
		String layout = "%d{yyyy-MM-dd HH:mm:ss} [%c] (%t) %m%n";
		pattern = new PatternLayout(layout);
		logFileName = PropertyManager.serachParam(CONSTANTS.LOG.LOG_PATH);
		if (logFileName == null) {
			throw new UndefinedLoggger("Log file not defined");
		} else {
			try {
				rolingAppender = new RollingFileAppender(pattern, logFileName);
			} catch (IOException e) {
				throw new UndefinedLoggger(e);
			}
		}
		setLogProperties();
	}

	private static void setLogProperties() throws UndefinedLoggger {
		String logMaxSize;
		logMaxSize = PropertyManager.serachParam(CONSTANTS.LOG.LOG_SIZE);
		if (logMaxSize == null) {
			logMaxSize = CONSTANTS.LOG.LOG_SIZE_DEFAULT;
		}
		rolingAppender.setMaxFileSize(logMaxSize);
		Integer logArchiveCount;
		logArchiveCount = PropertyManager
				.getInteger(CONSTANTS.LOG.LOG_ARCHIVE_SIZE);
		if (logArchiveCount == null) {
			rolingAppender
					.setMaxBackupIndex(CONSTANTS.LOG.LOG_ARCHIVE_SIZE_DEFAULT);
		} else {
			rolingAppender.setMaxBackupIndex(logArchiveCount.intValue());
		}
		if (rolingAppender == null) {
			throw new UndefinedLoggger("Appender not intitlized");
		}
	}
}
