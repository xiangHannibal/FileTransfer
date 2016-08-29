package com.bh.filetransferclient.test;

public class ByteTest {
	public byte[] i2b(int iSource) {
		byte[] bLocalArr = new byte[4];
		for ( int i = 0; (i < 4) && (i < 4); i++) {  
            bLocalArr[i] = (byte)( iSource>>8*i & 0xFF );  
        }  
		
		return bLocalArr;
	}
	
	private int b2i (byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {  
            int shift = (8 * i);  
            value += (b[i] & 0xFF) << shift;  
        }  
		
		return value;
	}
	
	public static void main(String[] args) {
		ByteTest t = new ByteTest();
		System.out.println(t.b2i(new byte[]{54, 56, 54, 50}));
	}
}
