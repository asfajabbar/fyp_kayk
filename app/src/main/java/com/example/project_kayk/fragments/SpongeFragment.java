package com.example.project_kayk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_kayk.R;
import com.example.project_kayk.Adapters.NestedAdapter;

import java.util.ArrayList;
import java.util.List;

public class SpongeFragment extends Fragment {

    public SpongeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cake_properties, container, false);

        init(view);

        return view;

    }

    private void init(View view) {
        TextView titleTextView = view.findViewById(R.id.title);
        RecyclerView cakePropertiesRecyclerview = view.findViewById(R.id.recylerview);

        titleTextView.setText("Sponge");

        cakePropertiesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        cakePropertiesRecyclerview.setAdapter(new NestedAdapter(getActivity(), getPropertiesList(), "sponge"));

    }

    public List<String> getPropertiesList()
    {

        List<String> spongeList = new ArrayList<>(); //sponge
        spongeList.add("Vanilla");
        spongeList.add("Chocolate");
        spongeList.add("Strawberry");
        spongeList.add("RedVelvet");

        return spongeList;

    }
}