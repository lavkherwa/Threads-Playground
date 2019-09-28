package com.lav.threads.questions;

public class ThreadLockExampleUsingSyncKeyword {

	String resource1 = "a";
	String resource2 = "b";

	public void execute() {
		new Thread(() -> {
			synchronized (resource1) {
				System.out.println("lock achieved on thread" + Thread.currentThread().getName());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("something went wrong in thread 1");
				}
				synchronized (resource2) {
					System.out.println(resource1 + resource2);
				}
			}
		}).start();

		new Thread(() -> {
			synchronized (resource2) {
				System.out.println("lock achieved on thread" + Thread.currentThread().getName());
				synchronized (resource1) {
					System.out.println(resource1 + resource2);
				}
			}
		}).start();
	}
	
	

	public static void main(String[] args) {
		ThreadLockExampleUsingSyncKeyword texmp = new ThreadLockExampleUsingSyncKeyword();
		texmp.execute();
	}

}
