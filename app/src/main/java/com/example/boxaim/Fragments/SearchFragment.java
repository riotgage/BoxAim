package com.example.boxaim.Fragments;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.boxaim.R;
import com.example.boxaim.authentication.LoginActivity;
import com.example.boxaim.util.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchFragment extends Fragment {
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private RelativeLayout mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragement_search,container,false);
        setHasOptionsMenu(true);

        return v;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu,menu);
//        MenuItem searchItem = menu.findItem(R.id.search_bar);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
//                    return true;
//                }
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//                    Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
//                    searchView.clearFocus();
//                    showProgressBar();
//                    return true;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//
//
//    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

}
