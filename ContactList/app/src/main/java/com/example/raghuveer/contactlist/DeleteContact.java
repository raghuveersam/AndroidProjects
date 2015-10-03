package com.example.raghuveer.contactlist;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteContact extends AppCompatActivity {

    EditText name,email,phonenumber;

    ArrayList<Contact> list;
    CharSequence[] item;
    Button delete;
    int index;
    ImageView image_upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        delete = (Button)findViewById(R.id.deletebutton_id);
        name =(EditText) findViewById(R.id.name_id);
        phonenumber = (EditText)findViewById(R.id.phonenumber_id);
        email = (EditText)findViewById(R.id.email_id);
        image_upload=(ImageView)findViewById(R.id.image_upload);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon1_hw);



        name.setKeyListener(null);
        phonenumber.setKeyListener(null);
        email.setKeyListener(null);
        delete.setEnabled(false);
        if(getIntent().getParcelableArrayListExtra("Deletecontacts")!=null){
            list= getIntent().getParcelableArrayListExtra("Deletecontacts");
        }
        item = new CharSequence[list.size()];
        int i=0;
        for(Contact c:list){
            item[i]= c.getName();
            i++;
        }
    }

  public void onclickDelete(View v){
      if(list.size() == 0){
          Toast.makeText(getApplicationContext(),"Contact list is empty",Toast.LENGTH_SHORT).show();
      }else {
          if (validateFields()) {       //

              list.remove(index);
              Intent i = new Intent();
              i.putParcelableArrayListExtra("Deletecontacts", list);
              setResult(RESULT_OK, i);
              finish();
          } else {
              return;
          }
      }
      }


    public void onclickSelectcontact(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteContact.this);
        builder.setTitle("Pick a contact").setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index=which;
                Contact c = list.get(which);
                name.setText(c.getName());
                email.setText(c.getEmail());
                phonenumber.setText(String.valueOf(c.getPhonenumber()));
                if(c.getImage()!=null) {
                    Bitmap bmp = BitmapFactory.decodeFile(c.getImage());
                    image_upload.setImageBitmap(bmp);
                }
            }
        });
        delete.setEnabled(true);
        builder.create().show();
    }
    public void onclickCancel(View v){
        finish();
    }
    public boolean validateFields() {

        if (name.getText().length() != 0 && email.getText().length() != 0 && phonenumber.getText().length() != 0) {

            if (phonenumber.getText().length() == 10) {
                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //}
        else {
            Toast.makeText(getApplicationContext(), "Mandatory Field missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
