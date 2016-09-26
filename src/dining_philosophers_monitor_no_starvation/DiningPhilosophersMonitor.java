package dining_philosophers_monitor_no_starvation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersMonitor {
	
	private Lock monitor = new ReentrantLock();
	
	public static final int NB_OF_PHILOSOPHERS = 5;
	
	private enum State {THINKING, HUNGRY, EATING}
	private State[] states = new State[NB_OF_PHILOSOPHERS];
	private Condition[] self = new Condition[NB_OF_PHILOSOPHERS];
	
	boolean[] leftHungry = new boolean[NB_OF_PHILOSOPHERS];
	boolean[] rightHungry = new boolean[NB_OF_PHILOSOPHERS];

	public DiningPhilosophersMonitor() {
      for (int i=0;i<5;i++) {
         this.states[i] = State.THINKING;
         System.out.println("Philosopher " + i + " is " + "THINKING");
         this.self[i] = monitor.newCondition();
         
         //NEW
         leftHungry[i] = false;
         rightHungry[i] = false;
      }  
	}   

	public void pickUp(int i) {
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
	      
	      // NEW
	      rightHungry[left(i)] = false;
	      leftHungry[right(i)] = false;
	      
		} finally {
	        monitor.unlock();
	    }
	}

	public void putDown(int i) {
		monitor.lock();
	    try{
	    	
		  this.states[i] = State.THINKING;
		  System.out.println("Philosopher " + i + " is " + "THINKING");
	      
		  // NEW
		  test(left(i));
	      if (this.states[left(i)] == State.HUNGRY)
	         leftHungry[i] = true;
	      
	      test(right(i));
	      if (this.states[right(i)] == State.HUNGRY)
	         rightHungry[i] = true;
	    
	    } finally {
	          monitor.unlock();
	    }
	}

	private void test(int i) {
		monitor.lock();
	    try{
	      if  (
	    		  this.states[right(i)] != State.EATING &&
	    		  this.states[i] == State.HUNGRY &&
	    		  this.states[left(i)] != State.EATING &&
	    		  !leftHungry[i] && 
	    		  !rightHungry[i]
			  ) {
	             this.states[i] = State.EATING;
	             System.out.println("Philosopher " + i + " is " + "EATING");
	             
	 			this.self[i].signal();
	      		}
	      } finally {
	          monitor.unlock();
	      }
	}
 
	private int left(int i) {
		return (i+1) % NB_OF_PHILOSOPHERS;
	}    

	private int right(int i) {
		return (i+4) % NB_OF_PHILOSOPHERS;
	}    
}
