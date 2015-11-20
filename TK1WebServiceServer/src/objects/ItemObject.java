package objects;


public class ItemObject{

	private int id, amount;
	private double price;
	private String name;
	
	public ItemObject(){
		
	}
	
	public static ItemObject createFalseObject(){
		ItemObject item = new ItemObject();
		item.setID(-1);
		return item;
	}
	
	public static ItemObject clone(ItemObject existing){
		ItemObject item = new ItemObject();
		item.setID(existing.getID());
		item.setName(existing.getName());
		item.setPrice(existing.getPrice());
		item.setAmount(existing.getAmount());
		return item;
	}
	
	public ItemObject(int id, String name, int amount, double price){
		this.id = id;
		this.amount = amount;
		this.name = name;
		this.price = price;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public int getAmount(){
		return amount;
	}
	
	public void setAmount(int amount){
		this.amount = amount;
	}
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public double getPrice(){
		return price;
	}
}
