import jadex.commons.future.IFuture;

public interface IBuyService
{
  public IFuture<String> buySomething(String prod, int num);    // the elements received by this function are used in the buyer agent. 
}
