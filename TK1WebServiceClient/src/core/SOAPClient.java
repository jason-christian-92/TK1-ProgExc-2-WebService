package core;

import java.awt.BorderLayout;
import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import objects.ItemObject;

import util.XMLParser;
import iface.IClient;
import impl.ServerSOAPImpl;
import impl.ServerSOAPImplService;

public class SOAPClient extends JFrame implements IClient{
	
	private ServerSOAPImplService serverStub;
	private ServerSOAPImpl server;
	private ClientGUI gui;
	
	public SOAPClient() {
		serverStub = new ServerSOAPImplService();
		server = serverStub.getServerSOAPImplPort();
		System.out.println("manages to connect with SOAP! retrieving item's list..");
		setupGUI();
		showItemList();
	}
	
	private void setupGUI(){
		gui = new ClientGUI();
		this.setSize(600, 300);
		this.setTitle("TK1 -SOAP-RPC- Client's Shopping Cart");
		this.setVisible(true);
		this.add(gui);
	}
	
	private void showItemList(){
		String xml = server.getItems();
		System.out.println("Item list retrieved! : "+xml); 
		try {
			ArrayList<ItemObject> items = XMLParser.parseToListItemObject(xml);
			gui.setTableContent(items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new SOAPClient();
	}

	@Override
	public void sendBuyRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendItemListRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendItemAvailabilityRequest(int id) {
		// TODO Auto-generated method stub
		
	}
	
}
