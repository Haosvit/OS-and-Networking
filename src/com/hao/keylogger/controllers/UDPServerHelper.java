package com.hao.keylogger.controllers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPServerHelper {

	private static UDPServerHelper instance;
	
	private DatagramSocket socket;
	private InetAddress address;
	private String hostName;
	private int port;
	
	private byte[] buffer = new byte[256];
	
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
		}
		catch (Exception e) {
			return false;
		}
	}

	public void startListenning() {
		// TODO làm đa luồng
		DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
		try {
			
		}
		catch (Exception e) {
			
		}
		
	}
	
	

}
