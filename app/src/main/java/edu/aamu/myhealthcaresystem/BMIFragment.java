package edu.aamu.myhealthcaresystem;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Created by WChoosilp-Asus on 9/4/2016.
 */
public class BMIFragment extends Fragment {

    RadioGroup radioGroup;
    RadioButton rbGender;
    EditText feet, inches, pounds, age, result;
    Button calculate, reset;
    TextView tvResult;
    Double dGender;

    public BMIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bmi, container, false);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.rgBMIGender);
        //rbMale = (RadioButton) rootView.findViewById(R.id.rbBMIMale);
        //rbFemale = (RadioButton) rootView.findViewById(R.id.rbBMIFemale);

        feet = (EditText) rootView.findViewById(R.id.etBMIHeightFeet);
        feet.requestFocus();
        inches = (EditText) rootView.findViewById(R.id.etBMIHeightInches);
        pounds = (EditText) rootView.findViewById(R.id.etBMIWeight);
        age = (EditText) rootView.findViewById(R.id.etBMIAge);
        result = (EditText) rootView.findViewById(R.id.etBMIResult);

        tvResult = (TextView) rootView.findViewById(R.id.tvBMIResult);

        calculate = (Button) rootView.findViewById(R.id.btnBMICalculate);
        reset = (Button) rootView.findViewById(R.id.btnBMIReset);

        // Set value for male = 0 and female = 1
        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
        rbGender = (RadioButton) rootView.findViewById(selectedGenderId);
        if (rbGender.getText().toString().startsWith("m") ||
                rbGender.getText().toString().startsWith("M")) {
            dGender = 1.0;  // Male = 1.0
        } else {
            dGender = 0.0;  // Female = 0.0
        }

        if(feet.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feet.setText("");
                feet.requestFocus();
                inches.setText("");
                pounds.setText("");
                age.setText("");
                result.setText("");
                tvResult.setText("");
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double dFeetToInch, dFinalResult, dInchToCentimeter, dAge;
                Double dCentimeter, dKilogram, dResult;
                Double dFeet, dInches, dPounds;
                String sFeet, sInches, sPounds, sAge;
                sAge = age.getText().toString();
                sFeet = feet.getText().toString();
                sInches = inches.getText().toString();
                sPounds = pounds.getText().toString();

                if (sAge.matches("") || sFeet.matches("") || sInches.matches("") || sPounds.matches("")) {
                    Toast.makeText(getActivity(), "Please enter your age, height and weight. ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dAge = Double.parseDouble(age.getText().toString());
                    dFeet = Double.parseDouble(feet.getText().toString());
                    dInches = Double.parseDouble(inches.getText().toString());
                    dPounds = Double.parseDouble(pounds.getText().toString());

                    // Set the range for feet, inches and pound
                    if ((dAge >= 10 && dAge <= 99) && (dFeet >= 4.0 && dFeet <= 7.0) && (dInches >= 0.0 && dInches <= 12.0) && (dPounds >= 80.0 && dPounds <= 800.0)) {
                        dFeetToInch = dFeet * 12;
                        dCentimeter = dFeetToInch + dInches;
                        dInchToCentimeter = dCentimeter * 2.54;
                        // Convert pound to kilogram
                        dKilogram = dPounds * 0.45;

                        dResult = dKilogram / dInchToCentimeter / dInchToCentimeter;
                        dFinalResult = dResult * 10000;

                        result.setText(String.format("%.2f", dFinalResult));

                        if (dFinalResult < 18.5) {
                            tvResult.setText("According to your information: " + "\n" +
                                    "You are underweight.");
                        } else if (dFinalResult > 18.5 && dFinalResult < 25) {
                            tvResult.setText("According to your information: " + "\n" +
                                    "You are very healthy.");
                        } else if (dFinalResult > 25 && dFinalResult < 30) {
                            tvResult.setText("According to your information: " + "\n" +
                                    "You are over weight.");
                        } else if (dFinalResult > 30) {
                            tvResult.setText("According to your information: " + "\n" +
                                    "You are obese!!!");
                        }
                    } else {
                        Toast.makeText(getActivity(), "The Age should be between 10 - 99 and the Height should be between 4 - 7 feet and 0.0 - 12 inches and the Weight should be between 80 - 800 pounds", Toast.LENGTH_SHORT).show();
                    }
                }

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        return rootView;
    }
}
