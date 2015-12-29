package happiness.mimove.inria.fr.rescomexample;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ambientic.crowdsource.core.GoFlowFactory;

import java.util.Calendar;

import happiness.mimove.inria.fr.rescomexample.crowdsourcing.SendDataHelper;
import happiness.mimove.inria.fr.rescomexample.models.Mood;


public class Home extends Activity {

    private static String Tag = "Home";

    private Button _BT_Submit;
    private String currentMood;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        //Initialize crowdsourcing
        SendDataHelper.getInstance().initCrowdsourcing(getApplicationContext());

    }




    @Override
    protected void onPause() {

        super.onPause();

    }


    @Override
    public void onResume() {

        super.onResume();
    }


    @Override
    protected void onStop() {



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
        SendDataHelper.getInstance().connectCrowdsourcing();
        Mood mood = new Mood();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mood.setTime(Calendar.getInstance());
        mood.setMood(currentMood);
        mood.setLatitude(location.getLatitude());
        mood.setLongitude(location.getLongitude());

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
