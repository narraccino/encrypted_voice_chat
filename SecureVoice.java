package com.narracci.securevoice;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;

import com.narracci.securevoice.utils.AES;
import com.narracci.securevoice.utils.Blowfish;
import com.narracci.securevoice.utils.RC4;
import com.narracci.securevoice.utils.RC5;
import com.narracci.securevoice.utils.RC6;
import com.narracci.securevoice.utils.TwoFish;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SecureVoice {
	static String algorithm;
	static String key = null;
	static int count;
	static float sampleRate;
	static InetAddress ip = null;
	static AES ob = null;
	static Blowfish obb = null;
	static RC4 obrc4 = null;
	static RC5 obrc5 = null;
	static RC6 obrc6 = null;
	static TwoFish obtf = null;
	static Receiver rx = null;
	static Transmitter tx = null;
	static boolean ipcheck;
	static ExecutorService es;

	public static void main(String[] args) throws Exception {

		reset();
		es = Executors.newCachedThreadPool();
		checkAddress();
		insertDestinationAddress();
		chooseQuality();

		Scanner input = new Scanner(System.in);
		Scanner in = new Scanner(System.in);
		System.out.println("Scegli l'algoritmo di crittografia:");
		System.out.print("1)AES\n2)Blowfish\n3)RC4\n4)RC5\n5)RC6\n6)Twofish\n---->");
		int number = input.nextInt();
		switch (number) {
		case 1:
			AESMenu();
			break;
		case 2:
			BlowfishMenu();
			break;
		case 3:
			RC4Menu();
			break;
		case 4:
			RC5Menu();
		case 5:
			RC6Menu();
		case 6:
			TwoFishMenu();
			break;
		default:
			AESMenu();
			break;
		}

		do {
			System.out.println("INSERIRE NUMERO 7 PER USCIRE: --> ");
			number = input.nextInt();
		} while (number != 7);
		rx.ferma();
		tx.ferma();
		es.shutdown();
		System.out.println("++ CHIUSURA PROGRAMMA ++");
		System.exit(0);

	}

	public static AudioFormat getAudioFormat(float sampleRate) { // you may change these parameters to fit you mic

		// 8000,11025,16000,22050,44100

		int sampleSizeInBits = 16; // 8,16
		int channels = 1; // 1,2
		boolean signed = true; // true,false
		boolean bigEndian = false; // true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public static void prepareExcel(float sample) throws Exception {

		File f = new File("/Users/giuseppenarracci/Desktop/" + algorithm + "-" + getSample() + ".xls");
		// File f = new File("/home/pi/Desktop/" + algorithm + "-" + getSample() +
		// ".xls");
		WritableWorkbook myexel = Workbook.createWorkbook(f);
		WritableSheet mysheet = myexel.createSheet("mySheet", 0);
		Label l = new Label(0, 0, "Enc. Time Nanoseconds");
		mysheet.addCell(l);
		myexel.write();
		myexel.close();
	}

	public static String getName() {
		return algorithm;
	}

	public static String getSample() {
		return Integer.toString((int) sampleRate);
	}

	public static void AESMenu() throws Exception {

		Scanner in = new Scanner(System.in);
		System.out.println("Scegli la dimensione della chiave:");
		System.out.print("1)128\n2)192\n3)256");
		System.out.print("---> ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		switch (number) {
		case 1:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "AES-128";
			ob = new AES(ip);
			rx = new Receiver(sampleRate, ob);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, ob);
			es.execute(tx);

			break;
		case 2:
			System.out.println("Hai scelto chiave da 192 bit");
			algorithm = "AES-192";
			ob = new AES(ip);
			rx = new Receiver(sampleRate, ob);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, ob);
			es.execute(tx);
			break;
		case 3:
			System.out.println("Hai scelto chiave da 256 bit");
			algorithm = "AES-256";
			ob = new AES(ip);
			rx = new Receiver(sampleRate, ob);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, ob);
			es.execute(tx);
			break;

		default:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "AES-128";
			ob = new AES(ip);
			rx = new Receiver(sampleRate, ob);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, ob);
			es.execute(tx);
			break;

		} // chiudo switch-case

	}

	public static void BlowfishMenu() throws Exception {

		Scanner in = new Scanner(System.in);
		algorithm = "Blowfish";
		obb = new Blowfish(ip);
		rx = new Receiver(sampleRate, obb);
		es.execute(rx);
		tx = new Transmitter(sampleRate, ip, obb);
		es.execute(tx);
	}

	public static void RC4Menu() throws Exception {
		Scanner in = new Scanner(System.in);
		algorithm = "RC4";
		System.out.println("Hai scelto RC4");
		obrc4 = new RC4(ip);
		rx = new Receiver(sampleRate, obrc4);
		es.execute(rx);
		tx = new Transmitter(sampleRate, ip, obrc4);
		es.execute(tx);
	}

	public static void RC5Menu() throws Exception {

		Scanner in = new Scanner(System.in);
		System.out.println("Scegli la dimensione della chiave:");
		System.out.print("1)128\n2)192\n3)256");
		System.out.print("---> ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		switch (number) {
		case 1:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "RC5-128";
			obrc5 = new RC5(ip);
			rx = new Receiver(sampleRate, obrc5);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc5);
			es.execute(tx);

			break;
		case 2:
			System.out.println("Hai scelto chiave da 192 bit");
			algorithm = "RC5-192";
			obrc5 = new RC5(ip);
			rx = new Receiver(sampleRate, obrc5);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc5);
			es.execute(tx);
			break;
		case 3:
			System.out.println("Hai scelto chiave da 256 bit");
			algorithm = "RC5-256";
			obrc5 = new RC5(ip);
			rx = new Receiver(sampleRate, obrc5);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc5);
			es.execute(tx);
			break;

		default:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "RC5-128";
			obrc5 = new RC5(ip);
			rx = new Receiver(sampleRate, obrc5);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc5);
			es.execute(tx);
			break;

		} // chiudo switch-case

	}

	public static void RC6Menu() throws Exception {

		Scanner in = new Scanner(System.in);
		System.out.println("Scegli la dimensione della chiave:");
		System.out.print("1)128\n2)192\n3)256");
		System.out.print("---> ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		switch (number) {
		case 1:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "RC6-128";
			obrc6 = new RC6(ip);
			rx = new Receiver(sampleRate, obrc6);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc6);
			es.execute(tx);

			break;
		case 2:
			System.out.println("Hai scelto chiave da 192 bit");
			algorithm = "RC6-192";
			obrc6 = new RC6(ip);
			rx = new Receiver(sampleRate, obrc6);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc6);
			es.execute(tx);
			break;
		case 3:
			System.out.println("Hai scelto chiave da 256 bit");
			algorithm = "RC6-256";
			obrc6 = new RC6(ip);
			rx = new Receiver(sampleRate, obrc6);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc6);
			es.execute(tx);
			break;

		default:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "RC6-128";
			obrc6 = new RC6(ip);
			rx = new Receiver(sampleRate, obrc6);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obrc6);
			es.execute(tx);
			break;

		} // chiudo switch-case

	}

	public static void TwoFishMenu() throws Exception {

		Scanner in = new Scanner(System.in);
		System.out.println("Scegli la dimensione della chiave:");
		System.out.print("1)128\n2)192\n3)256");
		System.out.print("---> ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		switch (number) {
		case 1:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "TwoFish-128";
			obtf = new TwoFish(ip);
			rx = new Receiver(sampleRate, obtf);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obtf);
			es.execute(tx);

			break;
		case 2:
			System.out.println("Hai scelto chiave da 192 bit");
			algorithm = "TwoFish-192";
			obtf = new TwoFish(ip);
			rx = new Receiver(sampleRate, obtf);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obtf);
			es.execute(tx);
			break;
		case 3:
			System.out.println("Hai scelto chiave da 256 bit");
			algorithm = "TwoFish-256";
			obtf = new TwoFish(ip);
			rx = new Receiver(sampleRate, obtf);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obtf);
			es.execute(tx);
			break;

		default:
			System.out.println("Hai scelto chiave da 128 bit");
			algorithm = "TwoFish-128";
			obtf = new TwoFish(ip);
			rx = new Receiver(sampleRate, obtf);
			es.execute(rx);
			tx = new Transmitter(sampleRate, ip, obtf);
			es.execute(tx);
			break;

		} // chiudo switch-case

	}

	public static void checkAddress() {
		String indirizzo = null;
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					indirizzo = addr.getHostAddress();

				}

				System.out.println("Il tuo IP e': " + indirizzo);
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

	public static void insertDestinationAddress() {
		Scanner in = new Scanner(System.in);
		do {
			ipcheck = false;
			System.out.print("Inserisci indirizzo IP destinatario: ");
			try {
				String TX_IP = in.nextLine();
				ip = InetAddress.getByName(TX_IP);
				System.out.println("L'IP del destinatario e': " + TX_IP + "\n");
			} catch (IOException e1) {
				ipcheck = true;
			}
		} while (ipcheck);
	}

	public static void chooseQuality() {
		System.out.println("Scegli la qualita' audio:");
		System.out.println("1)Molto Bassa\n2)Bassa\n3)Media\n4)Alta\n5)Molto Alta");
		System.out.print("---> ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		switch (number) {
		case 1:
			sampleRate = 8000;
			System.out.println("Hai scelto la qualita' Molto Bassa");
			break;
		case 2:
			sampleRate = 11025;
			System.out.println("Hai scelto la qualita' Bassa");
			break;
		case 3:
			sampleRate = 16000;
			System.out.println("Hai scelto la qualita' Media");
			break;
		case 4:
			sampleRate = 22050;
			System.out.println("Hai scelto la qualita' Alta");
			break;
		case 5:
			sampleRate = 44100;
			System.out.println("Hai scelto la qualita' Molto Alta");
			break;
		default:
			sampleRate = 8000;
			System.out.println("Qualita' di default: Molto Bassa");
			break;
		}
		System.out.println("");
	}

	public static void reset() {
		key = null;

		ip = null;
		ob = null;
		obb = null;
		obrc4 = null;
		obrc5 = null;
		obrc6 = null;
		obtf = null;
		rx = null;
		tx = null;
		es = null;
	}

}
