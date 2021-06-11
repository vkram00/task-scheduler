package com.task.executor.models;

import com.task.executor.public_interface.ITask;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class OneTimeTask implements ITask {

	private final Runnable command;
	private final Long delay;
	private final TimeUnit timeUnit;
	private Long executionTime;

	public OneTimeTask(Runnable command, Long delay, TimeUnit timeUnit){
		this.command = command;
		this.delay = delay;
		this.timeUnit = timeUnit;
		this.executionTime = System.currentTimeMillis() + this.timeUnit.toMillis(delay);
	}

	@Override public Long getExecutionTime() {
		return this.executionTime;
	}

	@Override public void execute(ExecutorService executorService) {
		executorService.submit(this.command);
	}
}
