import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;

import java.util.ArrayList;

@Agent
@Arguments({
		@Argument(name="product", clazz=String.class, defaultvalue="N/A"),
		@Argument(name="desiredPrice", clazz=Integer.class, defaultvalue="-1"),
		@Argument(name="nou", clazz=Integer.class, defaultvalue="-1")
})
@Service
@Description("This agent buys products.")
@ProvidedServices(@ProvidedService(type=IBuyService.class))
public class BuyerAgentBDI implements IBuyService {

	@Agent
	protected BDIAgent agent;

	protected String product;
	protected int desiredPrice;
	protected int numberOfUnits;
	protected Request r;
	protected ArrayList<Proposal> allProposals;

	@AgentCreated
	public void init() {

		product = (String) 	agent.getArgument("product");
		desiredPrice = (Integer) agent.getArgument("desiredPrice");
		numberOfUnits = (Integer) agent.getArgument("nou");

		r = new Request(product, numberOfUnits);

	}

	@AgentBody
	public void body()
	{
		System.out.println("Estou no body");
		SServiceProvider.getServices(agent.getServiceProvider(), ISellService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<ISellService>()
				{
			public void intermediateResultAvailable(ISellService is) {

			is.requireProposal(r);

			}
				});

		System.out.println("SIZE: " + allProposals.size());
	}


	@Override
	public IFuture<Boolean> sendProposal(Proposal p) {

		System.out.println("Buyer received a valid proposal, analysing...");
		if(p.getProduct().equals(this.product))
		{
			allProposals.add(p);

			return new Future<Boolean>(true);
		}


		return new Future<Boolean>(false);
	}

}