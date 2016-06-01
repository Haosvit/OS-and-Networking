package com.hao.keylogger.utils;

import javax.swing.ImageIcon;

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

	
	// icons
	public static final ImageIcon IC_FETCH = new ImageIcon("./Icons/fetch.png");
	public static final ImageIcon IC_FETCH_ALL = new ImageIcon("./Icons/fetch_all.png");	
	public static final ImageIcon IC_EXIT = new ImageIcon("./Icons/exit.png");
	public static final ImageIcon IC_SAVE = new ImageIcon("./Icons/save.png");
	public static final ImageIcon IC_SAVE_ALL = new ImageIcon("./Icons/save_all.png");	
	public static final ImageIcon IC_SWITCH_VIEW = new ImageIcon("./Icons/switch_view.png");
	public static final ImageIcon IC_LOAD_ALL = new ImageIcon("./Icons/load.png");
	public static final ImageIcon IC_STOP = new ImageIcon("./Icons/stop.png");
	public static final ImageIcon IC_START = new ImageIcon("./Icons/start.png");
	public static final ImageIcon IC_DELETE = new ImageIcon("./Icons/delete.png");
	
}
