package com.dreamlab.edgefs;

import org.junit.Test;

import com.dreamlab.edgefs.misc.Constants;

public class EncodeDecodeLogicTest {
	
	@Test
	public void testEncodeDecode() {
		
		byte j =-128;
		for(int i=0;i<256;i++) {
			long x = Constants.interpretByteAsLong(j);
			System.out.println("Encode j :"+ j+" : "+Constants.interpretByteAsLong((byte)j) + " decode j : "+Constants.encodeLongAsByte(x));
			
			j++;
		}
	}

}
