import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;

/**
 * Created by jorgelima on 20-11-2014.
 */
public class main {

    public static void main(String[] args){
        IFuture<IExternalAccess> platfut = Starter.createPlatform(args);
        ThreadSuspendable sus = new ThreadSuspendable();
        IExternalAccess platform = platfut.get(sus);
        System.out.println("Started platform: "+platform.getComponentIdentifier());

        IComponentManagementService cms = SServiceProvider.getService(platform.getServiceProvider(),
                IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);

        IComponentIdentifier cid = cms.createComponent("/home/jorgelima/workspace/AIAD/bin/SellerAgentBDI.class", null).getFirstResult(sus);
        System.out.println("Started Seller Agent component: " + cid);

    }



}
