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

public class RequestAcceptedFragment extends Fragment {

    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "Request Accept Fragment";
    public static final String DIALOG_TAG = "CustomFragDiolog";
    private FirebaseDatabase database;
    private ArrayList<RideRequestModel> rideRequestModelList;


    public RequestAcceptedFragment() {
        // Required empty public constructor
    }

    public static RequestAcceptedFragment newInstance(int position) {
        RequestAcceptedFragment fragment = new RequestAcceptedFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_request_accepted, container, false);

        RecyclerView recycler = rootView.findViewById(R.id.rv_requestAccept);

        RecyclerViewAdapterRequestAccept adapter = new RecyclerViewAdapterRequestAccept(getActivity(), rideRequestModelList, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("riderequests");

        //gets accepted requests if user is rider/accepter
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                rideRequestModelList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    RideRequestModel rideRequestModel = postSnapshot.getValue(RideRequestModel.class);
                    rideRequestModel.setKey( postSnapshot.getKey() );

                    //check if accepted and if user is rider/accepter
                    if (rideRequestModel.isAccepted() && (rideRequestModel.getRider().equals(CurrentUser.email) || rideRequestModel.getAcceptedBy().equals(CurrentUser.email))) {
                        Log.d(DEBUG, "Request not added: " + rideRequestModel);
                        continue;
                    } else {
                        //add request to list
                        rideRequestModelList.add( rideRequestModel );
                        Log.d(DEBUG, "Request added: " + rideRequestModel);

                    }
                }

                //implement this later
                Log.d( DEBUG, "ValueEventListener: notifying recyclerAdapter" );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Log.d(DEBUG, "Error reading requests from database: " + databaseError);
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
class RecyclerViewAdapterRequestAccept extends RecyclerView.Adapter<RecyclerViewAdapterRequestAccept.MyViewHolder> {

    private final RecyclerViewInterfave recyclerViewInterfave;
    Context context;
    static ArrayList<RideOfferModel> rideOfferModels;
    private final static String DEBUG = "Recycler View Adapter";

    public RecyclerViewAdapterRequestAccept(Context context, ArrayList<RideOfferModel> rideOfferModels,
                               RecyclerViewInterfave recyclerViewInterfave) {
        this.context = context;
        this.rideOfferModels = rideOfferModels;
        this.recyclerViewInterfave = recyclerViewInterfave;
        Log.d(DEBUG, "adapter created");
    }

    @NonNull
    @Override
    public RecyclerViewAdapterRequestAccept.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        Log.d(DEBUG, "On Create View Holder");
        return new RecyclerViewAdapterRequestAccept.MyViewHolder(view, recyclerViewInterfave);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterRequestAccept.MyViewHolder holder, int position) {
        //assign values to each row as they reenter screen

        RideOfferModel rideOfferModel = rideOfferModels.get(position);
        holder.name.setText(rideOfferModel.driver);
        holder.time.setText(rideOfferModel.date);
        holder.location.setText("To: " + rideOfferModel.to + " From: " + rideOfferModel.from);

        holder.join.setVisibility(View.GONE);


        Log.d(DEBUG, "recycler view item added: " + rideOfferModel);

    }

    @Override
    public int getItemCount() {
        //number of recylerviews to have
        return rideOfferModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, time, location;
        public Button join;

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