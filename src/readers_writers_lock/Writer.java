package readers_writers_lock;

import utils.SleepUtilities;

public class Writer implements Runnable {
	
	private int i;
	private ReadWriteLock db;
	
	public Writer(ReadWriteLock db, int i) {
		this.db = db;
		this.i = i;
	}
	
	public void run() {
		for (int j=0; j<10; j++) {
			SleepUtilities.nap();
			System.out.println("Writer: " + i + " wants to acquire write lock.");
			db.acquireWriteLock();
			System.out.println("Writer: " + i + " acquired write lock.");
			SleepUtilities.nap();
			System.out.println("Writer: " + i + " released write lock.");
			db.releaseWriteLock();
		}
	}
}
