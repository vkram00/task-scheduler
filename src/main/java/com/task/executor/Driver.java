package com.task.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Driver {

	private static Runnable getRunnableTask(String s) {
		return () -> {
			System.out.println(s +" started at " + System.currentTimeMillis() / 1000);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(s +" ended at " + System.currentTimeMillis() / 1000);
		};
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ScheduledExecutorServiceImpl taskExecutor = new ScheduledExecutorServiceImpl(8);
		Runnable task1 = getRunnableTask("Task1");
		taskExecutor.schedule(task1, 1, TimeUnit.SECONDS);
		Runnable task2 = getRunnableTask("Task2");
		taskExecutor.scheduleAtFixedRate(task2,1, 2, TimeUnit.SECONDS);
		Runnable task3 = getRunnableTask("Task3");
		taskExecutor.scheduleWithFixedDelay(task3,3,2,TimeUnit.SECONDS);
		taskExecutor.execute();
		/*Runnable task4 = getRunnableTask("Task4");
		taskExecutor.scheduleAtFixedRate(task4,1, 2, TimeUnit.SECONDS);*/

	}
}
