import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bridge.IComponentIdentifier;
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
		@Argument(name="initPrice", clazz=Double.class, defaultvalue="-1"),
		@Argument(name="initStock", clazz=Double.class, defaultvalue="-1")
})
@Service
@Description("This agent sells products.")
@ProvidedServices(@ProvidedService(type=ISellService.class))
public class SellerAgentBDI implements ISellService {

	@Agent
	protected BDIAgent agent;

	private String product;
	private double price;
	private double initialStock;
	private double stock;
	private double increasePercentage;
	private boolean stopIncoming;

	@Belief
	protected long negotiationTime = 10000;

	@Belief(updaterate=10000)
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


	@Plan(trigger = @Trigger(factchangeds = "time"))
	public void stockArrival()
	{
		double minStock = this.initialStock *0.6;
		if(stock < minStock) {
			double increased = stock + increasePercentage;
			this.setStock(increased);
			System.out.println("Stock Arrived: Plus " + increasePercentage + " Units");
			System.out.println("Actual Stock: " + this.stock);
		}

	}


	@Plan(trigger = @Trigger(factaddeds = "requests"))
	public void negotiationPlan() {

	}


	@Plan(trigger = @Trigger(factchangeds = "stock"))
	public void checkNewStockPlan()
	{
		double newPrice = this.price * (Math.pow(1-((this.stock-this.initialStock) / this.initialStock), 0.1));
		this.setPrice(newPrice);
	}


	@AgentCreated
	public void init() {
		this.product = (String) agent.getArgument("product");
		this.price = (Double) agent.getArgument("initPrice");
		this.stock = (Double) agent.getArgument("initStock");
		this.increasePercentage = stock * 0.15;
		this.initialStock = this.stock;
		stopIncoming = false;
	}

	@AgentBody
	public void body() {


	}

	public double calculateNewPrice(Proposal chosen, int count)
	{
		double factorQuantidade = (this.stock*1.05)/(chosen.getR().getNumberOfItems()+this.stock);
		double maxPrice = this.price * (1+(factorQuantidade*0.1)) * Math.pow((1-(double)count/4), 1);
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

		System.out.println("Recebi a aceitação da proposta, a efectuar venda!");
		IComponentIdentifier cid = agent.getComponentIdentifier();
		System.out.println("I am agent Seller: " + cid.getName());
		this.stock = this.stock-p.getR().getNumberOfItems();
		return new Future<Boolean>(true);
	}

	@Override
	public IFuture<Double> negotiation(Proposal p, int count) {
		System.out.println("Negotiation attempt nº: " + count +" !");
		if(count == 4)
		{
			return new Future<Double>(-1.0);
		}
		else {
			double newPrice = calculateNewPrice(p, count);

			return new Future<Double>(newPrice);
		}
	}
}

// TODO - Implement GUI to proper start agents with user input. - Review connections and BDI.