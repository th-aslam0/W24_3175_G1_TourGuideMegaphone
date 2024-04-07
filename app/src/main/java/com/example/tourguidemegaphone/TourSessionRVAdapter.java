package com.example.tourguidemegaphone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TourSessionRVAdapter extends RecyclerView.Adapter<TourSessionRVAdapter.MyViewHolder> {
    Context context;
    List<TourModel> toursList;
    private final OnItemClickListener listener;
    int SelectedInd = -1;

    public TourSessionRVAdapter(Context c, List<TourModel> toursList, OnItemClickListener listener) {
        this.context = c;
        this.toursList = toursList;
        this.listener= listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.tour_rv_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tourTitle.setText(toursList.get(position).getTourTitle());
        holder.tourStartTime.setText(toursList.get(position).getTourStartTime());
        holder.tourDuration.setText(toursList.get(position).getTourDuration());
        holder.tourPrice.setText(Double.toString(toursList.get(position).getTourPrice()));
        holder.editTour.setOnClickListener(v -> {
            if (listener != null) {
                listener.editItemClick(toursList.get(position), position);
            }
        });
        holder.deleteTour.setOnClickListener(v -> {
            if (listener != null) {
                listener.deleteItemClick(toursList.get(position), position);
            }
        });
        holder.joinTour.setOnClickListener(v -> {
            if (listener != null) {
                listener.joinItemClick(toursList.get(position), position);
            }
        });
//        holder.itemView.setOnClickListener(v -> {
//
//            if (holder.getAdapterPosition() == SelectedInd || SelectedInd == -1) {
//                holder.songControl.setImageResource(R.drawable.stop);
//            } else {
//                holder.songControl.setImageResource(R.drawable.play);
//            }
//            SelectedInd = position;
//            listener.onItemClick(toursList.get(position), position);
//        });
    }

    @Override
    public int getItemCount() {
        return toursList.size();
    }

    public interface OnItemClickListener {
        void editItemClick(TourModel item, int position);
        void deleteItemClick(TourModel item, int position);
        void joinItemClick(TourModel item, int position);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tourTitle;
        TextView tourStartTime;
        TextView tourDuration;
        TextView tourPrice;
        Button editTour;
        Button deleteTour;
        Button joinTour;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tourTitle = (TextView)itemView.findViewById(R.id.t_rv_item_title);
            tourStartTime = (TextView)itemView.findViewById(R.id.t_rv_item_startTime);
            tourDuration = (TextView)itemView.findViewById(R.id.t_rv_item_duration);
            tourPrice = (TextView)itemView.findViewById(R.id.t_rv_item_price);
            editTour = itemView.findViewById(R.id.t_rv_item_btn_edit);
            deleteTour = itemView.findViewById(R.id.t_rv_item_btn_delete);
            joinTour = itemView.findViewById(R.id.t_rv_item_btn_join);
        }

    }
}
