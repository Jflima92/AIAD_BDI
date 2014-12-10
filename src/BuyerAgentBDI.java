import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;

@Agent
@Arguments({
		@Argument(name="product", clazz=String.class, defaultvalue="N/A"),
		@Argument(name="desiredPrice", clazz=Integer.class, defaultvalue="-1"),
		@Argument(name="nou", clazz=Integer.class, defaultvalue="-1")
})
@Service
@Description("This agent buys products.")
public class BuyerAgentBDI  {

	@Agent
	protected BDIAgent agent;

	protected String product;
	protected int desiredPrice;
	protected int numberOfUnits;

	@AgentCreated
	public void init() {

		product = (String) 	agent.getArgument("product");
		desiredPrice = (Integer) agent.getArgument("desiredPrice");
		numberOfUnits = (Integer) agent.getArgument("nou");

	}

	@AgentBody
	public void body()
	{
		SServiceProvider.getServices(agent.getServiceProvider(), ISellService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<ISellService>()
				{
			public void intermediateResultAvailable(ISellService is) {

				Request r = new Request(product, numberOfUnits, desiredPrice);
				System.out.println("Vou tentar comprar " + numberOfUnits + " " + product + " por " + desiredPrice+ " euros!");
				is.buyRequest(r);
			}
				});	
	}
};