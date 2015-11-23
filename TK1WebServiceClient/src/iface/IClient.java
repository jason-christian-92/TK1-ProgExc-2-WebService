package iface;

public interface IClient {

	//function to send the login request to the server
	public void sendLogin();
	
	//function to send an amount of item specified by the itemId to be added to the cart
	public void sendItemToCart(int itemId, int amount);
	
	//function to send request of the content of the cart.
	public void sendCartRequest();
	
	//function to send checkout request to the server
	public void sendBuyRequest();
	
	//function to send request of the list of items sold
	public void sendItemListRequest();
}
