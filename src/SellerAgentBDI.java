import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IFuture;
import jadex.commons.future.Future;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;

import java.util.ArrayList;


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

	@Belief
	private ArrayList<Request> requests;

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


	@Goal
	public class queryBuyerAgent {

		@GoalResult
		protected Request re;


		public queryBuyerAgent(Request r) {
			this.re = r;
		}

	}


	@Plan(trigger = @Trigger(factaddeds = "requests"))
	public void analyzeRequest() {
		System.out.println("CENASSS");

				/*SServiceProvider.getServices(agent.getServiceProvider(), IBuyService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<IBuyService>()
				{
					public void intermediateResultAvailable(IBuyService is) {

						Proposal p = new Proposal(product, s, price);
						is.sendProposal(p);

					}
				});*/


		/*if(s.getPrice() == this.getPrice())
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
			System.out.println("Price not matched, please send a better request!");*/
	}


	@Plan(trigger = @Trigger(factchangeds = "stock"))
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

		while (!stopIncoming) {

			System.out.println("---");
			System.out.println("Recebi 10 produtos");
			this.setStock(stock + 10);
			agent.waitForDelay(5000).get();
			System.out.println("---");
		}
	}

	@Override
	public IFuture<Boolean> requireProposal(final Request r) {

		if (r.getProduct().equals(this.product) && r.getNumberOfItems() <= this.getStock()) {
			System.out.println("New request received! ");

			SServiceProvider.getServices(agent.getServiceProvider(), IBuyService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<IBuyService>()
			{
				public void intermediateResultAvailable(IBuyService is) {

					System.out.println("AQUI");
					Proposal p = new Proposal(product, r, price);
					is.sendProposal(p);

				}
			});
			return new Future<Boolean>(true);
		}
		System.out.println("The request was not accepted, either product or quantity are invalid");
		return new Future<Boolean>(false);
	}


	@Override
	public IFuture<Boolean> buyRequest(Request r) {

		return null;
	}
}

// TODO - Implement GUI to proper start agents with user input. - Review connections and BDI.