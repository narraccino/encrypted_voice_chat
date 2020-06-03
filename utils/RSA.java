package com.narracci.securevoice.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSA {

	public static final String ALGORITHM = "RSA";
	public static final String PRIVATE_KEY_B_FILE = "/home/pi/Desktop/privateB.key";
	public static final String PUBLIC_KEY_B_FILE = "/home/pi/Desktop/publicB.key";
	public static final String PUBLIC_KEY_A_FILE = "/home/pi/Desktop/publicA.key";

	public static String exchangeKey(InetAddress address)
			throws UnknownHostException, IOException, ClassNotFoundException {
		boolean unready = true;
		// genero le chiavi
		// System.out.println("GENERO LE CHIAVI RSA");
		generateKey();

		// scambio le chiavi pubbliche
		while (unready) {
			// System.out.println("LOOP SERVER");
			unready = serverB();
		}

		unready = true;
		// System.out.println("CHIAMO CLIENT B");
		while (unready) {
			// System.out.println("LooP CLIENT");
			unready = clientB(address);
		}

		FileInputStream fileInputStream = null;
		ObjectInputStream inputStream = null;

		File file = new File("/home/pi/Desktop/cryptedSymmetric.key");
		byte[] bFile = new byte[(int) file.length()];

		// read file into bytes[]
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);

		// Decrypt the cipher text using the private key.
		inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_B_FILE));
		final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		final String symmetricKey = decrypt(bFile, privateKey);

		return symmetricKey;

	}

	static boolean clientB(InetAddress address) throws UnknownHostException, IOException

	{

		int SOCKET_PORT = 13267;
		// final String SERVER = "192.168.1.99";
		final String FILE_TO_RECEIVED = "/home/pi/Desktop/cryptedSymmetric.key";
		final int FILE_SIZE = 6022;
		boolean flag = true;

		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		try {
			sock = new Socket(address.getHostAddress(), SOCKET_PORT);
			System.out.println("Connessione...");

			// receive file
			byte[] mybytearray = new byte[FILE_SIZE];
			InputStream is = sock.getInputStream();
			fos = new FileOutputStream(FILE_TO_RECEIVED);
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			current = bytesRead;

			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);

			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("File " + FILE_TO_RECEIVED + " scaricato (" + current + " bytes letti)");
			flag = false;
		} finally {
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
			if (sock != null) {
				sock.close();
			}
			return flag;
		}

	}

	static boolean serverB() throws IOException {
		final int SOCKET_PORT = 13267;
		final String FILE_TO_SEND = "/home/pi/Desktop/publicB.key";
		boolean flag = true;

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		ServerSocket servsock = null;
		Socket sock = null;
		try {
			servsock = new ServerSocket(); // create unbound ServerSocket
			servsock.setReuseAddress(true);
			servsock.bind(new InetSocketAddress(SOCKET_PORT));

			// servsock.bind(new InetSocketAddress(13267));
			// while while (true) {

			System.out.println("Attendo client A...");
			try {
				sock = servsock.accept();

				System.out.println("Connessione accettata : " + sock);
				// send file
				File myFile = new File(FILE_TO_SEND);
				byte[] mybytearray = new byte[(int) myFile.length()];
				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);
				bis.read(mybytearray, 0, mybytearray.length);
				os = sock.getOutputStream();
				System.out.println("Invio di " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				System.out.println("Inviato il file che contiente la chiave pubblica di B.");
				flag = false;
			} catch (Exception e) {
				System.out.println("NON RIUSCITO A FAR COLLEGARE SERVER B CON CLIENT A");
			}

			finally {
				if (bis != null)
					bis.close();
				if (os != null)
					os.close();
				if (sock != null) {
					sock.close();
				}

			}
			// while }
		} finally {
			if (servsock != null)
				servsock.close();

			// servsock.setReuseAddress(true);
			return flag;
		}

	}

	public static void generateKey() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();

			File privateKeyFile = new File(PRIVATE_KEY_B_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_B_FILE);

			// Create files to store public and private key
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			// Saving the Public key in a file
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static byte[] encrypt(String text, PublicKey key) {
		byte[] cipherText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());

			System.out
					.println("Bytes stringa: " + text.getBytes().length + "Bytes Testo criptato: " + cipherText.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	public static String decrypt(byte[] text, PrivateKey key) {
		byte[] dectyptedText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText);
	}

}
