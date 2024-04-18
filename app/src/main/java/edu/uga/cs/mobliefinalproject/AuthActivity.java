package edu.uga.cs.mobliefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class AuthActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        //Main Activity button info
        Intent intent = getIntent();
        String header = intent.getStringExtra(MainActivity.AUTH_HEADER);

        //Action Bar Back Button
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //fragment manager
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(header){
            case "Log In":
                fragmentTransaction.replace(R.id.fragmentContainerView3, LoginFragment.class, null);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.addToBackStack(header);
                fragmentTransaction.commit();
                break;
        }




    }
}