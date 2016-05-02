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
	
	UDPClientHelper udpClientHelper;

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
			new UDPClientHelper(this, view.getHostAddress(), view.getPort())
			.fetchLog(date);
			displayLog();
		} catch (SocketException | UnknownHostException e) {
			view.showErrorMessage("Fetch log error", "Invalid host address or port!");
			e.printStackTrace();
		}
		catch (NullPointerException ex) {
			view.showErrorMessage("Fetch log error", "Can not fetch log! Server is not running!");
		}
		
	}
	
	public void fetchAllLogs() {
		ArrayList<Log> logs = udpClientHelper.fetchAllLogs();
		// load to view
	}
	
	public void receiveLogFromServer(Log log) {
		this.log = log;
		displayLog();
	}

}
