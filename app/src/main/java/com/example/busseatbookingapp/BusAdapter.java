package com.example.busseatbookingapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private Context context;
    private List<Bus> busList;
    private List<Bus> busListFull; // Copy of original list for filtering
    private OnItemClickListener listener;

    public BusAdapter(Context context, List<Bus> busList, OnItemClickListener listener) {
        this.context = context;
        this.busList = busList;
        this.busListFull = new ArrayList<>(busList); // Create a copy of the original list
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.bind(bus, listener);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void filter(String searchText, String startFrom, String endDestination, String routeNo, String departureTime) {
        busList.clear();

        // If no filters applied, show full list
        if (TextUtils.isEmpty(searchText) && TextUtils.isEmpty(startFrom) && TextUtils.isEmpty(endDestination)
                && TextUtils.isEmpty(routeNo) && TextUtils.isEmpty(departureTime)) {
            busList.addAll(busListFull);
        } else {
            for (Bus bus : busListFull) {
                // Implement your filtering logic here
                if ((TextUtils.isEmpty(searchText) || bus.getBusNumber().toLowerCase().contains(searchText.toLowerCase()))
                        && (TextUtils.isEmpty(startFrom) || bus.getStartForm().toLowerCase().contains(startFrom.toLowerCase()))
                        && (TextUtils.isEmpty(endDestination) || bus.getEndDestination().toLowerCase().contains(endDestination.toLowerCase()))
                        && (TextUtils.isEmpty(routeNo) || bus.getBusRouteNo().toLowerCase().contains(routeNo.toLowerCase()))
                        && (TextUtils.isEmpty(departureTime) || bus.getDepartureTime().toLowerCase().contains(departureTime.toLowerCase()))) {
                    busList.add(bus);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Bus bus);
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewBusNumber;
        private TextView textViewRoute;
        private TextView textViewDepartureTime;
        private Button buttonMoreDetails;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBusNumber = itemView.findViewById(R.id.textViewBusNumber);
            textViewRoute = itemView.findViewById(R.id.textViewRoute);
            textViewDepartureTime = itemView.findViewById(R.id.textViewDepartureTime);
            buttonMoreDetails = itemView.findViewById(R.id.buttonMoreDetails);
        }

        public void bind(final Bus bus, final OnItemClickListener listener) {
            textViewBusNumber.setText("Bus No: " + bus.getBusNumber());
            textViewRoute.setText(bus.getStartForm() + " to " + bus.getEndDestination() + " - Route No: " + bus.getBusRouteNo());
            textViewDepartureTime.setText("Departure: " + bus.getDepartureTime());

            buttonMoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(bus);
                }
            });
        }
    }
}
