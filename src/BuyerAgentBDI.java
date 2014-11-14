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

	@AgentBody
	public void body()
	{
		SServiceProvider.getServices(agent.getServiceProvider(), ISellService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<ISellService>()
				{

			@Override
			public void intermediateResultAvailable(ISellService is) {
				
				System.out.println("Vou comprar 2 Samsungs Galaxy S5!");
				is.buyRequest("Samsung Galaxy S5", 2);
			}
				});	
	}
};