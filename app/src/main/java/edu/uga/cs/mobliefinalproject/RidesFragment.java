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

public class RidesFragment extends Fragment implements RecyclerViewInterfave{

    private static final String FRAGMENT_POSITION = "position";
    private static final String DEBUG = "RideRequestsFragment";
    public static final String DIALOG_TAG = "CustomFragDiolog";
    private FirebaseDatabase database;
    private ArrayList<RideRequestModel> rideRequestModelList;


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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rides, container, false);

        RecyclerView recycler = rootView.findViewById((R.id.recyclerView2));

        FloatingActionButton addRequest = rootView.findViewById(R.id.floatingActionButton2);


        RidesRecyclerViewAdapter adapter = new RidesRecyclerViewAdapter(getActivity(), rideRequestModelList,
                (RecyclerViewInterfave) this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));


        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("riderequests");

        //gets non-accepted ride requests
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                rideRequestModelList.clear(); // clear the current content; this is inefficient!
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RideRequestModel rideRequestModel = postSnapshot.getValue(RideRequestModel.class);
                    rideRequestModel.setKey(postSnapshot.getKey());

                    //check if accepted or your own offer
                    if (rideRequestModel.isAccepted()) {
                        Log.d(DEBUG, "Request not added: " + rideRequestModel);
                        continue;
                    } else {
                        //add request to list
                        rideRequestModelList.add(rideRequestModel);
                        Log.d(DEBUG, "Request added: " + rideRequestModel);

                    }
                }

                //implement this later
                Log.d(DEBUG, "ValueEventListener: notifying recyclerAdapter");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(DEBUG, "Error reading requests from database: " + databaseError);
            }
        });

        //request ride listener
        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RidesRecyclerViewAdapter.RequestDialogFragment().show(getChildFragmentManager(), DIALOG_TAG);

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
    static ArrayList<RideRequestModel> rideRequestModels;
    private final static String DEBUG = "Rides Recycler View Adapter";

    public RidesRecyclerViewAdapter(Context context,
                                    ArrayList<RideRequestModel> rideRequestModels,
                                    RecyclerViewInterfave recyclerViewInterfave) {
        this.context = context;
        this.rideRequestModels = rideRequestModels;
        this.recyclerViewInterfave = recyclerViewInterfave;
        Log.d(DEBUG, "adapter created");
    }

    @NonNull
    @Override
    public RidesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType) {
        //inflate layout and give look to our rows

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        Log.d(DEBUG, "On Create View Holder");
        return new RidesRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterfave);
    }

    @Override
    public void onBindViewHolder(@NonNull RidesRecyclerViewAdapter.MyViewHolder holder,
                                 int position) {
        //assign values to each row as they reenter screen
        RideRequestModel rideRequestModel = rideRequestModels.get(position);
        holder.name.setText(rideRequestModel.rider);
        holder.time.setText(rideRequestModel.date);
        holder.location.setText("To: " + rideRequestModel.to + " From: " + rideRequestModel.from);
        holder.button.setText("Drive!");

        Log.d(DEBUG, "recycler view item added: " + rideRequestModel);
    }

    @Override
    public int getItemCount() {
        //number of recylerviews to have
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
            button = itemView.findViewById(R.id.button8);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update request
                    int position = getAdapterPosition();
                    RideRequestModel rideRequestModel = rideRequestModels.get(position);
                    rideRequestModel.setAccepted(true);
                    rideRequestModel.setAcceptedBy(CurrentUser.email);
                    Log.d(DEBUG, "Request accepted by: " + rideRequestModel.getAcceptedBy());
                    EditRequestDialogFragment.updateRequest(position, rideRequestModel, 0);

                    //create verify
                    VerifyModel verifyModel = new VerifyModel(CurrentUser.email, rideRequestModel.getRider(), false, false);
                    verifyModel.setRefKey(rideRequestModel.getKey());
                    verifyModel.setType("riderequests");
                    createVerify(verifyModel);

                    Log.d(DEBUG, "Successfully joined request");
                }

                private void createVerify(VerifyModel verifyModel) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("verify");

                    myRef.push().setValue(verifyModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Show a quick confirmation
                                    //Toast.makeText(getActivity(), "Ride offer created: " + rideOfferModel,
                                    //        Toast.LENGTH_SHORT).show();
                                    Log.d(DEBUG, "New verify created: " + verifyModel);

                                    // Clear the EditTexts for next use.

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText( getActivity(), "Failed to create a ride offer: " + rideOfferModel,
                                    //        Toast.LENGTH_SHORT).show();
                                    Log.d(DEBUG, "Failed to create verify: " + verifyModel);
                                }
                            });
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


        }
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

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strFrom = from.getText().toString();
                        String strTo = to.getText().toString();
                        String strDate = date.getText().toString() + " || " + time.getText().toString();


                        //post to db
                        RideRequestModel rideRequestModel = new RideRequestModel(CurrentUser.email, "1", strFrom, strTo, strDate, false, "none");
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

            private void createNewRequest(RideRequestModel rideRequestModel) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("riderequests");

                myRef.push().setValue(rideRequestModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Show a quick confirmation
                                //Toast.makeText(getActivity(), "Ride offer created: " + rideOfferModel,
                                //        Toast.LENGTH_SHORT).show();
                                Log.d(DEBUG, "New ride request created: " + rideRequestModel);

                                // Clear the EditTexts for next use.

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText( getActivity(), "Failed to create a ride offer: " + rideOfferModel,
                                //        Toast.LENGTH_SHORT).show();
                                Log.d(DEBUG, "Failed to create ride request: " + rideRequestModel);
                            }
                        });
            }
        }


            /**
             *
             */
            //edit request dialog
            //Offer Dialog Handler
            public static class EditRequestDialogFragment extends DialogFragment {
                int position;

                public EditRequestDialogFragment newInstance(int position) {
                    this.position = position;
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
                    builder.setTitle("Edit Request");
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();
                        }
                    });

                    builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateRequest(position, rideRequestModels.get(position), 1);
                            /** functions to delete request view*/
                            dialog.dismiss();
                        }
                    });


                    builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RideRequestModel editRequest = new RideRequestModel(CurrentUser.email, "1", from.getText().toString(), to.getText().toString(), date.getText().toString() + " || " + time.getText().toString(), false, "none");
                            editRequest.setKey(rideRequestModels.get(position).getKey());

                            updateRequest(position, editRequest, 0);


                            //Toast.makeText(getActivity(), "From: " + strFrom + " To: " + strTo + " Date: "
                            //                + strDate + " Seats: " + strSeats,
                            //        Toast.LENGTH_SHORT).show();

                            // We forcefully dismiss and remove the Dialog, so it
                            // cannot be used again
                            dialog.dismiss();
                        }
                    });
                    // Create the AlertDialog and show it
                    return builder.create();
                }

                public static void updateRequest(int position, RideRequestModel rideRequestModel, int action) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //update
                    if (action == 0) {
                        Log.d(DEBUG, "Updating request at: " + position + "(" + rideRequestModel + ")");

                        // Update the recycler view to show the changes in the updated job lead in that view
                        //recycler.notifyItemChanged( position );

                        // Update this job lead in Firebase
                        // Note that we are using a specific key (one child in the list)
                        DatabaseReference ref = database
                                .getReference()
                                .child("riderequests")
                                .child(rideRequestModel.getKey());

                        // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
                        // to maintain job leads.
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().setValue(rideRequestModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(DEBUG, "updated request at: " + position + "(" + rideRequestModel + ")");
                                        //Toast.makeText(getActivity(), "Offer updated for " + rideOfferModel,
                                        //        Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(DEBUG, "failed to update request at: " + position + "(" + rideRequestModel + ")");
                                //Toast.makeText(getActivity(), "Failed to update " + rideOfferModel,
                                //        Toast.LENGTH_SHORT).show();
                            }
                        });
                        rideRequestModels.remove(position);
                    } else if (action == 1) {
                        Log.d(DEBUG, "Deleting request at: " + position + "(" + rideRequestModel + ")");

                        // remove the deleted job lead from the list (internal list in the App)
                        rideRequestModels.remove(position);


                        // Update the recycler view to remove the deleted job lead from that view
                        //recyclerAdapter.notifyItemRemoved( position );

                        // Delete this job lead in Firebase.
                        // Note that we are using a specific key (one child in the list)
                        DatabaseReference ref = database
                                .getReference()
                                .child("riderequests")
                                .child(rideRequestModel.getKey());

                        // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
                        // to maintain job leads.
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(DEBUG, "deleted request at: " + position + "(" + rideRequestModel + ")");
                                        //Toast.makeText(getActivity(), "Job lead deleted for " + jobLead.getCompanyName(),
                                        //        Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(DEBUG, "failed to delete request at: " + position + "(" + rideRequestModel + ")");
                                //Toast.makeText(getApplicationContext(), "Failed to delete " + jobLead.getCompanyName(),
                                //        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }





