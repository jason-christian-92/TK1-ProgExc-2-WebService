package iface;

import java.util.ArrayList;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import objects.ItemObject;

public interface IServer {

	
	/*
	 * function that increments the number of client logged in, and send
	 * the number back as the client ID. This function also creates empty cart for
	 * the new client.
	 * @return			[String] plain text containing the clientID
	*/
	public String login();
	
	/*
	 * This function receives addItem request from the client, computes the 
	 * available amount of the item, and compare it with the requested amount
	 * <p>
	 * @param clientId	[int] client's Id
	 * @param itemId	[int] item's Id
	 * @param amount	[int] the amount of the item requested
	 * @return			[String] XML containing status code (<code>) and message (<msg>)
	 */
	public String addToCart(int clientId, int itemId, int amount);
	
	/*
	 * This function searches the client's cart and returns the content
	 * back to the client
	 * <p>
	 * @param clientId	[int] client's Id
	 * @return			[String] XML containing the list of items
	 */
	public String getClientCart(int clientId);
	
	/*
	 * This function searches an item specific by its ID from the list of item
	 * <p>
	 * @param id		[int] item's ID
	 * @return			[String] XML containing the item's information
	 */
	public String getItemById(int id);
	
	/*
	 * This function computes the updated list of the item subtracted by the amount placed in
	 * each of the clients' cart
	 * <p>
	 * @return			[String] XML containing the list of items
	 */
	public String getItems();
	
	/*
	 * This function gets all items inside the client's cart, calculate total price and
	 * return it back to the client
	 * <p>
	 * @param clientId	[int] client's ID
	 * @return			[String] XML containing status code and message
	 */
	public String checkOutCart(int clientId);
	
	/*
	 * This function removes all items from the client's cart and the cart itself
	 * Note: visitor count is not reduced
	 * <p>
	 * @param clientId	[int] client's ID
	 * @return			[String] plain text, containing the success code 1. Just ignore.
	 */
	public String logout(int clientId);
	
}
