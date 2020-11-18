package com.example.boxaim.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.boxaim.FiltersActivity;
import com.example.boxaim.HomeActivity;
import com.example.boxaim.R;
import com.example.boxaim.authentication.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragement extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthstateListener;
    private Button mFilters;
    private static final String TAG="Account Fragment";
    Context mcontext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragement_profile,container,false);
        mFilters=v.findViewById(R.id.filter);
        setupFirebaseListener();

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
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"onAuthStateChanged: signed_in"+user.getUid());
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
