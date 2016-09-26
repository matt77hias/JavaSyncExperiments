package readers_writers_monitor_readerspref;

import utils.SleepUtilities;

public class Reader implements Runnable {
	private int i;
	private ReadWriteLock db;
	
	public Reader(ReadWriteLock db, int i) {
		this.db = db;
		this.i = i;
	}
	
	public void run() {
		for (int j=0; j<10; j++) {
			SleepUtilities.nap();
			System.out.println("Reader: " + i + " wants to acquire read lock.");
			db.acquireReadLock();
			System.out.println("Reader: " + i + " acquired read lock.");
			SleepUtilities.nap();
			System.out.println("Reader: " + i + " released read lock.");
			db.releaseReadLock();
		}
	}

}
