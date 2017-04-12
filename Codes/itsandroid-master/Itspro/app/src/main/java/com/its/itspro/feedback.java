package com.its.itspro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sai on 23/3/17.
 */

public class feedback extends Fragment implements View.OnClickListener {

    private FirebaseDatabase daa;
    private DatabaseReference ref,ref1,ref2;
    private EditText topicc,detailss;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.feedback,container,false);
        topicc = (EditText) v.findViewById(R.id.topic);
        detailss = (EditText) v.findViewById(R.id.details);
        Button push = (Button) v.findViewById(R.id.push);
        push.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daa = FirebaseDatabase.getInstance();
        ref = daa.getReference();
        ref = ref.child("My_user").child("Feedback");

        getActivity().setTitle("Feedback");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.push:
                push_data();
        }
    }

    private void push_data() {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s1 = topicc.getText().toString();
                String s2 = detailss.getText().toString();
                Log.i("strings ",s1 +"  "+s2);
                if (s1.isEmpty() || s2.isEmpty()){
                    Toast.makeText(getContext(), "Fill in something!!", Toast.LENGTH_SHORT).show();
                }
                else if(!s1.isEmpty() && !s2.isEmpty()){
                    long x = dataSnapshot.getChildrenCount();

                    ref.child(String.valueOf(x + 1)).child("Issue").setValue(s1);
                    ref.child(String.valueOf(x + 1)).child("Description").setValue(s2);
                    ref.child(String.valueOf(x + 1)).child("email").setValue(sharedprefs.getDefaults("email",getContext()));
                    Toast.makeText(getContext(), "Sent", Toast.LENGTH_LONG).show();
                    topicc.setText("");
                    detailss.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
