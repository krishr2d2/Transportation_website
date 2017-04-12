package com.its.itspro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.its.itspro.R.id.next;
import static com.its.itspro.R.id.to;

/**
 * Created by sai on 23/3/17.
 */

public class usermap extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DirectionFinderListener {

    public GoogleMap map;
    public SupportMapFragment supportMapFragment;
    private PlaceAutocompleteFragment autoCompleteFragment;
    private GoogleApiClient mGoogleApiClient;
    private int REQUEST_LOCATION = 1;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Marker marker, marker1;
    private Place fromplace,destplace;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView distance;
    private TextView eta;
    private Location lasloc;
    private LocationRequest lr;
    private int clickedViewId;

    private FirebaseDatabase daa;
    private DatabaseReference ref,ref1,ref2;

    private String b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.i("log i ", "client connected");
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        Log.i("log i ", "Client disconnected");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.usermap, container, false);
        Button from = (Button) v.findViewById(R.id.from);
        distance = (TextView) v.findViewById(R.id.fromaddress);
        eta = (TextView) v.findViewById(R.id.toaddress);
        Button to = (Button) v.findViewById(R.id.to);
        Button next = (Button) v.findViewById(R.id.next);
        next.setOnClickListener(this);
        from.setOnClickListener(this);
        to.setOnClickListener(this);

        daa = FirebaseDatabase.getInstance();
        ref = daa.getReference();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ref2=ref.child("My_user").child("Rider").child("Bookings");

        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.bookingmap);
        supportMapFragment.getMapAsync(this);
        lr = LocationRequest.create();
        lr.setInterval(5000);
        lr.setFastestInterval(1000);
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        getActivity().setTitle("Hire a cab");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.setMyLocationEnabled(true);
        LatLng sydney = new LatLng(0, 0);
       // map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(sydney,2);
        map.animateCamera(update);

    }

    /*MAIN CALL-BACK FOR THE BOOKING SYSTEM*/

    @Override
    public void onClick(View view) {
        clickedViewId = view.getId();
        switch(clickedViewId){
            case R.id.from:
                runSearchWidget(clickedViewId);
                break;
            case to:
                runSearchWidget(clickedViewId);
                break;
            case next:
                if(fromplace != null && destplace != null) {

                    final String mail,name,bookdate,period;
                    name = sharedprefs.getDefaults("nname",getContext());
                    mail = sharedprefs.getDefaults("email",getContext());
                    Calendar now = Calendar.getInstance();
                    if(now.get(Calendar.AM_PM) == Calendar.SUNDAY){
                        period = "PM";
                    }
                    else {
                        period = "AM";
                    }
                    now.getTime();
                    bookdate = String.valueOf(now.get(Calendar.DATE) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.YEAR)+"  "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)+" "+period);
                    Toast.makeText(getContext(), "Booking done from \n " + fromplace.getAddress().toString() + "\nto\n" + destplace.getAddress().toString(), Toast.LENGTH_LONG).show();
                    map.clear();
                    storeBookings.store(fromplace,destplace);
                    distance.setText("DISTANCE: 0 KM");
                    eta.setText("ETA: 0 HOURS");
                    LatLng zero = new LatLng(0,0);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(zero,1));

                    //completeBooking(fromplace,destplace);

                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long x = dataSnapshot.getChildrenCount();

                            ref2.child(String.valueOf(x+1)).child("email").setValue(mail);
                            ref2.child(String.valueOf(x+1)).child("name").setValue(name);
                            ref2.child(String.valueOf(x+1)).child("start").setValue(fromplace.getName().toString());
                            ref2.child(String.valueOf(x+1)).child("end").setValue(destplace.getName().toString());
                            ref2.child(String.valueOf(x+1)).child("date").setValue(bookdate);
                            ref2.child(String.valueOf(x+1)).child("distance").setValue(b);
                            ref2.child(String.valueOf(x+1)).child("Status").setValue("Pending");

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Log.i("Firebase reference",ref2.toString());
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                //Log.i("place",place.getName().toString());
                plotOnMap(place,clickedViewId);
            }else if (resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(getContext(),data);
                Toast.makeText(getContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getContext(),"Cancelled!!",Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean checkLocationsFixed(){
        return marker != null && marker1 != null;
    }

    private void plotOnMap(Place place, int clickedViewId) {
        String addr = place.getAddress().toString();
        LatLng position = place.getLatLng();
        if(clickedViewId == R.id.from){
            if(marker == null){
                marker = map.addMarker(new MarkerOptions()
                       .position(position));
                fromplace = place;
            }
            else {
                marker.remove();
                marker = map.addMarker(new MarkerOptions()
                       .position(position));
                fromplace = place;
            }
        }
        else if(clickedViewId == to){
            if (marker1 == null){
                marker1 = map.addMarker(new MarkerOptions()
                        .position(position));
                destplace = place;
            }
            else {
                marker1.remove();
                marker1 = map.addMarker(new MarkerOptions()
                        .position(position));
                destplace = place;
            }
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position,9);
        map.animateCamera(update);
        if(checkLocationsFixed()){
            sendRequest(fromplace,destplace,marker.getPosition(),marker1.getPosition());
        }
    }

    private void sendRequest(Place fromplace, Place destplace, LatLng position, LatLng position1) {
        /*
        * Most important two lines of this fragment...
        * */
        String origin = fromplace.getAddress().toString();
        String destination = destplace.getAddress().toString();
        Log.i("\n\nLOCATIONS FROM AND TO"," From: "+origin+" To: "+destination+"\n\n");
            try {
                new DirectionFinder(this,origin,destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    }

    /*
    * Runs fragment of autocomplete place locator..
    * */
    private void runSearchWidget(int clickedid) {
        try{
            AutocompleteFilter typeFiter = new AutocompleteFilter.Builder()
                    .setCountry("IN")
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFiter)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lasloc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getContext(), "Please wait.",
                "Finding direction..!", true);
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }
    private LatLngBounds ride;
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        marker.remove();
        marker1.remove();

        for (Route route : routes) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(route.startLocation,10);
            map.animateCamera(update);
            String a = route.duration.text;
            b = route.distance.text;
            distance.setText("Distance: " + b);
            eta.setText("ETA: " + a);
            originMarkers.add(map.addMarker(new MarkerOptions()
                   // .icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(map.addMarker(new MarkerOptions()
                   // .icon(BitmapDescriptorFactory.fromResource(R.drawable.end))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.CYAN).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(map.addPolyline(polylineOptions));
        }
        /*LatLng x = fromplace.getLatLng();
        LatLng y = destplace.getLatLng();
        double temp1 = x.latitude;
        double temp2 = x.longitude;
        double temp3 = y.latitude;
        double temp4 = y.longitude;
        ride = new LatLngBounds(new LatLng(temp1,temp2), new LatLng(temp3,temp4));
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(ride,0);
        map.animateCamera(update);*/
        //map.moveCamera(CameraUpdateFactory.newCameraPosition(fromplace.getLatLng()));

    }
}
