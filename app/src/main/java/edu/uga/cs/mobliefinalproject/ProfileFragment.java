package edu.uga.cs.mobliefinalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {

    private static final String FRAGMENT_POSITION = "position";


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}