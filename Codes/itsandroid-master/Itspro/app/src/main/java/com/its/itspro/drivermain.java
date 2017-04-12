package com.its.itspro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class drivermain extends AppCompatActivity {

    FirebaseDatabase daa;
    DatabaseReference ref,ref1,ref2;
    ProgressDialog prog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daa = FirebaseDatabase.getInstance();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ref = daa.getReference();
        ref1 = ref.child("My_user").child("Driver");
        setContentView(R.layout.activity_drivermain);
    }

    public void getInfo(View view) {
        EditText u = (EditText) findViewById(R.id.drivermail);
        String usr,pas;
        usr = u.getText().toString();
        if (usr.isEmpty()  || usr.equals("")){
            u.setError("Invalid input");
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_LONG).show();
        }
        if(!usr.isEmpty()){
            prog1 = new ProgressDialog(this);
            prog1.setIndeterminate(true);
            prog1.setMessage("Logging in");
            prog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog1.show();
            Authenticate(usr);
        }

    }

    private void Authenticate(final String usr) {
        ref1.orderByChild("Driver_email").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArrayList<String> det = new ArrayList<String>();
                Iterable<DataSnapshot> childs = dataSnapshot.getChildren();
                for (DataSnapshot ds: childs){
                    Log.i("det values",ds.toString());
                    String ds_key= ds.getKey();
                    if(ds_key.equals("Driver_email")){
                        String mails = ds.getValue().toString();
                        det.add(mails);
                    }
                }
                if(det.get(0).equals(usr)){
                    prog1.hide();
                    Toast.makeText(getApplicationContext(),"match found",Toast.LENGTH_LONG).show();
                    Intent in = new Intent(getApplicationContext(), drivernext.class);
                    startActivity(in);
                }
                else if(!det.get(0).equals(usr)){
                    prog1.hide();
                    Toast.makeText(getApplicationContext(),"No match found\n Try Again!!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
