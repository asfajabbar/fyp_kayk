package com.example.project_kayk.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project_kayk.Adapters.GridproductAdapter;
import com.example.project_kayk.Model.HorizontalProductModel;
import com.example.project_kayk.Model.Offers;
import com.example.project_kayk.Model.favouritesClass;
import com.example.project_kayk.Model.model;
import com.example.project_kayk.Model.user;
import com.example.project_kayk.Adapters.My_Adapter;
import com.example.project_kayk.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private CircleImageView image;
    private TextView mperson_name;
    private FirebaseAuth mAuth;
    private String Uid, name, photo;
    private FirebaseUser CurrentUser;
    private NavigationView navigationView;
    private ViewPager pager;
    private My_Adapter adapter;
    private List<model> models;
    private DatabaseReference m;
    private View mnavigationview;
    public CardView card1,card2,card3;
    private static List<favouritesClass> favourites;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();
        navigationView = findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mperson_name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);
        drawerLayout = findViewById(R.id.drawer);
        mToolBar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        card3 = (CardView) findViewById(R.id.card3);
        card1.setOnClickListener(this::onClick);
        card2.setOnClickListener(this::onClick);
        card3.setOnClickListener(this::onClick);
        findViewById(R.id.customcake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Customise_Frag.class));
            }
        });

    }
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.card1:
                i = new Intent(this,bakeries.class);
                startActivity(i);
                break;
            case R.id.card2:
                i = new Intent(this,bakeries.class);
                startActivity(i);
                break;
            case R.id.card3:
                i = new Intent(this, bakeries.class);
                startActivity(i);
                break;

        }

    }
    @Override
    public void onStart() {
        super.onStart();

        //Retrieve Header View User Data
        Navigation_view_header_data();

        //Retrieve Favourites
        Retrieve_fav();

        // FirstView
        Retrieve_Mini_Treats();

        // SecondView
        Retrieve_Jalal_Sons();

        //Third View
        Retrieve_Layers();

        // Fourth View
        Retrieve_CakesBakes();

        //fifth View
        Retrieve_CakeMystery();
        Retrieve_Malmo();
        Retrieve_Baba();
        Retrieve_Karamel();
        Retrieve_Baskin_Robins();

        // OFFERS
        Retrieve_offers();

        //Refresh CartIcon
        showCartIcon();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();
    }

    public void Retrieve_fav() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        favourites = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    favouritesClass fav = new favouritesClass();
                    fav = ds.getValue(favouritesClass.class);
                    favourites.add(fav);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    public void Retrieve_Mini_Treats() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Mini Treats");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Mini Treats");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Mini Treats");
                startActivity(intent);
            }
        });

    }

    public void Retrieve_Jalal_Sons() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView2);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Jalal Sons");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Jalal Sons");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Jalal Sons");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_Layers() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView3);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Layers");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Layers");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Layers");
                startActivity(intent);
            }
        });

    }

    public void Retrieve_CakesBakes() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView4);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Cakes & Bakes");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Cakes & Bakes");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Cakes & Bakes");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_CakeMystery() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView5);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Cake Mystery");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Cake Mystery");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Cake Mystery");
                startActivity(intent);
            }
        });
    }
    public void Retrieve_Malmo() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView6);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Malmo");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Malmo");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Malmo");
                startActivity(intent);
            }
        });
    }
    public void Retrieve_Baba() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView7);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Baba");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Baba");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Baba");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_Karamel() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView8);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Karamel");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Karamel");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Karamel");
                startActivity(intent);
            }
        });
    }
    public void Retrieve_Baskin_Robins() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView9);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Baskin Robins");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Baskin Robins");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Baskin Robins");
                startActivity(intent);
            }
        });
    }





    public void Retrieve_offers() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("offers");
        models = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Offers offer = new Offers();
                    offer = ds.getValue(Offers.class);
                    offer.setTitle(ds.getKey().toString());
                    models.add(new model(offer.getImg(), offer.getTitle(), offer.getDescribtion()));
                    adapter = new My_Adapter(models, MainActivity.this);
                    pager = findViewById(R.id.cardview);
                    pager.setAdapter((PagerAdapter) adapter);
                    pager.setPadding(130, 0, 130, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Profile) {
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(MainActivity.this, favourites_activity.class));
        }
        else if(id == R.id.feedback){
            startActivity(new Intent(MainActivity.this, feedback.class));
        }

        else if(id == R.id.Cart){
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(MainActivity.this, OrderActivity.class));
        }

        else if(id == R.id.help){
            startActivity(new Intent(MainActivity.this, help.class));
        }
        else if(id == R.id.about){
            startActivity(new Intent(MainActivity.this, aboutus.class));
        }

        else if (id == R.id.Logout) {
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(MainActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,loginActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("LogOut");
        alert.show();

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public void Navigation_view_header_data() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("Name").getValue().toString();
                    photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
                    mperson_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    private void showCartIcon(){
        //toolbar & cartIcon
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.main2_toolbar,null);
        actionBar.setCustomView(view);

        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout)findViewById(R.id.CustomCartIconContainer);
        PageTitle =(TextView)findViewById(R.id.PageTitle);
        CustomCartNumber = (TextView)findViewById(R.id.CustomCartNumber);

        PageTitle.setText("Kayk");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

    }


    private void setNumberOfItemsInCartIcon(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount()==1){
                        CustomCartNumber.setVisibility(View.GONE);
                    }
                    else {
                        CustomCartNumber.setVisibility(View.VISIBLE);
                        CustomCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount()-1));
                    }
                }
                else{
                    CustomCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    private void HandleTotalPriceToZeroIfNotExist(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("cart").child(Uid).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);
    }
}
