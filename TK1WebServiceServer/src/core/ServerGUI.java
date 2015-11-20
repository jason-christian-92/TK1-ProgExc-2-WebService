package core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ServerGUI extends JPanel{

	public static enum SERVER_TYPE{
		SOAP, REST
	};
	
	private JPanel pnlControl;
	private JButton btnSOAPServer, btnRESTServer;
	private JLabel lblStatusSOAP, lblStatusREST;
	private JTextArea txtStatuses;
	
	public ServerGUI(){
		this.setLayout(new BorderLayout());
		
		btnSOAPServer = new JButton("Deactivate SOAP Server");
		btnRESTServer = new JButton("Deactivate RESTful Server");
		lblStatusSOAP = new JLabel("Activated!");
		lblStatusSOAP.setBackground(Color.GREEN);
		lblStatusSOAP.setOpaque(true);
		lblStatusREST = new JLabel("Activated!");
		lblStatusREST.setBackground(Color.GREEN);
		lblStatusREST.setOpaque(true);
		
		txtStatuses = new JTextArea();
		txtStatuses.setEditable(false);
		txtStatuses.setRows(10);
		
		pnlControl = new JPanel();
		pnlControl.setLayout(new GridLayout(2, 2, 5, 5));
		pnlControl.add(btnSOAPServer);
		pnlControl.add(lblStatusSOAP);
		pnlControl.add(btnRESTServer);
		pnlControl.add(lblStatusREST);
		add(pnlControl, BorderLayout.CENTER);
		add(txtStatuses, BorderLayout.SOUTH);
	}
	
	public void setButtonListener(ActionListener listener){
		btnSOAPServer.addActionListener(listener);
		btnRESTServer.addActionListener(listener);
	}
	
	public void appendStatus(String stat){
		txtStatuses.append(stat+"\n");
	}
	
	public SERVER_TYPE getWhichButtonTypeClicked(ActionEvent e){
		if (e.getSource() == btnSOAPServer) return SERVER_TYPE.SOAP;
		return SERVER_TYPE.REST;
	}
	
	public void toggleServerStatus(boolean toggle, SERVER_TYPE typ){
		if (typ == SERVER_TYPE.SOAP){
			if (!toggle){
				lblStatusSOAP.setText("Deactivated!");
				lblStatusSOAP.setBackground(Color.RED);
				btnSOAPServer.setText("Activate SOAP Server");
				appendStatus("SOAP Server is deactivated!");
			} else {
				lblStatusSOAP.setText("Activated!");
				lblStatusSOAP.setBackground(Color.GREEN);
				btnSOAPServer.setText("Deactivate SOAP Server");
				appendStatus("SOAP Server is Activated!");
			}
		} else if (typ == SERVER_TYPE.REST){
			if (!toggle){
				lblStatusREST.setText("Deactivated!");
				lblStatusREST.setBackground(Color.RED);
				btnRESTServer.setText("Activate REST Server");
				appendStatus("REST Server is deactivated!");
			} else {
				lblStatusREST.setText("Activated!");
				lblStatusREST.setBackground(Color.GREEN);
				btnRESTServer.setText("Deactivate REST Server");
				appendStatus("REST Server is activated!");
			}
		}
		this.repaint();
	}
	
}
