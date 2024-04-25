package edu.uga.cs.mobliefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //Initialize views
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);

        //begin an instance of fragment manager / transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //receive intent
        Intent intent = getIntent();
        String header = intent.getStringExtra(MainActivity.AUTH_HEADER);

        //Action Bar Back Button set up
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        switch (header) {
            case "Log In":
                fragmentTransaction.replace(R.id.fragmentContainerView, LoginFragment.class, null);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            default:

        }
    }
}