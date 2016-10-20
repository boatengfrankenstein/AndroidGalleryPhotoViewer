package com.niel.galleryphotoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.RequestManager;

import java.util.Vector;

/**
 * Created by Nilesh Deokar on 28/09/2015.
 */
public class GridGalleryAdapter extends RecyclerView.Adapter<GridGalleryAdapter.MainViewHolder> {


    private Vector<PhoneAlbum> mPhoneAlbums = new Vector<>();

    private Vector<PhonePhoto> phonePhotos = new Vector<>();

    private Context mCon;

   // private ArrayList<GalleryPhotoAlbum> arrayListAlbums = new ArrayList<GalleryPhotoAlbum>();
    private RequestManager glide;


    public int selectedPosition = 0;

    public GridGalleryAdapter(Context context, Vector<PhonePhoto> mPhotoPhone, RequestManager glide) {
        this.phonePhotos = mPhotoPhone;
        mCon = context;
        this.glide = glide;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new MainViewHolder(LayoutInflater.from(mCon).inflate(R.layout.view_grid_item_media_chooser, parent, false));

    }



    @Override
    public void onBindViewHolder(MainViewHolder mHolder, int position) {




            glide.
                    load("file:" + phonePhotos.get(position).getPhotoUri())
                    .centerCrop()
                    .fitCenter()
                    .into(mHolder.imageView);

        if (position == 0 && selectedPosition < 1) {
            mHolder.over_view_rl.setBackgroundResource(R.drawable.orange_rectangle_imageview_border);
        } else {
            if (selectedPosition == position) {
                mHolder.over_view_rl.setBackgroundResource(R.drawable.orange_rectangle_imageview_border);
                //Toast.makeText(mContext,"selected "+position,Toast.LENGTH_SHORT).show();
            } else {
                mHolder.over_view_rl.setBackgroundResource(0);
            }
        }

    }


    @Override
    public int getItemCount() {

            return phonePhotos.size();

    }


    protected class MainViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        RelativeLayout over_view_rl ;

        private MainViewHolder(View v) {
            super(v);

            //imageView = (ImageView) v.findViewById(R.id.picture);

            imageView = (ImageView) v.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);
            over_view_rl = (RelativeLayout)v.findViewById(R.id.rlImg);


        }
    }



}