package com.task.executor;

import com.task.executor.models.OneTimeTask;
import com.task.executor.models.PeriodicTask;
import com.task.executor.models.RepetitiveTask;
import com.task.executor.public_interface.IScheduledExecutorService;
import com.task.executor.public_interface.ITask;

import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ScheduledExecutorServiceImpl implements IScheduledExecutorService {

	private final ExecutorService executorService;
	private final ReentrantLock lock;
	private final Condition newTaskReceived;
	private final PriorityQueue<ITask> taskQueue;

	public ScheduledExecutorServiceImpl(int workerThreads) {
		executorService = Executors.newFixedThreadPool(workerThreads);
		lock = new ReentrantLock();
		newTaskReceived = lock.newCondition();
		taskQueue = new PriorityQueue<>((x, y) -> Long.compare(x.getExecutionTime(), y.getExecutionTime()));
	}

	public void execute() throws InterruptedException, ExecutionException {
		Long timeToSleep = 0L;
		while (true) {
			lock.lock();
			try {
				while (taskQueue.isEmpty())
					newTaskReceived.await();
				ITask task = taskQueue.peek();
				while (!taskQueue.isEmpty()) {
					timeToSleep = task.getExecutionTime() - System.currentTimeMillis();
					if (timeToSleep <= 0)
						break;
					newTaskReceived.await(timeToSleep, TimeUnit.MILLISECONDS);
				}
				task = taskQueue.poll();
				task.execute(executorService);
				Long newExecTime = task.getExecutionTime();
				if(newExecTime >= System.currentTimeMillis())
					taskQueue.add(task);
			} catch (Exception ex) {
				System.out.println("Task Execution Failed!!!");
				ex.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	@Override public void schedule(Runnable command, long delay, TimeUnit unit) {
		lock.lock();
		try {
			System.out.println("One-Time Task Scheduling");
			ITask scheduledTask = new OneTimeTask(command, delay, unit);
			taskQueue.add(scheduledTask);
			newTaskReceived.signalAll();
		} catch (Exception ex) {
			System.out.println("One-Time Task Scheduling Failed!!!");
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		lock.lock();
		try {
			System.out.println("Periodic Task Scheduling");
			ITask scheduledTask = new PeriodicTask(command, initialDelay, period, unit);
			taskQueue.add(scheduledTask);
			newTaskReceived.signalAll();
		} catch (Exception ex) {
			System.out.println("Periodic Task Scheduling Failed!!!");
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		lock.lock();
		try {
			System.out.println("Repetitive Task Scheduling");
			ITask scheduledTask = new RepetitiveTask(command, delay, initialDelay, unit);
			taskQueue.add(scheduledTask);
			newTaskReceived.signalAll();
		} catch (Exception ex) {
			System.out.println("Repetitive Task Scheduling Failed!!!");
			ex.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
