package edu.uga.cs.mobliefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.Switch;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);



        //Initialize views
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);

        //receive intent
        Intent intent = getIntent();
        String header = intent.getStringExtra(MainActivity.AUTH_HEADER);

        //Action Bar Back Button set up
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment frag;

            switch(header){
                case "Log In":
                    frag = new LoginFragment();
                    break;
                default:
                    frag = new SignupFragment();
            }

            transaction
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, frag, null)
                    .commit();



    }
    }
