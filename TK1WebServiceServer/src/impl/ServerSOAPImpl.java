package impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import core.ServerGUI;


import objects.ClientShoppingCart;
import objects.ItemObject;
import objects.KassenbonObject;
import utils.ServerData;
import utils.XMLUtil;
import iface.IServer;

@WebService
@SOAPBinding(style=Style.RPC)
public class ServerSOAPImpl implements IServer{

	public ServerSOAPImpl(){
	}
	
	@WebMethod
	@Override
	public /*ArrayList<ItemObject>*/ String getItems() {
		// TODO Auto-generated method stub
		//return ItemsList.getItemsList();
		return XMLUtil.xmlFromItemsList(ServerData.getUpdatedItemList());
	}

	@WebMethod
	@Override
	public /*ItemObject*/ String getItemById(@WebParam(name="id") int id) {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItem(ServerData.getItemById(id), true);
	}

	@WebMethod
	@Override
	public int login() {
		// TODO Auto-generated method stub
		int clientId = ServerData.addNewClient();
		ClientShoppingCart cart = new ClientShoppingCart();
		cart.setClientID(clientId);
		ServerData.Carts.add(cart);
		return clientId;
	}

	@WebMethod
	@Override
	public void logout(int clientId) {
		// TODO Auto-generated method stub
		//System.out.println("ClientId "+clientId+" logged out! returning back all items in the cart...");
		ServerGUI.appendStatus("ClientId "+clientId+" logged out! returning back all items in the cart...");
		ServerData.removeAllItemsFromCart(clientId);
		ServerData.removeCartByClientId(clientId);
	}

	@WebMethod
	@Override
	public String addToCart(int clientId, int itemId, int amount) {
		// TODO Auto-generated method stub
		ClientShoppingCart cart = ServerData.getCartByClientId(clientId);
		ServerGUI.appendStatus("Receives item to be added to the cart:[clientID]"+clientId+" - [ItemId]"+itemId+" - [Amount]"+amount);
		if (amount == 0) {
			//remove item from cart
			ServerGUI.appendStatus("amount = 0  | removing itemId "+itemId+" from clientId "+clientId+"'s cart..");
			cart.removeItem(itemId);
			return "<data><status>2</status><msg>Item has been successfully removed from the cart!</msg></data>";
		}
		
		ItemObject existInCart = cart.getItemById(itemId);
		int left = ServerData.calculateLeftItemAmountById(clientId, itemId);
		if (existInCart == null){
			//no item yet
			ItemObject desiredItem = ServerData.getItemById(itemId);
			if (desiredItem.getID() == -1){
				return "<data><status>-3</status><msg>Item does not exist!</msg></data>";
			} else {
				ItemObject clone = ItemObject.clone(desiredItem);
				clone.setAmount(amount);
				cart.addItem(clone);
				ServerGUI.appendStatus("Stock:"+left+" | item is added to clientId "+clientId+"'s cart!");
				return "<data><status>1</status><msg>Item has been added to cart</msg></data>";
			}
		} else { //already exist, compare amount
			if (left > amount) {
				//enough
				existInCart.setAmount(amount);
				ServerGUI.appendStatus("Stock:"+left+" | item is added to clientId "+clientId+"'s cart!");
				return "<data><status>1</status><msg>item has been added to cart</msg></data>";
			} else {
				//not enough
				ServerGUI.appendStatus("Stock:"+left+" | Not enough quantity! item not added to clientId "+clientId+"'s cart!");
				return "<data><status>-2</status><msg>Not enough in stock! Only "+left+" items left!</msg></data>";
			}
		}
	}

	@WebMethod
	@Override
	public String getClientCart(int clientId) {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItemsList(ServerData.getCartByClientId(clientId).getItems());
	}

	@WebMethod
	@Override
	public double checkOutCart(int clientId) {
		// TODO Auto-generated method stub
		ServerGUI.appendStatus("clientId "+clientId+" requested checkout! processing...");
		double pay = ServerData.checkOut(clientId).calculateTotalPrice(0);
		ServerGUI.appendStatus("checkout process complete for clientId "+clientId+"! paid € "+pay);
		return pay;
	}	
}
