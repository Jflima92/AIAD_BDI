package test;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;


@Description("This agent declares a required clock service.")
@Agent
public class User2Agent
{
	@Agent
	MicroAgent agent;

	@AgentBody
	public void executeBody()
	{	

		SServiceProvider.getServices(agent.getServiceProvider(), sendMessageService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<sendMessageService>()
				{

			@Override
			public void intermediateResultAvailable(sendMessageService arg0) {
				// TODO Auto-generated method stub

				arg0.sendMessage();

			}
				});	
	}


}
