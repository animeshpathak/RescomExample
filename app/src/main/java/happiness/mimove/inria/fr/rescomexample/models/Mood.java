package happiness.mimove.inria.fr.rescomexample.models;

import com.ambientic.crowdsource.data.GoFlowValue;

import java.util.Calendar;

/**
 * Created by frebhi on 23/06/2015.
 */
public class Mood  extends GoFlowValue {
    private String mood;
    private double latitude;
    private double longitude;
    private String uid;
    private Calendar time;
    // activity recognition
    private String recogActivity;
    private int recogConfidence;
    public Mood() {
        super();
    }
    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public String getRecogActivity() {
        return recogActivity;
    }

    public void setRecogActivity(String recogActivity) {
        this.recogActivity = recogActivity;
    }

    public int getRecogConfidence() {
        return recogConfidence;
    }

    public void setRecogConfidence(int recogConfidence) {
        this.recogConfidence = recogConfidence;
    }
}
