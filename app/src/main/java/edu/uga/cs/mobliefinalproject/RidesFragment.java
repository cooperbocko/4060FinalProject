package edu.uga.cs.mobliefinalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    public static final String DIALOG_TAG = "CustomFragDiolog";
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

        FloatingActionButton addRequest = rootView.findViewById(R.id.floatingActionButton2);

        /*
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), rideRequestModelList,
                this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

         */

        //request ride listener
        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RidesRecyclerViewAdapter.MyViewHolder.RequestDialogFragment().show(getChildFragmentManager(), DIALOG_TAG);

            }
        });

        return rootView;
    }


    @Override
    public void onItemClick(int position) {

        new RidesRecyclerViewAdapter.EditRequestDialogFragment().show(getChildFragmentManager(),
                DIALOG_TAG);

    }



}
//
//
//
//Defines recyclerview
class RidesRecyclerViewAdapter extends RecyclerView.Adapter<RidesRecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterfave recyclerViewInterfave;
    Context context;
    ArrayList<RideRequestModel> rideRequestModels;

    public RidesRecyclerViewAdapter(Context context,
                                    ArrayList<RideRequestModel> rideRequestModels,
                                    RecyclerViewInterfave recyclerViewInterfave) {
        this.context = context;
        this.rideRequestModels = rideRequestModels;
        this.recyclerViewInterfave = recyclerViewInterfave;
    }

    @NonNull
    @Override
    public RidesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        return new RidesRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterfave);
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
        return rideRequestModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView name, time, location;
        private Button button;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterfave recyclerViewInterfave) {
            super(itemView);

            name = itemView.findViewById(R.id.textView2);
            time = itemView.findViewById(R.id.textView4);
            location = itemView.findViewById(R.id.textView5);
            //  button = itemView.findViewById(R.id.button3);

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

        }

        /**
         *
         */

        //request dialog handler
        public static class RequestDialogFragment extends DialogFragment {
            public static RequestDialogFragment newInstance() {
                return new RequestDialogFragment();
            }

            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.request_dialog,
                        (ViewGroup) getActivity().findViewById(R.id.linearLayout));
                final EditText from = (EditText) layout.findViewById(R.id.editTextText);
                final EditText to = (EditText) layout.findViewById(R.id.editTextText2);
                final EditText date = (EditText) layout.findViewById(R.id.editTextDate2);
                final EditText time = (EditText) layout.findViewById(R.id.editTextTime2);


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(layout);
                // Now configure the AlertDialog
                builder.setTitle("Request Ride");
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // We forcefully dismiss and remove the Dialog, so it
                        // cannot be used again
                        dialog.dismiss();
                    }
                });

                //
                //Add To DB in this method
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strFrom = from.getText().toString();
                        String strTo = to.getText().toString();
                        String strDate = date.getText().toString();
                        String strTime = time.getText().toString();


                        Toast.makeText(getActivity(), "From: " + strFrom + " To: " + strTo + " Date: "
                                        + strDate + " Time: " + strTime,
                                Toast.LENGTH_SHORT).show();

                        // We forcefully dismiss and remove the Dialog, so it
                        // cannot be used again
                        dialog.dismiss();
                    }
                });
                // Create the AlertDialog and show it
                return builder.create();
            }
        }

        /**
         *
         */
        //edit request dialog
        //Offer Dialog Handler
        public static class EditRequestDialogFragment extends DialogFragment {
            public static EditRequestDialogFragment newInstance() {
                return new EditRequestDialogFragment();
            }

            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.request_dialog,
                        (ViewGroup) getActivity().findViewById(R.id.linearLayout));
                final EditText from = (EditText) layout.findViewById(R.id.editTextText);
                final EditText to = (EditText) layout.findViewById(R.id.editTextText2);
                final EditText date = (EditText) layout.findViewById(R.id.editTextDate2);
                final EditText time = (EditText) layout.findViewById(R.id.editTextTime2);


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(layout);
                // Now configure the AlertDialog
                builder.setTitle("Edit Request Ride");
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // We forcefully dismiss and remove the Dialog, so it
                        // cannot be used again
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /** functions to delete request view*/
                        dialog.dismiss();
                    }
                });

                //
                //Add To DB in this meathod
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strFrom = from.getText().toString();
                        String strTo = to.getText().toString();
                        String strDate = date.getText().toString() + " || " + time.getText().toString();


                        //post to db
                        RideRequestModel rideRequestModel = new RideRequestModel()
                        (CurrentUser.email, strFrom, strTo, strDate, false, "none");
                        createNewRequest(rideRequestModel);


                        Toast.makeText(getActivity(), "From: " + strFrom + " To: " + strTo + " Date: "
                                        + strDate,
                                Toast.LENGTH_SHORT).show();

                        // We forcefully dismiss and remove the Dialog, so it
                        // cannot be used again
                        dialog.dismiss();
                    }
                });
                // Create the AlertDialog and show it
                return builder.create();
            }

        }
    }
}