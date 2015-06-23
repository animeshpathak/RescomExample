
package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.content.Context;
import android.util.Log;

import com.ambientic.crowdsource.core.GoFlowException;
import com.ambientic.crowdsource.core.GoFlowFactory;
import com.ambientic.crowdsource.core.GoFlowPushTask;

import happiness.mimove.inria.fr.rescomexample.models.Mood;

/**
 * Created by Georgios Mathioudakis (mathioudakis.giorgos@gmail.com) on 21/10/14.
 */
public class SendDataHelper {
    private static SendDataHelper _instance;
    private static String Tag = "SendDataHelper";

    private CrowdsourcingMonitor _monitor = null;
    private Station _station;
    private GoFlowPushTask<CSMeasurement> _pushTaskMeasurment;
    private GoFlowPushTask<CSActivityData> _pushTaskActivity;

    public static SendDataHelper getInstance() {
        if (_instance == null)
            _instance = new SendDataHelper();

        return _instance;
    }

    /**
     * Send a ActivityData object to the server
     *
     * @param data The ActivityData instance to send
     */
    public long sendActivityToServer(ActivityData data) {
        try {
            CSActivityData csActivity = new CSActivityData();
            csActivity.setCrowdSourcedValue(data);
            csActivity.setCrowdSourcedLocation(_station);

            if (_pushTaskActivity != null) {
                long requestId = _pushTaskActivity.sendData(csActivity);
                return requestId;
            } else {
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Send a measurement object to the server
     *
     * @param mood The measurement instance to send
     */
    public long sendMoodToServer(Mood mood) {
        try {
            //Station station1 = new Station("station1", "stationDescr 1", 0.0, 0.0);
            //CSMeasurementCSPushCallback callback = new CSMeasurementCSPushCallback();


            CSMeasurement csMeasurement = new CSMeasurement();
            csMeasurement.setCrowdSourcedValue(mood);
            csMeasurement.setCrowdSourcedLocation(_station);

            // reuse existing pushtask, but create if failure at start/runtime
            if (_pushTaskMeasurment == null) {
                try {
                    CSMeasurementPushCallback callback = new CSMeasurementPushCallback();
                    _pushTaskMeasurment = new GoFlowPushTask<CSMeasurement>(_station.getId(), CSMeasurement.class, callback);
                } catch (GoFlowException e) {
                    Log.d(Tag, "unable to create push task " + e.getMessage());
                }
            }
            if (_pushTaskMeasurment != null) {
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
     * Initialize the crowdsourcing middleware. Connecting to RabbitMQ and GoFlow server.
     * TODO: Check after the init when the crowdsourcing is initialized. Use the callbacks in MeasurementCSManager
     */
    public void initCrowdsourcing(Context context) {
        _station = new Station("_station", "stationDescr 1", 0.0, 0.0);
        _monitor = new CrowdsourcingMonitor();

        // initialize goflow
        try {
            // disable heartbeats to preserve battery - must be done before init
            GoFlowFactory.getSingleton().setHeartbeats(0);
            // init goflow
            Object[] args = new Object[2];
            args[0] = context;
            args[1] = _monitor;
            GoFlowFactory.getSingleton().initCrowdsourcing(args);
            Log.d(Tag, "goflow initialized");
        } catch (Exception e) {
            Log.e(Tag, "unable to init goflow " + e.getMessage());
            e.printStackTrace();
        }

        // initialize the push task once, since location does not change
        try {
            CSMeasurementPushCallback callback = new CSMeasurementPushCallback();
            _pushTaskMeasurment = new GoFlowPushTask<CSMeasurement>(_station.getId(), CSMeasurement.class, callback);
        } catch (GoFlowException e) {
            Log.d(Tag, "unable to create push task " + e.getMessage());
        }
    }
}