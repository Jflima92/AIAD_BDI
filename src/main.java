import GUI.MainMenu;
import GUI.Menu;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by jorgelima on 20-11-2014.
 */
public class main {

    public static void main(String[] args) throws IOException {

        IComponentManagementService cms;

        IFuture<IExternalAccess> platfut = Starter.createPlatform(args);
        final ThreadSuspendable sus = new ThreadSuspendable();
        final IExternalAccess platform = platfut.get(sus);
        System.out.println("Started platform: "+platform.getComponentIdentifier());


        cms = SServiceProvider.getService(platform.getServiceProvider(),
                IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);
            JFrame Frame = new Menu(cms, sus);


        /*IComponentIdentifier cid = cms.createComponent("/home/jorgelima/workspace/AIAD/bin/SellerAgentBDI.class", null).getFirstResult(sus);
        System.out.println("Started Seller Agent component: " + cid);*/

        /*SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                cms[0] = SServiceProvider.getService(platform.getServiceProvider(),
                        IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);
                MainMenu app = null;
                try {
                    app = new MainMenu(cms[0],sus);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                app.setVisible(true);
                System.out.println("cenass");
            }
        });*/


    }


}
