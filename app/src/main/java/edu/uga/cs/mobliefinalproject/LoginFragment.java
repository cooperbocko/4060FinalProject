package edu.uga.cs.mobliefinalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoginFragment extends Fragment {

    private EditText email,password;

    private Button btn;






    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        //views
         email = (EditText) rootView.findViewById(R.id.editTextText);
         password = (EditText) rootView.findViewById(R.id.editTextTextPassword);
         btn = (Button)  rootView.findViewById(R.id.button3);

         //Button listener
        btn.setOnClickListener(new LoginFragment.ButtonClickListener3());

        //Action Bar Back Button
        MainActivity ap = (MainActivity)getActivity();
        Toolbar toolBar = (Toolbar) rootView.findViewById(R.id.toolbar2);
        ap.setSupportActionBar(toolBar);
        ap.getSupportActionBar().setTitle("Log In");
        ap.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return rootView;
    }


    private class ButtonClickListener3 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            /*
            //IF USERNAME AND PASSWORD ARE CORRECT GO TO MAIN FRAGMENT
            Fragment loginFragment = new LoginFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView3, <add fragment or activity to go to>, null)
                    .addToBackStack(null)
                    .commit();
            */



        }
    }

}
