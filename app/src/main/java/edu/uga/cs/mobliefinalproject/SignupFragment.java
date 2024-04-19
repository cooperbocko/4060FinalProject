package edu.uga.cs.mobliefinalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SignupFragment extends Fragment {

    private EditText setEmail, setPassword, confirmPassword;

    private Button createAcc;

    public SignupFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        View rootView =  inflater.inflate(R.layout.fragment_signup, container, false);

        setEmail = (EditText) rootView.findViewById(R.id.editTextText2);
        setPassword = (EditText) rootView.findViewById(R.id.editTextText3);
        confirmPassword = (EditText) rootView.findViewById(R.id.editTextText4);
        createAcc = (Button) rootView.findViewById(R.id.button5);

        return rootView;
    }
}