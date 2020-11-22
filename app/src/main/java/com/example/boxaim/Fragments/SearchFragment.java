package com.example.boxaim.Fragments;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxaim.Adapters.PostAdapter;
import com.example.boxaim.R;
import com.example.boxaim.authentication.LoginActivity;
import com.example.boxaim.models.Post;
import com.example.boxaim.util.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    private RotateLoading mProgressBar;
    PostAdapter adapter;
    RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> mPosts;
    String TAG="Search";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragement_search,container,false);
        setHasOptionsMenu(true);

        recyclerView=v.findViewById(R.id.PostrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView=v.findViewById(R.id.search);
        searchView.setActivated(true);
        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        mProgressBar=v.findViewById(R.id.progressBar);
        mPosts=new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals(""))
                search_posts(newText);

                return true;
            }
        });
        return v;
    }

    private void search_posts(String postTitle) {
        showProgressBar();
        Query query= FirebaseDatabase.getInstance().getReference("posts").orderByChild("title")
                .startAt(postTitle)
                .endAt(postTitle+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPosts.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Post post=snap.getValue(Post.class);
                    assert post!=null;
                    mPosts.add(post);
                }
                Log.d(TAG, "onDataChange: "+mPosts.size());
                postAdapter=new PostAdapter(getContext(),mPosts);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressBar();
            }
        });
        hideProgressBar();
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.start();
    }

    private void hideProgressBar(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.stop();
        }
    }

}
