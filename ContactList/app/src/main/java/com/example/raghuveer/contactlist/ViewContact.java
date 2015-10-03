package com.example.raghuveer.contactlist;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Test;

import java.util.ArrayList;

public class ViewContact extends AppCompatActivity {

    TextView name,email,phonenumber;
    Button save;
    ArrayList<Contact> list;
    ImageButton menuleft,menuright,forward,backward;
    CharSequence[] item;
    int index;
    ImageView image_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        name = (TextView)findViewById(R.id.nametext_id);
        phonenumber = (TextView)findViewById(R.id.phonenumbertext_id);
        email = (TextView)findViewById(R.id.emailtext_id);
        image_upload =(ImageView)findViewById(R.id.image_upload);
        menuleft = (ImageButton)findViewById(R.id.menuleft_id);
        menuright= (ImageButton)findViewById(R.id.menuright_id);
        forward= (ImageButton)findViewById(R.id.fastforard_id);
        backward= (ImageButton)findViewById(R.id.backward_id);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon1_hw);

        index =0;
        if(getIntent().getParcelableArrayListExtra("Displaycontacts")!=null){
            list= getIntent().getParcelableArrayListExtra("Displaycontacts");
        }
        if(list.size()==0){
            name.setText("");
            phonenumber.setText("No contacts!!");
            email.setText("");
            phonenumber.setClickable(false);
            email.setClickable(false);
            /*menuright.setOnKeyListener(null);
            menuleft.setOnKeyListener(null);
            forward.setOnKeyListener(null);
            backward.setOnKeyListener(null);*/
        }
        else{
        display(index);
        }
    }

    public void onclickForward(View v){
        if(index <list.size()) {
            index = list.size() - 1;
            display(index);
        }
        }

    public void onclickBackward(View v){
        if(index < list.size()){
        index = 0;
        display(index);
        }
    }
    public void onclickMenuright(View v) {
        if (index < list.size()) {
            index++;
            if (index >= list.size()) {
                index = list.size() - 1;
            }
            display(index);
        }
    }


    public void onclickMenuleft(View v){
        if(index < list.size()){
        index--;
        if(index<0){
         index=0;
        }
        display(index);
    }
    }

    public void onclickFinish(View v){
        finish();
    }

    public void display(int index){

        Contact c;
        c = list.get(index);
        name.setText("Name :" + c.getName());
        phonenumber.setText("Phone #:" + c.getPhonenumber());
        email.setText("Email :" + c.getEmail());
        if(c.getImage()!=null) {
            Bitmap bmp = BitmapFactory.decodeFile(c.getImage());
            image_upload.setImageBitmap(bmp);
        }
        else{
            image_upload.setImageResource(R.drawable.avatarimage);
        }
    }

    public void makeCall(View v){
        String ph=phonenumber.getText().toString().split(":")[1];
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ph));
        startActivity(intent);
    }

    public void sendEmail(View v){
        String e=email.getText().toString().split(":")[1];
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",e, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}

