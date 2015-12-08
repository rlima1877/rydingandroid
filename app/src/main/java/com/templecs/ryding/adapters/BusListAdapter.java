package com.templecs.ryding.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.templecs.ryding.R;
import com.templecs.ryding.activities.BusDetails;
import com.templecs.ryding.model.Bus;

/**
 * Created by rafaellima on 10/22/15.
 *
 * This class receives a Context and a List<Bus> to populate recycler view.
 * This class also handles on click view for a particular bus list cell
 *
 */
public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private Activity activity;
    List<Bus> buses = Collections.emptyList();


    public BusListAdapter(Context context, List<Bus> buses){
        Log.d("JSON", "Context " + context.toString());
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.buses = buses;
        this.activity = (Activity)context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        final Bus currentBus = buses.get(position);
        holder.icon.setImageResource(R.drawable.bus_icon_32);
        holder.busNumber.setText(Integer.toString(currentBus.getBusNumber()));
        holder.busRoute.setText(currentBus.getBusRoute());


        //Click listener for each cell in the bus list.
        //This will change once we have ETA information
        //Pass current bus to next activity.

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BusDetails.class);
                intent.putExtra("Bus", currentBus);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        if(position % 2 == 0){
            holder.elementHolder.setBackgroundColor(Color.GRAY);
            holder.busRoute.setTextColor(Color.BLACK);
            holder.busNumber.setTextColor(Color.BLACK);
        }
    }

    //In case we want to let user delete an item from the list.
    //Create a button is custom layout with an onclick listener calling this method.
    //Will need use of database or shared preferences for changes to persist.
    public void delete(int position){
        buses.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView busNumber;
        TextView busRoute;
        ImageView icon;
        LinearLayout elementHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            busNumber = (TextView) itemView.findViewById(R.id.textViewBusNumber);
            busRoute = (TextView) itemView.findViewById(R.id.textViewBusRoute);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            elementHolder = (LinearLayout)itemView.findViewById(R.id.row_element);
        }
    }



}//end BusListAdapter
