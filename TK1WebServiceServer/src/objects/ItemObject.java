package objects;


public class ItemObject{

	private int id, amount;
	private double price;
	private String name;
	
	public ItemObject(){
		
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
