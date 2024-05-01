package edu.uga.cs.mobliefinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyFragment extends Fragment {
    private static final String DEBUG = "VerifyFragment";
    private static final String FRAGMENT_POSITION = "position";
    private static final String DIALOG_TAG = "CustomFragDiolog";
    private ArrayList<VerifyModel> verifyModelArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private FirebaseDatabase database;

    public VerifyFragment() {
        // Required empty public constructor
    }

    public static VerifyFragment newInstance(int position) {
        VerifyFragment fragment = new VerifyFragment();
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

        verifyModelArrayList = new ArrayList<>();
        userModelArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_verify, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_1);

        RecyclerViewAdapterVerify adapter = new RecyclerViewAdapterVerify(getActivity(), verifyModelArrayList, userModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("verify");
        DatabaseReference myref2 = database.getReference("users");


        //gets non-accepted offers
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                verifyModelArrayList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    VerifyModel verifyModel = postSnapshot.getValue(VerifyModel.class);
                    verifyModel.setKey( postSnapshot.getKey() );

                    if (verifyModel.rider.equals(CurrentUser.email) || verifyModel.driver.equals(CurrentUser.email)) {
                        Log.d(DEBUG, "Verify added: " + verifyModel);
                        verifyModelArrayList.add(verifyModel);
                    } else {
                        Log.d(DEBUG, "Verify not added: " + verifyModel);

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

        myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelArrayList.clear();
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    UserModel userModel = postSnapshot.getValue(UserModel.class);
                    userModel.setKey( postSnapshot.getKey() );

                    userModelArrayList.add(userModel);
                    Log.d(DEBUG, "user added: " + userModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return rootView;
    }
}

class RecyclerViewAdapterVerify extends RecyclerView.Adapter<RecyclerViewAdapterVerify.MyViewHolderVerify> {
    Context context;
    static ArrayList<VerifyModel> verifyModelArrayList;
    static ArrayList<UserModel> userModelArrayList;
    private final static String DEBUG = "Recycler View Adapter Verify";

    public RecyclerViewAdapterVerify(Context context, ArrayList<VerifyModel> verifyModels, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.verifyModelArrayList = verifyModels;
        this.userModelArrayList = userModelArrayList;
        Log.d(DEBUG, "Adapter created");
    }

    @NonNull
    @Override
    public RecyclerViewAdapterVerify.MyViewHolderVerify onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);

        Log.d(DEBUG, "On Create View Holder");
        return new RecyclerViewAdapterVerify.MyViewHolderVerify(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterVerify.MyViewHolderVerify holder,
                                 int position) {
        VerifyModel verifyModel = verifyModelArrayList.get(position);
        holder.title.setText("Driver: " + verifyModel.driver + " Rider: " + verifyModel.rider);
        holder.driveraccept.setText(verifyModel.driver + ": " + verifyModel.driverAccepted);
        holder.rideraccept.setText(verifyModel.rider + ": " + verifyModel.riderAccepted);
        holder.verifyButton.setText("verify");
    }


    @Override
    public int getItemCount() {

        return verifyModelArrayList.size();
    }

    public static class MyViewHolderVerify extends RecyclerView.ViewHolder {
        private TextView title, driveraccept, rideraccept;
        private Button verifyButton;

        public MyViewHolderVerify(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView2);
            driveraccept = itemView.findViewById(R.id.textView4);
            rideraccept = itemView.findViewById(R.id.textView5);
            verifyButton = itemView.findViewById(R.id.button8);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    VerifyModel verifyModel = verifyModelArrayList.get(position);

                    //if only one accept: update
                    //if both accept: update user points and delete verify and offer/request
                    if (!verifyModel.riderAccepted && !verifyModel.driverAccepted) {
                        if (CurrentUser.email.equals(verifyModel.rider)) {
                            verifyModel.riderAccepted = true;
                        } else if (CurrentUser.email.equals(verifyModel.driver)) {
                            verifyModel.driverAccepted = true;
                        }
                        //update
                        updateVerify(position, verifyModel, 0);
                    } else {
                        if (CurrentUser.email.equals(verifyModel.rider) && verifyModel.riderAccepted){
                            //no change!
                        } else if (CurrentUser.email.equals(verifyModel.driver) && verifyModel.driverAccepted) {
                            //no change!
                        } else {
                            //means that both will end up being verified
                            //update user points
                            UserModel driver = new UserModel();
                            UserModel rider = new UserModel();
                            for (int i = 0; i < userModelArrayList.size(); i++) {
                                Log.d(DEBUG, "getting user: " + userModelArrayList.get(i));
                                if (userModelArrayList.get(i).getEmail().equals(verifyModel.driver)){
                                    driver = userModelArrayList.get(i);
                                } else if (userModelArrayList.get(i).getEmail().equals(verifyModel.rider)){
                                    rider = userModelArrayList.get(i);
                                }
                            }
                            driver.setPoints(driver.getPoints() + 1);
                            rider.setPoints(rider.getPoints() - 1);
                            updateUser(driver);
                            updateUser(rider);

                            //delete verify
                            updateVerify(position, verifyModel, 1);

                            //delete offer/request
                            deleteRide(verifyModel.getRefKey(), verifyModel.getType());
                        }
                    }


                }
                //db calls
            });

            Log.d(DEBUG, "My view holder");


        }
    }

    public static void updateVerify( int position, VerifyModel verifyModel, int action ) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //update
        if( action == 0) {
            Log.d( DEBUG, "Updating verify at: " + position + "(" + verifyModel + ")" );

            // Update the recycler view to show the changes in the updated job lead in that view
            //recycler.notifyItemChanged( position );

            // Update this job lead in Firebase
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child( "verify" )
                    .child( verifyModel.getKey() );

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain job leads.
            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().setValue( verifyModel ).addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d( DEBUG, "updated verify at: " + position + "(" + verifyModel + ")" );
                            //Toast.makeText(getActivity(), "Offer updated for " + rideOfferModel,
                            //        Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    Log.d( DEBUG, "failed to update verify at: " + position + "(" + verifyModel + ")" );
                    //Toast.makeText(getActivity(), "Failed to update " + rideOfferModel,
                    //        Toast.LENGTH_SHORT).show();
                }
            });
            verifyModelArrayList.remove(position);
        }
        else if( action == 1 ) {
            Log.d( DEBUG, "Deleting verify at: " + position + "(" + verifyModel + ")" );

            // remove the deleted job lead from the list (internal list in the App)
            verifyModelArrayList.remove( position);


            // Update the recycler view to remove the deleted job lead from that view
            //recyclerAdapter.notifyItemRemoved( position );

            // Delete this job lead in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child( "verify" )
                    .child( verifyModel.getKey() );

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain job leads.
            ref.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d( DEBUG, "deleted verify at: " + position + "(" + verifyModel + ")" );
                            //Toast.makeText(getActivity(), "Job lead deleted for " + jobLead.getCompanyName(),
                            //        Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {
                    Log.d( DEBUG, "failed to delete verify at: " + position + "(" + verifyModel + ")" );
                    //Toast.makeText(getApplicationContext(), "Failed to delete " + jobLead.getCompanyName(),
                    //        Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void updateUser(UserModel userModel) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d( DEBUG, "Updating user: " + userModel.getEmail());

        DatabaseReference ref = database
                .getReference()
                .child( "users" )
                .child( userModel.getKey() );

        ref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                dataSnapshot.getRef().setValue( userModel ).addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d( DEBUG, "updated user: " + userModel.getEmail());
                        //Toast.makeText(getActivity(), "Offer updated for " + rideOfferModel,
                        //        Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Log.d( DEBUG, "failed to update user: " + userModel.getEmail());
                //Toast.makeText(getActivity(), "Failed to update " + rideOfferModel,
                //        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deleteRide(String key, String type){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d( DEBUG, "Deleting " + type + ": " + key);

        DatabaseReference ref = database
                .getReference()
                .child( type )
                .child(key);

        ref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                dataSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d( DEBUG, "deleted " + type + ": " + key);
                        //Toast.makeText(getActivity(), "Job lead deleted for " + jobLead.getCompanyName(),
                        //        Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {
                Log.d( DEBUG, "failed to delete " + type + ": " + key);
                //Toast.makeText(getApplicationContext(), "Failed to delete " + jobLead.getCompanyName(),
                //        Toast.LENGTH_SHORT).show();
            }
        });
    }
}