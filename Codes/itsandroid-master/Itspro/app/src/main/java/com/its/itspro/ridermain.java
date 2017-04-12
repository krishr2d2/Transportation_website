package com.its.itspro;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ridermain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private String original_name,nick_name,mail_name,photo_url;
    private Uri photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridermain);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);

            original_name = sharedprefs.getDefaults("name",getApplicationContext());

            nick_name = sharedprefs.getDefaults("nname",getApplicationContext());

            mail_name = sharedprefs.getDefaults("email",getApplicationContext());

            photo_url = sharedprefs.getDefaults("photo",getApplicationContext());

        Log.i("photo urlll", photo_url);
        photo = Uri.parse(photo_url);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new usermap()).commit();
            setTitle("Booking");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if (toggle.onOptionsItemSelected(menuItem)){
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        TextView t1,t2;
        Log.i("here","Entered Selected Navigation Syatem");
        int id = item.getItemId();
        Fragment fragment = null;
        ImageView view;
        switch (id){
            case(R.id.booking):
                fragment = new usermap();
                t1 = (TextView) findViewById(R.id.nameuser);
                t2 = (TextView) findViewById(R.id.mailuser);
                view = (ImageView) findViewById(R.id.profilephoto);
                t1.setText(nick_name);
                t2.setText(mail_name);
                Picasso.with(getApplicationContext()).load(photo).into(view);
                Log.i("here","Booking Fragment Initialized");
                break;
            case (R.id.track):
                fragment = new track();

                t1 = (TextView) findViewById(R.id.nameuser);
                t2 = (TextView) findViewById(R.id.mailuser);
                t1.setText(nick_name);
                t2.setText(mail_name);
                view = (ImageView) findViewById(R.id.profilephoto);
                Picasso.with(getApplicationContext()).load(photo).into(view);
                Log.i("here","Track yourself Fragment Initialized");
                break;
            case R.id.history:
                fragment = new history();

                t1 = (TextView) findViewById(R.id.nameuser);
                t2 = (TextView) findViewById(R.id.mailuser);
                t1.setText(nick_name);
                t2.setText(mail_name);
                view = (ImageView) findViewById(R.id.profilephoto);
                Picasso.with(getApplicationContext()).load(photo).into(view);
                Log.i("here","History Fragment Initialized");
                break;
            case R.id.feedback:
                fragment = new feedback();

                t1 = (TextView) findViewById(R.id.nameuser);
                t2 = (TextView) findViewById(R.id.mailuser);
                t1.setText(nick_name);
                t2.setText(mail_name);
                view = (ImageView) findViewById(R.id.profilephoto);
                Picasso.with(getApplicationContext()).load(photo).into(view);
                Log.i("here","Feedback Fragment Initialized");
                break;
            case R.id.about:
                fragment = new about();

                t1 = (TextView) findViewById(R.id.nameuser);
                t2 = (TextView) findViewById(R.id.mailuser);
                t1.setText(nick_name);
                t2.setText(mail_name);
                view = (ImageView) findViewById(R.id.profilephoto);
                Picasso.with(getApplicationContext()).load(photo).into(view);
                Log.i("here","About Fragment Initialized");
                break;
        }
        if(fragment!= null){

            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            Log.i("container","initialized");
            transaction.replace(R.id.container,fragment);
            transaction.commit();

        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
