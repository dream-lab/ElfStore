package com.dreamlab.edgefs;

import org.junit.Assert;
import org.junit.Test;

import com.dreamlab.edgefs.misc.BloomFilter;
import com.dreamlab.edgefs.misc.Constants;

public class BloomFilterTest {

	@Test
	public void storeAndSearchTest() {
		byte[] arr = new byte[Constants.BLOOM_FILTER_BYTES];
		BloomFilter.storeEntry("Name", "Sumit", arr);
		Assert.assertEquals(true, BloomFilter.search("Name", "Sumit", arr));
	}
	
	@Test
	public void consolidateBFiltersTest() {
		byte[] arr1 = new byte[Constants.BLOOM_FILTER_BYTES];
		byte[] arr2 = new byte[Constants.BLOOM_FILTER_BYTES];
		BloomFilter.storeEntry("Name", "Sumit", arr1);
		System.out.println("Sumit in arr1 : " + BloomFilter.search("Name", "Sumit", arr1));
		System.out.println("Sheshadri in arr1 : " + BloomFilter.search("Name", "Sheshadri", arr1));
		BloomFilter.storeEntry("Name", "Sheshadri", arr2);
		System.out.println("Sumit in arr2 : " + BloomFilter.search("Name", "Sumit", arr2));
		System.out.println("Sheshadri in arr2 : " + BloomFilter.search("Name", "Sheshadri", arr2));
		
		System.out.println("CONSOLIDATING NOW");
		byte[] arr3 = new byte[Constants.BLOOM_FILTER_BYTES];
		for(int i = 0; i < Constants.BLOOM_FILTER_BYTES; i++)
			arr3[i] = (byte) (arr3[i] | arr1[i]);
		for(int i = 0; i < Constants.BLOOM_FILTER_BYTES; i++)
			arr3[i] = (byte) (arr3[i] | arr2[i]);
		
		System.out.println("Sumit in arr3 : " + BloomFilter.search("Name", "Sumit", arr3));
		System.out.println("Sheshadri in arr3 : " + BloomFilter.search("Name", "Sheshadri", arr3));
	}
}
