package com.dreamlab.edgefs.misc;

public final class Constants {
	public static final String IP = "192.168.1.0";
	public static final String PORT = "9019";
	public static final String CONF_PATH = "./cluster.conf";
//	public static final String CONF_PATH = "./cluster.conf";
//	public static final String CONF_PATH = "./cluster.conf";
	public static final String TWO_PHASE_PRE_COMMIT = "PRE_COMMIT";
	public static final String TWO_PHASE_COMMIT = "COMMIT";
	public static final String RESET = "RESET";
	public static final String STATUS_NO = "NO";
	public static final String STATUS_YES = "YES";
	public static final byte SUCCESS = 1;
	public static final byte  FAILURE = 0;
	public static int KMAX = 5;
	// for now, keeping kmin as 2 .
	public static int KMIN = 2;

	// need to multiply in case of encoding
	public static int STORAGE_CONST = 16;

	// 1 min
	public static long WINDOW_BTW_LOCAL_UPDATES = 60 * 1000;

	// 1 min
	public static long WINDOW_BTW_GLOBAL_UPDATES = 60 * 1000;

	//this is used when median reliability is shifted keeping the
	//median storage fixed while doing local edges calculations
	public static int RELIABILITY_SAMPLE_BUCKETS = 10;

	public static int BUDDY_HEARTBEAT_WINDOW = 20 * 1000;

	public static int SUBSCRIBER_HEARTBEAT_WINDOW = 20 * 1000;
	
	public static int GLOBAL_INFO_SAMPLE_BUCKETS = 100;
	
	
	public static String EDGE_HEARTBEAT_INTERVAL = "edge.heartbeat.interval";
	
	public static String EDGE_DISK_SEND_HEARTBEATS = "edge.disk.send.heartbeats";
	
	public static String LOCAL_STATS_CALC_HEARTBEATS = "local.stats.calc.heartbeats";
	
	public static String EDGE_MISS_HEARTBEATS_MAX = "edge.miss.heartbeats.max";
	
	public static String EDGE_DISK_WATERMARK = "edge.disk.watermark";
	
	public static String BUDDY_HEARTBEAT_INTERVAL = "buddy.heartbeat.interval";
	
	public static String BUDDY_STATS_SEND_HEARTBEATS = "buddy.stats.send.heartbeats";
	
	public static String BUDDY_BLOOM_SEND_HEARTBEATS = "buddy.bloom.send.heartbeats";
	
	public static String SUBS_HEARTBEAT_INTERVAL = "subs.heartbeat.interval";
	
	public static String SUBS_STATS_SEND_HEARTBEATS = "subs.stats.send.heartbeats";
	
	public static String SUBS_BLOOM_SEND_HEARTBEATS = "subs.bloom.send.heartbeats";
	
	public static String GLOBAL_CALC_HEARTBEATS = "global.calc.heartbeats";
	
	public static String GLOBAL_BOOTSTRAP_HEARTBEATS = "global.bootstrap.heartbeats";
	
	public static String BUDDY_STATS_FORCE_SEND_HEARTBEATS = "buddy.stats.force.send.heartbeats";
	
	public static String BUDDY_BLOOM_FORCE_SEND_HEARTBEATS = "buddy.bloom.force.send.heartbeats";
	
	public static String SUBS_STATS_FORCE_SEND_HEARTBEATS = "subs.stats.force.send.heartbeats";
	
	public static String SUBS_BLOOM_FORCE_SEND_HEARTBEATS = "subs.bloom.force.send.heartbeats";
	
	//in case the property EDGE_DISK_WATERMARK is missing, we will be
	//using this value as our default watermark
	public static long CONSTANT_DISK_WATERMARK_EDGE = 100;
	
	public static int EDGE_CONTENTION_SLEEP_TIME = 50;
	
	public static int STATS_BYTES = 10;
	
	//using SHA1 instead of MD5
	public static int BLOOM_FILTER_BYTES = 20;
	
	public static String STREAM_METADATA_ID = "streamId";
	
	public static String STREAM_METADATA_START_TIME = "startTime";
	
	public static String MICROBATCH_METADATA_ID = "mbId";
	
	public static String MICROBATCH_METADATA_TIMESTAMP = "timestamp";
	
	//200MB is the limit when we stop writing to an edge
	public static int DISK_WATERMARK = 200;
	
	public static long interpretByteAsLong(byte b) {
		int diskSpace = (int) b;

		if (diskSpace >= -128 && diskSpace < -27) {
			diskSpace = (diskSpace + 128) * 10;
		} else if (diskSpace >= -27 && diskSpace <= 72) {
			diskSpace =  1000 + (diskSpace + 28) * 100;
		} else {
			diskSpace = 11000 + (diskSpace - 72) * 1000;
		}
		
//		System.out.println("The decode is "+diskSpace);
		return diskSpace;
	}

	// TODO::SWAMIJI to implement
	public static byte encodeLongAsByte(long l) {
		
		double disk_space_in_MB = l;
		double free_space_GB = (double) (disk_space_in_MB/1000.0);
		byte disk_space = 0;
		
	    if((free_space_GB)>66) {
	        disk_space = 127;
	    }

	    //this is for 11GB to 64GB
	    if(free_space_GB >=11.0 && free_space_GB<=66.0) {
	        disk_space = (byte) (free_space_GB + 61);
	    }
	    
	    //this is for 1100MB to 10900MB 
	    if(free_space_GB>=1.0 && free_space_GB < 11.0) {
	        double disk_space_in_100_MB = (disk_space_in_MB/100.0);
	        disk_space = (byte) (disk_space_in_100_MB - 38);
	    }

	    //this is for 0MB to 1000MB
	    if(free_space_GB>= 0.0 && free_space_GB<1.0) {
	        double disk_space_in_10_MB  = (disk_space_in_MB/10.0);
	        disk_space  =  (byte) (disk_space_in_10_MB - 128);
	    }

//	    System.out.println("The disk_space is "+disk_space);
		
		return disk_space;
		
	}
	


}
