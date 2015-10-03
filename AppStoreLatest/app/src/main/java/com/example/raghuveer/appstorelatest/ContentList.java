package com.example.raghuveer.appstorelatest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raghuveer on 10/2/2ar015.
 */
public class ContentList implements Parcelable {
    String imageurl,mediatitle,largeimageurl;
    String artist,category,summary,price,releasedate,duration,link;

    public ContentList(){

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLargeimageurl() {
        return largeimageurl;
    }

    public void setLargeimageurl(String largeimageurl) {
        this.largeimageurl = largeimageurl;
    }

    protected ContentList(Parcel in) {
        imageurl = in.readString();
        mediatitle = in.readString();
        largeimageurl = in.readString();
        artist = in.readString();
        category = in.readString();
        summary = in.readString();
        price = in.readString();
        releasedate = in.readString();
        duration = in.readString();
        link=in.readString();
    }

    public static final Creator<ContentList> CREATOR = new Creator<ContentList>() {
        @Override
        public ContentList createFromParcel(Parcel in) {
            return new ContentList(in);
        }

        @Override
        public ContentList[] newArray(int size) {
            return new ContentList[size];
        }
    };

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getMediatitle() {
        return mediatitle;
    }

    public void setMediatitle(String mediatitle) {
        this.mediatitle = mediatitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(imageurl);
        dest.writeString(mediatitle);
        dest.writeString(largeimageurl);
        dest.writeString(artist);
        dest.writeString(category);
        dest.writeString(summary);
        dest.writeString(price);
        dest.writeString(releasedate);
        dest.writeString(duration);
        dest.writeString(link);
    }



    public static ContentList createMediaList(JSONObject media,String type) throws JSONException{

        ContentList con = new ContentList();

        JSONArray images = media.getJSONArray("im:image");
        JSONObject small_image = images.getJSONObject(0);
        con.setImageurl(small_image.getString("label"));
        con.setLargeimageurl(images.getJSONObject(2).getString("label"));

        JSONObject title= media.getJSONObject("im:name");
        con.setMediatitle(title.getString("label").replace("(Unabridged)", ""));

        con.setArtist(media.getJSONObject("im:artist").getString("label"));
        con.setCategory(media.getJSONObject("category").getJSONObject("attributes").getString("label"));
        con.setReleasedate(media.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label"));


        if(type.equals("Books")|| type.equals("iTunes U")|| type.equals("MAC Apps") || type.equals("Podcast")) {


            con.setLink(media.getJSONObject("link").getJSONObject("attributes").getString("href"));
            if(media.has("summary")) {
                con.setSummary(media.getJSONObject("summary").getString("label"));
            }
            else{
                con.setSummary("Not Available");
            }
            con.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }

        if(type.equals("TV Shows")){
            con.setLink(media.getJSONArray("link").getJSONObject(0).getJSONObject("attributes").getString("href"));
            con.setSummary(media.getJSONObject("summary").getString("label"));
            con.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }

        if(type.equals("Movies")){
            con.setLink(media.getJSONArray("link").getJSONObject(0).getJSONObject("attributes").getString("href"));
            con.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }


        if(type.equals("iOS Apps")){
            con.setLink(media.getJSONObject("link").getJSONObject("attributes").getString("href"));
            con.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }

        if(type.equals("Audio Book")){
            con.setDuration(media.getJSONArray("link").getJSONObject(1).getJSONObject("im:duration").getString("label"));
            con.setLink(media.getJSONArray("link").getJSONObject(0).getJSONObject("attributes").getString("href"));
        }


        return con;

    }


}
