package edu.aamu.myhealthcaresystem;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Created by WChoosilp-Asus on 9/4/2016.
 */
public class HomeFragment extends Fragment {

    Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();

        Button logout = (Button) rootView.findViewById(R.id.bLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(HomeFragment.this).commit();

                Intent intentSignUP = new Intent(getActivity().getApplicationContext(), WelcomeActivity.class);
                startActivity(intentSignUP);
            }
        });

        return rootView;
    }
}
