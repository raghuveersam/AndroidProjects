package com.example.raghuveer.triviaapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Raghuveer on 9/26/2015.
 */

public class DeleteAllQuestions extends AppCompatActivity{

    public DeleteAllQuestions() {
        Toast.makeText(getApplicationContext(),"inside",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Demo", "inside");
        Toast.makeText(getApplicationContext(),"inside",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAllQuestions.this);
        builder.setTitle("Delete Questions");
        builder.setMessage("Are you sure you want to delete all questions?");
        builder.show();
    }

}
