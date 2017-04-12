package com.its.itspro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class drivernext extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivernext);
    }

    public void sendloc(View view) {
        int buttonid = view.getId();
        switch(buttonid){
            case R.id.sendlocation:
                Toast.makeText(this,"Location Sent",Toast.LENGTH_LONG).show();
        }
    }
}
