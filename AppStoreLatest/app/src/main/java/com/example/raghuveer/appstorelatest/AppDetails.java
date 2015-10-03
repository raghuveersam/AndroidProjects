//Rgahuveer Sampath Krishnamurthy
// John O' Connor

package com.example.raghuveer.appstorelatest;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AppDetails extends AppCompatActivity {

    TextView title;
    TextView releaseDate;
    ImageView image;
    LinearLayout layout;
    String type;
    ContentList content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        title = (TextView) findViewById(R.id.apptitle_id);
        releaseDate = (TextView) findViewById(R.id.releasedate_id);
        image = (ImageView) findViewById(R.id.image_id);
        layout = (LinearLayout) findViewById(R.id.parent);

        setTitle(getIntent().getExtras().getString("Innertitle"));
        content = getIntent().getExtras().getParcelable("Media");
        type = getIntent().getExtras().getString("Type");

        if(isConnected()){
            display();
        }
        else {
            Toast.makeText(getApplicationContext(),"No Network Connection",Toast.LENGTH_LONG).show();
        }

    }

    public void display() {


        title.setText(content.getMediatitle());
        releaseDate.setText(content.getReleasedate());
        Picasso.with(getApplicationContext()).load(content.largeimageurl).into(image);
        layout.addView(createTextView("Artist" + " " + ":" + " " + content.getArtist()));

        TextView category = (createTextView("Category" + " " + ":" + " " + content.getCategory()));
        layout.addView(category);


        if (type.equals("Audio Book")) {
            layout.addView(createTextView("Duration" + " " + ":" + " " + content.getDuration()));
        } else {
            if (type.equals("iOS Apps") || (type.equals("Movies"))) {
                layout.addView(createTextView("Price" + " " + ":" + " " + "$" + " " + content.getPrice()));
            } else {
                if (type.equals("Podcast")) {
                    layout.removeView(category);
                }
                layout.addView(createTextView("Summary" + " " + ":" + " " + content.getSummary()));
                layout.addView(createTextView("Price" + " " + ":" + " " + "$" + " " + content.getPrice()));
            }
        }

        layout.addView(createTextView("App Link in Store:"));
        TextView con = createTextView(content.getLink());
        con.setTextAppearance(this, android.R.style.TextAppearance_Small);
        con.setTextColor(getResources().getColor(R.color.md_blue_700));
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppDetails.this, PreviewActivity.class);
                intent.putExtra("URL", content.getLink());
                startActivity(intent);
            }
        });
        layout.addView(con);
        layout.addView(createTextView(" "));

    }


    public TextView createTextView(String text) {

        TextView view = new TextView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setTextAppearance(this, android.R.style.TextAppearance_Large);
        view.setText(text);
        return view;
    }


    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
