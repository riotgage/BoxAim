package com.example.boxaim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SelectPhotoDialog extends DialogFragment {
    private static final String TAG="SelectPhotoDialog";
    private static final int PICKFILE_REQUEST_CODE=1234;
    private static final int CAMERA_REQUEST_CODE=4321;

    public interface OnPhotoSelectedListener{
        void getImagePath(Uri imagePath);
        void getImageBitmap(Bitmap bitmap);
    }

    OnPhotoSelectedListener mOnPhotoSelectedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_select_photo,container,false);
        TextView selectPhoto= v.findViewById(R.id.dialogChoosePhoto);

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: accessing phones Memory");
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICKFILE_REQUEST_CODE);
            }
        });

        TextView takePhoto= v.findViewById(R.id.dialogOpenCamera);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: accessing phones Memory");
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==PICKFILE_REQUEST_CODE && resultCode==Activity.RESULT_OK){
            Uri selectedImageUri=data.getData();
            Log.d(TAG,"OnActivityResult: image uri :"+ selectedImageUri);

            //send uri to post fragment and dismiss dialog
            mOnPhotoSelectedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        }
        else if(requestCode ==CAMERA_REQUEST_CODE&& resultCode==Activity.RESULT_OK){

            Log.d(TAG,"OnActivityResult: Done Taking new Photo");
            Bitmap bitmap;
            bitmap= (Bitmap)data.getExtras().get("data");

            //send bitmap to post fragment and dismiss dialog
            mOnPhotoSelectedListener.getImageBitmap(bitmap);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {

        try {
            mOnPhotoSelectedListener =(OnPhotoSelectedListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG,"OnAttach: ClassCastException: "+ e.getMessage());
        }

        super.onAttach(context);
    }
}
