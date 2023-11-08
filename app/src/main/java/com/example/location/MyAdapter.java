package com.example.location;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Resources resources ;

    public interface OnItemtoDeleteClickListener {
        void onItemtoDeleteClick(int position);

    }
    private OnItemtoDeleteClickListener onItemtoDeleteClickListener;
    public void setOnItemtoDeleteClickListener(OnItemtoDeleteClickListener listener) {
        this.onItemtoDeleteClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    private List<Location> locationList;
    Context context;

    public MyAdapter( Context context,List<Location> locationList) {
        this.locationList = locationList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.address_of_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Location location = locationList.get(position);
        Log.d("adap","val" + location.getAddrss());
        holder.address.setText(location.getAddrss());
        holder.lat.setText(location.getLat());
        holder.lon.setText(location.getLon());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (onItemtoDeleteClickListener  != null) {
                    onItemtoDeleteClickListener.onItemtoDeleteClick(clickedPosition);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(clickedPosition);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return locationList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView address;
        TextView lat;
        TextView lon;
        Button deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.addressOutput);
            lat = itemView.findViewById(R.id.latOutput);
            lon = itemView.findViewById(R.id.lonOutput);
            deleteBtn = itemView.findViewById(R.id.deleteAddress);

        }
    }
    public void updateData(List<Location> newlocations) {
        locationList.clear();
        locationList.addAll(newlocations);
        notifyDataSetChanged();
    }
}
