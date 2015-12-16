package happiness.mimove.inria.fr.rescomexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ambientic.crowdsource.core.GoFlowFactory;

import java.util.Calendar;

import happiness.mimove.inria.fr.rescomexample.crowdsourcing.ActivityRecognitionHelper;
import happiness.mimove.inria.fr.rescomexample.crowdsourcing.ActivityRecognitionIntentService;
import happiness.mimove.inria.fr.rescomexample.crowdsourcing.CSMeasurement;
import happiness.mimove.inria.fr.rescomexample.crowdsourcing.LocationManagerHelper;
import happiness.mimove.inria.fr.rescomexample.crowdsourcing.SendDataHelper;
import happiness.mimove.inria.fr.rescomexample.models.Mood;


public class Home extends Activity {

    private static String Tag = "Home";

    private Button _BT_Submit;
    private String currentMood;

    private ActivityRecognitionHelper _activityManagerHelper = null;
    static private LocationManagerHelper _locationManagerHelper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        //Initialize crowdsourcing
        SendDataHelper.getInstance().initCrowdsourcing(getApplicationContext());
        _activityManagerHelper = ActivityRecognitionHelper.getSingleton(getApplicationContext());
        _activityManagerHelper.startService();
        //Used to start listening to user location
        if (_locationManagerHelper == null) {
            _locationManagerHelper = new LocationManagerHelper(getApplicationContext());
        }
        _locationManagerHelper.addBetterLocationListener(new LocationManagerHelper.BetterLocationListener() {
            public void onBetterLocation(Location location) {
                updatePosition(location);
            }
        });

    }


    private void updatePosition(Location location) {

    }


    @Override
    protected void onPause() {
        _locationManagerHelper.stop();
        _activityManagerHelper.stopService(); // stop activity recog
        super.onPause();

    }


    @Override
    public void onResume() {
        if (!_locationManagerHelper.isActive()) {
            _locationManagerHelper.findBestLocation(0, 0);
        }
        super.onResume();
    }


    @Override
    protected void onStop() {


        _locationManagerHelper.stop(); //Stop location tracking
        _activityManagerHelper.stopService(); // stop activity recog
        super.onStop();
    }


    public void initView() {
        setUpRadios();
        _BT_Submit = (Button) findViewById(R.id.submitButton);
        _BT_Submit.setEnabled(true);
        _BT_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMoodToServer();
                Toast.makeText(Home.this.getApplicationContext(), "Mood sent", Toast.LENGTH_LONG).show();

            }
        });
    }


    public void sendMoodToServer() {
        Mood mood = new Mood();
        Location location = _locationManagerHelper.getBestLastKnownLocation();
        final String device_id = GoFlowFactory.getSingleton().getDeviceInfo().getDeviceUID().toString();
        mood.setUid(device_id);
        mood.setTime(Calendar.getInstance());
        mood.setMood(currentMood);
        mood.setLatitude(location.getLatitude());
        mood.setLongitude(location.getLongitude());
        String sensorActiv = "";
        int sensorActivAccur = 0;

        ActivityRecognitionIntentService.DataActivity userActivity = ActivityRecognitionIntentService.getLastActivity();
        if (userActivity != null) {
            sensorActiv = userActivity.getLastActivity();
            sensorActivAccur = userActivity.getLastAccuracy();
            mood.setRecogActivity(sensorActiv);
            mood.setRecogConfidence(sensorActivAccur);
        }

        SendDataHelper.getInstance().sendMoodToServer(mood);
    }

    private void setUpRadios() {


        RadioButton sleepStartSelected = (RadioButton) findViewById(R.id.state_1);

        sleepStartSelected.setChecked(true);
        currentMood = "happy";

        RadioGroup radioGroupMood = (RadioGroup) findViewById(R.id.feelingsCheckboxes);
        radioGroupMood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(Tag, "change");
                switch (i) {
                    case R.id.state_1: {
                        currentMood = "happy";
                        break;
                    }
                    case R.id.state_2: {
                        currentMood = "sad";
                        break;
                    }
                    case R.id.state_3: {
                        currentMood = "not bad";
                        break;
                    }

                }
            }
        });
    }


}
