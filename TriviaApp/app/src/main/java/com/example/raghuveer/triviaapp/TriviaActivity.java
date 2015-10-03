package com.example.raghuveer.triviaapp;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity {

    AlertDialog alertDialog;
    ArrayList<Questions> contentlist;
    ArrayList<Integer> selectedanswer,answersforresult;
    ProgressBar progressImage;
    ImageView imageloadingImage;
    TextView imageLoadingText;
    Questions quest;
    RadioGroup radiogroup;
    final static int RESULT_ID = 101;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        index = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        selectedanswer = new ArrayList<Integer>();
        answersforresult = new ArrayList<Integer>();

        AlertDialog.Builder builder = new AlertDialog.Builder(TriviaActivity.this);
        builder.setTitle("Loading Questions");

        LinearLayout linearLayout = new LinearLayout(TriviaActivity.this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(TriviaActivity.this);
        progressBar.setLayoutParams(layoutParams);

        TextView textView = new TextView(TriviaActivity.this);
        textView.setLayoutParams(layoutParams);
        textView.setText("Loading...");
        textView.setTextAppearance(TriviaActivity.this, android.R.style.TextAppearance_Large);

        linearLayout.addView(progressBar);
        linearLayout.addView(textView);

        builder = new AlertDialog.Builder(TriviaActivity.this);
        builder.setTitle("Loading...")
                .setView(linearLayout)
                .setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        RequestParams params = new RequestParams("GET", "http://dev.theappsdr.com/apis/trivia_fall15/getAll.php");
        new QuestionsGenerate().execute(params);

    }


    public void display() {

        Log.d("display",index+"");
        progressImage = (ProgressBar) findViewById(R.id.progress_id);
        imageLoadingText = (TextView) findViewById(R.id.LoadingImage_id);
        imageloadingImage = (ImageView) findViewById(R.id.image_id);
        radiogroup = (RadioGroup) findViewById(R.id.Radiogroup_id);


        progressImage.setVisibility(View.VISIBLE);
        imageLoadingText.setVisibility(View.VISIBLE);
        imageloadingImage.setVisibility(View.INVISIBLE);


        if(contentlist.get(index).getUrl().equals("")){
            imageloadingImage.setImageResource(R.drawable.noimage);
            progressImage.setVisibility(View.INVISIBLE);
            imageLoadingText.setVisibility(View.INVISIBLE);
            imageloadingImage.setVisibility(View.VISIBLE);
        }else{
            RequestParams params = new RequestParams("GET", contentlist.get(index).getUrl());
            new GetImage().execute(params);
        }

        TextView textview = (TextView) findViewById(R.id.questiontext_id);
        TextView question = (TextView) findViewById(R.id.question_id);

        int tempvarIndex = index + 1;
        textview.setText("Q" + tempvarIndex);
        Questions temp_quest = contentlist.get(index);
        question.setText(temp_quest.getQuestion());

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> temp = contentlist.get(index).getAnswer();
        radiogroup.removeAllViews();


        for (int i = 0; i < temp.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(temp.get(i));
            radioButton.setLayoutParams(layoutParams);
            radiogroup.addView(radioButton);
        }

    }


    public void OnClickQuit(View V){
        finish();
    }


    public void OnClickNext(View V){

        boolean selectedflag=false;
        int question_id = 0,answer_choice=0;
        for(int j=0;j<radiogroup.getChildCount();j++){
            if(((RadioButton)radiogroup.getChildAt(j)).isChecked()){
                Log.d("selected", j + "");
               answer_choice=j;
                selectedflag=true;
            }
        }
        if(!selectedflag){
            Log.d("selected", "100");
            selectedanswer.add(100);
        }


        question_id = contentlist.get(index).getQuestion_no();
        //answer_choice = selectedanswer.get(index);
        RequestParams params = new RequestParams("POST","http://dev.theappsdr.com/apis/trivia_fall15/checkAnswer.php");
        params.addParams("gid","72d692d32aaac450dbb05975535c3ef4");
        params.addParams("qid",String.valueOf(question_id));
        params.addParams("a", String.valueOf(answer_choice));
        Log.d("url",params.getEncodedUrl());
        new AnswerList().execute(params);




       /* else{
            Intent intent = new Intent(TriviaActivity.this,ResultsActivity.class);
            startActivityForResult(intent,RESULT_ID);
        } */
    }

    private class AnswerList extends AsyncTask<RequestParams,Void,Integer>{

        BufferedReader bufferedReader = null;
        int status=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if(result==0){
                Toast.makeText(getApplicationContext(),"Answer is incorrect",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Answer is correct",Toast.LENGTH_SHORT).show();
            }
            answersforresult.add(result);
            Log.d("array", answersforresult + "");
            if(index < contentlist.size()-1){
                index = index+1;
                display();
            }else {
                Intent intent = new Intent(TriviaActivity.this,ResultsActivity.class);
                intent.putIntegerArrayListExtra("Results", answersforresult);
                startActivity(intent);
                finish();
            }

        }
    }

    private class QuestionsGenerate extends AsyncTask<RequestParams, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(RequestParams... params) {

            //ArrayList<String> arraylist = null;
            try {
                HttpURLConnection con = params[0].setConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";

                ArrayList<String> arraylist = new ArrayList<String>();
                while ((line = bufferedReader.readLine()) != null) {
                    //Log.d("te", line);
                    arraylist.add(line);
                }
                return arraylist;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override

        protected void onPostExecute(ArrayList<String> arrayList) {

            contentlist = new ArrayList<>();
            for (String element : arrayList) {
                boolean check_cond = false;
                String[] temp = element.split(";", -1);
                ArrayList<String> answerlist = new ArrayList<>();

                int check = temp.length - 1;
                 quest = new Questions();

                Log.d("Line", element);
                if (!(check < 5 || check > 10)) {
                    if (temp[check].equals("") && (temp[check - 1].equals("") || (temp[check - 1].contains("http://")))) {
                        //Log.d("check","Valid question");
                        check_cond = true;
                        answerlist.clear();
                        for (int i = 2; i <= (check - 2); i++) {
                            answerlist.add(temp[i]);
                            Log.d("answer" + i, temp[i]);
                            if (temp[i].trim().equals("")) {
                                //Log.d("check","finalinValid question");
                                check_cond = false;
                            }
                        }
                    }
                }

                if (check_cond) {
                    Log.d("check", "valid question");
                    Log.d("Id", temp[0]);
                    Log.d("quest", temp[1]);
                    Log.d("Count", check + "");
                    Log.d("URL", temp[check - 1]);
                    //Log.d("Last element ", temp[check]);
                    quest.setQuestion_no(Integer.valueOf(temp[0]));
                    quest.setQuestion(temp[1]);
                    quest.setAnswer(answerlist);
                    quest.setUrl(temp[check - 1]);
                    contentlist.add(quest);
                    //Log.d("opt", contentlist.size() + "");
                    Log.d("THE FINAL", quest.toString());

                } else {
                    Log.d("check", "invalid question");
                }
            }
            alertDialog.dismiss();
        Log.d("opt",contentlist.size() + "");
        display();
        }
    }

    private class GetImage extends AsyncTask<RequestParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(RequestParams... params) {

            try {
                HttpURLConnection con = params[0].setConnection();
                Bitmap image = BitmapFactory.decodeStream(con.getInputStream());
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                ImageView iv = (ImageView) findViewById(R.id.image_id);
                iv.setImageBitmap(result);
            }
            progressImage.setVisibility(View.INVISIBLE);
            imageLoadingText.setVisibility(View.INVISIBLE);
            imageloadingImage.setVisibility(View.VISIBLE);

        }
    }


}
