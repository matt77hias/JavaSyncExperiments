package readers_writers_lock;

public interface ReadWriteLock {
	
	public void acquireReadLock();
	
	public void acquireWriteLock();
	
	public void releaseReadLock();
	
	public void releaseWriteLock();
}
