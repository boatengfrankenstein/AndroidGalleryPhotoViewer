package com.niel.galleryphotoapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Shreya on 9/4/2015.
 */
public class ImageFragment extends Fragment {
    public ImageView imagePreview;
    private ArrayList<String> mSelectedItems = new ArrayList<>();
    private Vector<PhonePhoto> mGalleryModelList;

    private RecyclerView mImageRecyclerView;
    private GridGalleryAdapter mImageAdapterGallery;
    private RequestManager glide;
    private View mView;

    private DiskCacheStrategy diskCacheStrategy;


    public ImageFragment() {
        setRetainInstance(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        glide = Glide.with(this);

        if (mView == null) {
            mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

             mImageRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
            diskCacheStrategy = DiskCacheStrategy.RESULT;
            imagePreview = (ImageView) mView.findViewById(R.id.imagePreview);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
            mImageRecyclerView.setLayoutManager(layoutManager);

            if (getArguments() != null) {

                mGalleryModelList =  (Vector<PhonePhoto>)getArguments().get("vector");
                setAdapter();
            }
        } else {
             if (mImageAdapterGallery == null || mImageAdapterGallery.getItemCount() == 0) {
                Toast.makeText(getActivity(), "no_media_file_available", Toast.LENGTH_SHORT).show();
            }
        }


        return mView;
    }

    private void setAdapter() {

        if (mGalleryModelList.size() != 0) {

            mImageAdapterGallery = new GridGalleryAdapter(getActivity(), mGalleryModelList, glide);
            mImageRecyclerView.setAdapter(mImageAdapterGallery);
            String firstImagePath = mGalleryModelList.get(0).getPhotoUri();
            mSelectedItems.add(firstImagePath);
            glide.load(firstImagePath).diskCacheStrategy(diskCacheStrategy).skipMemoryCache(true).dontAnimate().into(imagePreview);

        } else {
            Toast.makeText(getActivity(), "No Media Files Available", Toast.LENGTH_SHORT).show();
        }


        mImageRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mImageRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {


            }

            @Override
            public void onItemClick(View view, int position) {


                if (mImageAdapterGallery.selectedPosition != position) {
                    PhonePhoto galleryModel = mGalleryModelList.get(position);


                    if (!galleryModel.isStatus()) {
                        long size = MediaChooserConstants.ChekcMediaFileSize(new File(galleryModel.getPhotoUri()), false);
                        if (size != 0) {
                            Toast.makeText(getActivity(), "File Size Exeeded" + "  " + MediaChooserConstants.SELECTED_IMAGE_SIZE_IN_MB + " " + "", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if ((MediaChooserConstants.MAX_MEDIA_LIMIT == MediaChooserConstants.SELECTED_MEDIA_COUNT)) {
                            if (MediaChooserConstants.SELECTED_MEDIA_COUNT < 2) {
                                Toast.makeText(getActivity(), "max_limit_file" + "  " + MediaChooserConstants.SELECTED_MEDIA_COUNT + " " + "", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Toast.makeText(getActivity(), "max_limit_file" + "  " + MediaChooserConstants.SELECTED_MEDIA_COUNT + " " + "", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    }

                    // inverse the status
                    galleryModel.setStatus(true);
                    mImageAdapterGallery.selectedPosition = position;

                    //mImageAdapterGallery.notifyItemChanged(position);
                    mImageRecyclerView.setAdapter(mImageAdapterGallery);
                    mImageAdapterGallery.notifyDataSetChanged();

                    if (galleryModel.isStatus()) {
                        mSelectedItems.add(galleryModel.getPhotoUri());
                        MediaChooserConstants.SELECTED_MEDIA_COUNT++;

                    } else {
                        mSelectedItems.remove(galleryModel.getPhotoUri().trim());
                        MediaChooserConstants.SELECTED_MEDIA_COUNT--;
                    }

                    glide.load(galleryModel.getPhotoUri()).diskCacheStrategy(diskCacheStrategy).skipMemoryCache(true).dontAnimate().into(imagePreview);


                }
            }
        }));



    }
    public ArrayList<String> getSelectedImageList() {
        return mSelectedItems;
    }


    public void addItem(String item) {
        if (mImageAdapterGallery != null) {
            PhonePhoto model = new PhonePhoto();
            model.setPhotoUri(item);

            mGalleryModelList.add(0, model);
            glide.load(model.getPhotoUri()).diskCacheStrategy(diskCacheStrategy).skipMemoryCache(true).dontAnimate().into(imagePreview);

            mImageAdapterGallery.notifyDataSetChanged();
        }
    }


    public void refreshData(Vector<PhonePhoto> mList){
        mGalleryModelList = mList;
        setAdapter();
    }

}