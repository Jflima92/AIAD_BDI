import jadex.commons.future.IFuture;


public interface ISellService
{
  public IFuture<Boolean> requireProposal(Request r);
  public IFuture<Boolean> buyRequest(Request r);
}
