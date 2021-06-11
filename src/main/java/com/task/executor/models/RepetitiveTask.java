package com.task.executor.models;

import com.task.executor.public_interface.ITask;
import lombok.Getter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Getter
public class RepetitiveTask implements ITask {

	private final Runnable command;
	private final Long initialDelay;
	private final Long delay;
	private final TimeUnit timeUnit;
	private Long executionTime;

	public RepetitiveTask(Runnable command, Long delay, Long initialDelay, TimeUnit timeUnit){
		this.command = command;
		this.delay = delay;
		this.initialDelay = initialDelay;
		this.timeUnit = timeUnit;
		this.executionTime = System.currentTimeMillis() + timeUnit.toMillis(initialDelay);
	}

	private void updateExecutionTime() {
		this.executionTime =  System.currentTimeMillis() + timeUnit.toMillis(delay);
	}

	@Override public Long getExecutionTime() {
		return this.executionTime;
	}

	@Override public void execute(ExecutorService executorService) throws ExecutionException, InterruptedException {
		Future<?> result = executorService.submit(command);
		result.get();
		updateExecutionTime();
	}
}
