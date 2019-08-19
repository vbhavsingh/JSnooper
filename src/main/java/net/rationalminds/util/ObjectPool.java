package net.rationalminds.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public abstract class ObjectPool<T> {

	private static JSnooperLogger LOGGER = LogFactory.getLogger(ObjectPool.class);

	private BlockingQueue<T> pool;

	private BlockingQueue<T> deadConns;

	private int maxPool;

	private short connCreated;

	public ObjectPool() {
		if (PropertyManager.getInteger("MAX_THREAD_COUNT") != null) {
			maxPool = PropertyManager.getInteger("MAX_THREAD_COUNT");
			if (maxPool != 1) {
				maxPool = (int) (maxPool * 1.5);
			}
		} else {
			maxPool = 8;
		}
		pool = new ArrayBlockingQueue<T>(maxPool);
		deadConns = new ArrayBlockingQueue<T>(maxPool);
	}

	protected abstract T create();

	public abstract void reconcile() throws InterruptedException;

	public abstract void expire(T o);

	public synchronized T checkOut() {
		LOGGER.info("Jms Pool Size: " + (maxPool - pool.remainingCapacity()));
		LOGGER.info("Dead Pool Size: " + deadConns.size());
		if (connCreated < maxPool) {
			T t = create();
			connCreated++;
			return t;
		} else if (pool.isEmpty()) {
			return null;
		} else {
			try {
				return pool.take();
			} catch (InterruptedException e) {
				LOGGER.error("Not able to acquire <T> from pool", e);
				return null;
			}
		}
	}

	public void checkIn(T t) throws InterruptedException {
		pool.put(t);
	}

	protected void destroyElement(T t) {
		deadConns.offer(t);
	}

	protected void rebuild() {
		while (!deadConns.isEmpty()) {
			try {
				pool.put(deadConns.take());
			} catch (InterruptedException e) {
				LOGGER.error("not able to reinistate dead jms connections", e);
				e.printStackTrace();
			}
		}
	}

}
