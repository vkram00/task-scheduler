package com.task.executor.public_interface;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public interface ITask {

	public Long getExecutionTime();

	public void execute(ExecutorService executorService) throws ExecutionException, InterruptedException;
}
