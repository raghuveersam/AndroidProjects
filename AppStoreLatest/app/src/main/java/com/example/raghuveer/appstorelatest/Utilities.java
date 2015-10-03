package com.example.raghuveer.appstorelatest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Raghuveer on 10/2/2015.
 */
public class Utilities {

    public Utilities() {
    }

    static public class MediaListParser {
        public MediaListParser() {
        }

        static ArrayList<ContentList> parseMedia(String input, String type) throws JSONException {

            ArrayList<ContentList> contentListArrayList = new ArrayList<>();

            JSONObject root = new JSONObject(input);
            JSONObject feed = root.getJSONObject("feed");
            JSONArray entry = feed.getJSONArray("entry");

            for (int i = 0; i < entry.length(); i++) {
                ContentList contentList = new ContentList();
                JSONObject media = entry.getJSONObject(i);

                ContentList content = ContentList.createMediaList(media, type);


                contentListArrayList.add(content);
            }
            return contentListArrayList;
        }
    }


}
