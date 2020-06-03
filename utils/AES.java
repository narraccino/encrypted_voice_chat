
package com.narracci.securevoice.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Taha Emara 
 * Website: http://www.emaraic.com 
 * Email : taha@emaraic.com
 * Created on: Jul 30, 2016
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    static String IV = "AAAAAAAAAAAAAAAA";
    static String initkey;
    
    public AES(InetAddress ip) throws UnknownHostException, ClassNotFoundException, IOException
    {
    	this.initkey= RSA.exchangeKey(ip);
    }

    public static byte[] encrypt(byte[] plainData, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "SunJCE");//CBC
        SecretKeySpec key = new SecretKeySpec(initkey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainData, offset, length);
    }

    public static byte[] decrypt(byte[] cipherSound, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "SunJCE");//CBC
        SecretKeySpec key = new SecretKeySpec(initkey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(cipherSound, offset, length);
    }
}
