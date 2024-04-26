package edu.uga.cs.mobliefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    //logins:dev1@gmail.com, password   : dev2@gmail.com, password
    //all below is test code to confirm that it is connected to firebase correctly. Can do anything with it.

    public static final String AUTH_HEADER = "header";
    private  Button login, signup;

    //public static final String TAG = "SuperApp";
    //private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = (Button) findViewById(R.id.button1);
        signup = (Button) findViewById(R.id.button2);

        login.setOnClickListener((new MainActivity.ButtonCLickListener1()));
        signup.setOnClickListener((new MainActivity.ButtonCLickListener2()));



    }



    private class ButtonCLickListener1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent   = new Intent(v.getContext(), AuthActivity.class);
            intent.putExtra(AUTH_HEADER, "Log In");


            startActivity(intent);



        }
    }

    public class ButtonCLickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {


            Intent intent   = new Intent(v.getContext(), AuthActivity.class);
            intent.putExtra(AUTH_HEADER, "Sign Up");

            startActivity(intent);


        }
    }
}
