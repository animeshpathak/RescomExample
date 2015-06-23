package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.util.Log;

import com.ambientic.crowdsource.core.GoFlowManagerCallback;
import com.ambientic.incentive.core.IncentiveCallback;

/**
 * Created by Georgios Mathioudakis (mathioudakis.giorgos@gmail.com) on 26/9/14.
 */
public class CrowdsourcingMonitor implements GoFlowManagerCallback {

    private static String Tag = "CroudsourcingMonitor";

    @Override
    public void crowdsourcingEnabled() {
        Log.d(Tag, "crowdsourcingEnabled");
    }

    @Override
    public void crowdsourcingDisabled() {
        Log.d(Tag, "crowdsourcingDisabled");
    }

    @Override
    public void crowdsourcingNetworkPending() {
        Log.d(Tag, "crowdsourcingNetworkPending");
    }

    @Override
    public IncentiveCallback getIncentiveCallback() {
        return null;
    }
}