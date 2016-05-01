package com.hao.keylogger.program;

import com.hao.keylogger.controllers.ServerLogController;
import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ServerView;

public class ServerKeyLoggerProgram {

	public ServerKeyLoggerProgram() {
	
	}

	public static void main(String[] args) {
		ServerView serverView = new ServerView();
		ServerLogController controller = new ServerLogController(serverView, new Log());
		
	}
}
