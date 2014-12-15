package Logic;

import Logic.Proposal;
import Logic.Request;
import jadex.commons.future.IFuture;


public interface ISellService
{
  public IFuture<Boolean> requireProposal(Request r);
  public IFuture<Boolean> acceptedProposal(Proposal p);
  public IFuture<Double> negotiation(Proposal p, int count);
  public IFuture<Boolean> retrieveSeller();

}