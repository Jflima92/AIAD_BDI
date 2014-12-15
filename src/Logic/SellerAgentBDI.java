package Logic;

import GUI.SellerWindow;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.IFuture;
import jadex.commons.future.Future;
import jadex.micro.annotation.*;

import java.util.ArrayList;
import java.util.Random;


@Agent
@Arguments({
		@Argument(name="product", clazz=String.class, defaultvalue="N/A"),
		@Argument(name="initPrice", clazz=Double.class, defaultvalue="-1"),
		@Argument(name="initStock", clazz=Double.class, defaultvalue="-1"),
		@Argument(name="strategy", clazz=Double.class, defaultvalue="1"),
		@Argument(name="variation", clazz=Double.class, defaultvalue="5"),
		@Argument(name="negotiaton", clazz=Double.class, defaultvalue="5")
})
@Service
@Description("This agent sells products.")
@ProvidedServices(@ProvidedService(type=ISellService.class))
public class SellerAgentBDI implements ISellService {

	@Agent
	protected BDIAgent agent;
	protected SellerWindow window;

	private String product;
	private double price;
	private double initialStock;
	private double stock;
	private double increasePercentage;
	private double strategy;
	private double variation;
	private double negotiation;
	private boolean stopIncoming;
	private achieveGoal achieveGoal;
	private int sales;

	@Belief
	protected long negotiationTime = 10000;

	@Belief(updaterate=15000)
	protected long receiveStockTime = System.currentTimeMillis();

	@Belief(updaterate=1000)
	protected long time = System.currentTimeMillis();

	@Belief
	private ArrayList<Request> requests;

	@Belief
	public double getStock() {
		return stock;
	}

	@Belief
	public void setStock(double stock) {
		this.stock = stock;
	}

	@Belief
	public double getPrice() {
		return price;
	}

	@Belief
	protected void setPrice(double price) {

		this.price = price;
	}


	@Plan(trigger = @Trigger(factchangeds = "receiveStockTime"))
	public void stockArrival()
	{
		double minStock = this.initialStock *0.6;
		if(stock < minStock) {
			double increased = stock + increasePercentage;
			this.setStock(increased);
			System.out.println("Stock Arrived: Plus " + increasePercentage + " Units");
			System.out.println("Actual Stock: " + this.stock);
			window.updatestock(stock);
		}

	}


	@Plan(trigger = @Trigger(factchangeds = "stock"))
	public void checkNewStockPlan()
	{
		double newPrice = this.price * (Math.pow(1-((this.stock-this.initialStock) / this.initialStock), 0.1));
		this.setPrice(newPrice);
		window.updateprice(newPrice);
	}

	@Plan(trigger=@Trigger(goals=achieveGoal.class))
	protected void launchRequestPlan() {
		try {
			agent.wait(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Goal(recur=true)
	public class achieveGoal {
		public achieveGoal(int totalToFullfill) {
			totalMissingMoney = totalToFullfill;
			totalUnits = 0;
		}

		public int totalMissingMoney;
		public int totalUnits;

		@GoalRecurCondition(beliefs="time")
		public boolean checkRecur() {
			// The buyer's job is done when all required units have been purchased
			return totalMissingMoney > 0;
		}
	}

	@AgentCreated
	public void init() {
		this.product = (String) agent.getArgument("product");
		this.price = (Double) agent.getArgument("initPrice");
		this.stock = (Double) agent.getArgument("initStock");
		this.strategy= (Double)agent.getArgument("strategy");
		this.variation = (Double) agent.getArgument("variation");
		this.negotiation = (Double) agent.getArgument("negotiation");

		sales =0;
		this.increasePercentage = stock * 0.15;
		this.initialStock = this.stock;
		stopIncoming = false;
		int objective = 10000;
		achieveGoal = new achieveGoal(objective);
		this.agent.dispatchTopLevelGoal(achieveGoal);
		window = new SellerWindow(product, stock, price);
		window.setVisible(false);
	}

	@AgentBody
	public void body() {


	}

	public double calculateNewPrice(Proposal chosen, int count)
	{
		double factorQuantidade = (this.stock*1.05)/(chosen.getR().getNumberOfItems()+this.stock);
		double maxPrice = this.price * (1+(factorQuantidade*(this.variation/100)) * Math.pow((1-(double)count/negotiation), this.strategy));
		return this.price+((maxPrice-this.price));
	}

	@Override
	public IFuture<Boolean> requireProposal(Request r) {

		IComponentIdentifier cid = agent.getComponentIdentifier();
		System.out.println("cid for agent Seller: " + cid);


		if (r.getProduct().equals(this.product) && this.stock > 0 ) {
			System.out.println("New request received! ");


			Request req = r.clone();

			if(r.getNumberOfItems() > this.stock)
			{

				req.numberOfItems = this.stock;
			}

			Proposal p = new Proposal(product, req, price, this);
			r.ba.sendProposal(p.clone());

			return new Future<Boolean>(true);
		}

		System.out.println("The request was not accepted, either product or quantity are invalid");
		return new Future<Boolean>(false);
	}


	@Override
	public IFuture<Boolean> acceptedProposal(Proposal p) {


		Random rand = new Random();
		int randomNum = 0 + (int)(Math.random()*10);
		System.out.println("Random " +randomNum);
		if(randomNum>3) {

			System.out.println("Recebi a aceitação da proposta, a efectuar venda a " + p.getPrice() + "€");
			sales += p.getR().getNumberOfItems();

			IComponentIdentifier cid = agent.getComponentIdentifier();
			System.out.println("I am agent Seller: " + cid.getName());
			this.stock = this.stock - p.getR().getNumberOfItems();
			this.achieveGoal.totalMissingMoney = this.achieveGoal.totalMissingMoney - ((int) p.getPrice() * (int) p.getR().getNumberOfItems());
			this.achieveGoal.totalUnits = this.achieveGoal.totalUnits + ((int) p.getPrice() * (int) p.getR().getNumberOfItems());
			System.out.println("Current earned money: " + this.achieveGoal.totalUnits);
			double avgprice = achieveGoal.totalUnits / sales;
			window.update(stock, this.achieveGoal.totalUnits, avgprice);
			window.addProposal(p);
			return new Future<Boolean>(true);
		}
		System.out.println("Contrato não cumprido, venda cancelada");
		return new Future<Boolean>(false);


	}

	@Override
	public IFuture<Double> negotiation(Proposal p, int count) {
		System.out.println("Logic.Negotiation attempt nº: " + count +" !");
		if(count == negotiation)
		{
			return new Future<Double>(-1.0);
		}
		else {
			double newPrice = calculateNewPrice(p, count);

			return new Future<Double>(newPrice);
		}
	}

	@Override
	public IFuture<Boolean> retrieveSeller() {
		System.out.println("estou a meter visivel");
		window.setVisible(true);
		return new Future<Boolean>(true);
	}
}

// TODO - Implement GUI to proper start agents with user input. - Review connections and BDI.