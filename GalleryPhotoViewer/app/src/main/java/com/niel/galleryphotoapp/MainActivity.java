package com.niel.galleryphotoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;


/**
 * Created by Nilesh on 9/4/2015.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });
    }

    private void startGallery() {
        Intent i = new Intent(MainActivity.this,HomeFragmentActivity.class);
        startActivityForResult(i,MediaChooserConstants.SELECT_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == MediaChooserConstants.SELECT_IMAGE_REQUEST_CODE) {// photo chooser
            if (responseCode == RESULT_OK) {
                setAdapter(data.getStringArrayListExtra("list"));
            }
        }
    }

    private void setAdapter(List<String> filePathList) {
        final String img_path_tostore = filePathList.get(filePathList.size() - 1);
        File file = new File(img_path_tostore);
     //   Toast.makeText(this,"selected img is :"+ img_path_tostore,Toast.LENGTH_LONG).show();
        Glide.with(this).load(file).into((ImageView) findViewById(R.id.imgSelected));
    }
}
