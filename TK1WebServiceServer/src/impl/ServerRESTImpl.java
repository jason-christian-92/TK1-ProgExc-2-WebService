package impl;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


import objects.ItemObject;
import utils.ItemsList;
import utils.XMLUtil;
import iface.IServer;

@Path("/server")
public class ServerRESTImpl implements IServer{

	@GET
	@Path("/getItems")
	@Override
	public /*ArrayList<ItemObject>*/String getItems() {
		// TODO Auto-generated method stub
		return XMLUtil.xmlFromItemsList(ItemsList.getItemsList());
	}
	
	@GET
	@Path("/getItems/{a}")
	@Override
	public boolean isItemAvailable(@PathParam("a") int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@GET
	@Path("/getItemById/{a}")
	@Override
	public String getItemById(@PathParam("a") int id) {
		// TODO Auto-generated method stub
		for (ItemObject obj : ItemsList.getItemsList()){
			if (obj.getID() == id) return /*obj*/XMLUtil.xmlFromItem(obj, true);
		}
		return null;
	}

}
