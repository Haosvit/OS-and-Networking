package com.hao.keylogger.models;

public interface Resource {
	final int DEFAULT_PORT = 17;
	
	//buffers
	/**
	 * client buffer size: 1 Gb
	 */
	final int CLIENT_BUFFER_SIZE = Integer.MAX_VALUE / 2; 
	/**
	 * server buffer size: 256 bytes
	 */
	final int SERVER_BUFFER_SIZE = 256;
	
	//Log file directory
	final String LOGS_DIRECTORY = "D:/Dai Hoc/Do an/Do an Mang/Source code/HookKeyboard/HookKeyboard/Logs";
	final String LOG_FILE_EXTENSION = ".txt";
	// request params
	final String FETCH_LOG_REQUEST = "fetch log by date";
	final String FETCH_ALL_LOG_REQUEST = "fetch all logs";
	
}
