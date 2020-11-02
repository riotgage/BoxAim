package com.example.boxaim.Fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.boxaim.R;
import com.example.boxaim.SelectPhotoDialog;
import com.example.boxaim.models.Post;
import com.example.boxaim.util.RotateBitmap;
import com.example.boxaim.util.UniversalImageLoader;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdditemFragement extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener{

    private static final String TAG = "PostFragment";
    private ImageView PostImage;
    private EditText mTitle, mDescription, mPrice, mCountry, mStateProvince, mCity, mContactEmail;
    private CardView mPostImage;
    private ProgressBar mProgressBar;
    private Button mPost;

    private Bitmap selectedBitmap;
    private Uri selectedUri;
    private byte []mUploadBytes;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO: Make additem fragment UI little good.

        View view = inflater.inflate(R.layout.fragement_additem, container, false);
        mPostImage = view.findViewById(R.id.post_image_card);
        PostImage=view.findViewById(R.id.post_image);
        mTitle = view.findViewById(R.id.post_title);
        mDescription = view.findViewById(R.id.post_desc);
        mPrice = view.findViewById(R.id.post_price);
        mCountry = view.findViewById(R.id.post_country);
        mStateProvince = view.findViewById(R.id.post_state);
        mCity = view.findViewById(R.id.post_city);
        mContactEmail = view.findViewById(R.id.post_email);
        mPost = view.findViewById(R.id.btn_post);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        init();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to post...");
                if(!isEmpty(mTitle.getText().toString())
                        && !isEmpty(mDescription.getText().toString())
                        && !isEmpty(mPrice.getText().toString())
                        && !isEmpty(mCountry.getText().toString())
                        && !isEmpty(mStateProvince.getText().toString())
                        && !isEmpty(mCity.getText().toString())
                        && !isEmpty(mContactEmail.getText().toString())){


                    //we have a bitmap and no Uri
                    showProgressBar();
                    if(selectedBitmap != null && selectedUri == null){
                        uploadNewPhoto(selectedBitmap);
                    }
                    //we have no bitmap and a uri
                    else if(selectedBitmap == null && selectedUri != null){
                        uploadNewPhoto(selectedUri);
                    }
                }else{
                    Toast.makeText(getActivity(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    //TODO : Check new way of doing Background Tasks Instead of AsyncTask
    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        Bitmap bitmap;
        public BackgroundImageResize(Bitmap bitmap) {
                this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Compressing Image", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.d(TAG,"DoinBackground: Compressing image");
            if(bitmap==null){
                try{
                    RotateBitmap rotateBitmap=new RotateBitmap();
                    bitmap=rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(),uris[0]);
                }
                catch (IOException e){
                    Log.d(TAG,"DoinBackground : IOException "+e.getMessage());
                }
            }
            byte[]bytes=null;
            bytes=getBytesFromBitmap(bitmap,100);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes =bytes;
            Toast.makeText(getActivity(), "Uploading Image ", Toast.LENGTH_SHORT).show();
            executeUploadTask();
        }


    }

    private void executeUploadTask(){
        showProgressBar();
        final String postId= FirebaseDatabase.getInstance().getReference().push().getKey();
        final StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                .child("posts/users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+
                        "/"+postId+"/post_image");

        UploadTask uploadTask=storageReference.putBytes(mUploadBytes);
        Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri == null)
                        return;
                    else{
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Post post = new Post();
                        post.setImage(downloadUri.toString());
                        post.setCity(mCity.getText().toString());
                        post.setContact_email(mContactEmail.getText().toString());
                        post.setDescription(mDescription.getText().toString());
                        post.setState_province(mStateProvince.getText().toString());
                        post.setPost_id(postId);
                        post.setPrice(mPrice.getText().toString());
                        post.setCountry(mCountry.getText().toString());
                        post.setTitle(mTitle.getText().toString());
                        post.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        resetFields();
                        hideProgressBar();
                        reference.child(getString(R.string.node_posts)).child(postId).setValue(post);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "could not upload Photo", Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });

    }
    public static byte[] getBytesFromBitmap(Bitmap bitmap,int quality){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return stream.toByteArray();
    }

    private void uploadNewPhoto(Bitmap bitmap){
        Log.d(TAG,"UploadNewPhoto: uploading a new image bitmap to storage");
        BackgroundImageResize resize=new BackgroundImageResize(bitmap);
        Uri uri=null;
        resize.execute(uri);
    }

    private void uploadNewPhoto(Uri uri){
        Log.d(TAG,"UploadNewPhoto: uploading a new uri to storage");
        BackgroundImageResize resize=new BackgroundImageResize(null);
        resize.execute(uri);
    }

    private void init(){

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog to choose new photo");
                SelectPhotoDialog dialog = new SelectPhotoDialog();
                dialog.show(getFragmentManager(),getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(AdditemFragement.this, 1);
            }
        });
    }

    private void resetFields(){
        UniversalImageLoader.setImage("", PostImage);
        mTitle.setText("");
        mDescription.setText("");
        mPrice.setText("");
        mCountry.setText("");
        mStateProvince.setText("");
        mCity.setText("");
        mContactEmail.setText("");
    }

    private void showProgressBar(){
        Log.d(TAG,"Progress bar : running");
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        Log.d(TAG,"Progress bar : stopped");
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }

    @Override
    public void getImagePath(Uri imagePath) {
        Log.d(TAG,"setting the image to "+imagePath);
        UniversalImageLoader.setImage(imagePath.toString(),PostImage);
        selectedBitmap=null;
        selectedUri=imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Log.d(TAG,"setting the image catched from camera");
        PostImage.setImageBitmap(bitmap);
        selectedBitmap=bitmap;
        selectedUri=null;
    }
}
