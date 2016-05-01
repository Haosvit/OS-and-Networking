package com.hao.keylogger.controllers;

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

	public boolean startServer() {
		try {
			address = InetAddress.getByName(hostName);
			socket = new DatagramSocket(port, address);
			
			return true;
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public void startListenning() {
		
	}
	
	

}
