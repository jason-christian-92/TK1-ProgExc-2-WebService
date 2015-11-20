package iface;

public interface IClient {

	public void sendBuyRequest();
	public void sendItemListRequest();
	public void sendItemAvailabilityRequest(int id);
}
