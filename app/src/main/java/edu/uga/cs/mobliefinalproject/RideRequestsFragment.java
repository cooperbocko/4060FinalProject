package edu.uga.cs.mobliefinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RideRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RideRequestsFragment extends Fragment {
    private static final String DEBUG = "RideRequestsFragment";
    private FirebaseDatabase database;
    private List<RideRequestModel> rideRequestModelList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RideRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RideRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RideRequestsFragment newInstance(String param1, String param2) {
        RideRequestsFragment fragment = new RideRequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }






        //list of ride offers
        rideRequestModelList = new ArrayList<>();


        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("riderequests");

        //gets non-accepted and non-owned ride requests
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                rideRequestModelList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    RideRequestModel rideRequestModel = postSnapshot.getValue(RideRequestModel.class);
                    rideRequestModel.setKey( postSnapshot.getKey() );

                    //check if accepted or your own offer
                    if (rideRequestModel.isAccepted() || rideRequestModel.getRider().equals(CurrentUser.email)) {
                        Log.d(DEBUG, "Request not added: " + rideRequestModel);
                        continue;
                    } else {
                        //add request to list
                        rideRequestModelList.add( rideRequestModel );
                        Log.d(DEBUG, "Request added: " + rideRequestModel);

                    }
                }

                //implement this later
                //Log.d( DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter" );
                //recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Log.d(DEBUG, "Error reading requests from database: " + databaseError);
            }
        } );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_requests, container, false);
    }
}