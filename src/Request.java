
public class Request implements Cloneable {


	public String product;
	public int numberOfItems;
	public BuyerAgentBDI ba;

	
	public Request(String prod, int noi)
	{
		this.product = prod;
		this.numberOfItems = noi;
	}
	
	public String getProduct() {
		return product;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public Request clone() throws CloneNotSupportedException {
		return (Request) super.clone();
	}
}
