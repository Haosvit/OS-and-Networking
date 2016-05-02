package com.hao.keylogger.controllers.Client;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.models.Resource;
import com.hao.keylogger.views.ClientView;

public class ClientLogController {
	private ClientView view;
	private Log log;
	ArrayList<Log> logs;

	public ClientLogController(ClientView view) {
		super();
		this.view = view;
		this.view.setController(this);
		this.view.setHost(UDPClientHelper.getLocalHostIP());
		this.view.setPort(Resource.DEFAULT_PORT);
	}

	public JFrame getLogView() {
		return view;
	}

	public void setLogView(ClientView logView) {
		this.view = logView;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public void displayLog() {
		view.setLogContent(log.getContent());
	}

	public void fetchLog(Date date){
		try {
			// the log will be displayed when it is fetched
			new UDPClientHelper(this, view.getHostAddress(), view.getPort())
			.fetchLog(date);
		} catch (SocketException | UnknownHostException e) {
			view.showErrorMessage("Fetch log error", "Invalid host address or port!");
			e.printStackTrace();
		}
		catch (NullPointerException ex) {
			view.showErrorMessage("Fetch log error", "Can not fetch log!");
		}
		
	}
	
	public void fetchAllLogs(){
		try {
			new UDPClientHelper(this, view.getHostAddress(), view.getPort()).fetchAllLogs();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// load to view
	}
	
	/**
	 * Display the fetched log to view
	 * @param log
	 */
	public void receiveLogFromServer(Log log) {
		this.log = log;
		ArrayList<String> logListNames = new ArrayList<String>();
		logListNames.add(log.getName());
		setLogList(logListNames);
		displayLog();
	}

	private void setLogList(ArrayList<String> logListNames) {
		view.setLogList(logListNames);
	}

}
