package net.rationalminds.transfer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class ConsumerThreadPool {

	private static JSnooperLogger LOGGER = LogFactory
			.getLogger(ConsumerThreadPool.class);

	private static int poolSize = 1;

	private static int maxPoolSize = 1;

	long keepAliveTime = 10;

	ThreadPoolExecutor threadPool = null;

	static {
		setPoolSize();
	}

	public ConsumerThreadPool(BlockingQueue bq) {
		threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS, bq,
				new ThreadPoolExecutor.DiscardPolicy());

	}

	public void runTask(Runnable task) {
		try {
			threadPool.execute(task);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public void shutDown() {
		threadPool.shutdown();
	}

	private static void setPoolSize() {

		final String maximumThreads = CONSTANTS.MAX_THREAD_COUNT;

		final String minimumThreads = CONSTANTS.MIN_THREAD_COUNT;

		/**
		 * Set size of WebService thread pool Default size is 10 and 15 if
		 * maximum < minimum than default size is used
		 */
		if (CONSTANTS.TRANSPORT_WS.equals(PropertyManager.TRANSPORT_MODE)) {
			if (PropertyManager.getInteger(minimumThreads) != null) {
				poolSize = PropertyManager.getInteger(minimumThreads);
			} else {
				poolSize = 10;
			}
			if (PropertyManager.getInteger(maximumThreads) != null) {
				maxPoolSize = PropertyManager.getInteger(maximumThreads);
			} else {
				maxPoolSize = 15;
			}
			if (poolSize > maxPoolSize) {
				poolSize = 10;
				maxPoolSize = 15;
			}
		}
		/**
		 * Size of thread pool in case of JMS transport is 1
		 */
		if (CONSTANTS.TRANSPORT_JMS.equals(PropertyManager.TRANSPORT_MODE)) {
			poolSize = 1;
			maxPoolSize = 1;
		}
		/**
		 * Size of thread pool in case of Servlet transport is 5
		 */
		if (CONSTANTS.TRANSPORT_SERVLET.equals(PropertyManager.TRANSPORT_MODE)) {
			if (PropertyManager.getInteger(minimumThreads) != null) {
				poolSize = PropertyManager.getInteger(minimumThreads);
			} else {
				poolSize = 5;
			}
			if (PropertyManager.getInteger(maximumThreads) != null) {
				maxPoolSize = PropertyManager.getInteger(maximumThreads);
			} else {
				maxPoolSize = 5;
			}
			if (poolSize > maxPoolSize) {
				poolSize = 5;
				maxPoolSize = 5;
			}
		}

	}
}
