import jadex.bridge.IComponentIdentifier;

public class Request extends CloneableObject {


	public String product;
	public double numberOfItems;
	public BuyerAgentBDI ba;

	
	public Request(String prod, int noi, BuyerAgentBDI ba)
	{
		this.ba = ba;
		this.product = prod;
		this.numberOfItems = noi;
	}
	
	public String getProduct() {
		return product;
	}

	public double getNumberOfItems() {
		return numberOfItems;
	}

	public Request clone() {
		return (Request) super.clone();
	}
}
