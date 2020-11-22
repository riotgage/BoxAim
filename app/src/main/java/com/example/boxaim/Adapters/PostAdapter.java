package com.example.boxaim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.boxaim.PostActivity;
import com.example.boxaim.R;
import com.example.boxaim.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter <PostAdapter.ViewHolder>{
    private Context mContext;
    private List<Post> mPosts;
    String TAG="PostAdapter";
    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Location,Price;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Location=itemView.findViewById(R.id.location);
            Price=itemView.findViewById(R.id.price);
            profile_image=itemView.findViewById(R.id.post_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=mPosts.get(position);
        holder.Price.setText(post.getPrice());
        holder.Location.setText(post.getCity());
        Log.d(TAG, "onBindViewHolder: "+post.getPost_id()+post.getImage());
        if(post.getImage().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.boxaimlogo);
        }
        else {
            if(holder.profile_image!=null)
            Glide.with(mContext).load(post.getImage()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PostActivity.class);
                intent.putExtra("postid",post.getPost_id());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
