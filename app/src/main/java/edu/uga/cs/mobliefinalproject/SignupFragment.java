package edu.uga.cs.mobliefinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupFragment extends Fragment {
    private static final String DEGUB = "SignupFragment";

    private EditText setEmail, setPassword, confirmPassword;

    private Button createAcc;

    public SignupFragment() {
        // Required empty public constructor
    }
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

        //views
        setEmail = rootView.findViewById(R.id.et_registerEmail);
        setPassword = rootView.findViewById(R.id.et_registerPassword);
        confirmPassword = rootView.findViewById(R.id.et_confrimPassword);
        createAcc = rootView.findViewById(R.id.btn_createAccount);

        //set listener
        createAcc.setOnClickListener(new RegisterButtonClickListener());


        return rootView;
    }

    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String email = setEmail.getText().toString();
            final String password = setPassword.getText().toString();

            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            // This is how we can create a new user using an email/password combination.
            // Note that we also add an onComplete listener, which will be invoked once
            // a new user has been created by Firebase.  This is how we will know the
            // new user creation succeeded or failed.
            // If a new user has been created, Firebase already signs in the new user;
            // no separate sign in is needed.
            firebaseAuth.createUserWithEmailAndPassword( email, password )
                    .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText( getActivity(),
                                        "Registered user: " + email,
                                        Toast.LENGTH_SHORT ).show();

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(DEGUB, "createUserWithEmail: success" );

                                createNewUser();

                                //FirebaseUser user = firebaseAuth.getCurrentUser(); ---> maybe use for current user..
                                //navigate to the home screen

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(DEGUB, "createUserWithEmail: failure", task.getException());
                                Toast.makeText(getActivity(), "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void createNewUser() {
        final String email = setEmail.getText().toString();
        final UserModel newUser = new UserModel(email, 5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.push().setValue( newUser )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Show a quick confirmation
                        Toast.makeText(getActivity(), "Job lead created for " + newUser.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(DEGUB, "New User Created: " + newUser.toString());

                        // Clear the EditTexts for next use.

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure( @NonNull Exception e ) {
                        Toast.makeText( getActivity(), "Failed to create a Job lead for " + newUser.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(DEGUB, "Failed to create New User: " + newUser.toString());
                    }
                });
    }
}