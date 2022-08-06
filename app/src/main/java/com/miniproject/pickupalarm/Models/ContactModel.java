package com.miniproject.pickupalarm.Models;

import android.graphics.Bitmap;
import android.net.Uri;

public class ContactModel {

    private String id;
    private String name;
    private String mobileNumber;
    private Bitmap photo;
    private Uri photoURI;

    private boolean selected;

    public ContactModel() {
    }

    public ContactModel(String id, String name, String mobileNumber, Bitmap photo, Uri photoURI, boolean selected) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.photo = photo;
        this.photoURI = photoURI;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(Uri photoURI) {
        this.photoURI = photoURI;
    }
}
