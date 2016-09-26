package readers_writers_monitor_writerspref;

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
	private int writersWaiting;
	
	public Database() {
		readerCount = 0;
		writerIn = false;
		writersWaiting = 0;
		readerCond = monitor.newCondition();
		writerCond = monitor.newCondition();
	}

	@Override
	public void acquireReadLock() {
		monitor.lock();
		try {
		
			if (writerIn) {
				try {
					readerCond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
					++writersWaiting;
					writerCond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				--writersWaiting;
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
			
			if (writersWaiting > 0) {
				writerCond.signal();
			} else {
				readerCond.signalAll();
			}
			
		} finally {
			monitor.unlock();
		}
	}
}
