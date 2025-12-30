package com.example.restaurantapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Reservation;

import java.util.ArrayList;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private final ArrayList<Reservation> reservations;
    private OnReservationActionClickListener actionClickListener;
    private boolean showEdit = false;
    private boolean showCancel = false;

    // Interface for handling clicks
    public interface OnReservationActionClickListener {
        void onEditClick(Reservation reservation);
        void onCancelClick(Reservation reservation);
    }

    // Constructor for read-only view
    public ReservationsAdapter(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    // Constructor for views with actions
    public ReservationsAdapter(ArrayList<Reservation> reservations, boolean showEdit, boolean showCancel, OnReservationActionClickListener listener) {
        this.reservations = reservations;
        this.showEdit = showEdit;
        this.showCancel = showCancel;
        this.actionClickListener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.dateTextView.setText("Date: " + reservation.getDate());
        holder.timeTextView.setText("Time: " + reservation.getTime());
        holder.guestsTextView.setText("Guests: " + String.valueOf(reservation.getNumberOfGuests()));

        // Control visibility of icons based on flags
        if (actionClickListener != null) {
            holder.actionsLayout.setVisibility(View.VISIBLE);
            holder.editIcon.setVisibility(showEdit ? View.VISIBLE : View.GONE);
            holder.cancelIcon.setVisibility(showCancel ? View.VISIBLE : View.GONE);

            if (showEdit) {
                holder.editIcon.setOnClickListener(v -> actionClickListener.onEditClick(reservation));
            }
            if (showCancel) {
                holder.cancelIcon.setOnClickListener(v -> actionClickListener.onCancelClick(reservation));
            }
        } else {
            holder.actionsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, guestsTextView;
        View actionsLayout;
        ImageView editIcon, cancelIcon;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.reservationDateTextView);
            timeTextView = itemView.findViewById(R.id.reservationTimeTextView);
            guestsTextView = itemView.findViewById(R.id.reservationGuestsTextView);
            actionsLayout = itemView.findViewById(R.id.reservation_actions_layout);
            editIcon = itemView.findViewById(R.id.edit_reservation_icon);
            cancelIcon = itemView.findViewById(R.id.cancel_reservation_icon);
        }
    }
}
