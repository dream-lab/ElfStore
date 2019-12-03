package com.dreamlab.edgefs.misc;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.constants.BloomFilter;
import com.dreamlab.edgefs.constants.Constants;

public class BloomFilter {

	private MessageDigest md;
	private static final Logger LOGGER = LoggerFactory.getLogger(BloomFilter.class);

	private BloomFilter() {
		try {
			this.md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private MessageDigest getMd() {
		return md;
	}

	/**
	 * we store the key value pairs in the bloomfilter i.e. byte[] by storing the
	 * hash of key:value in the array and looking up the incoming query by again
	 * calculating the hash
	 **/
	public static boolean search(String key, String value, byte[] bfArray) {
		String searchKey = key + ":" + value;
//		LOGGER.info("The key being searched is "+searchKey);
		
		byte[] hash = getHash(searchKey);
		ByteBuffer wrapped = ByteBuffer.wrap(hash);
		int byteToSet = wrapped.getInt() % Constants.DYNAMIC_BLOOM_FILTER_BYTES;
		if(byteToSet<0)
			byteToSet = byteToSet*-1;
		
		if(bfArray.length == Constants.DYNAMIC_BLOOM_FILTER_BYTES) {
//			LOGGER.info(" Dynamic bloomfilter being searched");
			if(1 == bfArray[byteToSet]) {
				return true;
			}
			return false;
		}
		
//		LOGGER.info("Static bloomfilter being searched ");
		for (int i = 0; i < hash.length; i++) {
			byte current = hash[i];
			for (int j = 0; j < 8; j++) {
				if ((current & ((byte) (1 << j))) != 0) {
					/**
					 * current byte has a 1 in the bit j, so corresponding bloomfilter should also
					 * have a 1 if the searched key is present in bfArray
					 **/
					if ((bfArray[i] & ((byte) (1 << j))) == 0)
						return false;
				}
			}
		}
		return true;
	}

	/** In case of microbatchId the entry will be stored in the following format 
	 * microbatchid:17:0 ( a sample 
	 * **/
	public static void storeEntry(String key, String value, byte[] bfArray) {
		String persistedKey = key + ":" + value;		
		byte[] hash = getHash(persistedKey);
		ByteBuffer wrapped = ByteBuffer.wrap(hash);
		int byteToSet = wrapped.getInt() % Constants.DYNAMIC_BLOOM_FILTER_BYTES;
		if(byteToSet<0)
			byteToSet = byteToSet*-1;
		
//		LOGGER.info(" The key being indexed is "+persistedKey+ " hash value "+byteToSet );		
		
		if(bfArray.length == Constants.DYNAMIC_BLOOM_FILTER_BYTES) {
			bfArray[byteToSet] = 1;	
		}else {
			for (int i = 0; i < hash.length; i++) {
				bfArray[i] = (byte) (bfArray[i] | hash[i]);
			}	
		}		
		
	}

	private static byte[] getHash(String str) {
		BloomFilter bFilter = new BloomFilter();
		MessageDigest md = bFilter.getMd();
		md.update(str.getBytes());
		byte[] digest = md.digest();
		return digest;
	}
	
}
