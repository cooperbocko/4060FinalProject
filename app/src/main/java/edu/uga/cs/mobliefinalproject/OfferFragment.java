package edu.uga.cs.mobliefinalproject;

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
import android.content.Context;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class OfferFragment extends Fragment {
    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "OfferFragment";
    private List<RideOfferModel> rideOfferModelList;
    private FirebaseDatabase database;

    FloatingActionButton options ,drive, request;

    TextView offerRide, requestRide;

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
        View rootView =  inflater.inflate(R.layout.fragment_offer, container, false);

        RecyclerView recycler = rootView.findViewById(R.id.recyclerView);

        //Set up model
        //setUpRideOfferModels(); still needs to be created


        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, rideOfferModelList );
        //recycler.setAdapter(adapter);
        //recycler.SetLayoutManager(new LinearLayoutManager(this);


        //Buttons
     options = rootView.findViewById(R.id.floatingActionButton);
       drive = rootView.findViewById(R.id.floatingActionButton3);
       request = rootView.findViewById(R.id.floatingActionButton2);
      offerRide = rootView.findViewById(R.id.textView8);
      requestRide = rootView.findViewById(R.id.textView7);

        drive.setVisibility(rootView.GONE);
        request.setVisibility(rootView.GONE);
        requestRide.setVisibility(rootView.GONE);
        offerRide.setVisibility(rootView.GONE);


        AtomicReference<Boolean> visible = new AtomicReference<>(false);

        options.setOnClickListener(view -> {
            if(!visible.get()){
                drive.show();
                request.show();
                requestRide.setVisibility(rootView.VISIBLE);
                offerRide.setVisibility(rootView.VISIBLE);

                visible.set(true);
            }else{
                drive.hide();
                request.hide();
                requestRide.setVisibility(rootView.GONE);
                offerRide.setVisibility(rootView.GONE);

                visible.set(false);

            }

        });


        return rootView;
    }
}

////
////

//Defines recyclerview
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<RideOfferModel> rideOfferModels;

    public RecyclerViewAdapter(Context context, ArrayList<RideOfferModel> rideOfferModels){
        this.context = context;
        this.rideOfferModels = rideOfferModels;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assign values to each row as they reenter screen

        //holder.name.setText();
        //holder.name.setText();
        //holder.name.setText();

        holder.delete.setText("Delete");

    }

    @Override
    public int getItemCount() {
        //number of recylerviews to have
        return 3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name, time, location;
        private Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView2);
            time = itemView.findViewById(R.id.textView4);
            location = itemView.findViewById(R.id.textView5);
            delete = itemView.findViewById(R.id.button3);


        }
    }
}