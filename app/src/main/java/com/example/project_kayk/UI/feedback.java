package com.example.project_kayk.UI;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_kayk.Model.User_feedback;
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

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class feedback extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    EditText feedback, user;
    Button submit_button;
    FirebaseDatabase database;
    Timer timer;
    DatabaseReference reference;
    private TextView mperson_name;
    private CircleImageView image;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private RecyclerView.Adapter my_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mToolBar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();
        feedback = findViewById(R.id.feedback);
        user = findViewById(R.id.enter_name);
        submit_button = findViewById(R.id.submitbtn);
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Feedback");
        DefineNavigation();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }


    private void DefineNavigation() {
        View mnavigationview;
        navigationView = findViewById(R.id.navegation_view2);
        drawerLayout = findViewById(R.id.drawer2);
        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mperson_name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getNavHeaderData();
    }
    private void getNavHeaderData() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(UserId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
                    mperson_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
        feedbackk();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))return true;
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(feedback.this, MainActivity.class));
        }
        else if (id == R.id.Profile) {
            startActivity(new Intent(feedback.this, UserProfileActivity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(feedback.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(feedback.this, OrderActivity.class));
        }

        else if (id == R.id.Logout) {
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(feedback.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(feedback.this,loginActivity.class);
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

    private void feedbackk() {
        User_feedback userFeedback = new User_feedback();
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feed = feedback.getText().toString().trim();
                String u = user.getText().toString().trim();
                if (TextUtils.isEmpty(feed) && (TextUtils.isEmpty(u))) {
                    Toast.makeText(feedback.this, "All Fields Empty", Toast.LENGTH_SHORT).show();
                } else {
                    userFeedback.setFeedback(feed);
                    userFeedback.setUsername(u);
                    reference.push().setValue(userFeedback);
                    Toast.makeText(feedback.this, "Submitted", Toast.LENGTH_SHORT).show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(feedback.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                    }, 1000);


                }
            }
        });
    }

}