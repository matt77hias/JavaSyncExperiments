package dining_philosophers_monitor_sync;

public class DiningPhilosophersMonitor {
	
	public static final int NB_OF_PHILOSOPHERS = 5;
	
	private enum State {THINKING, HUNGRY, EATING}
	private State[] states = new State[NB_OF_PHILOSOPHERS];
	
	public DiningPhilosophersMonitor() {
		
		for (int i=0; i<NB_OF_PHILOSOPHERS; i++) {
			this.states[i] = State.THINKING;
			System.out.println("Philosopher " + i + " is " + "THINKING");
		}
	}
	
	public synchronized void takeForks(int i) {
		this.states[i] = State.HUNGRY;
		System.out.println("Philosopher " + i + " is " + "HUNGRY");
		
		test(i);
		
		while (this.states[i] != State.EATING) {
			try {
				System.out.println("Philosopher " + i + " is " + "WAITING");
				
				wait();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void returnForks(int i) {
    	this.states[i] = State.THINKING;
    	System.out.println("Philosopher " + i + " is " + "THINKING");
	
    	test((i+NB_OF_PHILOSOPHERS-1) % NB_OF_PHILOSOPHERS);
    	test((i+1) % NB_OF_PHILOSOPHERS);
    	notifyAll();
	}
	
	private void test(int i) {
		if (
				this.states[(i+NB_OF_PHILOSOPHERS-1) % NB_OF_PHILOSOPHERS] != State.EATING &&
				this.states[i] == State.HUNGRY &&
				this.states[(i+1) % NB_OF_PHILOSOPHERS] != State.EATING
			) {
			
			this.states[i] = State.EATING;
			System.out.println("Philosopher " + i + " is " + "EATING");
		}
	}
}
