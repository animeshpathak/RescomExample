package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pgr on 22/02/2015.
 */
public class ActivityRecognitionReceiver extends BroadcastReceiver {
    private static String Tag = "ActRecog";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Tag, "onReceive");
    }
}
