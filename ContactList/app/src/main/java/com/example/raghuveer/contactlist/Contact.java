package com.example.raghuveer.contactlist;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor


import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

public class Contact  implements Parcelable{
    String name,email;
    long phonenumber;
    String image;

    public Contact(String name, String email, long phonenumber,String image) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.image=image;
    }

    public Contact(){
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getPhonenumber() {
        return phonenumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhonenumber(long phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeLong(phonenumber);
        dest.writeString(image);
    }


    public static final Parcelable.Creator<Contact> CREATOR
            = new Parcelable.Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    private Contact(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.phonenumber = in.readLong();
        this.image=in.readString();
    }
}
