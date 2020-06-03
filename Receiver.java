package com.narracci.securevoice;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.narracci.securevoice.utils.AES;
import com.narracci.securevoice.utils.Blowfish;
import com.narracci.securevoice.utils.RC4;
import com.narracci.securevoice.utils.RC5;
import com.narracci.securevoice.utils.RC6;
import com.narracci.securevoice.utils.TwoFish;

public class Receiver extends Thread {
	// these parameters must by used in the Transmitter class of the other client
	private static final String RX_IP = "localhost";
	private static final int RX_PORT = 1034;
	private static int tempBuffer;
	private LinkedList<byte[]> tailPacket = new LinkedList<byte[]>();
	boolean isAlive = false;
	static float quality;
	static AES ob = null;
	static Blowfish obb = null;
	static RC4 obrc4 = null;
	static RC5 obrc5 = null;
	static RC6 obrc6 = null;
	static TwoFish obtf = null;
	private boolean running = true;
	static DatagramSocket sock = null;
	static byte soundpacket[] = null;
	static DatagramPacket datagram = null;
	static DataLine.Info dataLineInfo;
	static SourceDataLine sourceDataLine;

	public Receiver(float sampleRate, AES object)
			throws SocketException, UnknownHostException, LineUnavailableException {
		this.quality = sampleRate;
		this.ob = object;
		this.tempBuffer = (int) sampleRate;
		this.sock = new DatagramSocket(RX_PORT);
		this.soundpacket = new byte[tempBuffer];
		this.datagram = new DatagramPacket(soundpacket, soundpacket.length, InetAddress.getByName(RX_IP), RX_PORT);
		this.dataLineInfo = new DataLine.Info(SourceDataLine.class, SecureVoice.getAudioFormat(quality));
		this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		sourceDataLine.start();
	}

	public Receiver(float sampleRate, Blowfish object)
			throws SocketException, UnknownHostException, LineUnavailableException {
		this.quality = sampleRate;
		this.obb = object;
		this.tempBuffer = ((int) sampleRate % 8) + (int) sampleRate;
		this.sock = new DatagramSocket(RX_PORT);
		setTempBufferLenght(tempBuffer);
		this.soundpacket = new byte[tempBuffer];
		this.datagram = new DatagramPacket(soundpacket, soundpacket.length, InetAddress.getByName(RX_IP), RX_PORT);
		this.dataLineInfo = new DataLine.Info(SourceDataLine.class, SecureVoice.getAudioFormat(quality));
		this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		sourceDataLine.start();
	}

	public Receiver(float sampleRate, RC4 object)
			throws SocketException, UnknownHostException, LineUnavailableException {
		this.quality = sampleRate;
		this.obrc4 = object;
		this.tempBuffer = (int) sampleRate;
		this.sock = new DatagramSocket(RX_PORT);
		setTempBufferLenght(tempBuffer);
		this.soundpacket = new byte[tempBuffer];
		this.datagram = new DatagramPacket(soundpacket, soundpacket.length, InetAddress.getByName(RX_IP), RX_PORT);
		this.dataLineInfo = new DataLine.Info(SourceDataLine.class, SecureVoice.getAudioFormat(quality));
		this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		sourceDataLine.start();
	}

	public Receiver(float sampleRate, RC5 object)
			throws SocketException, UnknownHostException, LineUnavailableException {
		this.quality = sampleRate;
		this.obrc5 = object;
		this.tempBuffer = ((int) sampleRate % 8) + (int) sampleRate;
		this.sock = new DatagramSocket(RX_PORT);
		setTempBufferLenght(tempBuffer);
		this.soundpacket = new byte[tempBuffer];
		this.datagram = new DatagramPacket(soundpacket, soundpacket.length, InetAddress.getByName(RX_IP), RX_PORT);
		this.dataLineInfo = new DataLine.Info(SourceDataLine.class, SecureVoice.getAudioFormat(quality));
		this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		sourceDataLine.start();
	}

	public Receiver(float sampleRate, RC6 object)
			throws SocketException, UnknownHostException, LineUnavailableException {
		this.quality = sampleRate;
		this.obrc6 = object;
		this.tempBuffer = ((int) sampleRate % 16) + (int) sampleRate;
		this.sock = new DatagramSocket(RX_PORT);
		setTempBufferLenght(tempBuffer);
		this.soundpacket = new byte[tempBuffer];
		this.datagram = new DatagramPacket(soundpacket, soundpacket.length, InetAddress.getByName(RX_IP), RX_PORT);
		this.dataLineInfo = new DataLine.Info(SourceDataLine.class, SecureVoice.getAudioFormat(quality));
		this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		sourceDataLine.start();
	}

	public Receiver(float sampleRate, TwoFish object)
			throws SocketException, UnknownHostException, LineUnavailableException {
		this.quality = sampleRate;
		this.obtf = object;
		this.tempBuffer = ((int) sampleRate % 16) + (int) sampleRate;
		this.sock = new DatagramSocket(RX_PORT);
		setTempBufferLenght(tempBuffer);
		this.soundpacket = new byte[tempBuffer];
		this.datagram = new DatagramPacket(soundpacket, soundpacket.length, InetAddress.getByName(RX_IP), RX_PORT);
		this.dataLineInfo = new DataLine.Info(SourceDataLine.class, SecureVoice.getAudioFormat(quality));
		this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		sourceDataLine.start();
	}

	Thread t = new Thread() {
		public void run() {
			while (running) {
				if (!tailPacket.isEmpty())
					speak(tailPacket.pop());
			}
			System.out.println("Thread t terminato (all'interno di Receiver");
		}
	};

	@Override
	public void run() {
		byte b[] = null;
		running = true;
		while (running) {

			b = rxFromUDP();
			tailPacket.push(b);
			if (tailPacket.size() == 3 && !isAlive) {

				t.start();
				isAlive = true;

			}

		}
		System.out.println("Thread Receiver terminato (all'interno di Receiver)\n\n");
	}

	public static byte[] rxFromUDP() {
		try {
			// sock = new DatagramSocket(RX_PORT);
			// byte soundpacket[] = new byte[tempBuffer];
			// DatagramPacket datagram = new DatagramPacket(soundpacket, soundpacket.length,
			// InetAddress.getByName(RX_IP),
			// RX_PORT);
			sock.receive(datagram);
			// sock.close();

			if (SecureVoice.algorithm == "AES-128" || SecureVoice.algorithm == "AES-192"
					|| SecureVoice.algorithm == "AES-256") {
				return ob.decrypt(datagram.getData(), 0, soundpacket.length);
			} else if (SecureVoice.algorithm == "Blowfish") {
				return obb.decrypt(datagram.getData(), 0, soundpacket.length);
			} else if (SecureVoice.algorithm == "RC4") {
				return obrc4.decrypt(datagram.getData(), 0, soundpacket.length);
			} else if (SecureVoice.algorithm == "RC5-128" || SecureVoice.algorithm == "RC5-192"
					|| SecureVoice.algorithm == "RC5-256") {
				return obrc5.decrypt(datagram.getData(), 0, soundpacket.length);
			} else if (SecureVoice.algorithm == "RC6-128" || SecureVoice.algorithm == "RC6-192"
					|| SecureVoice.algorithm == "RC6-256") {
				return obrc6.decrypt(datagram.getData(), 0, soundpacket.length);
			} else if (SecureVoice.algorithm == "TwoFish-128" || SecureVoice.algorithm == "TwoFish-192"
					|| SecureVoice.algorithm == "TwoFish-256") {
				return obtf.decrypt(datagram.getData(), 0, soundpacket.length);
			} else {
				return obb.decrypt(datagram.getData(), 0, soundpacket.length);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			// sock.close();
		}

	}

	public static void speak(byte soundbytes[]) {

		// try {
		// DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
		// SecureVoice.getAudioFormat(quality));
		// try (SourceDataLine sourceDataLine = (SourceDataLine)
		// AudioSystem.getLine(dataLineInfo)) {
		// sourceDataLine.open(SecureVoice.getAudioFormat(quality));
		// sourceDataLine.start();
		sourceDataLine.write(soundbytes, 0, soundbytes.length);
		sourceDataLine.drain();
		// }
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// }

	}

	public static void setTempBufferLenght(int a) {
		if (obb != null || obrc5 != null) {
			tempBuffer = (a - (a % 8)) + 8;

		} else if (obrc6 != null || obtf != null) {
			tempBuffer = (a - (a % 16)) + 16;
		} else {
			tempBuffer = a;
		}

	}

	public void ferma() {
		this.running = false;
	}
}
