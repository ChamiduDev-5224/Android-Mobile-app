package com.example.busseatbookingapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SeatAdapter  extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<String> seatList;
    public SeatAdapter(List<String> seatList) {
        this.seatList = seatList;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        String seat = seatList.get(position);
        holder.textViewSeatNumber.setText(seat);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSeatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSeatNumber = itemView.findViewById(R.id.textViewSeatNumber);
        }
    }
}