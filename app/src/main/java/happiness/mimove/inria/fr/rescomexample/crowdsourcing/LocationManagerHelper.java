package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

/**
 * Created by Georgios Mathioudakis (mathioudakis.giorgos@gmail.com) on 12/9/14.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;



/**
 * Verwaltet die vom Nutzer angegeben Positionsbestimmungsmethoden und bietet Hilfsmethoden zur Positionsbestimmung
 *
 */
public class LocationManagerHelper {
    // minimum frshness of location info to be considered
    public static final int MIN_FRESHNESS_MIN = 10;
    /**
     * ...wobei sich das "better" darauf bezieht, dass er ein neues Positionsevent verk_rpert, welches den Anspruch hat
     * im Sinne der Genauigkeit oder auch zeitlich "besser" zu sein, als ein vorheriges.
     */
    public interface BetterLocationListener extends EventListener {
        public void onBetterLocation(Location location);
    }

    private static boolean DEBUG_OUTPUT = false;

    private static String Tag = "LocationManagerHelper";
    private Context context;
    private LocationManager mLocationManager;
    private SharedPreferences mSharedPreferences;
    private Location currentBestLocation;
    private boolean active;
    private int minTime, minDistance;
    private List<BetterLocationListener> listenerList = new ArrayList<BetterLocationListener>();
    private LocationListener listener = new LocationListener() {

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(Tag, "Location status changed");

        }

        public void onProviderEnabled(String provider) {
            Log.d(Tag, "Provider: " + provider.toString() + " enabled");

        }

        public void onProviderDisabled(String provider) {
            Log.d(Tag, "Provider: " + provider.toString() + " disabled");

        }

        public void onLocationChanged(Location location) {


            // only consider location updates if the provided info is no more than X minutes old
            long now = System.currentTimeMillis();
            long diff = (now-location.getTime())/(1000*60);
            if (diff<MIN_FRESHNESS_MIN) {
                if (isBetterLocation(location, currentBestLocation)) {

                        logOutput("QSELF : better location");
                    currentBestLocation = location;
                    onBetterLocation(currentBestLocation);
                }
            }
        }
    };

    public LocationManagerHelper(Context context) {
        this.context = context;
        mLocationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
    }

    public String getBestProvider(Criteria criteria, boolean enabledOnly) {
        return mLocationManager.getBestProvider(criteria, enabledOnly);
    }

    public void findBestLocation(int minTime, int minDistance) {
        this.minTime = minTime;
        this.minDistance = minDistance;

        // removes current listener in manager
        mLocationManager.removeUpdates(listener);

        for (String provider : getProviders()) {
            // adds back the listener, prompting update
            mLocationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
            Location lastKnown = mLocationManager.getLastKnownLocation(provider);
            if (isBetterLocation(lastKnown, currentBestLocation)) {
                currentBestLocation = lastKnown;
                onBetterLocation(currentBestLocation);
            }
        }
        active = true;
    }

    public Location getBestLastKnownLocation() {
        Location currentBestLastKnown = null;
        for (String provider : getProviders()) {
            Location lastKnown = mLocationManager.getLastKnownLocation(provider);
            if (lastKnown != null) {
                //Log.v(Tag, "getBestLastKnownLocation " + provider + " " + lastKnown.getLatitude() + " " + lastKnown.getLongitude() + " " + lastKnown.getAccuracy());
                if (currentBestLastKnown == null || isBetterLocation(lastKnown, currentBestLastKnown)) {
                    //Log.v(Tag, "getBestLastKnownLocation is better location");
                    currentBestLastKnown = lastKnown;
                } else {
                    //Log.v(Tag, "getBestLastKnownLocation is not better location " + currentBestLastKnown.getAccuracy());
                }
            }
        }

        if(currentBestLastKnown == null) {
            //Log.v(Tag, "getBestLastKnownLocation no good location get BOGUS one" );
            currentBestLastKnown = new Location("GPS");
            currentBestLastKnown.setLatitude(0);
            currentBestLastKnown.setLongitude(0);
            currentBestLastKnown.setAccuracy(1000);
        }

        long now = System.currentTimeMillis();
        long diff = (now-currentBestLastKnown.getTime())/(1000*60);
        if (diff>MIN_FRESHNESS_MIN) {
            //Log.v(Tag, "getBestLastKnownLocation no fresh location get BOGUS one" + diff);
            currentBestLastKnown = new Location("GPS");
            currentBestLastKnown.setLatitude(0);
            currentBestLastKnown.setLongitude(0);
            currentBestLastKnown.setAccuracy(1000);
        }

        currentBestLocation = currentBestLastKnown;
        return currentBestLastKnown;
    }

    private List<String> getProviders() {
        return mLocationManager.getProviders(true);
    }

    public void stop() {
        mLocationManager.removeUpdates(listener);
        active = false;
    }

    /**
     * Angelehnt an http://developer.android.com/guide/topics/location/obtaining-user-location.html
     *
     * @param location
     * @param refLocation
     * @return
     */
    private boolean isBetterLocation(Location location, Location refLocation) {
        // if new location is null, discard
        if (location == null) {
            return false;
        } else if (refLocation == null) {
            // else if current ref location is null, only accept if the new location is no more
            // than 30 sec old


            // A new location is always better than no location...wenn sie nicht zu alt ist
            long t = location.getTime();
            long t2 = System.currentTimeMillis();
            long timeNowDelta = t2 - t;
            return (timeNowDelta < 30000);
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - refLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 30000;
        boolean isSignificantlyOlder = timeDelta < -30000;
        boolean isNewer = timeDelta > 0;

        // if the location is significantly newer (30 sec) then consider it better
        // if it is significantly older, just discard it
        if (isSignificantlyNewer) {

            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        // then if both new and old location are more or less same time (+/- 30 sec)
        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - refLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), refLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {

            return true;
        } else if (isNewer && !isLessAccurate) {

            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {

            return true;
        }
        return false;
    }

    /**
     * Von http://developer.android.com/guide/topics/location/obtaining-user-location.html
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void onBetterLocation(Location location) {
        for (BetterLocationListener listener : listenerList) {
            listener.onBetterLocation(location);
        }
    }

    public void addBetterLocationListener(BetterLocationListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    public void removeBetterLocationListener(BetterLocationListener listener) {
        listenerList.remove(listener);
    }

    public boolean isActive() {
        return active;
    }


    //
    //
    //

    // output filename - set as static as multiple LocationManagerHelper are created
    // (created in an activity onCreate)
    static private String outputFilename = null;
    static private SimpleDateFormat sdfLog = new SimpleDateFormat( "HH-mm-ss", Locale.US);

    private synchronized void createFile () {

        if (outputFilename!=null) {
            return;
        }

        String format = "yyyy-MM-dd-HH-mm-ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String filename = "QSelf_loc_" + sdf.format(new Date()) + ".txt";

        // stored on external/public avail
        File rootExtDir = Environment.getExternalStorageDirectory();
        String fullfilename = rootExtDir + "/" +filename;

        File outputFile = new File(fullfilename);

        if (outputFile.exists()) {
            System.out.println ("filename exists !");
        } else {
            System.out.println ("filename does not exists !");

            try {
                FileOutputStream fos = new FileOutputStream(outputFile);  // 2nd line
                fos.write("Location Output Log\n\n\n".getBytes());
                fos.close();

                System.out.println ("Location output file created : " + filename);
                outputFilename = fullfilename;
            } catch (Exception e) {
                // cannot open ? missing permission maybe
                e.printStackTrace();;
            }

        }
    }

        // checks that the timestamp in the location is same as the actual time on the device
        // if not append WARNING to string for each find in log file
        public void logOutput(String message, String locTimestamp){
            String tmp = sdfLog.format(new Date());
            if (!tmp.equals(locTimestamp)) {
                message += " WARNING LOC TIMESTAMP " + locTimestamp;
            }
            logOutput(message);
        }

        // append message to log file
        public void logOutput(String message){

        // prepare output line with time
        String format = "HH-mm-ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String debugLine = sdfLog.format(new Date()) + " : " + message + "\n";

        // create the file if not done yet
        createFile();

        // open the file, append and close
        try {
            FileWriter fw = new FileWriter(new File(outputFilename), true);
            fw.append(debugLine);
            fw.close();
        } catch (Exception e) {
        }
    }

}

