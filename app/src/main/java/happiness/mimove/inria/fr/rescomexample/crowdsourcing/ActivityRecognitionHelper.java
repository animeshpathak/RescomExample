package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;



/**
 * Created by pgr on 22/02/2015.
 */
public class ActivityRecognitionHelper implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static String Tag = "NoiseMonitoringHelper";
    private Context context;

    GoogleApiClient mGoogleActclient;
    PendingIntent mActivityRecognitionPendingIntent;
    Intent i;

    LocalBroadcastManager mBroadcastManager;

    boolean isActive = false;
    boolean isConnected = false;
    boolean triggerActivation = false;

    static ActivityRecognitionHelper singleton = null;

    synchronized static public ActivityRecognitionHelper getSingleton(Context context) {
        if (singleton==null) {
            singleton = new ActivityRecognitionHelper(context);
        }
        return singleton;
    }

    // empty constructor is required
    private ActivityRecognitionHelper() {

    }

    private ActivityRecognitionHelper(Context context) {
        this.context = context;

        i = new Intent(context, ActivityRecognitionIntentService.class);
        mBroadcastManager = LocalBroadcastManager.getInstance(context);

        mGoogleActclient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleActclient.connect();

        //Log.d(Tag, "created ActivityRecognitionHelper");
    }

    //
    //
    //


    synchronized public void stopService() {
        triggerActivation = false;
        if (isActive) {
            Log.d(Tag, "stop service");
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleActclient, mActivityRecognitionPendingIntent);
            isActive = false;
        }
    }

    synchronized public void startService() {
        if (!isActive) {
            if (isConnected) {
                //Log.d(Tag, "CONNECTED startService");
                i.setAction("ActivityRecognitionIntentService");
                mActivityRecognitionPendingIntent = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleActclient, 0, mActivityRecognitionPendingIntent);
                isActive = true;
            } else {
                //Log.d(Tag, "NOT CONNECTED WILL triggerActivation");
                triggerActivation = true;
            }
        }
    }

    //
    //
    //

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        //Log.d(Tag, "failed connection to google api client");
        isConnected = false;
    }

    @Override
    public void onConnected(Bundle arg0) {
        //Log.d(Tag, "connecting to google api client");
        isConnected = true;
        if (triggerActivation) {
            //Log.d(Tag, "triggerActivation");
            startService();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        //Log.d(Tag, "onConnectionSuspended");
        stopService();
    }

    @Override
    public void onDisconnected() {
        //Log.d(Tag, "disconnected from google api client");
        isConnected = false;
        isActive = false;
        triggerActivation = false;
    }

}
