import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;

@Agent
@Service
@Description("This agent buys products.")
public class BuyerAgentBDI  {

	@Agent
	protected BDIAgent agent;

	protected String product;
	protected int maxPrice;
	protected int numberOfUnits;

	@AgentBody
	public void body()
	{
		SServiceProvider.getServices(agent.getServiceProvider(), ISellService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<ISellService>()
				{
			public void intermediateResultAvailable(ISellService is) {

				Request r = new Request("Samsung Galaxy S5", 10, 729);
				System.out.println("Vou tentar comprar 10 Samsungs Galaxy S5 por 729 euros!");
				is.buyRequest(r);
			}
				});	
	}
};