package com.example.raghuveer.inclass2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ehr,emn;
    Button est, cst, pst, clr, mst;
    Switch st;
    String checkAmPm;
    TextView ans,checkAMPM_text,rst_text,fir_AMPM,pr_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        st = (Switch) findViewById(R.id.switch_id);
        checkAMPM_text = (TextView)findViewById(R.id.AP_id);
        st.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkAMPM_text.setText("PM");
                }else{
                    checkAMPM_text.setText("AM");
                }
            }
        });
    }

    public void onclick(View v){
        String str;
        int hr_num,mn_num;
        pr_obj =(TextView)findViewById(R.id.Prs_id);
        pr_obj.setText(null);
        ans = (TextView)findViewById(R.id.ans_id);
        ehr = (EditText) findViewById(R.id.Hr_id);
        emn = (EditText) findViewById(R.id.Mn_id);
        est = (Button) findViewById(R.id.EST_id);
        cst = (Button) findViewById(R.id.CST_id);
        mst = (Button) findViewById(R.id.MST_id);
        pst = (Button) findViewById(R.id.PST_id);


        rst_text =(TextView) findViewById(R.id.result_id);

        if(ehr.getText().length()==0 || emn.getText().length()==0){
            Log.d("Demo", String.valueOf(ehr.getText().length()));
            Toast.makeText(getApplicationContext(),"Invalid Hr/Mn",Toast.LENGTH_LONG).show();
            clearall();
        }
        else {
            hr_num = Integer.valueOf(ehr.getText().toString());
            mn_num = Integer.valueOf(emn.getText().toString());
            if ((hr_num > 12) || (mn_num >= 60) ||(hr_num < 0) || (mn_num < 0)|| (hr_num == 0)) {
                Toast.makeText(getApplicationContext(), " Enter valid Hr/min", Toast.LENGTH_LONG).show();
                clearall();
                return;
            }
            if (v.getId() == R.id.EST_id) {
                calculation(hr_num, mn_num, 5);
                rst_text.setText("EST : ");
            }
            else if (v.getId() == R.id.CST_id) {
                calculation(hr_num, mn_num, 6);
                rst_text.setText("CST : ");
            }
            else if (v.getId() == R.id.MST_id) {
                calculation(hr_num, mn_num, 7);
                rst_text.setText("MST : ");
            }
            else if (v.getId() == R.id.PST_id) {
                calculation(hr_num, mn_num, 8);
                rst_text.setText("PST : ");
            }
        }
    }

    public void calculation(int hour, int min, int fact){

        String min_str = String.valueOf(min) ;

        if(min < 10){
        min_str = "0" + String.valueOf(min) ;
        }

        if(checkAMPM_text.getText().toString().equals("PM")) {
           if(hour == 12){
               hour = 0;
           }
            hour = hour + 12 - fact;
            if(hour < 12){
                ans.setText(String.valueOf(hour) + ":" + min_str+"AM");
            }
            else {
                hour = hour - 12;
                //ehr.setText(hour);
                ans.setText(String.valueOf(hour) + ":" + min_str+"PM");
            }
        } else {
            if(hour == 12){
                hour = 0;
            }
            hour = hour - fact;
            if(hour > 0){
                ans.setText(String.valueOf(hour) + ":" + min_str+"AM");
            }
            else {
                hour = hour + 12;
                ans.setText(String.valueOf(hour) + ":" + min_str+"PM");
                pr_obj.setText("Previous Day");
            }
        }
    }

public void onclear(View v){
    clearall();
}

public void clearall(){
    pr_obj.setText(null);
    ehr.setText(null);
    emn.setText(null);
    st.setChecked(false);
    ans.setText(null);
    rst_text.setText("Result :");
    checkAMPM_text.setText("AM");
}
}
