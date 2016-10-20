package com.niel.galleryphotoapp;

import android.os.Parcel;
import android.os.Parcelable;

public class PhonePhoto implements Parcelable {

    private int id;
    private String albumName;
    private String photoUri;
    private boolean status = false;


    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName( String name ) {
        this.albumName = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri( String photoUri ) {
        this.photoUri = photoUri;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    protected PhonePhoto(Parcel in) {
        id = in.readInt();
        albumName = in.readString();
        photoUri = in.readString();
        status = in.readByte() != 0x00;
    }

    public PhonePhoto(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(albumName);
        dest.writeString(photoUri);
        dest.writeByte((byte) (status ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PhonePhoto> CREATOR = new Parcelable.Creator<PhonePhoto>() {
        @Override
        public PhonePhoto createFromParcel(Parcel in) {
            return new PhonePhoto(in);
        }

        @Override
        public PhonePhoto[] newArray(int size) {
            return new PhonePhoto[size];
        }
    };
}