package com.bekar.smartmedicalcare.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

public class TitleClass implements Parcelable {
    private String title;
    private String discription;

    public TitleClass() {
    }

    public TitleClass(String title, String discription) {
        this.title = title;
        this.discription = discription;
    }

    protected TitleClass(Parcel in) {
        title = in.readString();
        discription = in.readString();
    }

    public static final Creator<TitleClass> CREATOR = new Creator<TitleClass>() {
        @Override
        public TitleClass createFromParcel(Parcel in) {
            return new TitleClass(in);
        }

        @Override
        public TitleClass[] newArray(int size) {
            return new TitleClass[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(discription);
    }
}
