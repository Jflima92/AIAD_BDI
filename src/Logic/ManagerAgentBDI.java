package Logic;

import GUI.ManagerWindow;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorgelima on 15-12-2014.
 */

@Agent
@Service
@Description("This agent sells products.")
@ProvidedServices(@ProvidedService(type=IManagerService.class))
public class ManagerAgentBDI implements IManagerService {

    protected static double REPUTATION_DECAY = 2.0 / 3;

    @Agent
    protected BDIAgent agent;

    @Belief
    private Map<String, Double> sellerAgentReputations;
    private ManagerWindow window;

    @AgentBody
    public void init() {
        sellerAgentReputations = new HashMap<>();
        window = new ManagerWindow();
    }

    protected void initReputation(String agentName) {
        if (!sellerAgentReputations.containsKey(agentName)) {
            sellerAgentReputations.put(agentName, 1.0);

        }
    }

    public IFuture<Void> submitEvaluation(String agentName, boolean agentFulfilledContract) {
        System.out.println("foi submetido");
        initReputation(agentName);
        double currentReputation = sellerAgentReputations.get(agentName);
        if (agentFulfilledContract) {
            currentReputation++;
            window.updateAgent(agentName, currentReputation);
        } else {
            currentReputation *= REPUTATION_DECAY;
            window.updateAgent(agentName, currentReputation);
        }
        sellerAgentReputations.put(agentName, currentReputation);

        return new Future<>();
    }

    public IFuture<Void> addAgentReputation(String agentName, double reputation) {
        window.addAgent(agentName,reputation);

        return new Future<>();
    }




}
