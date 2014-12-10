
public class Request {
	
	private String product;
	private int numberOfItems;
	
	public Request(String prod, int noi)
	{
		this.product = prod;
		this.numberOfItems = noi;
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
}
