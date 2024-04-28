package edu.uga.cs.mobliefinalproject;

import static android.app.PendingIntent.getActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private static final String DIALOG_TAG = "CustomFragDiolog";
    private ArrayList<RideOfferModel> rideOfferModelList;
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_offer, container, false);

        RecyclerView recycler = rootView.findViewById(R.id.recyclerView);

        //Set up model
        //setUpRideOfferModels(); still needs to be created


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), rideOfferModelList );
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));



        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rideoffers");

        //gets non-accepted offers
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                rideOfferModelList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    RideOfferModel rideOfferModel = postSnapshot.getValue(RideOfferModel.class);
                    rideOfferModel.setKey( postSnapshot.getKey() );

                    //check if accepted
                    if (rideOfferModel.isAccepted()) {
                        Log.d(DEBUG, "Offer not added: " + rideOfferModel);
                        continue;
                    } else {
                        //add offer to list
                        rideOfferModelList.add( rideOfferModel );
                        Log.d(DEBUG, "Offer added: " + rideOfferModel);

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




        // Floating Action Buttons
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

        //Offer Button
        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecyclerViewAdapter.OfferDialogFragment().show(getChildFragmentManager(),
                        DIALOG_TAG);
            }
        });


        return rootView;
    }

}

//
//
//Defines recyclerview
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<RideOfferModel> rideOfferModels;
    private final static String DEBUG = "Recycler View Adapter";

    public RecyclerViewAdapter(Context context, ArrayList<RideOfferModel> rideOfferModels){
        this.context = context;
        this.rideOfferModels = rideOfferModels;
        Log.d(DEBUG, "adapter created");
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        Log.d(DEBUG, "On Create View Holder");
        return new RecyclerViewAdapter.MyViewHolder(view);
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

        holder.delete.setText("Delete");

        Log.d(DEBUG, "recycler view item added: " + rideOfferModel);

    }

    @Override
    public int getItemCount() {
        //number of recylerviews to have
        return rideOfferModels.size();
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

            Log.d(DEBUG, "My View Holder");


        }
    }

    //
    //

    //Offer Dialog Handler
    public static class OfferDialogFragment extends DialogFragment {
        public static OfferDialogFragment newInstance() {
            return new OfferDialogFragment();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.offer_dialog,
                    (ViewGroup) getActivity().findViewById(R.id.linearLayout));
            final EditText from = (EditText) layout.findViewById(R.id.editText);
            final EditText to = (EditText) layout.findViewById(R.id.editText2);
            final EditText date = (EditText) layout.findViewById(R.id.editTextDate);
            final EditText time = (EditText) layout.findViewById(R.id.editTextTime);
            final EditText seats = (EditText) layout.findViewById(R.id.editTextNumber);


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(layout);
            // Now configure the AlertDialog
            builder.setTitle("Offer Ride");
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    // We forcefully dismiss and remove the Dialog, so it
                    // cannot be used again
                    dialog.dismiss();
                }
            });

            //
            //Add To DB in this meathod
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strFrom = from.getText().toString();
                    String strTo = to.getText().toString();
                    String strDate = date.getText().toString() + " || " + time.getText().toString();
                    String strSeats = seats.getText().toString();

                    //post to db
                    RideOfferModel rideOfferModel = new RideOfferModel(CurrentUser.email, strSeats, strFrom, strTo, strDate, false, "none");
                    createNewOffer(rideOfferModel);



                    Toast.makeText(getActivity(), "From: " + strFrom + " To: " + strTo + " Date: "
                                    + strDate + " Seats: " + strSeats,
                            Toast.LENGTH_SHORT).show();

                    // We forcefully dismiss and remove the Dialog, so it
                    // cannot be used again
                    dialog.dismiss();
                }
            });
            // Create the AlertDialog and show it
            return builder.create();
        }


        private void createNewOffer(RideOfferModel rideOfferModel) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("rideoffers");

            myRef.push().setValue( rideOfferModel )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            //Toast.makeText(getActivity(), "Ride offer created: " + rideOfferModel,
                            //        Toast.LENGTH_SHORT).show();
                            Log.d(DEBUG, "New ride offer created: " + rideOfferModel);

                            // Clear the EditTexts for next use.

                        }
                    })
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure( @NonNull Exception e ) {
                            Toast.makeText( getActivity(), "Failed to create a ride offer: " + rideOfferModel,
                                    Toast.LENGTH_SHORT).show();
                            Log.d(DEBUG, "Failed to create ride offer: " + rideOfferModel);
                        }
                    });
        }
    }

    /*
    //Show Dialog
    void showDialogFragment(DialogFragment newFragment) {
        Log.d( "DialogFrag",
                "SimpleFragDialogActivity.showDialogFragment(): newFragment" + newFragment );
        newFragment(getSupportFragmentManager(), null);
    }

     */

}


