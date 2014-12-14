package Logic;

import Logic.Proposal;
import jadex.commons.future.IFuture;

public interface IBuyService
{
  public IFuture<Boolean> sendProposal(Proposal p);    // the elements received by this function are used in the buyer agent.
}
