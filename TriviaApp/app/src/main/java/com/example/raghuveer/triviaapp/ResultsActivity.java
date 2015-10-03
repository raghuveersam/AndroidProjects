package com.example.raghuveer.triviaapp;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    ArrayList<Integer> results;
    ProgressBar progressBar;
    TextView percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        progressBar= (ProgressBar)findViewById(R.id.progressbar_id);
        percentage=(TextView)findViewById(R.id.percentage_id);

        if(getIntent().getIntegerArrayListExtra("Results")!=null){
            results = getIntent().getIntegerArrayListExtra("Results");

        }
        int count=0;
        for(int result:results){

            if(result==1){
                count++;
            }
        }

        progressBar.setMax(results.size());
        progressBar.setProgress(count);


        float temp = (float)count/results.size();
        temp=temp*100;
        int percent = (int)temp;

        percentage.setText(String.valueOf(percent+"%"));
    }


    public void OnClickTryagain(View V){
        Intent intent = new Intent(ResultsActivity.this,TriviaActivity.class);
        //intent.putIntegerArrayListExtra("Results",answersforresult);
        startActivity(intent);
        finish();

    }

    public void OnClickQuit(View V){
        //Intent intent = new Intent(ResultsActivity.this,MainActivity.class);
        //intent.putIntegerArrayListExtra("Results",answersforresult);
        //startActivityForResult(intent,MAIN_ID);
        finish();

    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(ResultsActivity.this,MainActivity.class);
        //intent.putIntegerArrayListExtra("Results",answersforresult);
        //startActivityForResult(intent,MAIN_ID);
        finish();
    }
}
