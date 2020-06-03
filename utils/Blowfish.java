package com.narracci.securevoice.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Blowfish {
	static SecretKey key = null;
	static String initkey;
	static int bit;
	
	public Blowfish(InetAddress ip) throws UnknownHostException, IOException, NoSuchAlgorithmException, ClassNotFoundException {
		String encodedKey= RSA.exchangeKey(ip);
		this.initkey=encodedKey;
	}

	public static byte[] encrypt(byte[] plainData, int offset, int length) throws Exception {
		//System.out.println("Puliti da inviare: "+ plainData.length);
		SecretKeySpec secretKeySpec = new SecretKeySpec(initkey.getBytes(), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] cifrato= cipher.doFinal(plainData, offset, length);
		//System.out.println("cifrati inviati: "+ cifrato.length);
		return cifrato;

	}

	public static byte[] decrypt(byte[] cipherSound, int offset, int length) throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(initkey.getBytes(), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		byte[] decifrato= cipher.doFinal(cipherSound, offset, length);
		return decifrato;
	}

}