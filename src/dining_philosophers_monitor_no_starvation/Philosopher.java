package dining_philosophers_monitor_no_starvation;

import utils.SleepUtilities;

public class Philosopher implements Runnable {
	
	private DiningPhilosophersMonitor monitor;
	private int i;
	
	public Philosopher(DiningPhilosophersMonitor monitor, int i) {
		this.monitor = monitor;
		this.i = i;
	}

	@Override
	public void run() {
		for (int j=0; j<10; j++) {
			
			monitor.pickUp(i);
			
			SleepUtilities.nap();
			
			monitor.putDown(i);
		}
	}
}