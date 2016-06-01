package com.hao.keylogger.views;

import com.hao.keylogger.controllers.server.ServerLogController;

public interface IServerView {
	String getHost();
	int getPort();
	String getMonitorContent();
	
	void appendToMonitor(String msg);
	void setHost(String host);
	void setPort(int port);
	
	void setController(ServerLogController controller);
}
