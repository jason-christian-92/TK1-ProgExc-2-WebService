package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import impl.ServerSOAPImpl;

import javax.swing.JFrame;
import javax.xml.ws.Endpoint;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import core.ServerGUI.SERVER_TYPE;

public class Server extends JFrame implements WindowListener{

	public static final String SOAP_URI = "http://localhost:8090/ws/tk1wsshoppingcart";
	public static final String REST_URI = "http://localhost:8080/tk1wsshoppingcart";
	
	private Endpoint ep;
	
	private HttpServer restServer;
	private boolean isRestServerOn;
	
	private ServerGUI gui;
	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SERVER_TYPE typ = gui.getWhichButtonTypeClicked(e);
			if (typ == SERVER_TYPE.SOAP){
				if (ep.isPublished()){
					ep.stop();
					gui.toggleServerStatus(false, typ);
				} else {
					ep = Endpoint.publish(SOAP_URI, new ServerSOAPImpl());
					gui.toggleServerStatus(true, typ);
				}
			} else {
				//for REST server...
				if (isRestServerOn){
					restServer.stop(0);
					gui.toggleServerStatus(false, typ);
					isRestServerOn = false;
				} else {
					restServer.start();
					gui.toggleServerStatus(true, typ);
					isRestServerOn = true;
				}
			}
		}
	};
	
	public Server() throws IOException{
		ep = Endpoint.publish(SOAP_URI, new ServerSOAPImpl());
		System.out.println("WSDL server successfully deployed!");
		
		isRestServerOn = true;
		restServer = HttpServerFactory.create(REST_URI);
		restServer.start();
		System.out.println("RESTFul server successfully deployed!");
		
		setupGUI();
	}
	
	private void setupGUI(){
		this.setTitle("TK1 Web Service Control Panel");
		this.setSize(400, 250);
		this.setVisible(true);
		gui = new ServerGUI();
		gui.setButtonListener(listener);
		this.add(gui);
		this.addWindowListener(this);
		
		gui.toggleServerStatus(true, SERVER_TYPE.SOAP);
		gui.toggleServerStatus(true, SERVER_TYPE.REST);
	}
	
	public static void main(String[] args){
		/*	
		 * 	[SOAP]
		 * 	terminal command for exporting the stub, and then added to the client's project files
		 *  wsimport -keep -s src -d bin http://localhost:8090/ws/tk1wsshoppingcart?wsdl
		 *  
		 *  to kill the SOAP/REST service from terminal:
		 *  1. 	in terminal: lsof -i:8090
		 *  	for [REST]: lsof -i:8080
		 *  2. 	in terminal: kill -9 <process' PID>
		 */
		
		try {
			new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {	}
	@Override
	public void windowClosed(WindowEvent arg0) {	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.out.println("closing all server..");
		if (ep.isPublished()){
			ep.stop();
			gui.toggleServerStatus(false, SERVER_TYPE.SOAP);
		}
		if (isRestServerOn){
			restServer.stop(0);
			gui.toggleServerStatus(false, SERVER_TYPE.REST);
			isRestServerOn = false;
		}
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {	}
	@Override
	public void windowIconified(WindowEvent arg0) {	}
	@Override
	public void windowOpened(WindowEvent arg0) {	}
	
}
