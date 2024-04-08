package com.example.tourguidemegaphone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TouristSessionRVAdapter extends RecyclerView.Adapter<TouristSessionRVAdapter.MyViewHolder> {
    Context context;
    List<TourModel> toursList;
    private final OnItemClickListener listener;
    int SelectedInd = -1;

    public TouristSessionRVAdapter(Context c, List<TourModel> toursList, OnItemClickListener listener) {
        this.context = c;
        this.toursList = toursList;
        this.listener= listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.tourist_rv_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tourTitle.setText(toursList.get(position).getTourTitle());
        holder.tourStartTime.setText(toursList.get(position).getTourStartTime());
        holder.tourDuration.setText(toursList.get(position).getTourDuration());
        holder.tourPrice.setText(Double.toString(toursList.get(position).getTourPrice()));
        holder.viewTour.setOnClickListener(v -> {
            if (listener != null) {
                listener.viewItemClick(toursList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toursList.size();
    }

    public interface OnItemClickListener {
        void viewItemClick(TourModel item, int position);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tourTitle;
        TextView tourStartTime;
        TextView tourDuration;
        TextView tourPrice;
        Button viewTour;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tourTitle = (TextView)itemView.findViewById(R.id.to_rv_item_title);
            tourStartTime = (TextView)itemView.findViewById(R.id.to_rv_item_startTime);
            tourDuration = (TextView)itemView.findViewById(R.id.to_rv_item_duration);
            tourPrice = (TextView)itemView.findViewById(R.id.to_rv_item_price);
            viewTour = itemView.findViewById(R.id.to_rv_item_view_btn);

        }

    }
}
