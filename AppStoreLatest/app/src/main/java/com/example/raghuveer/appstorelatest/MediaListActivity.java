//Rgahuveer Sampath Krishnamurthy
// John O' Connor

package com.example.raghuveer.appstorelatest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MediaListActivity extends AppCompatActivity {

    BufferedReader bufferedreader;
    ArrayList<ContentList> list;
    LinearLayout linearlayout;
    String title;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        linearlayout=(LinearLayout)findViewById(R.id.linearlayout_id);
        title = getIntent().getExtras().getString("TITLE");
        setTitle(title);

        long present = loadSavedPreferences();
        long diff= (System.currentTimeMillis()-present)/60000;

        if(diff>=2){
            if (isConnected()) {
                String url;
                url = getIntent().getExtras().getString("LINK");
                new GetData().execute(url,title);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection.Try again later", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            setLayout(list);
        }




    }

    private class GetData extends AsyncTask<String,Void,ArrayList<ContentList>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MediaListActivity.this);
            progressDialog.setMessage("Loading" + " " + " " + title);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<ContentList> doInBackground(String... params) {
            ArrayList<String> arraylist = null;
            ArrayList<ContentList> objectlists = null;

            try {
                URL url;
                url = new URL(params[0]);
                HttpURLConnection con =(HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                bufferedreader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line=" ";
                arraylist = new ArrayList<String>();
                while ((line = bufferedreader.readLine())!=null){
                    arraylist.add(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("demo",arraylist.toString());
            String input=" ";
            for(String s : arraylist){
                input+=s;
            }
            try {
                objectlists = Utilities.MediaListParser.parseMedia(input,params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return objectlists;
        }

        @Override
        protected void onPostExecute(ArrayList<ContentList> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            list=result;

            SharedPreferences.Editor editor;
            sharedPreferences = MediaListActivity.this.getSharedPreferences(title, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String jsonContent = gson.toJson(list);
            editor.putString("LIST", jsonContent);
            editor.putLong("TIME", System.currentTimeMillis());
            editor.commit();

            setLayout(list);


        }
    }

    View.OnClickListener click= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int temp = v.getId();
            Intent intent = new Intent(MediaListActivity.this,AppDetails.class);
            intent.putExtra("Type",title);
            intent.putExtra("Innertitle",title+""+"Details");
            intent.putExtra("Media", list.get(temp));
            startActivity(intent);
        }
    };


    View.OnLongClickListener longclick= new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {

            int i = v.getId();
            linearlayout.removeViewAt(i);
            list.remove(i);
            updateID();

            SharedPreferences.Editor editor;
            sharedPreferences = MediaListActivity.this.getSharedPreferences(title, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String jsonContent = gson.toJson(list);
            editor.putString("LIST", jsonContent);
            editor.commit();
            return true;
        }
    };

    public void updateID(){
        int i=0;
        for(ContentList temp:list){
            linearlayout.getChildAt(i).setId(i);
            i++;
        }
    }

    public long loadSavedPreferences() {
        long beginning = 0;
        sharedPreferences = getSharedPreferences(title, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("LIST")) {
            beginning = sharedPreferences.getLong("TIME", 0);
            String jsonContent = sharedPreferences.getString("LIST", null);
            Gson gson = new Gson();
            ContentList[] content = gson.fromJson(jsonContent, ContentList[].class);
            list = new ArrayList<ContentList>();
            for (ContentList t : content) {
                list.add(t);
            }
        }
        return beginning;
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

    public void setLayout(ArrayList<ContentList> contentLists){
        int temp=0;
        for(ContentList contents : contentLists){

            LinearLayout layout = new LinearLayout(MediaListActivity.this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setOnClickListener(click);
            layout.setId(temp);
            layout.setOnLongClickListener(longclick);

            ImageView imageView = new ImageView(MediaListActivity.this);
            ViewGroup.LayoutParams layoutChild = new ViewGroup.LayoutParams(200, 200);
            imageView.setLayoutParams(layoutChild);
            Picasso.with(getApplicationContext()).load(contents.getImageurl()).into(imageView);

            TextView empty = new TextView(MediaListActivity.this);
            ViewGroup.LayoutParams layoutEmpty = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            empty.setLayoutParams(layoutEmpty);
            empty.setText("   ");

            TextView textView = new TextView(MediaListActivity.this);
            ViewGroup.LayoutParams layoutText = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutText);
            String title = contents.getMediatitle().replace("(Unabridged)", "");
            textView.setText(title);
            textView.setTextAppearance(MediaListActivity.this, android.R.style.TextAppearance_Large);


            layout.addView(imageView);
            layout.addView(empty);
            layout.addView(textView);
            linearlayout.addView(layout);
            temp++;
        }

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
