package com.quality.protocol.util;

public class DataUtil {
	public static byte[] intToBytes(int i) {

		byte[] abyte0 = new byte[4];
		abyte0[0] = (byte) (0xff & i);
		abyte0[1] = (byte) ((0xff00 & i) >> 8);
		abyte0[2] = (byte) ((0xff0000 & i) >> 16);
		abyte0[3] = (byte) ((0xff000000 & i) >> 24);
		return abyte0;
	}

	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 24) & 0xFF000000);
		return addr;
	}
}