package edu.uga.cs.mobliefinalproject;

import android.content.Intent;
import android.os.Bundle;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "Profile Fragment";

  public static final String BUTTON_TYPE = "Button Type";
    private FirebaseDatabase database;
    private UserModel user;

    private TextView greeting, totalRides;

    private Button logout, requestAcc, ridesAcc, verifiedRides;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(int position) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int position = getArguments().getInt(FRAGMENT_POSITION);

        }

        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        //gets current user data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UserModel userModel = postSnapshot.getValue(UserModel.class);
                    userModel.setKey(postSnapshot.getKey());
                    if (userModel.getEmail().equals(CurrentUser.email)) {
                        //set the profile data and update the usermodel in this fragment
                        user = userModel;
                        greeting.setText("Welcome, " + user.getEmail());
                        totalRides.setText("Points: " + user.getPoints());
                        Log.d(DEBUG, "Current user data found: " + userModel.toString());
                        break;
                    } else {
                        continue;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG, "Failed to get current user data of: " + CurrentUser.email + " database error: " + error);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        greeting = rootView.findViewById(R.id.textView);
        totalRides = rootView.findViewById(R.id.textView3);
        logout = rootView.findViewById(R.id.button4);
        verifiedRides = rootView.findViewById(R.id.button5);
        requestAcc = rootView.findViewById(R.id.button7);
        ridesAcc = rootView.findViewById(R.id.button6);

        ridesAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(rootView.getContext(), SettingsActivity.class);
                i.putExtra(BUTTON_TYPE, "rides accepted");
                startActivity(i);

            }
        });
        requestAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(rootView.getContext(), SettingsActivity.class);
                i.putExtra(BUTTON_TYPE, "request accepted");
                startActivity(i);

            }
        });


        verifiedRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(rootView.getContext(), SettingsActivity.class);
                i.putExtra(BUTTON_TYPE, "verified rides");
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}