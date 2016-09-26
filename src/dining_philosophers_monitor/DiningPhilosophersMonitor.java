package dining_philosophers_monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersMonitor {
	
	private Lock monitor = new ReentrantLock();
	
	public static final int NB_OF_PHILOSOPHERS = 5;
	
	private enum State {THINKING, HUNGRY, EATING}
	private State[] states = new State[NB_OF_PHILOSOPHERS];
	private Condition[] self = new Condition[NB_OF_PHILOSOPHERS];
	
	public DiningPhilosophersMonitor() {
		
		for (int i=0; i<NB_OF_PHILOSOPHERS; i++) {
			this.states[i] = State.THINKING;
			System.out.println("Philosopher " + i + " is " + "THINKING");
			this.self[i] = monitor.newCondition();
		}
	}
	
	public void takeForks(int i) {
		monitor.lock();
	    try{
			this.states[i] = State.HUNGRY;
			System.out.println("Philosopher " + i + " is " + "HUNGRY");
			
			test(i);
			
			if (this.states[i] != State.EATING) {
				try {
					System.out.println("Philosopher " + i + " is " + "WAITING");
					
					this.self[i].await();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	    } finally {
            monitor.unlock();
        }
	}
	
	public void returnForks(int i) {
		monitor.lock();
        try{
        	this.states[i] = State.THINKING;
        	System.out.println("Philosopher " + i + " is " + "THINKING");
		
        	test((i+NB_OF_PHILOSOPHERS-1) % NB_OF_PHILOSOPHERS);
        	test((i+1) % NB_OF_PHILOSOPHERS);
        } finally {
            monitor.unlock();
        }
	}
	
	private void test(int i) {
		if (
				this.states[(i+NB_OF_PHILOSOPHERS-1) % NB_OF_PHILOSOPHERS] != State.EATING &&
				this.states[i] == State.HUNGRY &&
				this.states[(i+1) % NB_OF_PHILOSOPHERS] != State.EATING
			) {
			
			this.states[i] = State.EATING;
			System.out.println("Philosopher " + i + " is " + "EATING");
			
			this.self[i].signal();
		}
	}
}
