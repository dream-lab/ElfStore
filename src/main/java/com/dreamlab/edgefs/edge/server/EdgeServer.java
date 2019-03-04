package com.dreamlab.edgefs.edge.server;

import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.edge.handler.EdgeServiceHandler;
import com.dreamlab.edgefs.edge.model.Edge;
import com.dreamlab.edgefs.misc.EdgeConstants;
import com.dreamlab.edgefs.thrift.EdgeService;

public class EdgeServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(EdgeServer.class);

	private static EdgeServiceHandler edgeServiceHandler;
	private static EdgeService.Processor eventProcessor;

	public static void main(String[] args) {

		if (args == null)
			return;
		if (args.length < 8) {
			// can give the format here
			LOGGER.error("Minimum 8 arguments are needed");
			return;
		}

		short edgeId = Short.parseShort(args[0]);
		String edgeIp = args[1];
		int edgePort = Integer.parseInt(args[2]);
		int reliability = Integer.parseInt(args[3]);
		String fogIp = args[4];
		int fogPort = Integer.parseInt(args[5]);
		String dataPath = args[6];
		String baseLog = args[7];

		Edge self = new Edge(edgeId, edgeIp, edgePort, (byte) reliability,
				fogIp, fogPort, dataPath, baseLog);

		try {
			edgeServiceHandler = new EdgeServiceHandler(self);
			eventProcessor = new EdgeService.Processor(edgeServiceHandler);

			Runnable edgeRunnable = new Runnable() {

				@Override
				public void run() {
					bootstrapEdge(eventProcessor, edgePort);
				}
			};

			Thread t1 = new Thread(edgeRunnable);
			t1.start();
			
			self.registerEdgeToFog();

			Runnable heartbeatToFog = new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(EdgeConstants.HEARTBEAT_INTERVAL * 1000);
						} catch (InterruptedException e) {
							LOGGER.error("Error occured in edge heartbeat thread " + e);
							e.printStackTrace();
						}
						self.sendHeartbeatToFog();
					}
				}
			};
			
			Thread t2 = new Thread(heartbeatToFog);
			t2.start();

		} catch (Exception e) {
			
		}

	}

	private static void bootstrapEdge(EdgeService.Processor eventProcessor, int edgePort) {
		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(edgePort);
			TServer server = new TNonblockingServer(
					new TNonblockingServer.Args(serverTransport).processor(eventProcessor));

			LOGGER.info("Starting the Edge Server.. ");
			server.serve();
			LOGGER.info("Closed the connection Thrift Server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
