package edu.uga.cs.mobliefinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class AuthHomeFragment extends Fragment {

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

            Fragment loginFragment = new LoginFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView3, loginFragment, null)
                    .addToBackStack(null)
                    .commit();


        }
    }

    public class ButtonCLickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Fragment signupFragment = new SignupFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView3, signupFragment, null)
                    .addToBackStack(null)
                    .commit();

        }
    }
}