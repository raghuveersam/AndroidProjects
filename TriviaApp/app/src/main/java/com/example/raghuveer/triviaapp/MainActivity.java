package com.example.raghuveer.triviaapp;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {


    AlertDialog  alertDialog;
    public static final int CREATE_QUESTION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void OnClickDeleteAllQuestions(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Questions");
        builder.setMessage("Are you sure you want to delete all questions?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("demo", "deleted array");

                RequestParams params = new RequestParams("POST","http://dev.theappsdr.com/apis/trivia_fall15/deleteAll.php");
                params.addParams("gid","72d692d32aaac450dbb05975535c3ef4");
                new Delete().execute(params);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("demo", "remain same");
            }
        });
        builder.show();

        //Intent intent = new Intent(MainActivity.this, DeleteAllQuestions.class);

    }


    public void OnClickStartTrivia(View v){
        Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
        startActivity(intent);
    }

    private class Delete extends AsyncTask<RequestParams, Void, Integer> {
        BufferedReader bufferedReader = null;
        int status=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LinearLayout linearLayout = new LinearLayout(MainActivity.this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);

            ProgressBar progressBar = new ProgressBar(MainActivity.this);
            progressBar.setLayoutParams(layoutParams);

            TextView textView = new TextView(MainActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setText("Deleting...");
            textView.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_Large);

            TextView textViewspace = new TextView(MainActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setText("  ");

            linearLayout.addView(progressBar);
           // linearLayout.addView(textViewspace);
            linearLayout.addView(textView);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Deleting Questions")
                    .setView(linearLayout)
                    .setCancelable(true);

            alertDialog = builder.create();
            alertDialog.show();
        }

        @Override
        protected Integer doInBackground(RequestParams... params) {

            try {
                HttpURLConnection con = params[0].setConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line="";
                if((line=bufferedReader.readLine())!=null){
                    status = Integer.valueOf(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            alertDialog.dismiss();
            Log.d("Demo",status+"");
            return;
        }
    }

    public void OnClickCreateQuestion(View v){
        Intent intent = new Intent(getApplicationContext(),CreateQuestionActivity.class);
        startActivity(intent);
    }

    public void OnClickExit(View v){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
