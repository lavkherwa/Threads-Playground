package com.lav.threads.questions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* Parallel make 3 IO operations [Service calls] and gather the info and return using threads */
public class ScatterGatherPattern {

	public static void main(String[] args) {

		Set<Integer> results;
		try {
			results = getFinalResult();

			Integer total = 0;

			for (Integer result : results) {
				total = result + total;
			}

			System.out.println(total / 3);

		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.out.println("Current tasks timed out");
		}

	}

	private static Set<Integer> getFinalResult() throws InterruptedException, ExecutionException, TimeoutException {

		Set<Integer> results = Collections.synchronizedSet(new HashSet<>());

		CompletableFuture<Void> task1 = CompletableFuture.runAsync(new ScatterGatherPattern().new Task(results));
		CompletableFuture<Void> task2 = CompletableFuture.runAsync(new ScatterGatherPattern().new Task(results));
		CompletableFuture<Void> task3 = CompletableFuture.runAsync(new ScatterGatherPattern().new Task(results));

		CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);

		allTasks.get(3, TimeUnit.SECONDS); // wait only for 3 seconds else stop

		return results;

	}

	private class Task implements Runnable {

		private Set<Integer> results;

		public Task(Set<Integer> results) {
			this.results = results;
		}

		@Override
		public void run() {

			try {
				Thread.sleep(4000); // Make task wait for 4 seconds, so it fails
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int result = new Random().nextInt(1000);
			results.add(result);
		}

	}

}
