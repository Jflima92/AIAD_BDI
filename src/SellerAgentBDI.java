import GUI.SellerAgentConfigurations;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
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

import javax.swing.*;


@Agent
@Service
@Description("This agent sells products.")
@ProvidedServices(@ProvidedService(type=ISellService.class))
public class SellerAgentBDI implements ISellService {
	private SellerAgentConfigurations configurations;

	@Agent
	protected BDIAgent agent;

	private String product;
	private int price;
	private int stock;
	private boolean stopIncoming;
	private Request req;


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
	public void analyzeRequest(ChangeEvent event) {
		Request s;
		s = (Request) event.getValue();

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
	public void checkNewStockPlan(ChangeEvent event, IPlan plan) {
		Integer v = (Integer) event.getValue();
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

		System.out.println("CRIADO");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("here");
				configurations = new SellerAgentConfigurations();
				product = configurations.getInsertProductNameTextField().toString();
				price = configurations.getInsertInitialPriceTextField();
				stock = configurations.getInitialStockTextField();
				stopIncoming = false;

			}

		});

		configurations.waitForEnd();

		/*this.product = configurations.getInsertProductNameTextField().toString();
		this.price = configurations.getInsertInitialPriceTextField();
		this.stock = configurations.getInitialStockTextField();
		this.stopIncoming = false;*/


		/*this.product = "Samsung Galaxy S5";
		this.price = 729;
		this.stock = 25;
		this.stopIncoming = false;*/

	}

	@AgentBody
	public void body() {

		agent.waitForDelay(1000).get();

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