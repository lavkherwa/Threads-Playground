package com.lav.threads.questions;

import java.util.UUID;

/*
 * Thread variables are local to a specific thread 
 * and are therefore thread safe, this is introduced to improve 
 * efficiency and maintain integrity
 * 
 * Spring uses this at a lot of places
 *   - SecurityContextHolder
 *   - RequestContextHolder
 *   - LocaleContextHolder
 * 
 */

public class ThreadLocalExample {

	public static ThreadLocal<String> threadVariable = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				System.out.println("Value of thread local variable in current thread before is: "
						+ ThreadLocalExample.threadVariable.get());
				ThreadLocalExample.threadVariable.set(UUID.randomUUID().toString());
				System.out.println("Value of thread local variable in current thread after is: "
						+ ThreadLocalExample.threadVariable.get());

				// After this remove the value from thread local
				ThreadLocalExample.threadVariable.remove();
			}

			).start();
		}

	}
}
