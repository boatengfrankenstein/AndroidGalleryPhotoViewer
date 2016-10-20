package com.niel.galleryphotoapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Shreya on 9/4/2015.
 */

public class HomeFragmentActivity extends BaseActivity implements View.OnClickListener,OnPhoneImagesObtained {


    private static Uri fileUri;
    private Context context;
    private Vector<PhoneAlbum> mPhoneAlbums = new Vector<>();
    private ImageFragment imageFragment;
    private Spinner spinner_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homr_media_chosser);
        context = this;

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.custom_actionbar, null);

        setupToolbar();
        setupBackButton();

        getSupportActionBar().setCustomView(v);

        v.findViewById(R.id.camera).setOnClickListener(this);
        v.findViewById(R.id.upload).setOnClickListener(this);

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        DeviceImageManager.getPhoneAlbums(this,this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));

                String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
                setCapturedImage(fileUriString);


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Action cancelled", Toast.LENGTH_SHORT).show();
           }
        }
    }



    private String getRightAngleImage(String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree, photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }

    private String rotateImage(int degree, String imagePath) {

        if (degree <= 0) {
            return imagePath;
        }
        try {
            Bitmap b = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if (b.getWidth() > b.getHeight()) {
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    private void setCapturedImage(final String imagePath) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    return getRightAngleImage(imagePath);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return imagePath;
            }

            @Override
            protected void onPostExecute(String imagePath) {
                super.onPostExecute(imagePath);
                if (imageFragment == null) {
                    imageFragment = new ImageFragment();
                    imageFragment.addItem(imagePath);
                } else {
                    imageFragment.addItem(imagePath);
                }
            }
        }.execute();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:

                if (imageFragment != null) {
                    if (imageFragment.getSelectedImageList() != null && imageFragment.getSelectedImageList().size() > 0) {
                        Intent imageIntent = new Intent();
                        imageIntent.setAction("lNc_imageSelectedAction");
                        imageIntent.putStringArrayListExtra("list", imageFragment.getSelectedImageList());
                        setCapturedImage(imageFragment.getSelectedImageList().get(imageFragment.getSelectedImageList().size() - 1));

                        setResult(Activity.RESULT_OK, imageIntent);


                    }
                }
                finish();
                break;

            case R.id.camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                startActivityForResult(intent, MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onComplete(Vector<PhoneAlbum> albums) {

        mPhoneAlbums = albums;

        if(!mPhoneAlbums.isEmpty()){

        // display Album names in spinner
        ArrayList<String> list = new ArrayList<String>();

        for(PhoneAlbum model : albums ){
            list.add(model.getName());
        }

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);

        spinner_nav.setAdapter(spinAdapter);

        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                refreshData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        if (MediaChooserConstants.showImage) {
            FragmentManager fragmentManager = getFragmentManager();
             imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("tab1");
            if (imageFragment == null) {
                imageFragment = new ImageFragment();
            }


            if (imageFragment != null && imageFragment instanceof ImageFragment) {
                //Fragment already exists
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(imageFragment);

                Bundle bundle = new Bundle();
                bundle.putSerializable("vector", (Serializable)albums.get(0).getAlbumPhotos());
                imageFragment.setArguments(bundle);


                fragmentTransaction.add(R.id.realTabcontent, imageFragment, "tab1").commit();

            } else {
                //Add Fragment

                Bundle bundle = new Bundle();
                bundle.putSerializable("vector", (Serializable)albums.get(0).getAlbumPhotos());
                imageFragment.setArguments(bundle);



                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.realTabcontent, imageFragment, "tab1").commit();

            }

        }
        }else {
            showToast(getString(R.string.no_img_found));
        }
    }

    private void refreshData(int position) {

            try {
                imageFragment.refreshData(mPhoneAlbums.get(position).getAlbumPhotos());
            }catch (Exception e){
                e.printStackTrace();
            }

    }
    @Override
    public void onError() {
        showToast(getString(R.string.no_img_found));
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MediaChooserConstants.folderName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}