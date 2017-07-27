package edu.aamu.myhealthcaresystem;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Created by WChoosilp-Asus on 9/4/2016.
 */
public class CardiacFragment extends Fragment {

    EditText etAge, etYear, etTotalCholesterol, etHDLCholesterol, etSystolicBP, etDiastolicBP, etResult;
    RadioButton rbGender, rbTreatedHBP, rbDiabetes, rbSmoker;
    RadioGroup rgGender, rgHBP, rgDiabetes, rgSmoker;
    Button bCalculate, bReset;
    TextView tvResult;
    ImageButton totalCholesterol, hdlCholesterol, systolic, diastolic;

    Double dGender, dTreatedHBP, dDiabetes, dSmoker;

    public CardiacFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cardiac, container, false);

        etAge = (EditText) rootView.findViewById(R.id.etCardiacAge);
        etAge.requestFocus();
        etYear = (EditText) rootView.findViewById(R.id.etCardiacYear);
        etTotalCholesterol = (EditText) rootView.findViewById(R.id.etCardiacTotalCholesterol);
        etHDLCholesterol = (EditText) rootView.findViewById(R.id.etCardiacHDLCholesterol);
        etSystolicBP = (EditText) rootView.findViewById(R.id.etCardiacSystolicPressure);
        etDiastolicBP = (EditText) rootView.findViewById(R.id.etCardiacDiastolicPressure);
        etResult = (EditText) rootView.findViewById(R.id.etCardiacResult);

        tvResult = (TextView) rootView.findViewById(R.id.tvCardiacResult);

        rgGender = (RadioGroup) rootView.findViewById(R.id.rgCardiacGender);
        rgHBP = (RadioGroup) rootView.findViewById(R.id.rgCardiacBP);
        rgDiabetes = (RadioGroup) rootView.findViewById(R.id.rgCardiacDiabetes);
        rgSmoker = (RadioGroup) rootView.findViewById(R.id.rgCardiacSmoker);

        bCalculate = (Button) rootView.findViewById(R.id.btnCardiacCalculate);
        bReset = (Button) rootView.findViewById(R.id.btnCardiacReset);

        totalCholesterol = (ImageButton) rootView.findViewById(R.id.imgBtnCardiacTotalCholesterolHelp);
        hdlCholesterol = (ImageButton) rootView.findViewById(R.id.imgBtnCardiacHDLCholesterolHelp);
        systolic = (ImageButton) rootView.findViewById(R.id.imgBtnCardiacSystolicHelp);
        diastolic = (ImageButton) rootView.findViewById(R.id.imgBtnCardiacDiastolicHelp);

        // Set value for male = 0 and female = 1
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        rbGender = (RadioButton) rootView.findViewById(selectedGenderId);
        if (rbGender.getText().toString().startsWith("m") ||
                rbGender.getText().toString().startsWith("M")) {
            dGender = 1.0;  // Male = 1.0
        } else {
            dGender = 0.0;  // Female = 0.0
        }

        // Set value for Treated High Blood Pressure
        int selectedTreatedHBPId = rgHBP.getCheckedRadioButtonId();
        rbTreatedHBP = (RadioButton) rootView.findViewById(selectedTreatedHBPId);
        if (rbTreatedHBP.getText().toString().startsWith("y") ||
                rbTreatedHBP.getText().toString().startsWith("Y")) {
            dTreatedHBP = 1.0;  // Yes = 1.0
        } else {
            dTreatedHBP = 0.0;  // No = 0.0
        }

        // Set value for Diabetes
        int selectedDiabetesId = rgDiabetes.getCheckedRadioButtonId();
        rbDiabetes = (RadioButton) rootView.findViewById(selectedDiabetesId);

        if (rbDiabetes.getText().toString().startsWith("y") ||
                rbDiabetes.getText().toString().startsWith("Y")) {
            dDiabetes = 1.0;  // Yes = 1.0
        } else {
            dDiabetes = 0.0;  // No = 0.0
        }

        // Set value for Smoker
        int selectedSmokerId = rgSmoker.getCheckedRadioButtonId();
        rbSmoker = (RadioButton) rootView.findViewById(selectedSmokerId);
        if (rbSmoker.getText().toString().startsWith("y") ||
                rbSmoker.getText().toString().startsWith("Y")) {
            dSmoker = 1.0;  // Yes = 1.0
        } else {
            dSmoker = 0.0;  // No = 0.0
        }

        totalCholesterol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Total cholesterol");
                builder.setMessage("Total cholesterol is the total amount of cholesterol in your blood. Your total cholesterol includes low-density lipoprotein (LDL, or “bad”) cholesterol and high-density lipoprotein (HDL, or “good”) cholesterol.")
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

        hdlCholesterol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("HDL cholesterol");
                builder.setMessage("HDL cholesterol is the name given to the cholesterol in the bloodstream that is carried by \"high density lipoprotein,” or HDL.")
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

        systolic.setOnClickListener(new View.OnClickListener() {
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

        diastolic.setOnClickListener(new View.OnClickListener() {
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

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAge.setText("");
                etAge.requestFocus();
                etYear.setText("");
                etTotalCholesterol.setText("");
                etHDLCholesterol.setText("");
                etSystolicBP.setText("");
                etDiastolicBP.setText("");
                etResult.setText("");
                tvResult.setText("");
            }
        });

        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sAge = etAge.getText().toString();
                String sYear = etYear.getText().toString();
                String sTotalCholesterol = etTotalCholesterol.getText().toString();
                String sHDLCholesterol = etHDLCholesterol.getText().toString();
                String sSystolicBP = etSystolicBP.getText().toString();
                String sDiastolicBP = etDiastolicBP.getText().toString();

                Double dAge, dYear, dHDLCholesterol, dTotalCholesterol, dSystolicBP, dDiastolicBP;

                Double dSystolicA, dDiastolicA, dSystolicM, dDiastolicM, dSystolicMU, dDiastolicMU,
                        dSystolicSigma, dDiastolicSigma, dSystolicU, dDiastolicU, dSystolicP, dDiastolicP;

                if (sAge.matches("") || (sYear.matches("")) || sTotalCholesterol.matches("") || sHDLCholesterol.matches("")
                        || sSystolicBP.matches("") || sDiastolicBP.matches("")) {
                    Toast.makeText(getActivity(),
                            "Please check the value in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    dAge = Double.parseDouble(sAge);
                    dYear = Double.parseDouble(sYear);
                    dHDLCholesterol = Double.parseDouble(sHDLCholesterol);
                    dTotalCholesterol = Double.parseDouble(sTotalCholesterol);
                    dSystolicBP = Double.parseDouble(sSystolicBP);
                    dDiastolicBP = Double.parseDouble(sDiastolicBP);

                    // Check for the range of input
                    if (dAge >= 90 || dAge <= 20) {
                        Toast.makeText(getActivity(),
                                "Age should be between 20 and 90.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dYear >= 10 || dYear <= 2) {
                        Toast.makeText(getActivity(),
                                "Year should be between 2 and 10.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dHDLCholesterol >= 5 || dHDLCholesterol <= 0.3) {
                        Toast.makeText(getActivity(),
                                "HDL Cholesterol should be between 0.3 and 5.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dTotalCholesterol >= 12 || dTotalCholesterol <= 2) {
                        Toast.makeText(getActivity(),
                                "Total Cholesterol should be between 2 and 12.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dSystolicBP >= 250 || dSystolicBP <= 80) {
                        Toast.makeText(getActivity(),
                                "Systolic Blood Pressure should be between 80 and 250.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dDiastolicBP >= 160 || dDiastolicBP <= 30) {
                        Toast.makeText(getActivity(),
                                "Diastolic Blood Pressure should be between 30 and 160.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ((dSystolicBP - dDiastolicBP) < 9.9) {
                        Toast.makeText(getActivity(),
                                "The subtraction of Systolic Blood Pressure from Diastolic Blood Pressure should be less than 9.9.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ((dTotalCholesterol - dHDLCholesterol) < 0.49) {
                        Toast.makeText(getActivity(),
                                "The subtraction of Total Cholesterol from HDL Cholesterol should be less than 0.49.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // Start Calculation
                    dSystolicA = 11.1122 - 0.9119 * Math.log(dSystolicBP) - 0.2767 * dSmoker - 0.7181 * Math.log(dTotalCholesterol / dHDLCholesterol) - 0.5865 * dTreatedHBP;

                    dDiastolicA = 11.0938 - 0.867 * Math.log(dDiastolicBP) - 0.2789 * dSmoker - 0.7142 * Math.log(dTotalCholesterol / dHDLCholesterol) - 0.7195 * dTreatedHBP;

                    if (dGender == 1.0) {
                        // Male
                        dSystolicM = (dSystolicA - 1.4792 * Math.log(dAge)) - 0.1759 * dDiabetes;
                    } else {
                        // Female
                        dSystolicM = (dSystolicA - 5.8549) + 1.8515 * Math.log(dAge / 74) * Math.log(dAge / 74) - 0.3758 * dDiabetes;
                    }

                    if (dGender == 1.0) {
                        // Male
                        dDiastolicM = (dDiastolicA - 1.6343 * Math.log(dAge)) - 0.2082 * dDiabetes;
                    } else {
                        // Female
                        dDiastolicM = (dDiastolicA - 6.5306) + 2.1059 * Math.log(dAge / 74) * Math.log(dAge / 74) - 0.4055 * dDiabetes;
                    }

                    dSystolicMU = 4.4181 + dSystolicM;
                    dDiastolicMU = 4.428 + dDiastolicM;

                    dSystolicSigma = Math.exp(-0.3155 - 0.2784 * dSystolicM);
                    dDiastolicSigma = Math.exp(-0.3171 - 0.2825 * dDiastolicM);

                    dSystolicU = (Math.log(dYear) - dSystolicMU) / dSystolicSigma;
                    dDiastolicU = (Math.log(dYear) - dDiastolicMU / dDiastolicSigma);

                    dSystolicP = 1 - Math.exp(- Math.exp(dSystolicU));
                    dDiastolicP = 1 - Math.exp(- Math.exp(dDiastolicU));

                    //Intent myIntent = new Intent(view.getContext(),CardiacResultActivity.class);
                    String sResult;

                    // Final risk score
                    if (dDiastolicP > dSystolicP) {
                        etResult.setText(String.format("%.2f", dDiastolicP));
                    } else {
                        etResult.setText(String.format("%.2f", dSystolicP));
                    }

                    // Display information for result
                    double dResult = Double.parseDouble(etResult.getText().toString());

                    if(dResult > 20.0) {
                        tvResult.setText("Highest risk: A greater than 20% risk that you will develop a heart attack or die from coronary disease in the next 10 years. This risk can be reduced by addressing and managing your risk factors with the help of your doctor.");
                    } else if(dResult >= 10.0 && dResult <= 20.0 ) {
                        tvResult.setText("Intermediate risk: A 10 to 20% risk that you will develop a heart attack or die from coronary disease in the next 10 years. This risk can be reduced by addressing and managing your risk factors with the help of your doctor.");
                    } else if(dResult < 10.0) {
                        tvResult.setText("Low risk: Less than 10% risk that you will develop a heart attack or die from coronary disease in the next 10 years. Continue to manage your risk factors and visit your doctor regularly to assess your risk.");
                    }

                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        return rootView;
    }
}
