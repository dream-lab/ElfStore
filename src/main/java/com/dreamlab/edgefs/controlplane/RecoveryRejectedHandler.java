package com.dreamlab.edgefs.controlplane;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.model.RecoverTask;

/**
 * This class will deal with the recovery tasks which cannot be handled by the
 * ThreadPoolExecutor as a result of having used up all the threads with no space
 * available in the queue for placing the tasks. Such tasks are rejected and the 
 * method rejectedExecution is invoked then
 * CURRENT: Placing a log statement to validate this is what is causing issue #1
 * FINAL: Make the queue bigger and maybe increase the number of threads for task
 * execution so that tasks are not rejected
 *
 */
public class RecoveryRejectedHandler implements RejectedExecutionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryRejectedHandler.class);
	
	public RecoveryRejectedHandler() {
		
	}
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		RecoverTask recoverTask = (RecoverTask) r;
		String rejectedBlockId = recoverTask.getMicrobatchId();
		LOGGER.info("The blockId : {} is rejected for recovery, hence will not be recovered", rejectedBlockId);
	}

}
