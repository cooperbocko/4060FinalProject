package edu.uga.cs.mobliefinalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class RidesAcceptedFragment extends Fragment {

    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "Accepted Rides";

    public static final String DIALOG_TAG = "CustomFragDiolog";
    private ArrayList<RideOfferModel> rideOfferModelList;
    private FirebaseDatabase database;

    public RidesAcceptedFragment() {
        // Required empty public constructor
    }

    public static RidesAcceptedFragment newInstance(int position) {
        RidesAcceptedFragment fragment = new RidesAcceptedFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_rides_accepted, container, false);

        RecyclerView recycler = rootView.findViewById(R.id.rv_rideAccept);

        RARecyclerViewAdapter adapter = new RARecyclerViewAdapter(getActivity(), rideOfferModelList,
                this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rideoffers");

        //gets accepted offers if you are a driver/accepter
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                rideOfferModelList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    RideOfferModel rideOfferModel = postSnapshot.getValue(RideOfferModel.class);
                    rideOfferModel.setKey( postSnapshot.getKey() );

                    //check if accepted and if driver/accepter
                    if (rideOfferModel.isAccepted() && (rideOfferModel.driver.equals(CurrentUser.email) || rideOfferModel.acceptedBy.equals(CurrentUser.email))) {
                        Log.d(DEBUG, "Ride Offer not added: " + rideOfferModel);
                        continue;
                    } else {
                        //add offer to list
                        rideOfferModelList.add( rideOfferModel );
                        Log.d(DEBUG, "Ride Offer added: " + rideOfferModel);

                    }
                }

                //implement this later
                Log.d(DEBUG, "ValueEventListener: notifying recyclerAdapter" );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Log.d(DEBUG, "Error reading offers from database: " + databaseError);
            }
        } );

        return rootView;
    }
}
/**
 *
 *
 *
 */
//Defines recyclerview
class RARecyclerViewAdapter extends RecyclerView.Adapter<RARecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterfave recyclerViewInterfave;
    Context context;
    static ArrayList<RideOfferModel> rideOfferModels;
    private final static String DEBUG = "Recycler View Adapter";

    public RARecyclerViewAdapter(Context context, ArrayList<RideOfferModel> rideOfferModels,
                                 RecyclerViewInterfave recyclerViewInterfave) {
        this.context = context;
        this.rideOfferModels = rideOfferModels;
        this.recyclerViewInterfave = recyclerViewInterfave;
        Log.d(DEBUG, "adapter created");
    }

    @NonNull
    @Override
    public RARecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        Log.d(DEBUG, "On Create View Holder");
        return new RARecyclerViewAdapter.MyViewHolder(view, recyclerViewInterfave);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assign values to each row as they reenter screen

        RideOfferModel rideOfferModel = rideOfferModels.get(position);
        holder.name.setText(rideOfferModel.driver);
        holder.time.setText(rideOfferModel.date);
        holder.location.setText("To: " + rideOfferModel.to + " From: " + rideOfferModel.from);


        //holder.name.setText();
        //holder.name.setText();
        //holder.name.setText();

        holder.join.setVisibility(View.GONE);


        Log.d(DEBUG, "recycler view item added: " + rideOfferModel);
    }

    @Override
    public int getItemCount() {
        //number of recylerviews to have
        return rideOfferModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, time, location;
        private Button join;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterfave recyclerViewInterfave) {
            super(itemView);

            name = itemView.findViewById(R.id.textView2);
            time = itemView.findViewById(R.id.textView4);
            location = itemView.findViewById(R.id.textView5);
            join = itemView.findViewById(R.id.button8);

            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("handlejoiningride", "Successfully joined ride");
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterfave != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterfave.onItemClick(position);
                        }
                    }
                }
            });


            Log.d(DEBUG, "My View Holder");


        }

    }
}