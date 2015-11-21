package utils;

import java.util.ArrayList;
import java.util.Arrays;

import objects.ClientShoppingCart;
import objects.ItemObject;
import objects.KassenbonObject;

public class ServerData {

	private static int CLIENT_COUNT = 0;
	
	public static double TOTAL_EARNING = 0;
	public static ArrayList<ClientShoppingCart> Carts = new ArrayList<ClientShoppingCart>();
	public static ArrayList<KassenbonObject> Kassenbons = new ArrayList<KassenbonObject>();
	public static ArrayList<ItemObject> Items = new ArrayList<ItemObject>(
			Arrays.asList(
					new ItemObject(1, "Fettarme Milch Ja! 500 ml", 10, 0.45),
					new ItemObject(2, "Bernard Matthews geflügeln 80g", 12, 0.79),
					new ItemObject(3, "Danone Frucht Zwerge 6 x 50g", 6, 0.99),
					new ItemObject(4, "Henglein Schupfnudeln 2 x 500g", 10, 1.49),
					new ItemObject(5, "Bertolli Sauce 400g", 19, 1.79),
					new ItemObject(6, "Ben and Jerry's Ice Cream 500ml", 15, 4.98),
					new ItemObject(7, "Dr. Oetker Vitalis Müsli 450g", 2, 2.29)
			)
	);
	
	
	
	public static KassenbonObject checkOut(int clientId){
		KassenbonObject kassenbon = new KassenbonObject(clientId);
		ClientShoppingCart cart = getCartByClientId(clientId);
		ArrayList<ItemObject> items = cart.getItems();
		while (items.size() > 0){
			ItemObject paidItem = items.remove(0);
			kassenbon.addItem(paidItem);
			ItemObject stock = getItemById(paidItem.getID());
			if (stock.getID() != -1){
				stock.setAmount(stock.getAmount() - paidItem.getAmount());
			}
		}
		cart.clearCart();
		Kassenbons.add(kassenbon);
		return kassenbon;
	}
	
	public static void removeAllItemsFromCart(int clientId){
		ClientShoppingCart cart = getCartByClientId(clientId);
		ArrayList<ItemObject> itemsInCart = cart.getItems();
		for (ItemObject item:Items){
			for (ItemObject itemInCart:itemsInCart){
				if (item.getID() == itemInCart.getID()){
					item.setAmount(item.getAmount()+itemInCart.getAmount());
					break;
				}
			}
		}
	}
	
	public static void removeCartByClientId(int clientId){
		for (int i = 0 ; i < Carts.size() ; i++){
			ClientShoppingCart cart = Carts.get(i);
			if (cart.getClientID() == clientId){
				Carts.remove(i);
				break;
			}
		}	
	}
	
	//item lists with amount already substracted by the client's shopping cart 
	public static ArrayList<ItemObject> getUpdatedItemList(){
		ArrayList<ItemObject> updated = new ArrayList<ItemObject>();
		for (ItemObject item:Items){
			ItemObject updatedItem = ItemObject.clone(item);
			for (ClientShoppingCart cart:Carts){
				ItemObject existInCart = cart.getItemById(updatedItem.getID());
				updatedItem.setAmount(updatedItem.getAmount() - ((existInCart==null) ? 0 : existInCart.getAmount()));
			}
			updated.add(updatedItem);
		}
		return updated;
	}
	
	public static ItemObject getItemById(int id){
		for (ItemObject obj : Items){
			if (obj.getID() == id){
				return obj;
			}
		}
		return ItemObject.createFalseObject();
	}
	
	public static ClientShoppingCart getCartByClientId(int clientId){
		for (ClientShoppingCart obj : Carts){
			if (obj.getClientID() == clientId){
				return obj;
			}
		}
		return ClientShoppingCart.createFalseCart();
	}
	
	private static int getItemAmountFromArrayList(int itemId, ArrayList<ItemObject> items){
		for (ItemObject obj : items){
			if (obj.getID() == itemId) return obj.getAmount();
		}
		return -9999;
	}
	
	public static int calculateLeftItemAmountById(int clientId, int itemId){
		int itemInStock = -9999;
		for (ItemObject obj : Items){
			if (obj.getID() == itemId){
				itemInStock = obj.getAmount();
				break;
			}
		}
		if (itemInStock == -9999) return -9999;
		
		for (int i = 0 ; i < Carts.size() ; i++){
			ClientShoppingCart cart = Carts.get(i);
			if (cart.getClientID() == clientId) continue;
			int itemsInCart = getItemAmountFromArrayList(itemId, cart.getItems());
			if (itemsInCart == -9999) continue;
			itemInStock -= itemsInCart;
		}
		
		return itemInStock;
	}
	
	public static synchronized int addNewClient(){
		CLIENT_COUNT += 1;
		return CLIENT_COUNT;
	}
	
}
