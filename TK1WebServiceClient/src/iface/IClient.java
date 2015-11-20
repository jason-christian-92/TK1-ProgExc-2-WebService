package iface;

public interface IClient {

	public void sendLogin();
	public void sendItemToCart(int itemId, int amount);
	public void sendCartRequest();
	public void sendBuyRequest();
	public void sendItemListRequest();
}
