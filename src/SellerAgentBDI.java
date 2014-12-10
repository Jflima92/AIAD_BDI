import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.IFuture;
import jadex.commons.future.Future;
import jadex.micro.annotation.*;


@Agent
@Arguments({
		@Argument(name="product", clazz=String.class, defaultvalue="N/A"),
		@Argument(name="initPrice", clazz=Integer.class, defaultvalue="-1"),
		@Argument(name="initStock", clazz=Integer.class, defaultvalue="-1")
})
@Service
@Description("This agent sells products.")
@ProvidedServices(@ProvidedService(type=ISellService.class))
public class SellerAgentBDI implements ISellService {

	@Agent
	protected BDIAgent agent;

	private String product;
	private int price;
	private int stock;
	private boolean stopIncoming;
	private Request req;
	@Belief
	protected int test;


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
	protected void setPrice(int price) {
		this.price = price;
	}

	@Belief
	public Request getReq() {
		return req;
	}

	@Belief
	public void setReq(Request req) {
		this.req = req;
	}


	@Goal
	public class queryBuyerAgent
	{

		@GoalResult
		protected Request re;


		public queryBuyerAgent(Request r)
		{
			this.re = r;
		}

	}


	@Plan(trigger=@Trigger(factchangeds = "req"))
	public void analyzeRequest(Request s) {

		if(s.getPrice() == this.getPrice())
		{
			System.out.println("The price was matched, checking number availability...");
			agent.waitForDelay(500);

			if(s.getNumberOfItems() <= this.getStock())
			{
				System.out.println("There is enough stock to provide your request, purchase order confirmed.");
				this.setStock(this.stock - s.getNumberOfItems());
			}
		}
		else
			System.out.println("Price not matched, please send a better request!");
	}


	@Plan(trigger=@Trigger(factchangeds="stock"))
	public void checkNewStockPlan(int v) {
		//TODO
	}



	@AgentCreated
	public void init() {
		this.product = (String) agent.getArgument("product");
		this.price = (Integer) agent.getArgument("initPrice");
		this.stock = (Integer) agent.getArgument("initStock");
		stopIncoming = false;
	}

	@AgentBody
	public void body() {

		while(!stopIncoming) {

			System.out.println("---");
			System.out.println("Recebi 10 produtos");
			this.setStock(stock + 10);
			agent.waitForDelay(5000).get();
			System.out.println("---");
		}
	}

	@Override
	public IFuture<Boolean> buyRequest(Request r) {

		if(r.getProduct().equals(this.product))
		{
			this.setReq(r);
			System.out.println("New request received for approval! ");
			return new Future<Boolean>(true);
		}
		System.out.println("Não vendo o que está a comprar :(!");
		return new Future<Boolean>(false);
	}
}

// TODO - Implement GUI to proper start agents with user input. - Review connections and BDI.