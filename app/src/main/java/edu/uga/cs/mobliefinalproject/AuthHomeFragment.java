package edu.uga.cs.mobliefinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class AuthHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final String TAG = "SuperApp";

    public static final String AUTH_HEADER = "header";

    private Button login, signup;

    public AuthHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AuthHomeFragment newInstance(String param1, String param2) {
        AuthHomeFragment fragment = new AuthHomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_auth_home, container, false);

        //Initialize buttons
        login = (Button) rootView.findViewById(R.id.button1);
        signup = (Button) rootView.findViewById(R.id.button2);


        login.setOnClickListener((new AuthHomeFragment.ButtonCLickListener1()));
        signup.setOnClickListener((new AuthHomeFragment.ButtonCLickListener2()));

        return rootView;
    }

    private class ButtonCLickListener1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    public class ButtonCLickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}