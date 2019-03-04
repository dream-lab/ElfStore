package com.dreamlab.edgefs.model;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;

public class CheckerTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckerTask.class);
	
	private Fog fog;
	private FogServiceHandler handler;
	private ThreadPoolExecutor executor;
	private BlockingQueue<Runnable> queue;
	
	public CheckerTask(Fog fog, ThreadPoolExecutor exec, FogServiceHandler handler,
			BlockingQueue<Runnable> queue) {
		super();
		this.fog = fog;
		this.executor = exec;
		this.handler = handler;
		this.queue = queue;
	}
	
	
	@Override
	public void run() {
		while (true) {
			try {
				// will be configurable
				Thread.sleep(150000);
			} catch (InterruptedException e) {
				LOGGER.error("Exception : " + e);
				e.printStackTrace();
			}
			LOGGER.info("Going to check if there is a dead edge");
			for (Entry<Short, EdgeInfo> entry : fog.getLocalEdgesMap().entrySet()) {
				EdgeInfo edgeInfo = entry.getValue();
				//should not check here only for status "A". Either check for
				//both !"A"(Active) and !"R"(dead edge in microbatch Recovery mode)
				//or check for "D" (Dead edge where microbatch recovery not started yet)
				if (edgeInfo != null && edgeInfo.getStatus().equals("D")) {
					LOGGER.info("Found a dead edge");
					List<String> mbList = fog.getEdgeMicrobatchMap().get(entry.getKey());
					if (mbList != null) {
						LOGGER.info("Dead EdgeId: " + edgeInfo.getNodeId() + " had " + mbList.size()
								+ " microbatches, adding to executor queue at :" + System.currentTimeMillis());
						int size = mbList.size();
						for (int i = 0; i < size; i++) {
							executor.execute(
									new RecoverTask(edgeInfo.getStats().getReliability(), mbList.get(i), handler));
						}
					}
					//once all microbatches of the dead edge are added to the executor queue
					//of the recovery thread pool, mark status as "R" so that checker doesn't
					//pick this dead edge again for recovery
					edgeInfo.setStatus("R");
				}
			}
		}
	}
	
	
}
