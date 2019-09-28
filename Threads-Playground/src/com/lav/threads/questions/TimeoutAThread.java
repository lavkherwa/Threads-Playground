package com.lav.threads.questions;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* Question : Timeout a thread after 10 minutes */
public class TimeoutAThread {

	/*- JAVA threads cannot be killed and they can be only requested to stop
	 * 
	 *  using Interrupts or volatile
	 */

	public static void main(String[] args) {

		/* 1. Runnable attempt */
		{
			Task1 task1 = new Task1();

			ExecutorService es1 = Executors.newFixedThreadPool(2);

			// start the task
			es1.submit(task1);

			// timeout for 10 minutes
			try {
				Thread.sleep(10 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// stop the thread by shutting down the thread pool
			es1.shutdown(); // won't accept new tasks
			/*
			 * running tasks are ATTEMPTED to be stopped [No Guarantee], additional
			 * interrupt handling is required at task implementation
			 */
			es1.shutdownNow();
		}

		/* 2. Callable attempt */
		{
			Task2 task2 = new Task2();

			ExecutorService es2 = Executors.newFixedThreadPool(2);

			Future<String> future = es2.submit(task2);

			try {
				System.out.println(future.get(10, TimeUnit.MINUTES).toString());
			} catch (InterruptedException | ExecutionException e) {
				System.out.println(e.getMessage());
			} catch (TimeoutException e) {
				/*
				 * running tasks are ATTEMPTED to be canceled [No Guarantee], additional
				 * interrupt handling is required at task implementation
				 */
				future.cancel(true);
			}
		}

	}

	public static class Task1 implements Runnable {

		@Override
		public void run() {
			/*
			 * we should keep checking for interrupt and stop current processing of task
			 * because shutdown will internally calls interrupt and won't stop the task
			 * actually
			 */

			while (!Thread.currentThread().isInterrupted()) {
				System.out.println("Task1 start is triggered");
			}

			return;
		}
	}

	public static class Task2 implements Callable<String> {

		@Override
		public String call() throws Exception {
			/*
			 * we should keep checking for interrupt and stop current processing of task
			 * because future.cancel will internally calls interrupt and won't stop the task
			 * actually
			 */
			while (!Thread.currentThread().isInterrupted()) {
				return "Task2 start is triggered";
			}

			return null;
		}
	}

}
