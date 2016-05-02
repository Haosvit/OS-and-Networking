package com.hao.keylogger.models;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private InetAddress host;
	private int port;
	private Date dateOfLog;
	private String content;
	
	public Log() {
	}

	public Log(InetAddress host, int port, Date dateOfLog, String content) {
		super();
		this.host = host;
		this.port = port;
		this.dateOfLog = dateOfLog;
		this.content = content;
	}
	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress serverAddress) {
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
		this.name = new SimpleDateFormat("d-M-yyyy").format(dateOfLog);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
