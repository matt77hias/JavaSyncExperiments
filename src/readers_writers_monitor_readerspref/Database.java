package readers_writers_monitor_readerspref;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Database implements ReadWriteLock {
	
	public static final int NB_OF_READERS = 5;
	public static final int NB_OF_WRITERS = 5;
	
	private Lock monitor = new ReentrantLock();
	
	private Condition readerCond;
	private Condition writerCond;
	
	private int readerCount;
	private boolean writerIn;
	private int readersWaiting;
	
	public Database() {
		readerCount = 0;
		writerIn = false;
		readersWaiting = 0;
		readerCond = monitor.newCondition();
		writerCond = monitor.newCondition();
	}

	@Override
	public void acquireReadLock() {
		monitor.lock();
		try {
		
			if (writerIn) {
				try {
					++readersWaiting;
					readerCond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				--readersWaiting;
			}
			
			++readerCount;
		
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void acquireWriteLock() {
		monitor.lock();
		try {
		
			if (readerCount > 0 || writerIn) {
				try {
					writerCond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			writerIn = true;
		
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void releaseReadLock() {
		monitor.lock();
		try {
		
			--readerCount;
			if (readerCount == 0) {
				writerCond.signal();
			}
		
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void releaseWriteLock() {
		monitor.lock();
		try {
			
			writerIn = false;
			
			if (readersWaiting > 0) {
				readerCond.signalAll();
			} else {
				writerCond.signal();
			}
			
		} finally {
			monitor.unlock();
		}
	}
}
