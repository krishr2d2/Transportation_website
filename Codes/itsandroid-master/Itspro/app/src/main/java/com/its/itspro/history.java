package com.its.itspro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by sai on 23/3/17.
 */

public class history extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Tuple> myDataset;

    private FirebaseDatabase daa;
    private DatabaseReference ref,ref1,ref2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataset = new ArrayList<>();
        daa = FirebaseDatabase.getInstance();
        ref = daa.getReference();
        ref1 = ref.child("My_user").child("Rider").child("Bookings");

        initDataset();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Past Rides");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history,container,false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private void initDataset() {
        Tuple pair;
        ArrayList a = storeBookings.retrieveDestinations();
        ArrayList b = storeBookings.retrieveOrigins();
        Log.i("orifing",b.toString());
        Log.i("desfing",a.toString());
        ArrayList<String> origin = new ArrayList<String>();
        ArrayList<String> dest = new ArrayList<String>();
        int bookingsize = storeBookings.retrieveDestinations().size();

        for(Object element: a){
            Place p = (Place) element;
            origin.add(p.getName().toString());
        }
        for(Object element: b){
            Place p = (Place) element;
            dest.add(p.getName().toString());
        }
        for (int i=0;i<bookingsize;i++){
            pair = new Tuple();
            Log.i("entered ","loop");
            pair.start = origin.get(i);
            pair.end = dest.get(i);
            myDataset.add(pair);
            Log.i("pair print ","a: " + pair.start.toString() + "b: " + pair.end.toString());
            Log.i("pair print ",pair.toString());
        }
        Log.i("pairsss : ",myDataset.toString());
    }

}
