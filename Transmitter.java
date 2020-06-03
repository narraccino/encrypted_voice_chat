
package com.narracci.securevoice;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

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

public class Transmitter extends Thread {

	static InetAddress ip = null;
	private static final int TX_PORT = 1234;
	float quality;
	public static int row = 1;
	static AES ob = null;
	static Blowfish obb = null;
	static RC4 obrc4 = null;
	static RC5 obrc5 = null;
	static RC6 obrc6 = null;
	static TwoFish obtf = null;
	private boolean running = true;
	static DatagramSocket sock = null;

	public Transmitter(float sampleRate, InetAddress address, AES object) throws SocketException {
		this.quality = sampleRate;
		this.ip = address;
		this.ob = object;
		this.sock = new DatagramSocket();
	}

	public Transmitter(float sampleRate, InetAddress address, Blowfish object) throws SocketException {
		this.quality = sampleRate;
		this.ip = address;
		this.obb = object;
		this.sock = new DatagramSocket();
	}

	public Transmitter(float sampleRate, InetAddress address, RC4 object) throws SocketException {
		this.quality = sampleRate;
		this.ip = address;
		this.obrc4 = object;
		this.sock = new DatagramSocket();
	}

	public Transmitter(float sampleRate, InetAddress address, RC5 object) throws SocketException {
		this.quality = sampleRate;
		this.ip = address;
		this.obrc5 = object;
		this.sock = new DatagramSocket();
	}

	public Transmitter(float sampleRate, InetAddress address, RC6 object) throws SocketException {
		this.quality = sampleRate;
		this.ip = address;
		this.obrc6 = object;
		this.sock = new DatagramSocket();
	}

	public Transmitter(float sampleRate, InetAddress address, TwoFish object) throws SocketException {
		this.quality = sampleRate;
		this.ip = address;
		this.obtf = object;
		this.sock = new DatagramSocket();
	}

	@Override
	public void run() {

		Mixer.Info minfo[] = AudioSystem.getMixerInfo();
		for (Mixer.Info minfo1 : minfo) {
			System.out.println(minfo1);
		}

		if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
			try {
				DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class,
						SecureVoice.getAudioFormat(quality));
				TargetDataLine line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
				line.open(SecureVoice.getAudioFormat(quality));
				line.start();
				byte tempBuffer[] = new byte[line.getBufferSize()];
				System.out.println(tempBuffer.length);
				// Receiver.setTempBufferLenght(tempBuffer.length);

				while (running) {

					int read = line.read(tempBuffer, 0, tempBuffer.length);

					if (SecureVoice.algorithm == "AES-128" || SecureVoice.algorithm == "AES-192"
							|| SecureVoice.algorithm == "AES-256") {
						// long startTime = System.nanoTime();
						byte[] encrypt = ob.encrypt(tempBuffer, 0, read);
						// long estimatedTime = System.nanoTime() - startTime;
						sendToUDP(encrypt);
						// saveDate(estimatedTime);
						// System.out.println("Encryption Time in microseconds: " + estimatedTime /
						// 1000);
					} else if (SecureVoice.algorithm == "Blowfish") {
						// long startTime = System.nanoTime();
						byte[] encrypt = obb.encrypt(tempBuffer, 0, read);
						// long estimatedTime = System.nanoTime() - startTime;
						sendToUDP(encrypt);
						// saveDate(estimatedTime);
						// System.out.println("Encryption Time in microseconds: " + estimatedTime /
						// 1000);
					} else if (SecureVoice.algorithm == "RC4") {
						// long startTime = System.nanoTime();
						byte[] encrypt = obrc4.encrypt(tempBuffer, 0, read);
						// long estimatedTime = System.nanoTime() - startTime;
						sendToUDP(encrypt);
						// saveDate(estimatedTime);
						// System.out.println("Encryption Time in microseconds: " + estimatedTime /
						// 1000);
					} else if (SecureVoice.algorithm == "RC5-128" || SecureVoice.algorithm == "RC5-192"
							|| SecureVoice.algorithm == "RC5-256") {
						// long startTime = System.nanoTime();
						byte[] encrypt = obrc5.encrypt(tempBuffer, 0, read);
						// long estimatedTime = System.nanoTime() - startTime;
						sendToUDP(encrypt);
						// saveDate(estimatedTime);
						// System.out.println("Encryption Time in microseconds: " + estimatedTime /
						// 1000);
					} else if (SecureVoice.algorithm == "RC6-128" || SecureVoice.algorithm == "RC6-192"
							|| SecureVoice.algorithm == "RC6-256") {
						// long startTime = System.nanoTime();
						byte[] encrypt = obrc6.encrypt(tempBuffer, 0, read);
						// long estimatedTime = System.nanoTime() - startTime;
						sendToUDP(encrypt);
						// saveDate(estimatedTime);
						// System.out.println("Encryption Time in microseconds: " + estimatedTime /
						// 1000);
					} else if (SecureVoice.algorithm == "TwoFish-128" || SecureVoice.algorithm == "TwoFish-192"
							|| SecureVoice.algorithm == "TwoFish-256") {
						// long startTime = System.nanoTime();
						byte[] encrypt = obtf.encrypt(tempBuffer, 0, read);
						// long estimatedTime = System.nanoTime() - startTime;
						sendToUDP(encrypt);
						// saveDate(estimatedTime);
						// System.out.println("Encryption Time in microseconds: " + estimatedTime /
						// 1000);
					}

				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}

			System.out.print("Transmitter Terminato..\n\n");
		}

	}

	public static void sendToUDP(byte soundpacket[]) {
		// DatagramSocket sock = null;
		try {
			// sock = new DatagramSocket();
			// sock.setReuseAddress(true);
			sock.send(new DatagramPacket(soundpacket, soundpacket.length, ip, TX_PORT));
			// sock.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// sock.close();
		}
	}

	public static void saveDate(long estimated) throws Exception {

		Workbook aWorkBook = Workbook.getWorkbook(new File(
				"/Users/giuseppenarracci/Desktop/" + SecureVoice.getName() + "-" + SecureVoice.getSample() + ".xls"));

		WritableWorkbook aCopy = Workbook.createWorkbook(new File(
				"/Users/giuseppenarracci/Desktop/" + SecureVoice.getName() + "-" + SecureVoice.getSample() + ".xls"),
				aWorkBook);

		WritableSheet aCopySheet = aCopy.getSheet(0);

		Label l1 = new Label(0, row, String.valueOf(estimated));

		aCopySheet.addCell(l1);

		aCopy.write();
		aCopy.close();

		row++;

	}

	public void ferma() {
		this.running = false;
	}

}
