package com.its.itspro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Userdriver extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient gpclient;
    private FirebaseAuth auth1;
    private FirebaseAuth.AuthStateListener authStateListener;
    public String name_of_user,email_of_user,nickname_of_user,photo_of_user;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdriver);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        /*if(pref.getBoolean("activity_executed", false)){
        Intent intent = new Intent(this, ridermain.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.apply();
        }*/

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gpclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
/*
    Fire base authentication..

*/
        auth1 = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("SIgned in ==", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("SIgned out ==", "onAuthStateChanged:signed_out");
                }
            }
        };

        TextView ridersignin = (TextView) findViewById(R.id.rider);
        TextView driversignin = (TextView) findViewById(R.id.driver);

        ridersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.rider:
                        signIn();
                        break;
                }
            }
        });

        driversignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.driver:
                        gotodriver();
                        break;
                }
            }
        });

    }

    private void gotodriver() {
        Intent go2main = new Intent(this, drivermain.class);
        startActivity(go2main);
    }

    @Override
    public void onStart(){
        super.onStart();
        auth1.addAuthStateListener(authStateListener);
        Log.i("Added on state listener","start");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth1.removeAuthStateListener(authStateListener);
            Log.i("stop","rmeoved onstarte listener");
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gpclient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
        Log.i("entered sign in page","now starts verifying");
    }

    @Override
    public void onActivityResult(int rcode, int recode, Intent data){
        super.onActivityResult(rcode, recode, data);
        if (rcode == RC_SIGN_IN) {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignIn(result);
        }

    }

    private void handleSignIn(GoogleSignInResult result) {

        Log.i("resulttt",String.valueOf(result.isSuccess()));
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            //Bundle b = new Bundle();
            //Log.i("account details",acct.getDisplayName());
            //Log.i("acc1",acct.getDisplayName());
            name_of_user = acct.getGivenName();
            nickname_of_user = acct.getDisplayName();
            email_of_user = acct.getEmail();
            photo_of_user = acct.getPhotoUrl().toString();

            sharedprefs.setDefaults("email",email_of_user,getApplicationContext());
            sharedprefs.setDefaults("name",name_of_user,getApplicationContext());
            sharedprefs.setDefaults("nname",nickname_of_user,getApplicationContext());
            sharedprefs.setDefaults("photo",photo_of_user,getApplicationContext());
            firebaseAuthWithGoogle(acct);
            // Log.i("acc1",acct.getPhotoUrl().getEncodedPath());
            Intent go2main = new Intent(this, ridermain.class);
            startActivity(go2main);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.i("Firebase starts", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        auth1.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.i("task", "signInWithCredential:onComplete:"+task.isSuccessful());
                    if(!task.isSuccessful()){
                        Log.i("task","signInWithCredential:", task.getException());
                        Toast.makeText(Userdriver.this,"Auth failed",Toast.LENGTH_LONG).show();
                    }
                }
            });


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
Log.i("failed",connectionResult.toString());
    }
}
