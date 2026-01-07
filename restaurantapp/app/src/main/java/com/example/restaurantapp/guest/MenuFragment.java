package com.example.restaurantapp.guest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.database.MenuRepository;
import com.example.restaurantapp.model.MenuItem;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private MenuRepository menuRepository;
    private ArrayList<MenuItem> menuItems;
    private MenuAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuRepository = new MenuRepository(getContext());
        menuRepository.addInitialMenuItems(); // Add initial data if the DB is empty

        RecyclerView menuRecyclerView = view.findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        menuItems = menuRepository.getAllMenuItems();

        adapter = new MenuAdapter(menuItems);
        menuRecyclerView.setAdapter(adapter);

        return view;
    }
}
