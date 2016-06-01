package com.hao.keylogger.views;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import com.hao.keylogger.controllers.Client.ClientLogController;

public interface IClientView {
	InetAddress getHostAddress() throws UnknownHostException;
	int getPort();
	String getLog();
	Date getDatePicked();
	
	void setHost(String host);
	void setPort(int port);
	void setLogContent(String log);
	
	void setController(ClientLogController controller);
}
