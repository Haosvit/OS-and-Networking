package com.hao.keylogger.controllers.Client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

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
