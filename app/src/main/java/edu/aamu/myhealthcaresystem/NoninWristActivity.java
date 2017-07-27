package edu.aamu.myhealthcaresystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

import edu.aamu.myhealthcaresystem.nonin.NoninServiceStatus;
import edu.aamu.myhealthcaresystem.nonin.NoninTCPSender;

/**
 * Created by WChoosilp-Asus on 9/4/2016.
 */
public class NoninWristActivity extends AppCompatActivity {

    Toolbar toolbar;
    //Button home;
    TextView tvNonin;

    private SharedPreferences preferences;
    private static String noninaddress;
    private static int port;
    private boolean noninON;
    NoninTCPSender noninSender;
    NoninServiceStatus noninService;
    byte buffer[];
    TextView pulse, oxygen, info, pulseInfo;
    ImageButton callNurse, callAmbulance;
    ConnectionStatus connectionStatus;
    Timer mytimer;
    private boolean newconnectionnonin;
    private static String ipaddress;

    private GoogleApiClient client;

    public NoninWristActivity() {
        newconnectionnonin = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "WristOX Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://edu.aamu.myhealthcaresystem/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    class Connection {
    }

    class ConnectionStatus extends TimerTask {

        public void run() {
            NoninWristActivity noninWristActivity = NoninWristActivity.this;
            class Connection
                    implements Runnable {

                public void run() {
                    if (noninService.connected) {
                        if (!newconnectionnonin) {
                            boolean flag = newconnectionnonin = true;
                            Toast.makeText(getApplicationContext(), "CONNECTED WITH NONIN WRISTOX2 DEVICE!", Toast.LENGTH_LONG).show();
                        }
                        ShowData();
                    } else if (newconnectionnonin) {
                        boolean flag2 = newconnectionnonin = false;
                        Toast.makeText(getApplicationContext(), "DISCONNECTED FROM NONIN WRISTOX2 DEVICE!", Toast.LENGTH_LONG).show();
                    }
                }

                final ConnectionStatus connection;

                Connection() {
                    connection = ConnectionStatus.this;
                    //super();
                }
            }

            Connection conn = new Connection();
            noninWristActivity.runOnUiThread(conn);
        }

        final NoninWristActivity noninWristActivity;

        private ConnectionStatus() {
            noninWristActivity = NoninWristActivity.this;
            //super();
        }

        ConnectionStatus(Connection connection) {
            this();
        }
    }

    public void ShowData() {
        if (noninON) {
            TextView oxygen = (TextView) findViewById(R.id.tvNoninOSData);
            StringBuilder stringbuilderOxygen = new StringBuilder();
            int i = noninService.oxygen;
            String sOxygen = stringbuilderOxygen.append(i).append("").toString();
            oxygen.setText(sOxygen);

            // Check the level of Oxygen
            Double dOxygen = Double.parseDouble(sOxygen);
            if (dOxygen > 90) {
                //info.setText("Warning message!");
                info.setText("Your Oxygen Saturation Info: " + dOxygen);

                callNurse.setVisibility(View.VISIBLE);

                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                callNurse.startAnimation(anim);
            }

            TextView pulse = (TextView) findViewById(R.id.tvNoninPulseData);
            StringBuilder stringbuilderPulse = new StringBuilder();
            int j = noninService.pulse;
            String sPulse = stringbuilderPulse.append(j).append("").toString();
            pulse.setText(sPulse);

            // Check the level of pulse rate
            Double dPulse = Double.parseDouble(sPulse);
            if (dPulse > 70) {
                //info.setText("Warning message!");
                pulseInfo.setText("Your Pulse Rate Info: " + dPulse);

                callAmbulance.setVisibility(View.VISIBLE);

                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500); // Manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                callAmbulance.startAnimation(anim);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonin_wrist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //home = (Button) findViewById(R.id.btnHome);
        tvNonin = (TextView) findViewById(R.id.tvNoninToolbarHome);
        info = (TextView) findViewById(R.id.tvNoninInfo);
        pulseInfo = (TextView) findViewById(R.id.tvNoninPulseInfo);

        callNurse = (ImageButton) findViewById(R.id.imgBtnNoninCallNurse);
        callNurse.setVisibility(View.INVISIBLE);
        callAmbulance = (ImageButton) findViewById(R.id.imgBtnNoninCallAmbulance);
        callAmbulance.setVisibility(View.INVISIBLE);

        // Set the TextView to custom digital font
        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
                "DS-DIGIT.TTF");

        byte abyte0[] = new byte[375];
        buffer = abyte0;
        TextView pulseData = (TextView) findViewById(R.id.tvNoninPulseData);
        pulse = pulseData;
        pulseData.setTypeface(myTypeface);
        TextView oxygenData = (TextView) findViewById(R.id.tvNoninOSData);
        oxygen = oxygenData;
        oxygenData.setTypeface(myTypeface);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = sharedpreferences;
        noninaddress = preferences.getString("noninaddress", "00:1C:05:01:47:7F");
        port = preferences.getInt("port", 8080);
        boolean flag = preferences.getBoolean("noninON", true);
        noninON = flag;
        int i = port;
        String s = ipaddress;
        NoninTCPSender NoninTCPSender1 = new NoninTCPSender(i, s);
        noninSender = NoninTCPSender1;
        String s1 = noninaddress;
        NoninTCPSender NoninTCPSender2 = noninSender;
        NoninServiceStatus servicespp = new NoninServiceStatus(s1, 10000, NoninTCPSender2);
        noninService = servicespp;
        noninService.start();
        noninSender.start();

        if (noninON) {
            noninService.UnPause();
            noninSender.UnPause();
        }
        //int j = Log.e("NONIN_WRIST0X2", "+++ ON CREATE +++");

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        tvNonin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignUP = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentSignUP);
                finish();
            }
        });

/*        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignUP = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentSignUP);
                finish();
            }
        });*/

    }

    public void onDestroy() {
        super.onDestroy();
        //int i = Log.e("NONIN_WRIST0X2", "+++ ON DESTROY +++");
        noninService.Stop();
        noninSender.Stop();
        noninService = null;
        noninSender = null;
    }

    public void onPause() {
        super.onPause();
        mytimer.cancel();
        //int i = Log.e("NONIN_WRIST0X2", "+++ ON PAUSE +++");
    }

    public void onRestart() {
        //int i = Log.e("NONIN_WRIST0X2", "+++ ON RESTART +++");
        super.onRestart();

        int l = noninService.getState().hashCode();
        int i1 = Thread.State.TERMINATED.hashCode();
        if (l == i1) {
            String s1 = noninaddress;
            NoninTCPSender NoninTCPSender1 = noninSender;
            NoninServiceStatus servicespp = new NoninServiceStatus(s1, 10000, NoninTCPSender1);
            noninService = servicespp;
            noninService.start();
        }
        int j1 = noninSender.getState().hashCode();
        int k1 = Thread.State.TERMINATED.hashCode();
        if (j1 == k1) {
            int l1 = port;
            String s2 = ipaddress;
            NoninTCPSender NoninTCPSender2 = new NoninTCPSender(l1, s2);
            noninSender = NoninTCPSender2;
            noninSender.start();
        }
        if (noninON) {
            noninService.UnPause();
            noninSender.UnPause();
        }
    }

    public void onResume() {
        super.onResume();
        Timer timer = new Timer();
        mytimer = timer;
        ConnectionStatus ticktackread = new ConnectionStatus(null);
        connectionStatus = ticktackread;
        Timer timer1 = mytimer;
        ConnectionStatus ticktackread1 = connectionStatus;
        long l = 1000L;
        timer1.schedule(ticktackread1, 1000L, l);

        //int j = Log.e("NONIN_WRIST0X2", "+++ ON RESUME +++");
    }

    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "WristOX Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://edu.aamu.myhealthcaresystem/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        //int i = Log.e("NONIN_WRIST0X2", "+++ ON STOP +++");
        noninService.Pause();
        noninSender.Pause();
        client.disconnect();
    }
}
