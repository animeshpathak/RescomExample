package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.util.Log;

import com.ambientic.crowdsource.core.GoFlowPushCallback;



public class CSMeasurementPushCallback implements GoFlowPushCallback<CSMeasurement> {

    private static String Tag = "Callbacks";

    @Override
    public void failedPush(long requestId) {
        Log.e(Tag, "Push failed: " + requestId);
    }

    @Override
    public void successPush(long requestId) {
        Log.d(Tag, "Push succeeded: " + requestId);
    }

    @Override
    public void delayedPush(long requestId) {
        Log.d(Tag, "Push delayed: " + requestId);
    }
}