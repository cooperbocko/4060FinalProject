package edu.uga.cs.mobliefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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



         /*
        mAuth = FirebaseAuth.getInstance();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace( android.R.id.content, homeFragment ).commit();

        //TextView textView = findViewById(R.id.tv_1);

        /*mAuth = FirebaseAuth.getInstance();

        String email = "dev1@gmail.com";
        String password = "password";

        mAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d( TAG, "signInWithEmail:success" );
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.d( TAG, "signInWithEmail:failure", task.getException() );
                            Toast.makeText( MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "message" );


        // Read from the database value for ”message”
        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String message = dataSnapshot.getValue( String.class );

                Log.d( TAG, "Read message: " + message );
                textView.setText( message );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d( TAG, "Failed to read value.", error.toException() );
            }
        });


         */



         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.ride_offers) {
                RideOffersFragment rideOffersFragment = new RideOffersFragment();
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, rideOffersFragment).commit();
                return true;

            } else if(item.getItemId() == R.id.ride_requests) {
                RideRequestsFragment rideRequestsFragment = new RideRequestsFragment();
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, rideRequestsFragment).commit();
                return true;

            }else {
                // The user's action isn't recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
            }

    }
}

