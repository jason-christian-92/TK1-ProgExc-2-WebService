package core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import iface.IClient;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import objects.ItemObject;
import objects.StatusMessageObject;

import util.XMLParser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterface;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RESTClient extends JFrame implements IClient,WindowListener{

	public static final String REST_URI 		= "http://localhost:8080/tk1wsshoppingcart";
	public static final String URI_LOGIN 		= "/server/login";
	public static final String URI_LOGOUT 		= "/server/logout/"; //param: clientId
	public static final String URI_GETITEMS 	= "/server/getItems";
	public static final String URI_ADDTOCART 	= "/server/addToCart/"; //param: clientId, itemId, amount
	public static final String URI_GETCART 		= "/server/getClientCart/"; //param: clientId
	public static final String URI_CHECKOUT 	= "/server/checkoutCart/"; //param: clientId
	
	private ClientGUI gui;
	private int clientId;
	
	private ClientConfig config;
	private Client client;
	private WebResource service;
	
	public RESTClient(){
		config = new DefaultClientConfig();
		client = Client.create(config);
		service = client.resource(REST_URI);
		clientId = -1;

		setupGUI();
		sendLogin();
	}
	
	public static void main(String[] args){
		new RESTClient();
	}
	
	private void setupGUI(){
		gui = new ClientGUI(this);
		this.setSize(625, 300);
		this.setTitle("TK1 -RESTful- Client's Shopping Cart");
		this.setVisible(true);
		this.add(gui);
		this.addWindowListener(this);	
	}
	
	@Override
	public void sendLogin() {
		// TODO Auto-generated method stub
		WebResource loginService = service.path(URI_LOGIN);
		String result = loginService.accept(MediaType.TEXT_PLAIN).get(String.class);
		System.out.println("result: "+result);
		clientId = Integer.parseInt(result);
		gui.setStatus("Successfully logged in! Obtained client ID: "+clientId+" from server!");
		sendItemListRequest();
	}

	@Override
	public void sendItemToCart(int itemId, int amount) {
		// TODO Auto-generated method stub
		WebResource addToCartService = service.path(URI_ADDTOCART).path(clientId+"/"+itemId+"/"+amount);
		String result = addToCartService.accept(MediaType.TEXT_XML).get(String.class);
		try {
			StatusMessageObject status = XMLParser.parseStatusMessage(result);
			gui.setStatus(status.getMessage());
			
			//successful status code > 0, get shopping cart from server
			if (status.getStatusCode() > 0){
				sendCartRequest();
			}
			//update item list
			sendItemListRequest();
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendCartRequest() {
		// TODO Auto-generated method stub
		WebResource getCartService = service.path(URI_GETCART).path(String.valueOf(clientId));
		String result = getCartService.accept(MediaType.TEXT_XML).get(String.class);
		try {
			ArrayList<ItemObject> cart = XMLParser.parseToListItemObject(result);
			gui.setupCart(cart);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendBuyRequest() {
		// TODO Auto-generated method stub
		WebResource checkOutService = service.path(URI_CHECKOUT).path(String.valueOf(clientId));
		String result = checkOutService.accept(MediaType.TEXT_XML).get(String.class);
		try {
			StatusMessageObject status = XMLParser.parseStatusMessage(result);
			if (status.getStatusCode() == 1){
				gui.setupCart(null);	
			}
			sendItemListRequest();
			gui.setStatus(status.getMessage());
			JOptionPane.showConfirmDialog(null, 
					status.getMessage(),
					"Check out information!", JOptionPane.OK_OPTION);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendItemListRequest() {
		// TODO Auto-generated method stub
		WebResource itemListReqService = service.path(URI_GETITEMS);
		String result = itemListReqService.accept(MediaType.TEXT_XML).get(String.class);
		try{
			ArrayList<ItemObject> items = XMLParser.parseToListItemObject(result);
			gui.setTableContent(items);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	@Override
	public void windowActivated(WindowEvent e) {	}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("client logging out!");
		WebResource logoutService = service.path(URI_LOGOUT).path(String.valueOf(clientId));
		logoutService.accept(MediaType.TEXT_PLAIN).get(String.class);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}

}
