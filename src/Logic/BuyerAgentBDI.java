package Logic;

import GUI.BuyerWindow;
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
		@Argument(name="nou", clazz=Integer.class, defaultvalue="-1"),
		@Argument(name="strategy", clazz=Double.class, defaultvalue="1"),
		@Argument(name="variation", clazz=Double.class, defaultvalue="5"),
		@Argument(name="negotiaton", clazz=Double.class, defaultvalue="5")
})
@Service
@Description("This agent buys products.")
@ProvidedServices(@ProvidedService(type=IBuyService.class))
public class BuyerAgentBDI implements IBuyService {

	@Agent
	protected BDIAgent agent;

	protected String product;
	protected double desiredPrice;
	protected double negotiationPrice;
	private double strategy;
	private double variation;
	private double negotiation;
	protected int numberOfUnits;
	protected Request request;
	protected ArrayList<Proposal> allProposals;
	protected PurchasingGoal actualGoal;
	protected Boolean isProcessing;
	protected BuyerWindow window;

	@Belief(updaterate=2000)
	protected long time = System.currentTimeMillis();

	@Plan(trigger=@Trigger(goals=PurchasingGoal.class))
	protected void launchRequestPlan() throws InterruptedException {

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
		this.strategy= (Double)agent.getArgument("strategy");
		this.variation = (Double) agent.getArgument("variation");
		this.negotiation = (Double) agent.getArgument("negotiation");
		allProposals = new ArrayList<Proposal>();
		isProcessing = false;
		negotiationPrice=desiredPrice;

		window = new BuyerWindow(product, numberOfUnits, desiredPrice);
		window.setVisible(false);


		actualGoal = new PurchasingGoal(numberOfUnits);
		this.agent.dispatchTopLevelGoal(actualGoal);
	}


	@AgentBody
	public void body() throws InterruptedException {

		Thread.sleep(1000);

	}

	public void processProposals()
	{

		System.out.println("Processing Proposal");
		isProcessing = true;
		int count = 1;

		final Proposal chosen = chooseProposal();

		if(chosen.getPrice() > this.desiredPrice)
		{
			while(count <= negotiation)
			{
				double auxPrice = this.negotiationPrice;
				auxPrice = auxPrice+ ((auxPrice*(1+(variation/100))-auxPrice)*Math.pow(((double)count/negotiation),strategy));
				negotiationPrice=auxPrice;

				System.out.println();
				System.out.println("Buyer proposed price: "+ auxPrice);



				double newPrice = chosen.getSa().negotiation(chosen, count).get();

				System.out.println("Seller proposed price: "+ newPrice);

				if(newPrice == -1.0)
				{
					System.out.println("Seller refused to keep negotiation!");
					negotiationPrice= desiredPrice;


				}
				else if(newPrice <= auxPrice)
				{
					System.out.println("estou a aceitar");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Proposal chosenClone = chosen.clone();
					chosenClone.setPrice(newPrice);
					if(chosen.getSa().acceptedProposal(chosenClone).get()) {

						actualGoal.totalMissingUnits -= chosen.getR().numberOfItems;

						SServiceProvider.getServices(agent.getServiceProvider(), IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<IManagerService>()
						{
							@Override
							public void intermediateResultAvailable(IManagerService is) {
								is.submitEvaluation(chosen.getSa().agent.getAgentName(), true);
							}
						});

						window.update((int) chosen.getR().getNumberOfItems());
						System.out.println("------Actualiza progress bar-------");
						window.addProposal(chosen);
					}
					else{

					SServiceProvider.getServices(agent.getServiceProvider(), IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<IManagerService>()
					{
						@Override
						public void intermediateResultAvailable(IManagerService is) {
							is.submitEvaluation(chosen.getSa().agent.getAgentName(), false);
						}
					});}

					allProposals.remove(chosen);
					request = null;
					count = (int)negotiation;
					negotiationPrice= desiredPrice;
				}

				count++;
			}

			System.out.println("Buyer refused to keep negotiation!");
		}
		else {
			Proposal chosenClone = chosen.clone();
			if(chosen.getSa().acceptedProposal(chosenClone).get()) {
				actualGoal.totalMissingUnits -= chosen.getR().numberOfItems;
				allProposals.remove(chosen);
				request = null;
				window.update((int) chosen.getR().getNumberOfItems());
				SServiceProvider.getServices(agent.getServiceProvider(), IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<IManagerService>()
				{
					@Override
					public void intermediateResultAvailable(IManagerService is) {
						is.submitEvaluation(chosen.getSa().agent.getAgentName(), true);
					}
				});
				System.out.println("------Actualiza progress bar-------");
			}
			else {
				SServiceProvider.getServices(agent.getServiceProvider(), IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<IManagerService>() {
					@Override
					public void intermediateResultAvailable(IManagerService is) {
						is.submitEvaluation(chosen.getSa().agent.getAgentName(), false);
					}
				});
			}
		}

		request = null;
		isProcessing = false;
	}


	public Proposal chooseProposal()
	{
		proposalComparator comp = new proposalComparator();
		Collections.sort(allProposals, comp);
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


	@Override
	public IFuture<Boolean> retrieveBuyer() {

		window.setVisible(true);
		return new Future<Boolean>(true);
	}



}