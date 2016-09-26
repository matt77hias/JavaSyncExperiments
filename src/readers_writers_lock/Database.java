package readers_writers_lock;

import java.util.concurrent.Semaphore;

public class Database implements ReadWriteLock {
	
	private int readerCount;
	private Semaphore mutex; 	// for reader count
	private Semaphore db; 		// for database
	
	public Database() {
		readerCount = 0;
		mutex = new Semaphore(1);
		db = new Semaphore(1);
	}

	@Override
	public void acquireReadLock() {
		try {
			mutex.acquire();
		
		++readerCount;
		if (readerCount == 1) {
			try {
				db.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			mutex.release();
		}
	}

	@Override
	public void acquireWriteLock() {
		try {
			db.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void releaseReadLock() {
		try {
			mutex.acquire();
		
		--readerCount;
		if (readerCount == 0) {
			db.release();
		}
		
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			mutex.release();
		}
	}

	@Override
	public void releaseWriteLock() {
		db.release();
	}

}
