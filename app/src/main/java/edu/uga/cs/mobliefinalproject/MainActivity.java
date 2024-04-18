package edu.uga.cs.mobliefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    //all below is test code to confirm that it is connected to firebase correctly. Can do anything with it.

    public static final String TAG = "SuperApp";

    public static final String AUTH_HEADER = "header";

    private Button login, signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.tv_1);

        login = (Button) findViewById(R.id.button1);

        signup = (Button) findViewById(R.id.button2);

        login.setOnClickListener((new ButtonCLickListener1()));
        signup.setOnClickListener((new ButtonCLickListener2()));


        /*
        mAuth = FirebaseAuth.getInstance();
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
    }

    //Log in Button Handler
    private class ButtonCLickListener1 implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), AuthActivity.class);
            intent.putExtra(AUTH_HEADER, "Log In");

            startActivity(intent);

        }
    }

    //Sign Up Button Handler
    private class ButtonCLickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), AuthActivity.class);
            intent.putExtra(AUTH_HEADER, "Sign Up");

            startActivity(intent);

        }
    }
}