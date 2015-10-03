//Rgahuveer Sampath Krishnamurthy
// John O' Connor


package com.example.raghuveer.appstorelatest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View v){
        Intent intent = new Intent(MainActivity.this,MediaListActivity.class);
        intent.putExtra("TITLE", (((TextView) v).getText().toString()));

        String input = ((TextView) v).getText().toString();

        if(input.equals("Audio Book")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/topaudiobooks/limit=25/json");
        }else if(input.equals("Books")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/topfreeebooks/limit=25/json");
        }else if(input.equals("iOS Apps")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/newapplications/limit=25/json");
        }
        else if(input.equals("iTunes U")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/topitunesucollections/limit=25/json");
        }
        else if(input.equals("MAC Apps")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/topfreemacapps/limit=25/json");
        }
        else if(input.equals("Movies")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/topmovies/limit=25/json");
        }
        else if(input.equals("Podcast")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/toppodcasts/limit=25/json");
        }
        else if(input.equals("TV Shows")){
            intent.putExtra("LINK","https://itunes.apple.com/us/rss/toptvepisodes/limit=25/json");
        }

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
