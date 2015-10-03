package com.example.raghuveer.contactlist;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ArrayList<Contact> list = new ArrayList<Contact>();
    public static final int CREATE_REQUESTID = 100;
    public static final int EDIT_REQUESTID = 101;
    public static final int DELETE_REQUESTID = 102;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon1_hw);

        findViewById(R.id.createcontact_id).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateContact.class);
                startActivityForResult(intent, CREATE_REQUESTID);
            }
        });

        findViewById(R.id.editcontact_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditContact.class);
                intent.putParcelableArrayListExtra("Contacts",list);
                startActivityForResult(intent, EDIT_REQUESTID);
            }
        });

        findViewById(R.id.deleteconatact_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteContact.class);
                intent.putParcelableArrayListExtra("Deletecontacts",list);
                startActivityForResult(intent,DELETE_REQUESTID);
            }
        });

        findViewById(R.id.displaycontact_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewContact.class);
                intent.putParcelableArrayListExtra("Displaycontacts",list);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_REQUESTID) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().getParcelable("createcontact") != null) {
                    Contact contact = data.getExtras().getParcelable("createcontact");
                    list.add(contact);
                }
            }
        }
        if(requestCode==EDIT_REQUESTID){
            if(resultCode==RESULT_OK){
                if (data.getExtras().getParcelableArrayList("EditedContacts")!=null){
                    list = data.getExtras().getParcelableArrayList("EditedContacts");
                }
            }
        }
        if(requestCode==DELETE_REQUESTID){
            if(resultCode==RESULT_OK){
                if (data.getExtras().getParcelableArrayList("Deletecontacts")!=null){
                    list = data.getExtras().getParcelableArrayList("Deletecontacts");

                }
            }
        }
        Collections.sort(list, new Comparator<Contact>() {
            public int compare(Contact v1, Contact v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        Log.d("finalsortedarray",list+"");
    }

    public void doCancel(View v){
        finish();
    }
}

