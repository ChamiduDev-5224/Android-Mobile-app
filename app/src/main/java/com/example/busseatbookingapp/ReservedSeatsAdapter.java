package com.example.busseatbookingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReservedSeatsAdapter extends RecyclerView.Adapter<ReservedSeatsAdapter.SeatViewHolder> {
    private List<Reservation> seatReservations;

    public ReservedSeatsAdapter(List<Reservation> seatReservations) {
        this.seatReservations = seatReservations;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserved_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Reservation seatReservation = seatReservations.get(position);
        holder.seatNumber.setText(String.valueOf(seatReservation.getSeatNumber()));
        holder.reservingDate.setText(seatReservation.getReservingDate());
        holder.email.setText(seatReservation.getEmail());
        holder.checkingPlace.setText(seatReservation.getCheckingPlace());
    }

    @Override
    public int getItemCount() {
        return seatReservations.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView seatNumber, reservingDate, email, checkingPlace;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatNumber = itemView.findViewById(R.id.textViewSeatNumber);
            reservingDate = itemView.findViewById(R.id.textViewReservingDate);
            email = itemView.findViewById(R.id.textViewEmail);
            checkingPlace = itemView.findViewById(R.id.textViewCheckingPlace);
        }
    }
}
