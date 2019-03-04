package com.dreamlab.edgefs.misc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BloomFilter {

	private MessageDigest md;

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

	//we store the key value pairs in the bloomfilter i.e. byte[]
	//by storing the hash of key:value in the array and looking
	//up the incoming query by again calculating the hash
	public static boolean search(String key, String value, byte[] bfArray) {
		String searchKey = key + ":" + value;
		
		byte[] hash = getHash(searchKey);
		for (int i = 0; i < hash.length; i++) {
			byte current = hash[i];
			for (int j = 0; j < 8; j++) {
				if ((current & ((byte) (1 << j))) != 0) {
					//current byte has a 1 in the bit j, so corresponding
					//bloomfilter should also have a 1 if the searched key
					//is present in bfArray
					if((bfArray[i] & ((byte) (1 << j))) == 0)
						return false;
				}
			}
		}
		return true;
	}
	
	public static void storeEntry(String key, String value, byte[] bfArray) {
		String persistedKey = key + ":" + value;
		byte[] hash = getHash(persistedKey);
		for(int i = 0; i < hash.length; i++) {
			bfArray[i] = (byte) (bfArray[i] | hash[i]);
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
