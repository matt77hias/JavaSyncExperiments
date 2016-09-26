package readers_writers_monitor_nopref;

public interface ReadWriteLock {
	
	public void acquireReadLock();
	
	public void acquireWriteLock();
	
	public void releaseReadLock();
	
	public void releaseWriteLock();
}
