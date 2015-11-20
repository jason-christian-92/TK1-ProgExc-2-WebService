package impl;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


import objects.ClientShoppingCart;
import objects.ItemObject;
import utils.ServerData;
import utils.XMLUtil;
import iface.IServer;

@Path("/server")
public class ServerRESTImpl implements IServer{

	@GET
	@Path("/getItems")
	@Override
	public /*ArrayList<ItemObject>*/String getItems() {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItemsList(ServerData.getUpdatedItemList());
	}
	
	@GET
	@Path("/getItemById/{a}")
	@Override
	public String getItemById(@PathParam("a") int id) {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItem(ServerData.getItemById(id), true);
	}

	@GET
	@Path("/login")
	@Override
	public int login() {
		// TODO Auto-generated method stub
		int clientId = ServerData.addNewClient();
		ClientShoppingCart cart = new ClientShoppingCart();
		cart.setClientID(clientId);
		ServerData.Carts.add(cart);
		return clientId;
	}

	@GET
	@Path("/logout/{clientId}")
	@Override
	public void logout(@PathParam("clientId") int clientId) {
		// TODO Auto-generated method stub
		
	}

	@GET
	@Path("addToCart/{clientId}/{itemId}/{amt}")
	@Override
	public String addToCart(@PathParam("clientId") int clientId, @PathParam("itemId")int itemId, @PathParam("amt")int amount) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("getClientCart/{clientId}")
	@Override
	public String getClientCart(@PathParam("clientId") int clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("checkoutCart/{clientId}")
	@Override
	public double checkOutCart(@PathParam("clientId") int clientId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
