package test;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;


@Description("This agent declares a required clock service.")
@Agent
@Service
@ProvidedServices(@ProvidedService(type = sendMessageService.class))
public class User1Agent implements sendMessageService
{
	@Agent
	MicroAgent agent;
	
	String msg = new String("Aquela Mensagem");
	
	@AgentBody
	public void executeBody()
	{
	}

	@Override
	public IFuture<String> sendMessage() {
		
		System.out.println(msg);
		return new Future<String>(msg);
	}
}