package edu.aamu.myhealthcaresystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Created by WChoosilp-Asus on 9/4/2016.
 */
public class BloodPressureFragment extends Fragment {

    RadioButton male, female, yes, no;
    Button calculate, reset;
    EditText systolic, diastolic, age, etResult;
    TextView tvResult;
    ImageButton systolicHelp, diastolicHelp, callNurse, callAmbulance;

    Animation anim = new AlphaAnimation(0.0f, 1.0f);

    public BloodPressureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blood_pressure, container, false);

        male = (RadioButton) rootView.findViewById(R.id.rbBPMale);
        female = (RadioButton) rootView.findViewById(R.id.rbBPFemale);
        yes = (RadioButton) rootView.findViewById(R.id.rbBPTakingMedicineYes);
        no = (RadioButton) rootView.findViewById(R.id.rbBPTakingMedicineNo);
        systolic = (EditText) rootView.findViewById(R.id.etBPSystolic);
        diastolic = (EditText) rootView.findViewById(R.id.etBPDiastolic);
        age = (EditText) rootView.findViewById(R.id.etBPAge);
        age.requestFocus();
        etResult = (EditText) rootView.findViewById(R.id.etBPResult);

        tvResult = (TextView) rootView.findViewById(R.id.tvBPResult);
        //tvResult.setMovementMethod(new ScrollingMovementMethod());

        calculate = (Button) rootView.findViewById(R.id.btnBloodPressureCalculate);
        reset = (Button) rootView.findViewById(R.id.btnBloodPressureReset);

        callNurse = (ImageButton) rootView.findViewById(R.id.imgBtnBPCallNurse);
        callNurse.setVisibility(View.INVISIBLE);
        callAmbulance = (ImageButton) rootView.findViewById(R.id.imgBtnBPCallAmbulance);
        callAmbulance.setVisibility(View.INVISIBLE);

        systolicHelp = (ImageButton) rootView.findViewById(R.id.imgBtnBPSystoclicHelp);
        diastolicHelp = (ImageButton) rootView.findViewById(R.id.imgBtnBPDiastolicHelp);


        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        systolicHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Systolic Pressure");
                builder.setMessage("The top number, which is also the higher of the two numbers, measures the pressure in the arteries when the heart beats (when the heart muscle contracts).")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        diastolicHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Diastolic Pressure");
                builder.setMessage("Medical Definition of diastolic blood pressure. : the lowest arterial blood pressure of a cardiac cycle occurring during diastole of the heart—called also diastolic pressure; compare systolic blood pressure.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systolic.setText("");
                diastolic.setText("");
                age.setText("");
                age.requestFocus();
                etResult.setText("");
                tvResult.setText("");
                callNurse.setVisibility(View.INVISIBLE);
                callNurse.clearAnimation();
                callAmbulance.setVisibility(View.INVISIBLE);
                callAmbulance.clearAnimation();
            }
        });

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        callAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "911"));
                startActivity(callIntent);
            }
        });

        callNurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "2561234567"));
                startActivity(callIntent);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAge = age.getText().toString();
                String sSystolic = systolic.getText().toString();
                String sDiastolic = diastolic.getText().toString();

                if (!sSystolic.matches("") || !sDiastolic.matches("") || !sAge.matches("")) {
                    Double a = Double.parseDouble(sAge);
                    Double s = Double.parseDouble(sSystolic);
                    Double d = Double.parseDouble(sDiastolic);

                    if ((a >= 10 && a <= 99) && (s >= 80.0 && s <= 250.0) && (d >= 30 && d <= 160)) {
                        Double dMAP = (s + (2 * d)) / 3;

                        if (s >= 180 || d >= 120) {
                            tvResult.setText("Hypertensive Crisis\n\n" +
                                    "(Emergency care needed)\n\n" +
                                    "A hypertensive crisis is a severe increase in blood pressure that can lead to a stroke. Extremely high blood pressure — a top number (systolic pressure) of 180 millimeters of mercury (mm Hg) or higher or a bottom number (diastolic pressure) of 120 mm Hg or higher — damages blood vessels. They become inflamed and may leak fluid or blood. As a result, the heart may not be able to pump blood effectively.");
                            tvResult.setTextColor(Color.RED);
                            callAmbulance.setVisibility(View.VISIBLE);
                            callAmbulance.startAnimation(anim);
                            callNurse.setVisibility(View.INVISIBLE);
                            callNurse.clearAnimation();

                        } else if (s >= 160 || d >= 100) {
                            tvResult.setText("Hypertension 2\n\n" +
                                    "Stage 2 hypertension, also known as late high blood pressure or severe high blood pressure, is generally characterized by a systolic blood pressure value of greater than 159 mmHg, or a diastolic blood pressure value of 99 mmHg. Stage 2 hypertension indicates you have moderate to severe high blood pressure.Treatment guidelines allow for much less flexibility in the initial approach to Stage 2 Hypertension, and those diagnosed at this stage are usually started on anti-hypertension medicines immediately.\n\n" +
                                    "Stage 2 Hypertension, which is a very serious form of high blood pressure, also requires more frequent blood pressure checks and a high level of careful monitoring.\n\n" +
                                    "Anyone with Stage 2 Hypertension should seek immediate treatment.");
                            tvResult.setTextColor(Color.BLUE);
                            callNurse.setVisibility(View.VISIBLE);
                            callNurse.startAnimation(anim);
                            callAmbulance.setVisibility(View.INVISIBLE);
                            callAmbulance.clearAnimation();

                        } else if (s >= 140 || d >= 90) {
                            tvResult.setText("Hypertension 1\n\n" +
                                    "Stage 1 hypertension is a systolic pressure ranging from 140 to 159 mm Hg or a diastolic pressure ranging from 90 to 99 mm Hg. Normal blood pressure is the level above which minimal vascular damage occurs.  The incidence of hypertension is higher in older individuals, women and non-Hispanic blacks.\n\n" +
                                    "");
                        } else if (s >= 120 || d >= 80) {
                            tvResult.setText("Prehypertension\n\n" +
                                    "In prehypertension, the systolic (top number) reading is 120 mmHg-139 mmHg, or the diastolic (bottom number) reading is 80 mmHg-89 mmHg.\n" +
                                    "\n" +
                                    "Prehypertension is a warning sign that you may get high blood pressure in the future. High blood pressure increases your risk of heart attack, stroke, coronary heart disease, heart failure, and kidney failure.");
                            tvResult.setTextColor(Color.BLACK);
                            callNurse.setVisibility(View.INVISIBLE);
                            callNurse.clearAnimation();
                            callAmbulance.setVisibility(View.INVISIBLE);
                            callAmbulance.clearAnimation();
                        } else {
                            tvResult.setText("Normal\n\n" +
                                    "Congratulations on having blood pressure numbers that are within the normal (optimal) range of less than 120/80 mm Hg. Keep up the good work and stick with heart-healthy habits like following a balanced diet and getting regular exercise.");
                            tvResult.setTextColor(Color.BLACK);
                            callNurse.setVisibility(View.INVISIBLE);
                            callNurse.clearAnimation();
                            callAmbulance.setVisibility(View.INVISIBLE);
                            callAmbulance.clearAnimation();
                        }
                        etResult.setText(String.format("%.2f", dMAP));

                    } else {
                        Toast.makeText(getActivity(), "The Age should be between 10 - 99 and Systolic should be between 80 - 250 and Diastolic should be between 30 - 160.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter your Age, Systolic and Diastolic.", Toast.LENGTH_SHORT).show();
                    return;
                }

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        return rootView;
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }
            }
        }
    }
}


