package com.example.project_kayk.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_kayk.R;
import com.example.project_kayk.fragments.FillingFragment;
import com.example.project_kayk.fragments.GarnishFragment;
import com.example.project_kayk.fragments.IcingFragment;
import com.example.project_kayk.fragments.LayersFragment;
import com.example.project_kayk.fragments.SpongeFragment;
import com.example.project_kayk.fragments.TierFragment;
import com.example.project_kayk.utils.SingletonClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Customise_Frag extends AppCompatActivity {
    ImageView imageView;
    TextView priceTextView;

    Button next;
    int count = 1;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customise_frag);

        priceTextView = findViewById(R.id.pricetag);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.cake1);

        replaceFragment(new LayersFragment());
        clicks();

    }

    public void setImage(String imageFromAssets) {
        // load image

        SingletonClass.image = imageFromAssets;

        priceTextView.setText("$ " + String.valueOf(SingletonClass.totalPrice));

        try {
            // get input stream
            InputStream ims = getAssets().open(imageFromAssets);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }
    }

    private void clicks() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (++count) {
                    case 1: {
                        next.setVisibility(View.VISIBLE);
                        replaceFragment(new LayersFragment());
                        break;
                    }
                    case 2: {
                        next.setVisibility(View.VISIBLE);
                        if (SingletonClass.cakeProperties.getLayers().equals(""))
                            Toast.makeText(Customise_Frag.this, "Please select layer first", Toast.LENGTH_SHORT).show();
                        else
                            replaceFragment(new SpongeFragment());
                        break;
                    }
                    case 3: {
                        next.setVisibility(View.VISIBLE);
                        if (SingletonClass.cakeProperties.getLayers().equals("Two")) {

                            if (SingletonClass.cakeProperties.getSponge().equals(""))
                                Toast.makeText(Customise_Frag.this, "Please select sponge first", Toast.LENGTH_SHORT).show();
                            else
                                replaceFragment(new FillingFragment());
                        } else {
                            if (SingletonClass.cakeProperties.getSponge().equals(""))
                                Toast.makeText(Customise_Frag.this, "Please select sponge first", Toast.LENGTH_SHORT).show();
                            else
                                replaceFragment(new IcingFragment());
                        }
                        break;
                    }
                    case 4: {
                        next.setVisibility(View.VISIBLE);
                        if (SingletonClass.cakeProperties.getLayers().equals("Two")) {
                            if (SingletonClass.cakeProperties.getFilling().equals(""))
                                Toast.makeText(Customise_Frag.this, "Please select filling first", Toast.LENGTH_SHORT).show();
                            else
                                replaceFragment(new IcingFragment());
                        } else {
                            if (SingletonClass.cakeProperties.getIcing().equals(""))
                                Toast.makeText(Customise_Frag.this, "Please select icing first", Toast.LENGTH_SHORT).show();
                            else
                                replaceFragment(new GarnishFragment());
                        }
                        break;
                    }
                    case 5: {
                        if (SingletonClass.cakeProperties.getLayers().equals("Two")) {
                            if (SingletonClass.cakeProperties.getIcing().equals(""))
                                Toast.makeText(Customise_Frag.this, "Please select icing first", Toast.LENGTH_SHORT).show();
                            else {
                                replaceFragment(new GarnishFragment());
                                next.setVisibility(View.VISIBLE);
                            }
                        } else {
                            replaceFragment(new TierFragment());
                            next.setVisibility(View.GONE);
                        }
                        break;
                    }
                    case 6: {
                        if (SingletonClass.cakeProperties.getLayers().equals("Two")) {
                            if (SingletonClass.cakeProperties.getGarnish().equals(""))
                                Toast.makeText(Customise_Frag.this, "Please select garnish first", Toast.LENGTH_SHORT).show();
                            else
                                replaceFragment(new TierFragment());
                            next.setVisibility(View.GONE);
                        }
                        break;
                    }
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        --count;
        next.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container, fragment);

        if (count > 1) {
            transaction.addToBackStack(null);
        }

// Commit the transaction
        transaction.commit();
    }

}