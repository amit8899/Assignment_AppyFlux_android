package com.example.testapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.AdapterUsers;
import com.example.testapplication.R;

import static com.example.testapplication.MainActivity.usersArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    public AdapterUsers adapterUsers;
    private static HomeFragment homeFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeFragment = this;
        adapterUsers = new AdapterUsers(usersArrayList, getContext());
        recyclerView = root.findViewById(R.id.recycler_users);
        recyclerView.setAdapter(adapterUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    public static HomeFragment getInstance(){
        return homeFragment;
    }
}