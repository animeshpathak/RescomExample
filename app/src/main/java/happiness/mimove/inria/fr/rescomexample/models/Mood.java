package happiness.mimove.inria.fr.rescomexample.models;

import com.ambientic.crowdsource.data.GoFlowValue;

import java.util.Calendar;

/**
 * Created by frebhi on 23/06/2015.
 */
public class Mood extends GoFlowValue {

    private String mood;
    //locatio info
    private double latitude;
    private double longitude;

    private Calendar time;


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


    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }


}
