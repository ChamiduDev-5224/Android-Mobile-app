package com.example.busseatbookingapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;
    private List<Reservation> reservationListFull; // Copy of original list for filtering
    private OnItemClickListener listener;

    public ReservationAdapter(Context context, List<Reservation> reservationList, OnItemClickListener listener) {
        this.context = context;
        this.reservationList = reservationList;
        this.reservationListFull = new ArrayList<>(reservationList); // Create a copy of the original list
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_manage_item, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.bind(reservation, listener);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void filter(String searchText) {
        reservationList.clear();

        if (searchText.isEmpty()) {
            reservationList.addAll(reservationListFull);
        } else {
            for (Reservation reservation : reservationListFull) {
                if (reservation.getCheckingPlace().toLowerCase().contains(searchText.toLowerCase())) {
                    reservationList.add(reservation);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onEditClick(Reservation reservation);
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCheckingPlace;
        private TextView textViewSeatNumber;
        private TextView textViewReservingDate;
        private TextView textViewStatusLabel;
        private ImageButton buttonEdit;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCheckingPlace = itemView.findViewById(R.id.textViewCheckingPlace);
            textViewSeatNumber = itemView.findViewById(R.id.textViewSeatNumber);
            textViewReservingDate = itemView.findViewById(R.id.textViewReservingDate);
            textViewStatusLabel = itemView.findViewById(R.id.textViewStatusLabel);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }

        public void bind(final Reservation reservation, final OnItemClickListener listener) {
            textViewCheckingPlace.setText("Checking Place: " + reservation.getCheckingPlace());
            textViewSeatNumber.setText("Seat No: " + reservation.getSeatNumber());
            textViewReservingDate.setText("Reserving Date: " + reservation.getReservingDate());

            // Safely parse the reserving date
            long reservingDate = parseDate(reservation.getReservingDate());

            String statusLabel = getStatusLabel(reservation.getStatus(), reservingDate);
            textViewStatusLabel.setText(statusLabel);

            // Set background color and text color for status label
            GradientDrawable background = (GradientDrawable) textViewStatusLabel.getBackground();
            int backgroundColor;
            switch (statusLabel) {
                case "Active":
                    backgroundColor = Color.parseColor("#80cfa0"); // Reduced opacity green
                    buttonEdit.setVisibility(View.VISIBLE); // Show edit button for active reservations
                    break;
                case "Expired":
                    backgroundColor = Color.parseColor("#ff9999"); // Reduced opacity red
                    buttonEdit.setVisibility(View.GONE); // Hide edit button for expired reservations
                    break;
                case "Deactivate":
                default:

                    backgroundColor = Color.parseColor("#ffd580"); // Reduced opacity yellow
                    buttonEdit.setVisibility(View.GONE); // Hide edit button for Deactivate reservations
                    break;
            }
            background.setColor(backgroundColor);
            textViewStatusLabel.setTextColor(Color.WHITE); // Set text color to white
            background.setCornerRadius(8f); // Set corner radius
            // Adjust margin for Expired status
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textViewStatusLabel.getLayoutParams();
            if (statusLabel.equals("Expired")) {
                layoutParams.setMargins(0, 0, 112, 0); // 20dp margin on the right
            } else if (statusLabel.equals("Deactivate")) {
                layoutParams.setMargins(0, 0, 43, 0); // 20dp margin on the right
            }else {
                layoutParams.setMargins(0, 0, 0, 0); // No margin
            }

            textViewStatusLabel.setLayoutParams(layoutParams);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(reservation);
                }
            });
        }

        private long parseDate(String dateString) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date date = sdf.parse(dateString);
                if (date != null) {
                    return date.getTime();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private String getStatusLabel(int status, long reservingDate) {
            long currentDate = System.currentTimeMillis();
            if (status == 1 && reservingDate >= currentDate) {
                return "Active";
            } else if (reservingDate < currentDate) {
                return "Expired";
            } else {
                return "Deactivate";
            }
        }


    }
}