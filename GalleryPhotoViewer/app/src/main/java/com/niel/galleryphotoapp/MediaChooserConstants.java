package com.niel.galleryphotoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import java.io.File;

/**
 * Created by Nilesh on 9/4/2015.
 */
public class MediaChooserConstants {

    public static final int BUCKET_SELECT_IMAGE_CODE = 1000;
    public static final int BUCKET_SELECT_VIDEO_CODE = 2000;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int SELECT_IMAGE_REQUEST_CODE = 111;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    /**
     * folder name on which captured photo & video are saved on sd card.
     */
    public static String folderName = "Mutterfly";
    /**
     * No of item that can be selected. Default is 100.
     */
    public static int MAX_MEDIA_LIMIT = 100;
    /**
     * Selected media file count.
     */
    public static int SELECTED_MEDIA_COUNT = 0;
    public static boolean showCameraVideo = true;
    public static boolean showVideo = true;
    public static boolean showImage = true;
    public static int SELECTED_IMAGE_SIZE_IN_MB = 20;
    public static int SELECTED_VIDEO_SIZE_IN_MB = 20;

    public static long ChekcMediaFileSize(File mediaFile, boolean isVideo) {

        /** Get length of file in bytes */
        long fileSizeInBytes = mediaFile.length();

        /** Convert the bytes to Kilobytes (1 KB = 1024 Bytes) */
        long fileSizeInKB = fileSizeInBytes / 1024;

        /** Convert the KB to MegaBytes (1 MB = 1024 KBytes) */
        long fileSizeInMB = fileSizeInKB / 1024;

        int requireSize = 0;
        if (isVideo) {
            requireSize = MediaChooserConstants.SELECTED_VIDEO_SIZE_IN_MB;
        } else {
            requireSize = MediaChooserConstants.SELECTED_IMAGE_SIZE_IN_MB;
        }
        if (fileSizeInMB <= requireSize) {
            return 0;
        }
        return fileSizeInMB;
    }



}