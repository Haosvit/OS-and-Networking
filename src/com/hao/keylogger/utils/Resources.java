package com.hao.keylogger.utils;

public interface Resources {
	final int DEFAULT_PORT = 4567;
	
	//buffers
	/**
	 * client buffer size: 0.5 Gb
	 */
	final int CLIENT_BUFFER_SIZE = Integer.MAX_VALUE / 4; 
	/**
	 * server buffer size: 256 bytes
	 */
	final int SERVER_BUFFER_SIZE = 256;
	
	// Time out in milliseconds
	final int TIME_OUT = 3000;
	
	//Log file
	final String LOGS_DIRECTORY = "Logs";
	final String LOG_FILE_EXTENSION = ".txt";
	final String LOGS_CLIENT_DIRECTORY = "Saved Logs";

	// request params
	final String FETCH_LOG_REQUEST = "fetch log by date";
	final String FETCH_ALL_LOG_REQUEST = "fetch all logs";

	final String DELETE_ALL_HOST_LOGS = "delete all host logs";
	final String STOP_LOGGER = "stop logger";
	final String START_LOGGER = "start logger";
	
	// key logger path
	final String KEY_LOGGER_PATH = "HookKeyboard.exe";

	final String KEY_LOGGER_NAME = "HookKeyboard.exe";
	final String START_LOGGER_BATCH_FILE_NAME = "start_logger.bat";
	
}
