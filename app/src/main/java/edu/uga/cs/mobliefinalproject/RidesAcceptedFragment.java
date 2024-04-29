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

import java.util.ArrayList;

public class RidesAcceptedFragment extends Fragment {

    public RidesAcceptedFragment() {
        // Required empty public constructor
    }

    public static RidesAcceptedFragment newInstance(int position) {
        RidesAcceptedFragment fragment = new RidesAcceptedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_rides_accepted, container, false);
    }
}
/**
 *
 *
 *
 */
//Defines recyclerview
class RARecyclerViewAdapter extends RecyclerView.Adapter<RARecyclerViewAdapter.MyViewHolder>  {

    private final RecyclerViewInterfave recyclerViewInterfave;
    Context context;
    static ArrayList<RideOfferModel> rideOfferModels;
    private final static String DEBUG = "Recycler View Adapter";

    public RARecyclerViewAdapter(Context context, ArrayList<RideOfferModel> rideOfferModels,
                               RecyclerViewInterfave recyclerViewInterfave){
        this.context = context;
        this.rideOfferModels = rideOfferModels;
        this.recyclerViewInterfave = recyclerViewInterfave;
        Log.d(DEBUG, "adapter created");
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        Log.d(DEBUG, "On Create View Holder");
        return new RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterfave);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{

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
                    if(recyclerViewInterfave != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterfave.onItemClick(position);
                        }
                    }
                }
            });


            Log.d(DEBUG, "My View Holder");



        }

    }