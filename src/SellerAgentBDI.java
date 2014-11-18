import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Goal.ExcludeMode;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalTargetCondition;
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

	//Beliefs 

	@Belief
	private String product;
	
	@Belief
	private String getProduct()
	{
		return this.product;
	}
	
	@Belief
	private void setProduct(String product)	{
		this.product = product;
	}
	
	@Belief
	private int price;
	
	@Belief
	public int getPrice() {
		return price;
	}

	@Belief
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Belief
	private int negotiationPriceLimit = 600;
		

    @Belief
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
	private int stockLimit = 1000; 

	private boolean stopIncoming;
	
	/* -------------------------------------------------------------------------------------------------------------------------------------------------- */
	
	//Goals
	
	@Goal(excludemode=ExcludeMode.Never)
	public class MaintainStorageGoal			//Goal that prevents stock from reaching limit by by triggering implementPromotion plan
	{
		@GoalMaintainCondition(beliefs={"stock"})
		protected boolean maintain()
		{
			if(getStock() > (0.20*stockLimit))  //Implements promotion plan when stock is higher than 20% of the stockLimit (supostamente xD)
			{
				stopIncoming = true;
				return false;
			}
			else
			{
				stopIncoming = false;
				return true;
			}
		}

		@GoalTargetCondition(beliefs={"stock"})
		protected boolean target()
		{
			return getStock()<99;
		}
	} 
	
	/* -------------------------------------------------------------------------------------------------------------------------------------------------- */
	
	//Plans

	@Plan(trigger=@Trigger(factchangeds="price"))
	public void checkPricePlan(ChangeEvent event, IPlan plan) {
		Integer s;
        s = (Integer) event.getValue();
        System.out.println("New Price: " + s);
	}

	@Plan(trigger=@Trigger(goals=MaintainStorageGoal.class))
	public void implementPromotion() {

		System.out.println("Stock is getting full, start promotion.");
		stopIncoming = true;
		this.setPrice(price-5);
		this.setStock(stock-25);
		System.out.println("PROMOTIONS!! New Price: " + this.price);

	}
	
	/* -------------------------------------------------------------------------------------------------------------------------------------------------- */


	@AgentCreated
	public void init() {
		this.product = "Samsung Galaxy S5";
		this.price = 729;
		this.stock = 25;
		this.stopIncoming = false;
	}

	@AgentBody
	public void body() {
		agent.dispatchTopLevelGoal(new MaintainStorageGoal());
		agent.waitForDelay(1000).get();

		while(true)
		{
			if(stock <= stockLimit)
				stopIncoming = false;
			else
				stopIncoming = true;
			
			
			if(!stopIncoming)
			{
				System.out.println("---");
				System.out.println("Stock Actual: " + this.stock);
				System.out.println("Recebi 15 produtos");
				this.setStock(stock + 15);
				agent.waitForDelay(1000).get();
				System.out.println("---");
			}
			else
			{
				System.out.println("O stock encontra-se cheio, foi ordenada uma paragem na receção de equipamentos!");
			}


			agent.waitForDelay(2000).get();
		}
	}

	@Override
	public IFuture<Boolean> buyRequest(Request r) {

		if(r.getProduct().equals(this.product))
		{
			System.out.println("Uma compra foi efectuada com sucesso!!");
			this.stock = this.stock - r.getNumberOfItems();
			return new Future<Boolean>(true);
		}

		System.out.println("Não vendo o que está a comprar :(!");
		return new Future<Boolean>(false);
	}
}