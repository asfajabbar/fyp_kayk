package com.example.project_kayk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project_kayk.R;
import com.example.project_kayk.UI.ProductInfoActivity;
import com.example.project_kayk.utils.SingletonClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierFragment extends Fragment {

    public TierFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmetn_cake_cost, container, false);

        init(view);

        return view;

    }

    private void init(View view) {

        TextView addtocart = view.findViewById(R.id.addtocart);
        TextView titleTextView = view.findViewById(R.id.title);
        TextView layer = view.findViewById(R.id.layer);
        TextView title_layer = view.findViewById(R.id.title_layer);
        TextView sponge = view.findViewById(R.id.sponge);
        TextView title_sponge = view.findViewById(R.id.title_sponge);
        TextView filling = view.findViewById(R.id.filling);
        TextView title_filling = view.findViewById(R.id.title_filling);
        TextView icing = view.findViewById(R.id.icing);
        TextView title_icing = view.findViewById(R.id.title_icing);
        TextView garnish = view.findViewById(R.id.garnish);
        TextView title_garnish = view.findViewById(R.id.title_garnish);
        TextView totalPrice = view.findViewById(R.id.totalPrice);
        EditText cakename = view.findViewById(R.id.cakename);

        titleTextView.setText("Price detail of your cake");

        layer.setText(String.valueOf(SingletonClass.layerPrice) + " $");
        sponge.setText(String.valueOf(SingletonClass.spongePrice) + " $");
        filling.setText(String.valueOf(SingletonClass.fillingPrice) + " $");
        icing.setText(String.valueOf(SingletonClass.icingPrice) + " $");
        garnish.setText(String.valueOf(SingletonClass.garnishPrice) + " $");

        title_layer.setText("Layer ("+SingletonClass.cakeProperties.getLayers()+")");
        title_sponge.setText("Sponge ("+SingletonClass.cakeProperties.getSponge()+")");
        title_filling.setText("Filling ("+SingletonClass.cakeProperties.getFilling()+")");
        title_icing.setText("Icing ("+SingletonClass.cakeProperties.getIcing()+")");
        title_garnish.setText("Garnish ("+SingletonClass.cakeProperties.getGarnish()+")");

        String totalPriceValue = String.valueOf(SingletonClass.layerPrice + SingletonClass.spongePrice + SingletonClass.fillingPrice +
                SingletonClass.icingPrice + SingletonClass.garnishPrice + SingletonClass.tierPrice);

        totalPrice.setText("Total Price: "+ totalPriceValue + " Rs");


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cakename.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please enter cake name!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
                    intent.putExtra("Product Name",cakename.getText().toString());
                    intent.putExtra("Product Price",totalPriceValue);
                    intent.putExtra("Product Image","Custom Cake");
                    intent.putExtra("Product ExpiryDate","0");
                    intent.putExtra("Product IsFavorite", "false");
                    intent.putExtra("Is Offered","no");
                    intent.putExtra("assetimage","no");

                    startActivity(intent);

                }

            }
        });

    }
}