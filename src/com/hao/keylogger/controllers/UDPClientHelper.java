package com.hao.keylogger.controllers;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import com.hao.keylogger.models.Log;

public class UDPClientHelper {
	public enum RequestCommand {
		GET_LOG
	}
	
	private static UDPClientHelper instance;
	
	private DatagramSocket socket;
	private InetAddress address;
	private String hostName;
	private int port;
	
	private UDPClientHelper(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}
	
	public static UDPClientHelper getInstance(String hostName, int port) {
		if (instance == null) {
			instance = new UDPClientHelper(hostName, port);
		}
		return instance;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void connect() throws SocketException, UnknownHostException{
			socket = new DatagramSocket();
			address = InetAddress.getByName(hostName);
	}
	
	public Log fetchLog(Date date) {
		Log log = new Log();
		log.setContent(date.toString());
		return log;
	}
	
	public ArrayList<Log> fetchAllLogs() {
		ArrayList<Log> logs = new ArrayList<Log>();
		logs.add(fetchLog(new Date()));
		return logs;
	}

	
}
