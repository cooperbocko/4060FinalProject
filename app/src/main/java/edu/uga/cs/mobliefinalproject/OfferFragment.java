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


public class OfferFragment extends Fragment {
    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "OfferFragment";
    private List<RideOfferModel> rideOfferModelList;
    private FirebaseDatabase database;

    public OfferFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OfferFragment newInstance(int position) {
        OfferFragment fragment = new OfferFragment();
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



        //list of ride offers
        rideOfferModelList = new ArrayList<>();


        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rideoffers");

        //gets non-accepted and non-owned ride offers
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                rideOfferModelList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    RideOfferModel rideOfferModel = postSnapshot.getValue(RideOfferModel.class);
                    rideOfferModel.setKey( postSnapshot.getKey() );

                    //check if accepted or your own offer
                    if (rideOfferModel.isAccepted() || rideOfferModel.getDriver().equals(CurrentUser.email)) {
                        Log.d(DEBUG, "Offer not added: " + rideOfferModel);
                        continue;
                    } else {
                        //add offer to list
                        rideOfferModelList.add( rideOfferModel );
                        Log.d(DEBUG, "Offer added: " + rideOfferModel);

                    }
                }

                //implement this later
                //Log.d( DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter" );
                //recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Log.d(DEBUG, "Error reading offers from database: " + databaseError);
            }
        } );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer, container, false);
    }
}