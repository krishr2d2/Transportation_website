package com.its.itspro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sai on 29/3/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Tuple> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView o,d,s,a;
        public RelativeLayout mTextView;
        public ViewHolder(View v) {
            super(v);
            o = (TextView) v.findViewById(R.id.bookingfrom);
            d = (TextView) v.findViewById(R.id.bookingto);
            s = (TextView) v.findViewById(R.id.stat);
            //a = (TextView) v.findViewById(R.id.arrow);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Tuple> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Tuple a = mDataset.get(position);
        Log.i("position in array", String.valueOf(position));
        String s1 = a.start.toString();
        String s2 = a.end.toString();
        Log.i("string1",s1);
        Log.i("string2",s2);
        TextView textView = holder.o;
        textView.setText(s1);
        TextView textView1 = holder.d;
        textView1.setText(s2);
        Log.i("pair ","from + "+s1+"to +"+s2);
//        setHasStableIds(true);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
/*        for(String s : mDataset){
            Log.i("values ",s);
        }*/
        return mDataset.size();
    }
}