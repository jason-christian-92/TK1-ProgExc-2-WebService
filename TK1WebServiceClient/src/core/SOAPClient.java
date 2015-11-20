package core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import objects.ItemObject;
import objects.StatusMessageObject;

import util.XMLParser;
import iface.IClient;
import impl.ServerSOAPImpl;
import impl.ServerSOAPImplService;

public class SOAPClient extends JFrame implements IClient,WindowListener{
	
	private ServerSOAPImplService serverStub;
	private ServerSOAPImpl server;
	private ClientGUI gui;
	private int clientId;
	
	public SOAPClient() {
		clientId = -1;
		serverStub = new ServerSOAPImplService();
		server = serverStub.getServerSOAPImplPort();
		
		setupGUI();
		sendLogin();
	}
	
	public static void main(String[] args){
		new SOAPClient();
	}
	
	private void setupGUI(){
		gui = new ClientGUI(this);
		this.setSize(625, 300);
		this.setTitle("TK1 -SOAP-RPC- Client's Shopping Cart");
		this.setVisible(true);
		this.add(gui);
		this.addWindowListener(this);
	}

	@Override
	public void sendBuyRequest() {
		// TODO Auto-generated method stub
		double sum = server.checkOutCart(clientId);
		gui.setupCart(null);
		sendItemListRequest();
		gui.setStatus("Checkout completed! Total sum to be paid: € "+sum);
		JOptionPane.showConfirmDialog(null, 
				"Server successfully checked out all items in your cart!\nTotal paid: € "+sum,
				"Check out success!", JOptionPane.OK_OPTION);
	}

	@Override
	public void sendItemListRequest() {
		// TODO Auto-generated method stub
		String xml = server.getItems();
		try {
			ArrayList<ItemObject> items = XMLParser.parseToListItemObject(xml);
			gui.setTableContent(items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendLogin() {
		// TODO Auto-generated method stub
		clientId = server.login();
		gui.setStatus("Successfully logged in! Obtained client ID: "+clientId+" from server!");
		sendItemListRequest();
	}

	@Override
	public void sendItemToCart(int itemId, int amount) {
		// TODO Auto-generated method stub
		try{
			String result = server.addToCart(clientId, itemId, amount);
			StatusMessageObject status = XMLParser.parseStatusMessage(result);
			gui.setStatus(status.getMessage());
			
			//successful status code > 0, get shopping cart from server
			if (status.getStatusCode() > 0){
				sendCartRequest();
			}
			//update item list
			sendItemListRequest();
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void sendCartRequest() {
		// TODO Auto-generated method stub
		try{
			String result = server.getClientCart(clientId);
			ArrayList<ItemObject> cart = XMLParser.parseToListItemObject(result);
			gui.setupCart(cart);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {	}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.out.println("client logging out!");
		server.logout(clientId);
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {	}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}
	
}
