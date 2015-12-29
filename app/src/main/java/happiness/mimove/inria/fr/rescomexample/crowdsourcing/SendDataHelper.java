package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.ambientic.crowdsource.core.GoFlowException;
import com.ambientic.crowdsource.core.GoFlowFactory;
import com.ambientic.crowdsource.core.GoFlowPushTask;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;


import happiness.mimove.inria.fr.rescomexample.models.Mood;

public class SendDataHelper {
    private static SendDataHelper _instance;
    private static String Tag = "SendDataHelper";

    private CrowdsourcingMonitor _monitor = null;
    private Station _station;
    private GoFlowPushTask<CSMeasurement> _pushTaskMeasurment;

    private Context context;


    public static SendDataHelper getInstance(){
        if(_instance == null)
            _instance = new SendDataHelper();

        return _instance;
    }

    /**
     * private constructor
      */
    private SendDataHelper() {
    };




    /**
     * Send a measurement object to the server
     *
     * @param measurement The measurement instance to send
     */
    public long sendMoodToServer(Mood measurement){
        // make sure the connection is started
        //connectCrowdsourcing();

        // initialize the push task once, since location does not change
        try {
            // create the callback
            CSMeasurementPushCallback callback = new CSMeasurementPushCallback();
            // init the task
            _pushTaskMeasurment = new GoFlowPushTask<CSMeasurement>(_station.getId(), CSMeasurement.class, callback);
            // provide the task to the callback so that it can unregister the publisher
            callback.setTask (_pushTaskMeasurment);
        } catch (GoFlowException e) {
            Log.d(Tag, "unable to create push task " + e.getMessage());
        }


        try {
            CSMeasurement csMeasurement = new CSMeasurement();
            csMeasurement.setCrowdSourcedValue(measurement);

            // make sure it is clean
            _station.setAddress(null);

            //


            // now that the station has been updated, set the CS location with the current station value
            // if (measurement.get_location().getLatitude() !=0 && measurement.get_location().getLongitude() !=0 )
            csMeasurement.setCrowdSourcedLocation(_station);

            // reuse existing pushtask, but create if failure at start/runtime
            if (_pushTaskMeasurment==null) {
                try {
                    CSMeasurementPushCallback callback = new CSMeasurementPushCallback();
                    _pushTaskMeasurment = new GoFlowPushTask<CSMeasurement>(_station.getId(), CSMeasurement.class, callback);
                    // provide the task to the callback so that it can unregister the publisher
                    callback.setTask (_pushTaskMeasurment);
                } catch (GoFlowException e) {
                    Log.d(Tag, "unable to create push task " + e.getMessage());
                }
            }

            if (_pushTaskMeasurment!=null) {
                long requestId = _pushTaskMeasurment.sendData(csMeasurement);
                return requestId;
            } else {
                Log.d(Tag, "sendMeasurementToServer failed CS not yet initialized");
            }
        } catch (Exception e) {
            // global catch to prevent app from crashing due to some unexpected network error
            Log.d(Tag, "sendMeasurementToServer failed " + e.getMessage());
        }

        return 0;
    }




    /**
     *
     */
    public void connectCrowdsourcing() {
        GoFlowFactory.getSingleton().connect();
    }

    /**
     *
     */
    public void disconnectCrowdsourcing() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GoFlowFactory.getSingleton().disconnect();
    }


    /**
     * Initialize the crowdsourcing middleware. Connecting to RabbitMQ and GoFlow server.
     */

    public void initCrowdsourcing(Context context) {

        this.context = context;

        _station = new Station("_station", 0.0, 0.0);
        _monitor = new CrowdsourcingMonitor();

        this.context = context;


        // initialize goflow
        try {
            // disable heartbeats to preserve battery - must be done before init
            GoFlowFactory.getSingleton().setHeartbeats(0);
            // init goflow
            Object[] args = new Object[2];
            args[0] = context;
            args[1] = _monitor;

            // do not start the connection, just init the fields
            GoFlowFactory.getSingleton().initCrowdsourcing(args, false);
            Log.d(Tag, "goflow initialized");
        } catch (Exception e) {
            Log.e(Tag, "unable to init goflow " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     *
     */
    public void terminateCrowdsourcing() {
        // wait 2 seconds
        //Create a new thread only if it does not exist or has been terminated.
        Thread terminateThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                GoFlowFactory.getSingleton().terminateCrowdsourcing(); //Stop crowdsourcing middleware
            }
        };
        terminateThread.start();
    }


}