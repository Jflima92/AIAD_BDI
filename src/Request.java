
public class Request {
	
	private String product;
	private int numberOfItems;
	private int price;
	
	public Request(String prod, int noi, int price)
	{
		this.product = prod;
		this.numberOfItems = noi;
		this.price = price;
	}
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
