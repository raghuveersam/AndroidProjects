//Homework 1
//MainActivity.java
//Raghuveer Sampath Krishnamurthy, John OConnor

package com.example.raghuveer.alcoholcal;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText wegv;
    boolean checksave,checkhistory,firstimeedit;
    RadioButton but1oz;
    RadioGroup rdoGrp;
    int weight, alcoholPer, ozVal;
    ArrayList<Integer> ozarray = new ArrayList<Integer>();
    ArrayList<Integer> alcperarray = new ArrayList<Integer>();
    double res,finalRes,gender;
    SeekBar sBar;
    TextView alcPer, bacPrt,messTxt;
    Switch genSwt;
    ProgressBar proBar;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.wineglass);

        res = 0;
        ozVal = 1;
        alcoholPer = 5;
        firstimeedit = true;
        finalRes=0;

        but1oz = (RadioButton) findViewById(R.id.oz1id);
        wegv = (EditText) findViewById(R.id.weight_id);
        alcPer = (TextView) findViewById(R.id.alcoholPct);
        proBar = (ProgressBar) findViewById(R.id.progress_id);
        messTxt = (TextView) findViewById(R.id.message);
        final TextView malefemletext = (TextView)findViewById(R.id.MF_id);

        but1oz.setChecked(true);

        genSwt = (Switch) findViewById(R.id.switchid);
        sBar = (SeekBar) findViewById(R.id.alcoholSeekbar);
        sBar.setMax(25);
        sBar.setProgress(5);
        sBar.incrementProgressBy(5);
        alcPer.setText(5 + "% ");
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 5;
                progress = progress * 5;
                alcoholPer = progress;
                alcPer.setText(progress + "% ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        genSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checksave = false;
                if (isChecked) {
                    malefemletext.setText(" M");
                    gender = 0.73;
                } else {
                    gender = 0.66;
                    malefemletext.setText(" F");
                }
            }
        });


        wegv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                checksave = false;
            }
        });
    }

    public void onclicksave(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        wegv = (EditText) findViewById(R.id.weight_id);
        if (wegv.getText().length() == 0) {
            Log.d("Tag", "");
            Toast.makeText(getApplicationContext(), "Enter the weight in lbs.", Toast.LENGTH_SHORT).show();
        } else {
            weight = Integer.valueOf(wegv.getText().toString());
            if(weight == 0 ){
                Toast.makeText(getApplicationContext(), "Enter valid weight.", Toast.LENGTH_SHORT).show();
            }
            else {
                checksave = true;
                if (firstimeedit) {
                    Log.d("This is first time ", "");
                } else {
                    //weight = Integer.valueOf(wegv.getText().toString());
                    Log.d("This is history", "");
                    callhistory();
                }
            }
        }
    }


    public void onclickaddDrink(View v) {

        wegv = (EditText) findViewById(R.id.weight_id);
        rdoGrp = (RadioGroup) findViewById(R.id.radiogrp_id);
        Log.d("Inside onclick drink",checksave+"");
        if ((wegv.getText().length() == 0) || (!checksave)) {
            setError();
        }
        else {
            weight = Integer.valueOf(wegv.getText().toString());
            if(rdoGrp.getCheckedRadioButtonId()== R.id.oz1id){
                ozVal = 1;
            }
             if(rdoGrp.getCheckedRadioButtonId()== R.id.oz2id){
                ozVal = 5;
            }
             if(rdoGrp.getCheckedRadioButtonId()== R.id.oz3id){
                ozVal = 12;
            }
            if(genSwt.isChecked()){
                gender = 0.73;

            }else{
                gender = 0.66;
            }
            checkhistory = false;
            calculation(ozVal,alcoholPer,weight,gender);
            display();
        }
    }

    public void setError() {
        Toast.makeText(getApplicationContext(), "Enter the weight in lbs.", Toast.LENGTH_SHORT).show();
        checksave = false;
    }


    public void callhistory(){
        weight = Integer.valueOf(wegv.getText().toString());
        if(genSwt.isChecked()){
            gender = 0.73;

        }else{
            gender = 0.66;
        }
        checkhistory = true;
        finalRes=0;
        for (int i=0;i<ozarray.size();i++) {
            calculation(ozarray.get(i), alcperarray.get(i),weight,gender);
        }
        display();
    }

    public void calculation(int getozvalue,int getalcpercentage,int getweight,double getgender) {

        float calalcper = (float) 0.00;
        firstimeedit = false;
          if(!checkhistory){
            ozarray.add(getozvalue);
            alcperarray.add(getalcpercentage);
          }
        calalcper = (float) (getalcpercentage*0.01);
            res = (((calalcper * getozvalue*5.14) / (getweight * getgender)));
            finalRes = finalRes +res;
            Log.d("this is alc per",calalcper+"");
            Log.d("this is oz val",getozvalue+"");
            Log.d("this is weight",getweight+"");
            Log.d("this is genval",getgender+"");
            Log.d("this is res",finalRes+"");
    }

    public void display (){

        bacPrt = (TextView) findViewById(R.id.bacvalue);
        int progessvalue =0;
        bacPrt.setText(String.format("BAC Level: %.3f", finalRes));
        progessvalue = (int) (finalRes*100/0.25);
        proBar.setProgress(progessvalue);

        if(finalRes <=0.08){
            messTxt.setText("You're Safe");
            messTxt.setBackgroundColor(getResources().getColor(R.color.md_green_600));
        }
        if(( 0.08 < finalRes)&&(finalRes<0.20)){
            messTxt.setText("Be careful...");
            messTxt.setBackgroundColor(getResources().getColor(R.color.md_yellow_600));
        }
        if(finalRes > 0.20){
            messTxt.setText("Over the limit!");
            messTxt.setBackgroundColor(getResources().getColor(R.color.md_red_600));
        }
        if(finalRes >= 0.25){
            enableButton(false);
            Toast.makeText(getApplicationContext(), "No more drinks for you.", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearall(){

        enableButton(true);
        wegv.setText("");
        genSwt.setChecked(true);
        sBar.setProgress(5);
        messTxt.setText("");
        but1oz.setChecked(true);
        checksave = false;
        firstimeedit=false;
        display();
        bacPrt.setText(String.format("BAC Level: %.2f", finalRes));
    }

    public void enableButton(boolean condition){
        ViewGroup parentlayout = (ViewGroup)findViewById(R.id.parent_id);
        for(int i=0;i<parentlayout.getChildCount();i++){
            ViewGroup v =(ViewGroup)parentlayout.getChildAt(i);
            for(int j=0;j<v.getChildCount();j++){
                View vi = v.getChildAt(j);
                if(vi instanceof ViewGroup){
                    for(int t=0;t<((ViewGroup) vi).getChildCount();t++){
                        View rb= (View)((ViewGroup) vi).getChildAt(t);
                        rb.setEnabled(condition);
                    }
                }
                if(vi instanceof RadioGroup){
                    for(int z=0;z<((RadioGroup) vi).getChildCount();z++){
                       RadioButton rb= (RadioButton)((RadioGroup) vi).getChildAt(z);
                        rb.setEnabled(condition);
                    }
                }
                else {
                    vi.setEnabled(condition);
                }
            }
        }
    findViewById(R.id.reset_id).setEnabled(true);
    }
   public void OnclickReset(View v){
       res = 0;
       ozVal = 1;
       alcoholPer = 5;
       firstimeedit = true;
       finalRes=0;
       clearall();
       ozarray.clear();
       alcperarray.clear();
   }
}


