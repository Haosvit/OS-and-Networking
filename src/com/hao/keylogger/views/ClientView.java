/**
 * 
 */
package com.hao.keylogger.views;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hao.keylogger.controllers.Client.ClientLogController;
import com.hao.keylogger.controllers.Client.IClientView;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * @author Hao Svit
 *
 */
public class ClientView extends JFrame implements ActionListener, IClientView {
	private static final String BTN_SAVE_LOG = "btn_saveLog";

	private static final long serialVersionUID = 1L;

	private static final String BTN_FETCH_LOG_NAME = "btn_fetchLog";
	private static final String BTN_FETCH_ALL_LOG_NAME = "btn_fetchAllLog";

	private static final String WINDOW_TITLE = "Keylogger Client";

	ClientLogController controller;

	JTextField tf_host;
	JTextField tf_port;
	JTextArea ta_logView;
	JDatePickerImpl datePicker;
	JComboBox<String> list_logList;
	
	ArrayList<String> logListNames = new ArrayList<String>();


	public ClientView() {
		InitFrame();
		// TODO set look and feel
		// try {
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		// } catch (ClassNotFoundException | InstantiationException |
		// IllegalAccessException
		// | UnsupportedLookAndFeelException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public InetAddress getHostAddress() throws UnknownHostException {
		return InetAddress.getByName(tf_host.getText());
	}

	@Override
	public int getPort() {
		int port;
		try {
			port = Integer.parseInt(tf_port.getText());
		} catch (Exception ex) {
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
		setTitle(WINDOW_TITLE);
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

		conPanel.add(lb_host);
		conPanel.add(tf_host);
		conPanel.add(lb_port);
		conPanel.add(tf_port);

		// Fucntions panel
		JPanel networkFuncPanel = new JPanel();
		
		// function panel for log view
		JPanel logViewFuncPanel = new JPanel();
		
		// label choose date
		JLabel lb_chooseDate = new JLabel("Choose date");
		networkFuncPanel.add(lb_chooseDate);

		// Date picker
		UtilDateModel dateModel = new UtilDateModel();
		dateModel.setValue(new Date());
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
		datePicker = new JDatePickerImpl(datePanel);

		networkFuncPanel.add(datePicker);

		// button fetch log
		JButton btn_fetchLog = new JButton("Fetch log");
		btn_fetchLog.setName(BTN_FETCH_LOG_NAME);
		networkFuncPanel.add(btn_fetchLog);
		btn_fetchLog.addActionListener(this);

		networkFuncPanel.add(Box.createRigidArea(new Dimension(30, 0)));

		// button fetch all log
		JButton btn_fetchAllLog = new JButton("Fetch all logs");
		btn_fetchAllLog.setName(BTN_FETCH_ALL_LOG_NAME);
		networkFuncPanel.add(btn_fetchAllLog);
		btn_fetchAllLog.addActionListener(this);


		// label choose log to show
		JLabel lb_chooseLog = new JLabel("Choose log");
		logViewFuncPanel.add(lb_chooseLog);
		
		// log list
		list_logList = new JComboBox<String>();
		list_logList.setPreferredSize(new Dimension(200, 25));
		list_logList.addActionListener(this);
		
		// button save log
		JButton btn_saveLog = new JButton("Save log");
		btn_saveLog.setName(BTN_SAVE_LOG);
		
		logViewFuncPanel.add(list_logList);
		

		// adding 2 panels to north panel
		northPanel.add(conPanel);
		northPanel.add(networkFuncPanel);
		northPanel.add(logViewFuncPanel);

		ta_logView = new JTextArea(5, 10);
		ta_logView.setLineWrap(true);
		ta_logView.setWrapStyleWord(true);
		ta_logView.setEditable(false);
		ta_logView.setMargin(new Insets(4, 4, 4, 4));
		JScrollPane scrollPane = new JScrollPane(ta_logView);
		//TODO add status bar
		
		// adding panels to main container
		getContentPane().add(northPanel, "North");
		getContentPane().add(scrollPane);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource().getClass().equals(Class.forName("javax.swing.JButton"))) {
				// button clicked
				JButton btn = (JButton) event.getSource();
				switch (btn.getName()) {
				case BTN_FETCH_LOG_NAME:
					ta_logView.setText("");
					controller.fetchLog(getDatePicked());
					break;
				case BTN_FETCH_ALL_LOG_NAME:
					controller.fetchAllLogs();
					break;
				}
			} 
			else if (event.getSource().getClass().equals(Class.forName("javax.swing.JComboBox"))) {
				// combobox clicked
				JComboBox<String> cb = (JComboBox<String>) event.getSource();
				int index;
				if ((index = cb.getSelectedIndex()) >= 0) {
					controller.displayLog(index);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showErrorMessage(String caption, String msg) {
		JOptionPane.showMessageDialog(null, msg, caption, JOptionPane.ERROR_MESSAGE);
	}
	
	public void setLogList(ArrayList<String> logList) {
		this.logListNames = logList;
		list_logList.removeAllItems();
		for (String logName : logListNames){
			list_logList.addItem(logName);
		}
		list_logList.setSelectedIndex(0);
	}

	public int getLogListSelectedIndex() {
		return list_logList.getSelectedIndex();
	}

	public void scrollLogViewToTop() {
		ta_logView.setCaretPosition(0);
		
	}

}
