package com.narracci.securevoice.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RC4 {

	static String initkey;
	private static String algorithm = "RC4";

	public RC4(InetAddress ip) throws UnknownHostException, IOException, ClassNotFoundException {
		this.initkey= RSA.exchangeKey(ip);
	}

	public static byte[] encrypt(byte[] plainData, int offset, int length) throws Exception {
		// create a binary key from the argument key (seed)

		Cipher cipher = Cipher.getInstance(algorithm);
		SecretKeySpec key = new SecretKeySpec(initkey.getBytes("UTF-8"), "RC4");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(plainData, offset, length);
	}

	public static byte[] decrypt(byte[] plainData, int offset, int length) throws Exception {

		Cipher cipher = Cipher.getInstance(algorithm);
		SecretKeySpec key = new SecretKeySpec(initkey.getBytes("UTF-8"), "RC4");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(plainData, offset, length);
	}
}
