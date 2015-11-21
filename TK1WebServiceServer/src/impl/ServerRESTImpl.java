package impl;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.RequestWrapper;

import core.ServerGUI;


import objects.ClientShoppingCart;
import objects.ItemObject;
import utils.ServerData;
import utils.XMLUtil;
import iface.IServer;

@Path("/server")
public class ServerRESTImpl implements IServer{

	@GET
	@Path("/getItems")
	@Produces(MediaType.TEXT_XML)
	@Override
	public /*ArrayList<ItemObject>*/String getItems() {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItemsList(ServerData.getUpdatedItemList());
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("/getItemById/{a}")
	@Override
	public String getItemById(@PathParam("a") int id) {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItem(ServerData.getItemById(id), true);
	}

	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String login() {
		// TODO Auto-generated method stub
		int clientId = ServerData.addNewClient();
		ClientShoppingCart cart = new ClientShoppingCart();
		cart.setClientID(clientId);
		ServerData.Carts.add(cart);
		ServerGUI.appendStatus("new client logged in using REST protocol! Given ID:"+clientId);
		return String.valueOf(clientId);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/logout/{clientId}")
	@Override
	public String logout(@PathParam("clientId") int clientId) {
		// TODO Auto-generated method stub
		ServerGUI.appendStatus("ClientId "+clientId+" logged out! returning back all items in the cart...");
		ServerData.removeAllItemsFromCart(clientId);
		ServerData.removeCartByClientId(clientId);
		return String.valueOf(1);
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("addToCart/{clientId}/{itemId}/{amt}")
	@Override
	public String addToCart(@PathParam("clientId") int clientId, @PathParam("itemId")int itemId, @PathParam("amt")int amount) {
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
				if (left < amount){
					return "<data><status>-2</status><msg>Not enough in stock! Only "+left+" items left!</msg></data>";
				} else {
					ItemObject clone = ItemObject.clone(desiredItem);
					clone.setAmount(amount);
					cart.addItem(clone);
					ServerGUI.appendStatus("Stock:"+left+" | item is added to clientId "+clientId+"'s cart!");
					return "<data><status>1</status><msg>Item has been added to cart</msg></data>";
				}
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
	
	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("getClientCart/{clientId}")
	@Override
	public String getClientCart(@PathParam("clientId") int clientId) {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItemsList(ServerData.getCartByClientId(clientId).getItems());
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("checkoutCart/{clientId}")
	@Override
	public String checkOutCart(@PathParam("clientId") int clientId) {
		// TODO Auto-generated method stub
		ServerGUI.appendStatus("clientId "+clientId+" requested checkout! processing...");
		double pay = ServerData.checkOut(clientId).calculateTotalPrice(0);
		ServerGUI.appendStatus("checkout process complete for clientId "+clientId+"! paid € "+pay);
		return String.valueOf(pay);
	}

}
