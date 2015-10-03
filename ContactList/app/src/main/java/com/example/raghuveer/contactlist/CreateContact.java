package com.example.raghuveer.contactlist;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.regex.Pattern;

public class CreateContact extends AppCompatActivity {

    EditText name, email, phonenumber;
    Button save;
    ImageView image;
    String result;
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        name = (EditText) findViewById(R.id.name_id);
        email = (EditText) findViewById(R.id.email_id);
        phonenumber = (EditText) findViewById(R.id.phonenumber_id);
        image = (ImageView)findViewById(R.id.image_upload);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon1_hw);

    }

    //start
    public void uploadImage(View v) {
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
                    image.setImageBitmap(yourSelectedImage);
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



    public void onclicksave(View v) {

            if(validateFields()) {    //
                Contact contact = new Contact();

                contact.setName(name.getText().toString());
                contact.setEmail(email.getText().toString());
                contact.setPhonenumber(Long.parseLong(phonenumber.getText().toString()));
                contact.setImage(result);
                Intent intent = new Intent();
                intent.putExtra("createcontact", contact);
                setResult(RESULT_OK, intent);
                Toast.makeText(getApplicationContext(),"Saved Successfully !",Toast.LENGTH_SHORT).show();
                finish();
            }
        else{
                return;
            }
    }

    public boolean validateFields() {
        if (name.getText().length() != 0 && email.getText().length() != 0 && phonenumber.getText().length() != 0) {
            Log.d("Tag", "its working");
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
        } else {
            Toast.makeText(getApplicationContext(), "Mandatory Field missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void doCancel(View v){
        finish();
    }

}
