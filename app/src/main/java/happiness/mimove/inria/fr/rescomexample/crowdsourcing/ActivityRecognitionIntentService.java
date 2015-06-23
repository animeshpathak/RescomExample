package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by pgr on 22/02/2015.
 */
public class ActivityRecognitionIntentService extends IntentService {
    private static String Tag = "ActRecog";

    // static variable as new service created each time - required to let Measurement collect activity info
    static DataActivity lastActivity ;

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    static public DataActivity getLastActivity () {
        return lastActivity;
    }


    //
    //
    //

    private String getTypes(int type) {
        if(type == DetectedActivity.UNKNOWN)
            return "Unknown";
        else if(type == DetectedActivity.IN_VEHICLE)
            return "In Vehicle";
        else if(type == DetectedActivity.ON_BICYCLE)
            return "On Bicycle";
        else if(type == DetectedActivity.RUNNING)
            return "Running";
        else if(type == DetectedActivity.ON_FOOT)
            return "On Foot";
        else if(type == DetectedActivity.STILL)
            return "Still";
        else if(type == DetectedActivity.TILTING)
            return "Tilting";
        else if(type == DetectedActivity.WALKING)
            return "Walking";
        else
            return "";
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals("ActivityRecognitionIntentService")) {
            if(ActivityRecognitionResult.hasResult(intent)){
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                DetectedActivity mpactivity = result.getMostProbableActivity();

                //if (lastActivity!=null) {
                //    long delay = (System.currentTimeMillis() - lastActivity.getTs()) / 1000;
                //    Log.d(Tag, "Delay since last activity (s)  " + delay);
                //}

                lastActivity = new DataActivity( getTypes(mpactivity.getType()) , mpactivity.getConfidence());

                // for now, get directly from member variable
                // Log.d(Tag, "Send to broadcast");
                // Intent i = new Intent("ACTIVITY_RECOGNITION_DATA");
                // i.putExtra("Activity", getTypes(mpactivity.getType()));
                // i.putExtra("Confidence", mpactivity.getConfidence());
                // LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
            }
        }
    }


    public class DataActivity {

        String activity = "";
        int accuracy = -1;
        long ts = 0;

        public DataActivity (String activity, int accuracy ) {
            this.activity = activity;
            this.accuracy = accuracy;
            ts = System.currentTimeMillis();
            Log.d(Tag, "User Activity " + activity + " accur " + accuracy + " at time " + ts);
        }

        public String getLastActivity() {
            return activity;
        }

        public int getLastAccuracy() {
            return accuracy;
        }

        public long getTs() {
            return ts;
        }

    }
}
