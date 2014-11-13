import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IPlan;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentCreated;
import jadex.micro.annotation.Description;
 
@Agent
@Description("An agent that sells products.")
public class SellerAgentBDI {
 
	@Agent
	protected BDIAgent agent;
	
	private int price;
	private int stock; 
	
	@Belief
	public int getStock() {
		return stock;
	}
	
	@Belief
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	@Belief
	public int getPrice() {
		return price;
	}
 
	@Belief
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Belief(dynamic = true)
	protected boolean isHigherThanLimit = (stock == 30);
 
	
	@Plan(trigger=@Trigger(factchangeds="price"))
	public void checkPricePlan(ChangeEvent event, IPlan plan) {
		int s = (int) event.getValue();		
		System.out.println("New Price: " + s);
	}
	
	@Plan(trigger=@Trigger(factchangeds="stock"))
	public void checkNewStockPlan(ChangeEvent event, IPlan plan) {
		int v = (int) event.getValue();
		if(v > 30)
		{
			System.out.println("Stock is getting full, start promotion.");
			this.setPrice(price-5);
		}
		else
		{
		System.out.println("New Stock Size: " + v);
		}
	}
		

	
	@AgentCreated
	public void init() {
		this.price = 20;
		this.stock = 25;
		this.isHigherThanLimit = false;
	}

	@AgentBody
	public void body() {
		agent.waitForDelay(1000).get();
		
		System.out.println("---");
		System.out.println("Recebi 3 produtos");
		this.setStock(stock + 3);
		agent.waitForDelay(1000).get();
		
		System.out.println("---");
		System.out.println("Recebi 2 produtos");
		this.setStock(stock + 2);
		agent.waitForDelay(1000).get();
		
		System.out.println("---");		
		System.out.println("Recebi 1 produtos");
		this.setStock(stock + 1);
		agent.waitForDelay(1000).get();
		
		System.out.println("---");
	}
}