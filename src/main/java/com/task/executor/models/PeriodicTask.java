package com.task.executor.models;

import com.task.executor.public_interface.ITask;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class PeriodicTask implements ITask {

	private final Runnable command;
	private final Long initialDelay;
	private final Long period;
	private final TimeUnit timeUnit;
	private Long executionTime;

	public PeriodicTask(Runnable command, Long initialDelay, Long period, TimeUnit timeUnit){
		this.command = command;
		this.initialDelay = initialDelay;
		this.period = period;
		this.timeUnit = timeUnit;
		this.executionTime = System.currentTimeMillis() + this.timeUnit.toMillis(initialDelay);
	}

	private void updateExecutionTime() {
		this.executionTime += timeUnit.toMillis(period);
	}

	@Override public Long getExecutionTime() {
		return this.executionTime;
	}

	@Override public void execute(ExecutorService executorService) {
		updateExecutionTime();
		executorService.submit(command);
	}
}
