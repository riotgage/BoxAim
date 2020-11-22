package com.example.boxaim.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.boxaim.FiltersActivity;
import com.example.boxaim.HomeActivity;
import com.example.boxaim.R;
import com.example.boxaim.authentication.LoginActivity;
import com.example.boxaim.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragement extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthstateListener;
    private Button mFilters;
    private static final String TAG="Account Fragment";
    TextView email,mobile,posts,photos,username;
    ImageView profile;
    Context mcontext;
    Bundle bundle;
    DatabaseReference reference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragement_profile,container,false);
        mFilters=v.findViewById(R.id.filter);
        setupFirebaseListener();
        bundle=getArguments();
        mcontext=getContext();
        profile=v.findViewById(R.id.profile_image);
        email=v.findViewById(R.id.email);
        mobile=v.findViewById(R.id.mobile);
        posts=v.findViewById(R.id.posts);
        photos=v.findViewById(R.id.photos);
        username=v.findViewById(R.id.username);
        if(bundle.getString("profile").equals("default")){
            profile.setImageResource(R.mipmap.boxaimlogo);
        }
        else{
            Glide.with(mcontext).load(bundle.getString("profile")).into(profile);
        }
        email.setText(bundle.getString("email"));
        username.setText(bundle.getString("name"));
        photos.setText(bundle.getString("photos"));
        posts.setText(bundle.getString("photos"));
        mobile.setText(bundle.getString("mobile"));
        mFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FiltersActivity.class));
            }
        });
        return v;
    }

    private void setupFirebaseListener(){
        Log.d(TAG,"setupFirebaseListener ; setting up the auth state Listener");

        mAuthstateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fuser=firebaseAuth.getCurrentUser();
                if(fuser!=null){
                    Log.d(TAG,"onAuthStateChanged: signed_in"+fuser.getUid());
                }
                else{
                    Log.d(TAG,"onAuthStateChanged: signed_out");
                    Intent intent=new Intent(mcontext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthstateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthstateListener ==null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthstateListener);
        }
    }
}
