package com.hao.keylogger.models;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String host;
	private int port;
	private Date dateOfLog;
	private String content;
	
	public Log() {
	}

	public Log(String host, int port, Date dateOfLog, String content) {
		super();
		this.host = host;
		this.port = port;
		this.dateOfLog = dateOfLog;
		this.content = content;
	}
	public String getHost() {
		return host;
	}

	public void setHost(String serverAddress) {
		this.host = serverAddress;
	}
	
	public void setPort(int serverPort) {
		this.port = serverPort;
	}
	
	public int getPort() {
		return port;
	}

	public Date getDateOfLog() {
		return dateOfLog;
	}

	public void setDateOfLog(Date dateOfLog) {
		this.dateOfLog = dateOfLog;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
