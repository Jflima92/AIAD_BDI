package Logic;

import jadex.commons.future.IFuture;

/**
 * Created by jorgelima on 15-12-2014.
 */

    public interface IManagerService {

        public IFuture<Void> submitEvaluation(String agentID, boolean agentFulfilledContract);
        public IFuture<Void> addAgentReputation(String agentName, double reputation);
}
