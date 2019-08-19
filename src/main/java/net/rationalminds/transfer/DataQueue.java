package net.rationalminds.transfer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.rationalminds.util.PropertyManager;

public class DataQueue extends Thread {

	private static DataQueue dataQueue;

	private static ArrayBlockingQueue<TransferItf> dataPool;

	private static ConsumerThreadPool ctp;

	private DataQueue() {
		dataPool = new ArrayBlockingQueue<TransferItf>(
				PropertyManager.POOL_SIZE);
		ctp = new ConsumerThreadPool(dataPool);
		
		start();	
	}

	public static BlockingQueue<TransferItf> getDataQueue() {
		if (dataQueue == null) {
			dataQueue = new DataQueue();

		}
		return dataPool;
	}

	public void run() {
		try {
			while (true) {
				ctp.runTask(dataPool.take());
			}
		} catch (InterruptedException iex) {
		}
	}

}
