package com.example.raghuveer.contactlist;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditContact extends AppCompatActivity {

    EditText name, email, phonenumber;
    Button save;
    ArrayList<Contact> list;
    CharSequence[] item;
    int index;
    ImageView image_upload;
    String result,imageTemp;
    boolean image_edited=false,flag=false;
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);


        name = (EditText) findViewById(R.id.name_id);
        phonenumber = (EditText) findViewById(R.id.phonenumber_id);
        email = (EditText) findViewById(R.id.email_id);
        image_upload = (ImageView) findViewById(R.id.image_upload);

        name.setKeyListener(null);
        phonenumber.setKeyListener(null);
        email.setKeyListener(null);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon1_hw);

        if (getIntent().getParcelableArrayListExtra("Contacts") != null) {
            list = getIntent().getParcelableArrayListExtra("Contacts");
        }
        item = new CharSequence[list.size()];
        int i = 0;
        for (Contact c : list) {
            item[i] = c.getName();
            i++;
        }
    }

    public void onclickselectcontact(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditContact.this);
        builder.setTitle("Pick a contact").setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;
                Contact c = list.get(which);
                name.setText(c.getName());
                email.setText(c.getEmail());
                phonenumber.setText(String.valueOf(c.getPhonenumber()));

                if(c.getImage()!=null) {
                    imageTemp =c.getImage();
                    Bitmap bmp = BitmapFactory.decodeFile(c.getImage());
                    image_upload.setImageBitmap(bmp);
                }
            }

        });

        name.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
        email.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
        phonenumber.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
        phonenumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.create().show();
    }

    public void onClickEdit(View v) {

        if (validateFields()) {

            Contact c = new Contact();
            c.setName(name.getText().toString());
            c.setPhonenumber(Long.valueOf(phonenumber.getText().toString()));
            c.setEmail(email.getText().toString());
            if(image_edited){
                    c.setImage(result);
                }else{
                    c.setImage(imageTemp);
                }
            list.set(index, c);
            Intent i = new Intent();
            i.putParcelableArrayListExtra("EditedContacts", list);
            setResult(RESULT_OK, i);
            Toast.makeText(getApplicationContext(),"Edited Successfully !",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            return;
        }
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

    public void uploadImage(View v) {
        image_edited=true;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    getRealPathFromURI(selectedImage);
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    image_upload.setImageBitmap(yourSelectedImage);
                }
        }
    }

    private void getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
        cursor.close();
    }
    //end

    public void doCancel(View v){
        finish();
    }
}
