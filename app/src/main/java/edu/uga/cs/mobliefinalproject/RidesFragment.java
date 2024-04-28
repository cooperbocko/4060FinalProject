package edu.uga.cs.mobliefinalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class RidesFragment extends Fragment {

    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "RideRequestsFragment";
    private FirebaseDatabase database;
    private List<RideRequestModel> rideRequestModelList;


    public RidesFragment() {
        // Required empty public constructor
    }

    public static RidesFragment newInstance(int position) {
        RidesFragment fragment = new RidesFragment();
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
        View rootView =  inflater.inflate(R.layout.fragment_rides, container, false);

        RecyclerView recycler = rootView.findViewById((R.id.recyclerView2));

        return rootView;
    }
}
//
//
//
//Defines recyclerview
class RidesRecyclerViewAdapter extends RecyclerView.Adapter<RidesRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<RideRequestModel> rideRequestModels;

    public RidesRecyclerViewAdapter(Context context, ArrayList<RideOfferModel> rideOfferModels) {
        this.context = context;
        this.rideRequestModels = rideRequestModels;
    }

    @NonNull
    @Override
    public RidesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        return new RidesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RidesRecyclerViewAdapter.MyViewHolder holder,
                                 int position) {
        //assign values to each row as they reenter screen

        //holder.name.setText();
        //holder.time.setText();
        //holder.location.setText();


        holder.button.setText("Select");

    }

    @Override
    public int getItemCount() {
        //number of recylerviews to have
        //Change this number to length of array list
        return 3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, time, location;
        private Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView2);
            time = itemView.findViewById(R.id.textView4);
            location = itemView.findViewById(R.id.textView5);
            button = itemView.findViewById(R.id.button3);


        }
    }
}