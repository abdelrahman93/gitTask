package com.example.asherif.task.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by A.Sherif on 24-Dec-17.
 */

public class Company implements Parcelable {
    String name;
    String domain;
    String logo;

    public Company(String name, String domain, String logo) {
        this.name = name;
        this.domain = domain;
        this.logo = logo;
    }

    protected Company(Parcel in) {
        name = in.readString();
        domain = in.readString();
        logo = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(domain);
        parcel.writeString(logo);
    }
}
