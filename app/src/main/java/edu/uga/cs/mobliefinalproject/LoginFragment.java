package edu.uga.cs.mobliefinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment {
    private static final String DEBUG = "Login Fragment";
    private FirebaseAuth mAuth;
    private EditText et_email;
    private EditText et_password;
    private Button btn_login;


    public LoginFragment() {
        // Required empty public constructor
    }
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
        et_email = rootView.findViewById(R.id.et_email);
        et_password = rootView.findViewById(R.id.et_password);
        btn_login = rootView.findViewById(R.id.btn_login);

        //Button listener
        btn_login.setOnClickListener(new LoginButtonListener());


        return rootView;

    }


    private class LoginButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //get text from edit texts
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            //check email
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.d(DEBUG, "Valid Email");
            } else {
                //Toast.makeText(getActivity(), "Invalid Email Address!", Toast.LENGTH_SHORT).show();
                Log.d(DEBUG, "Invalid Email");
                return;
            }

            //check password
            if (password.isEmpty()) {
                Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                Log.d(DEBUG, "Invalid Password");
                return;
            } else {
                Log.d(DEBUG, "Valid Password");
            }

            //sign in with built in firebase auth
            mAuth = FirebaseAuth.getInstance();

            mAuth.signInWithEmailAndPassword( email, password )
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(DEBUG, "signInWithEmail:success" );
                                //FirebaseUser user = mAuth.getCurrentUser(); ---> maybe use this for current user
                                //transition to new fragment and update current user
                                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                                startActivity(intent);


                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Log.d(DEBUG, "signInWithEmail:failure", task.getException() );
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
