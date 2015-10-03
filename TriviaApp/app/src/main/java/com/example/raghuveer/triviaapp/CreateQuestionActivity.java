package com.example.raghuveer.triviaapp;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

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

import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStreamReader;

public class CreateQuestionActivity extends AppCompatActivity {
    int answer = 0;
    EditText question;
    EditText option;
    RadioGroup rg;
    int count = 1;
    ImageView image;
    ArrayList<String> options_answers = new ArrayList<>();
    ProgressDialog dialog;
    private static final int SELECT_PHOTO = 100;
    String result;
    boolean image_changed = false;
    ProgressDialog dialog1;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        question = (EditText) findViewById(R.id.question);
        option = (EditText) findViewById(R.id.option);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        image = (ImageView) findViewById(R.id.image);
    }


    public boolean doValidation() {

        if (question.getText().length() > 0) {
            if (rg.getChildCount() > 2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public void doAddOption(View v) {

        if (option.getText().length() > 0) {
            RadioButton radioButton = new RadioButton(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(option.getText());
            options_answers.add(option.getText().toString());

            rg.addView(radioButton);
            if (count == 1) {
                radioButton.setChecked(true);
                count++;
            }
            option.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Enter valid option", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void onSubmit(View v) {


        if (doValidation()) {

            for (int i = 0; i < rg.getChildCount(); i++) {
                if (((RadioButton) rg.getChildAt(i)).isChecked()) {
                    answer = i;
                    break;
                }
            }

            if (image_changed) {
                new ImageUpload().execute(result);


            } else {
                RequestParams params = new RequestParams("POST", "http://dev.theappsdr.com/apis/trivia_fall15/saveNew.php");
                params.addParams("gid", "72d692d32aaac450dbb05975535c3ef4");
                params.addParams("q", params.createQuestion(question.getText().toString(), "", options_answers, answer));
                new CreateQuestion().execute(params);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Enter valid question and options", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private class CreateQuestion extends AsyncTask<RequestParams, Void, Integer> {

        BufferedReader reader = null;


        @Override
        protected Integer doInBackground(RequestParams... params) {
            try {
                HttpURLConnection con = params[0].setConnection();
                String line = "";
                int status = 0;
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                if ((line = reader.readLine()) != null) {
                    status = Integer.valueOf(line);
                }
                return status;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dialog1.dismiss();
            Log.d("output", integer + "");
           finish();
        }
    }

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
                    image_changed = true;
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


    private class ImageUpload extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog1 = new ProgressDialog(CreateQuestionActivity.this);
            dialog1.setMessage("Sending Image and Question");
            dialog1.setCancelable(false);
            dialog1.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog1.dismiss();
            url =s;
            RequestParams params = new RequestParams("POST", "http://dev.theappsdr.com/apis/trivia_fall15/saveNew.php");
            params.addParams("gid", "72d692d32aaac450dbb05975535c3ef4");
            params.addParams("q", params.createQuestion(question.getText().toString(), url, options_answers, answer));
            new CreateQuestion().execute(params);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost("http://dev.theappsdr.com/apis/trivia_fall15/uploadPhoto.php");
                File file = new File(params[0]);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                FileBody contentFile = new FileBody(file);

                entity.addPart("uploaded_file", contentFile);

                postMethod.setEntity(entity);
                client.execute(postMethod);

                HttpResponse response = client.execute(postMethod);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                Log.d("url", s.toString());
                return s.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
