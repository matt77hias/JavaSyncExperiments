package dining_philosophers_monitor_sync;

public class Init {

	public static void main(String[] args) {
		DiningPhilosophersMonitor monitor = new DiningPhilosophersMonitor();
		for (int i=0; i<DiningPhilosophersMonitor.NB_OF_PHILOSOPHERS; i++) {
			new Thread(new Philosopher(monitor, i)).start();
		}
	}
}
