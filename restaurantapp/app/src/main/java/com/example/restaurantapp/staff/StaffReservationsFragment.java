package com.example.restaurantapp.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.restaurantapp.R;

public class StaffReservationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This fragment will display a list of all reservations for staff to manage.
        // For now, it just inflates the layout.
        // You could later use the same ReservationsAdapter as the guest view, or a more detailed one for staff.
        return inflater.inflate(R.layout.staff_fragment_reservations, container, false);
    }
}
