package core;

import iface.IClient;
import impl.ServerSOAPImpl;
import impl.ServerSOAPImplService;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceException;

import org.xml.sax.SAXException;

import objects.ItemObject;
import objects.StatusMessageObject;
import util.XMLParser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class WebServClient extends JFrame implements IClient,WindowListener{

	//URI for REST
	public static final String REST_URI 		= "http://localhost:8080/tk1wsshoppingcart";
	public static final String URI_LOGIN 		= "/server/login";
	public static final String URI_LOGOUT 		= "/server/logout/"; //param: clientId
	public static final String URI_GETITEMS 	= "/server/getItems";
	public static final String URI_ADDTOCART 	= "/server/addToCart/"; //param: clientId, itemId, amount
	public static final String URI_GETCART 		= "/server/getClientCart/"; //param: clientId
	public static final String URI_CHECKOUT 	= "/server/checkoutCart/"; //param: clientId
	
	//connection variables for REST
	private ClientConfig config;
	private Client client;
	private WebResource service;
	
	//connection variables for SOAP
	private ServerSOAPImplService serverStub;
	private ServerSOAPImpl server;
	
	//GUI, client ID, and service type
	private ClientGUI gui;
	private int clientId;
	public static final String SERVICE_TYPE_SOAP = "soap";
	public static final String SERVICE_TYPE_REST = "rest";
	private String servType;
	
	public WebServClient(String servType){
		if (!servType.equals(SERVICE_TYPE_REST) && !servType.equals(SERVICE_TYPE_SOAP)){
			System.out.println("Unknown service type: "+servType);
			return;
		}
		
		clientId = -1;
		this.servType = servType;
		if (servType.equals(SERVICE_TYPE_SOAP)){
			serverStub = new ServerSOAPImplService();
			server = serverStub.getServerSOAPImplPort();
		} else {
			config = new DefaultClientConfig();
			client = Client.create(config);
			service = client.resource(REST_URI);
		}
		setupGUI();
		sendLogin();
	}
	
	private void setupGUI(){
		gui = new ClientGUI(this);
		this.setSize(625, 300);
		if (this.servType.equals(SERVICE_TYPE_SOAP)){
			this.setTitle("TK1 -SOAP-RPC- Client's Shopping Cart");
		} else {
			this.setTitle("TK1 -RESTful- Client's Shopping Cart");
		}
		this.setVisible(true);
		this.add(gui);
		this.addWindowListener(this);
	}
	
	public static void main(String[] args){
		if (args.length == 0){
			System.out.println("please specify service type '"+SERVICE_TYPE_SOAP+"' " +
					"or '"+SERVICE_TYPE_REST+"' as the parameter!");
			return;
		}
		new WebServClient(args[0]);
	}
	
	public void setOKDialog(String title, String message){
		JOptionPane.showOptionDialog(null, message, title,
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE,
				null, new Object[]{"OK"}, "OK");
	}
	
	@Override
	public void sendLogin() {
		// TODO Auto-generated method stub
		try{
			String result = "";
			if (this.servType.equals(SERVICE_TYPE_SOAP)){
				result = server.login();
			} else {
				WebResource loginService = service.path(URI_LOGIN);
				result = loginService.accept(MediaType.TEXT_PLAIN).get(String.class);
			}
			clientId = Integer.parseInt(result);
			gui.setStatus("Successfully logged in! Obtained client ID: "+clientId+" from server!");
			sendItemListRequest();
		} catch (ClientHandlerException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The RESTFul server is not responding!! Closing application...");
			System.exit(0);
		} catch (WebServiceException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The SOAP-RPC server is not responding!! Closing application...");
			System.exit(0);
		}
	}

	@Override
	public void sendItemToCart(int itemId, int amount) {
		// TODO Auto-generated method stub
		try {
			String result="";
			if (this.servType.equals(SERVICE_TYPE_SOAP)){
				result = server.addToCart(clientId, itemId, amount);
			} else {
				WebResource addToCartService = service.path(URI_ADDTOCART).path(clientId+"/"+itemId+"/"+amount);
				result = addToCartService.accept(MediaType.TEXT_XML).get(String.class);
			}
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
		} catch (ClientHandlerException ex){ 
			ex.printStackTrace();
			setOKDialog("Error", "The RESTFul server is not responding!!");
		} catch (WebServiceException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The SOAP-RPC server is not responding!!");
		}
		
	}

	@Override
	public void sendCartRequest() {
		// TODO Auto-generated method stub
		try {
			String result="";
			if (this.servType.equals(SERVICE_TYPE_SOAP)){
				result = server.getClientCart(clientId);
			} else {
				WebResource getCartService = service.path(URI_GETCART).path(String.valueOf(clientId));
				result = getCartService.accept(MediaType.TEXT_XML).get(String.class);
			}
		
			ArrayList<ItemObject> cart = XMLParser.parseToListItemObject(result);
			gui.setupCart(cart);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The RESTFul server is not responding!!");
		} catch (WebServiceException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The SOAP-RPC server is not responding!!");
		}
	}

	@Override
	public void sendBuyRequest() {
		// TODO Auto-generated method stub
		try {
			String result = "";
			if (this.servType.equals(SERVICE_TYPE_SOAP)){
				result = server.checkOutCart(clientId);
			} else {
				WebResource checkOutService = service.path(URI_CHECKOUT).path(String.valueOf(clientId));
				result = checkOutService.accept(MediaType.TEXT_XML).get(String.class);
			}
		
			StatusMessageObject status = XMLParser.parseStatusMessage(result);
			if (status.getStatusCode() == 1){
				gui.setupCart(null);	
			}
			sendItemListRequest();
			gui.setStatus(status.getMessage());
			setOKDialog("Check out confirmation!", status.getMessage());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The RESTFul server is not responding!!");
		} catch (WebServiceException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The SOAP-RPC server is not responding!!");
		}
	}

	@Override
	public void sendItemListRequest() {
		// TODO Auto-generated method stub
		try {
			String xml = "";
			if (this.servType.equals(SERVICE_TYPE_SOAP)){
				xml = server.getItems();
			} else {
				WebResource itemListReqService = service.path(URI_GETITEMS);
				xml = itemListReqService.accept(MediaType.TEXT_XML).get(String.class);
			}
		
			ArrayList<ItemObject> items = XMLParser.parseToListItemObject(xml);
			gui.setTableContent(items);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The RESTFul server is not responding!!");
		} catch (WebServiceException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The SOAP-RPC server is not responding!!");
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
		try{
			if (this.servType.equals(SERVICE_TYPE_SOAP)){
				server.logout(clientId);
			} else {
				WebResource logoutService = service.path(URI_LOGOUT).path(String.valueOf(clientId));
				logoutService.accept(MediaType.TEXT_PLAIN).get(String.class);
			}
		} catch (ClientHandlerException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The RESTFul server is not responding!!\nExiting anyway...");
		} catch (WebServiceException ex){
			ex.printStackTrace();
			setOKDialog("Error", "The SOAP-RPC server is not responding!!\nExiting anyway...");
		} finally{
			//just exit the system, despite error!
			System.exit(0);
		}
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
