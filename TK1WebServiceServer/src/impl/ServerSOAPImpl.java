package impl;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.ResponseWrapper;


import objects.ItemObject;
import utils.ItemsList;
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
		return XMLUtil.xmlFromItemsList(ItemsList.getItemsList());
	}
	
	@WebMethod 
	@Override
	public boolean isItemAvailable(@WebParam(name="id") int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@WebMethod
	@Override
	public /*ItemObject*/ String getItemById(@WebParam(name="id") int id) {
		// TODO Auto-generated method stub
		for (ItemObject obj : ItemsList.getItemsList()){
			if (obj.getID() == id) return /*obj*/ XMLUtil.xmlFromItem(obj, true);
		}
		return "";
	}	
}
