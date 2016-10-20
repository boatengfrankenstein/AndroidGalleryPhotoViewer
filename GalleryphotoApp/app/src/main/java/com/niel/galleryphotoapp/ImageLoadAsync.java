package com.niel.galleryphotoapp;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by Shreya on 9/4/2015.
 */
public class ImageLoadAsync extends MediaAsync<String, String, String> {

    private ImageView mImageView;
    private Context mContext;
    private int mWidth;

    public ImageLoadAsync(Context context, ImageView imageView, int width) {
        mImageView = imageView;
        mContext = context;
        mWidth = width;
    }

    @Override
    protected String doInBackground(String... params) {
        return params[0];
    }

    @Override
    protected void onPostExecute(String result) {
        Glide.with(mContext)
                .load(new File(result))
                .override(mWidth, mWidth)
                //  .centerCrop().placeholder(R.drawable.camera_place)
                .into(mImageView);

    }

}