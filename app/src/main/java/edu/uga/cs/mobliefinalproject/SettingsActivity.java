package edu.uga.cs.mobliefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent i = getIntent();

        String btnSelect = i.getStringExtra(ProfileFragment.BUTTON_TYPE);

        Toolbar toolBar3 = findViewById(R.id.toolbar3);

        setSupportActionBar(toolBar3);
        getSupportActionBar().setTitle(btnSelect);


        switch (btnSelect){
            case "verified rides":
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView2, VerifyFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
                break;
            case "request accepted":
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.fragmentContainerView2, RequestAcceptedFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
                break;
            case "rides accepted":
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                fragmentManager3.beginTransaction()
                        .replace(R.id.fragmentContainerView2, RidesAcceptedFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();
                break;
        }

    }
}