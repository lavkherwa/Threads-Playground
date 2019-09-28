package com.lav.threads.questions;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadLockExampleUsingReentrantLock {

	ReentrantLock lock1 = new ReentrantLock();
	ReentrantLock lock2 = new ReentrantLock();

	public void execute() {
		new Thread(() -> {
			try {
				process1();
			} catch (InterruptedException e) {
				System.out.println("something wron happened during process 1");
			}
		}).start();

		new Thread(() -> this.process2()).start();

		/*- or we can have a thread define as below as well
		 * 
		 *  new Thread(this::process2).start();
		 *  
		 *  :: implies method of this class
		 *  
		 *  */
	}

	public void process1() throws InterruptedException {
		lock1.lock();
		Thread.sleep(100); // Add some sleep for other thread to achieve lock 2
		lock2.lock();

		System.out.println("process 1 going on");

		lock1.unlock();
		lock2.unlock();
	}

	public void process2() {
		lock2.lock();
		lock1.lock();

		System.out.println("process 2 going on");

		lock2.unlock();
		lock1.unlock();

	}

	public static void main(String[] args) {
		ThreadLockExampleUsingReentrantLock tle1 = new ThreadLockExampleUsingReentrantLock();
		tle1.execute();

	}
}
