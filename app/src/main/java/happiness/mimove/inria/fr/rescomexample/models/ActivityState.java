package happiness.mimove.inria.fr.rescomexample.models;

import com.ambientic.crowdsource.data.GoFlowValue;

import java.util.Calendar;

/**
 * Created by frebhi on 23/06/2015.
 */
public class ActivityState extends GoFlowValue {
    private String state;
    private double latitude;
    private String longitude;
    private String uid;
    private Calendar time;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}
