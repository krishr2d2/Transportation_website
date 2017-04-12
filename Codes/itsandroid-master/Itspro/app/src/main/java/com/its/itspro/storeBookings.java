package com.its.itspro;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by sai on 28/3/17.
 */

public class storeBookings{
    private FirebaseDatabase daa;
    private DatabaseReference ref;
    private static ArrayList<Place> in = new ArrayList<>();
    private static ArrayList<Place> out = new ArrayList<>();

    public static String formatKey(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s,"UTF8");
        encode = encode.replace(".","");
        encode = encode.replace("#","");
        encode = encode.replace("$","");
        encode = encode.replace("[","");
        encode = encode.replace("]","");
        encode = encode.replace("%","");
        return encode;
    }

    public static void store(Place fromplace, Place destplace) {
        in.add(fromplace);
        out.add(destplace);
    }

    public static ArrayList retrieveOrigins(){
        return in;
    }

    public static ArrayList retrieveDestinations(){
        return out;
    }
}
