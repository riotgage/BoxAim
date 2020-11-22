package com.example.boxaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.boxaim.Fragments.AdditemFragement;
import com.example.boxaim.Fragments.SearchFragment;
import com.example.boxaim.Fragments.ProfileFragement;
import com.example.boxaim.Fragments.WishlistFragement;
import com.example.boxaim.authentication.LoginActivity;
import com.example.boxaim.models.User;
import com.example.boxaim.util.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.victor.loading.rotate.RotateLoading;

public class HomeActivity extends AppCompatActivity {

    private static String TAG="SearchActivity";
    private static final int REQUEST_CODE = 1;
    private RotateLoading mProgressBar;
    DatabaseReference reference;
    Bundle bundle;
    RelativeLayout progressLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bundle=new Bundle();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        Toolbar myToolbar  = findViewById(R.id.Maintoolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
        VerifyPermissions();
        initProgressBar();
        showProgressBar();
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
                bundle.putString("name",user.getName());
                bundle.putString("email",user.getEmail());
                bundle.putString("mobile",user.getMobile());
                bundle.putString("profile",user.getProfile());
                bundle.putString("posts",String.valueOf(user.getPosts()));
                bundle.putString("photos",String.valueOf(user.getPhotos()));
                bundle.putString("userid",user.getUser_id());
                hideProgressBar();
                bottomNavigationView.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SearchFragment()).commit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch(item.getItemId()){
                case R.id.nav_home:
                    selectedFragment=new SearchFragment();
                    break;

                case R.id.nav_additem:
                    selectedFragment=new AdditemFragement();
                    break;

                case R.id.nav_wishlist:
                    selectedFragment=new WishlistFragement();
                    break;

                case R.id.nav_profile:
                    selectedFragment=new ProfileFragement();
                    selectedFragment.setArguments(bundle);
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    private void VerifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(HomeActivity.this,
                    permissions,
                    REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        VerifyPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }


    private void showProgressBar(){
        Log.d(TAG, "showProgressBar: Showing Progressbar");
        progressLayout.setVisibility(View.VISIBLE);
        mProgressBar.start();

    }

    private void hideProgressBar(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            progressLayout.setVisibility(View.INVISIBLE);
            mProgressBar.stop();
        }
    }

    private void initProgressBar(){
        progressLayout=findViewById(R.id.progressLayout);
        mProgressBar=findViewById(R.id.progressBar);
        progressLayout.setVisibility(View.INVISIBLE);
    }
}