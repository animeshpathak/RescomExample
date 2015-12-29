package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import android.util.Log;

import com.ambientic.crowdsource.core.GoFlowPushCallback;
import com.ambientic.crowdsource.core.GoFlowPushTask;

/**
 * Created by Georgios Mathioudakis (mathioudakis.giorgos@gmail.com) on 26/9/14.
 */
public class CSMeasurementPushCallback implements GoFlowPushCallback<CSMeasurement> {

    private static String Tag = "Callbacks";
    private GoFlowPushTask<CSMeasurement> pushTaskMeasurment = null;

    @Override
    public void failedPush(long requestId) {
        Log.e(Tag, "Push failed: " + requestId);



        //MeasurementManager.getInstance().updateMeasurementStatus(requestId, UploadStatus.NotUploaded);

        if (pushTaskMeasurment!= null) {
            pushTaskMeasurment.terminate();
            pushTaskMeasurment = null;
        }
    }

    @Override
    public void successPush(long requestId) {
        Log.d(Tag, "Push succeeded: " + requestId);


        //MeasurementManager.getInstance().updateMeasurementStatus(requestId, UploadStatus.Uploaded);
        if (pushTaskMeasurment!= null) {
            pushTaskMeasurment.terminate();
            pushTaskMeasurment = null;
        }
    }

    @Override
    public void delayedPush(long requestId) {
        Log.d(Tag, "Push delayed: " + requestId);
    }

    public void setTask(GoFlowPushTask<CSMeasurement> pushTaskMeasurment) {
        this.pushTaskMeasurment = pushTaskMeasurment;
    }
}