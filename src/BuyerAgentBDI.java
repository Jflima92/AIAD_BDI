import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Agent
@Arguments({
		@Argument(name="product", clazz=String.class, defaultvalue="N/A"),
		@Argument(name="desiredPrice", clazz=Double.class, defaultvalue="-1"),
		@Argument(name="nou", clazz=Integer.class, defaultvalue="-1")
})
@Service
@Description("This agent buys products.")
@ProvidedServices(@ProvidedService(type=IBuyService.class))
public class BuyerAgentBDI implements IBuyService {

	@Agent
	protected BDIAgent agent;

	protected String product;
	protected double desiredPrice;
	protected int numberOfUnits;
	protected Request request;
	protected ArrayList<Proposal> allProposals;
	protected PurchasingGoal actualGoal;
	protected Boolean isProcessing;

	@Belief(updaterate=2000)
	protected long time = System.currentTimeMillis();

	@Plan(trigger=@Trigger(goals=PurchasingGoal.class))
	protected void launchRequestPlan() {
		/** If a Request is already running, don't launch another */
		if (request != null) {
			if (!isProcessing) {
				processProposals();
			}} else {
			request = new Request(product, Math.min(actualGoal.totalMissingUnits, numberOfUnits), this);
			SServiceProvider.getServices(agent.getServiceProvider(), ISellService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<ISellService>() {
				public void intermediateResultAvailable(ISellService is) {

					allProposals.clear();
					is.requireProposal(request.clone());
				}
			});

		}

		throw new PlanFailureException();
	}

	@Goal(recur=true)
	public class PurchasingGoal {
		public PurchasingGoal(int totalToBuy) {
			totalMissingUnits = totalToBuy;
			totalUnits = totalToBuy;
		}

		public int totalMissingUnits;
		public int totalUnits;

		@GoalRecurCondition(beliefs="time")
		public boolean checkRecur() {
			if(allProposals.size() == 0)
				request = null;
			// The buyer's job is done when all required units have been purchased
			return totalMissingUnits != 0;
		}
	}

	@AgentCreated
	public void init() {

		product = (String) 	agent.getArgument("product");
		desiredPrice = (Double) agent.getArgument("desiredPrice");
		numberOfUnits = (Integer) agent.getArgument("nou");
		allProposals = new ArrayList<Proposal>();
		isProcessing = false;


		actualGoal = new PurchasingGoal(numberOfUnits);
		this.agent.dispatchTopLevelGoal(actualGoal);
	}


	@AgentBody
	public void body() throws InterruptedException {

		Thread.sleep(1000);

	}

	public void processProposals()
	{
		isProcessing = true;
		int count = 1;

		Proposal chosen = chooseProposal();

		if(chosen.getPrice() > this.desiredPrice)
		{
			while(count <= 4)
			{
				double auxPrice = this.desiredPrice;
				auxPrice = auxPrice*(1+0.1);

				double newPrice = chosen.getSa().negotiation(chosen, count).get();

				if(newPrice == -1.0)
				{
					System.out.println("Attempt to negotiate failed!");

				}
				else if(newPrice <= auxPrice)
				{
					System.out.println("estou a aceitar");
					Proposal chosenClone = chosen.clone();
					chosenClone.setPrice(newPrice);
					chosen.getSa().acceptedProposal(chosenClone);
					actualGoal.totalMissingUnits -= chosen.getR().numberOfItems;

					allProposals.remove(chosen);
					request = null;
					count = 4;
				}

				count++;
			}

		}
		else {

			chosen.getSa().acceptedProposal(chosen);
			actualGoal.totalMissingUnits -= chosen.getR().numberOfItems;
			allProposals.remove(chosen);
			request = null;

		}

		request = null;
		isProcessing = false;
	}


	public Proposal chooseProposal()
	{
		proposalComparator comp = new proposalComparator();
		Collections.sort(allProposals, comp);
	/*	for(int i = 0; i< allProposals.size(); i++)
		{
			if(allProposals.get(i).getSa().getStock() <=0)
			{
				allProposals.remove(i);
			}
		}
*/
		return allProposals.get(0);
	}

	@Override
	public IFuture<Boolean> sendProposal(Proposal p) {

		System.out.println("Buyer received a valid proposal, analysing...");

		if(p.getProduct().equals(this.product))
		{
			allProposals.add(p);
			System.out.println("SIZE: " + allProposals.size());
			return new Future<Boolean>(true);
		}


		return new Future<Boolean>(false);
	}

	public class proposalComparator implements Comparator<Proposal> {

		@Override
		public int compare(Proposal o1, Proposal o2) {
			// TODO Auto-generated method stub
			if(o1.getPrice() > o2.getPrice())
			{
				return 1;
			}
			else if(o1.getPrice() == o2.getPrice())
				return 0;
			else
				return -1;

		}
	}

}