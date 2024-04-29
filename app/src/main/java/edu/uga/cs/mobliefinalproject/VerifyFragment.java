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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_verify, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_1);

        RecyclerViewAdapterVerify adapter = new RecyclerViewAdapterVerify(getActivity(), verifyModelArrayList );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //database stuff
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("verify");

        //gets non-accepted offers
        myRef.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                verifyModelArrayList.clear(); // clear the current content; this is inefficient!
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    VerifyModel verifyModel = postSnapshot.getValue(VerifyModel.class);
                    verifyModel.setKey( postSnapshot.getKey() );

                    //check if accepted
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

        return rootView;
    }
}

class RecyclerViewAdapterVerify extends RecyclerView.Adapter<RecyclerViewAdapterVerify.MyViewHolderVerify> {
    Context context;
    ArrayList<VerifyModel> verifyModelArrayList;
    private final static String DEBUG = "Recycler View Adapter Verify";

    public RecyclerViewAdapterVerify(Context context, ArrayList<VerifyModel> verifyModels) {
        this.context = context;
        this.verifyModelArrayList = verifyModels;
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
            verifyButton = itemView.findViewById(R.id.button3);

            Log.d(DEBUG, "My view holder");
        }
    }
}