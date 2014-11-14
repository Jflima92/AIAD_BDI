import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.IFuture;
import jadex.commons.future.Future;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentCreated;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.ProvidedServices;
import jadex.micro.annotation.ProvidedService;

@Agent
@Service
@Description("This agent sells products.")
@ProvidedServices(@ProvidedService(type=ISellService.class))
public class SellerAgentBDI implements ISellService {

	@Agent
	protected BDIAgent agent;

	private String product;
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
		if(v > 50)
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
		this.product = "Samsung Galaxy S5";
		this.price = 20;
		this.stock = 25;
		this.isHigherThanLimit = false;
	}

	@AgentBody
	public void body() {

		agent.waitForDelay(1000).get();

		System.out.println("---");
		System.out.println("Stock Actual: " + this.stock);
		System.out.println("Recebi 3 produtos");
		this.setStock(stock + 3);
		agent.waitForDelay(5000).get();

		System.out.println("---");
		System.out.println("Recebi 2 produtos");
		this.setStock(stock + 2);
		agent.waitForDelay(5000).get();

		System.out.println("---");		
		System.out.println("Recebi 1 produtos");
		this.setStock(stock + 1);
		agent.waitForDelay(5000).get();
		
		System.out.println("---");		
		System.out.println("Recebi 5 produtos");
		this.setStock(stock + 5);
		agent.waitForDelay(5000).get();
		
		System.out.println("---");		
		System.out.println("Recebi 3 produtos");
		this.setStock(stock + 3);
		agent.waitForDelay(5000).get();
		
		System.out.println("---");		
		System.out.println("Recebi 10 produtos");
		this.setStock(stock + 10);
		agent.waitForDelay(5000).get();
		
		System.out.println("---");		
		System.out.println("Recebi 2 produtos");
		this.setStock(stock + 2);
		agent.waitForDelay(5000).get();

		System.out.println("---");
	}

	@Override
	public IFuture<Boolean> buyRequest(String prod, int num) {

		if(prod.equals(this.product))
		{
			System.out.println("Uma compra foi efectuada com sucesso!!");
			this.stock = this.stock-num;
			return new Future<Boolean>(true);
		}

		System.out.println("Não vendo o que está a comprar :(!");
		return new Future<Boolean>(false);
	}
}