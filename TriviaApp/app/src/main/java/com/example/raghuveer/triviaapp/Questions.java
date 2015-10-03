package com.example.raghuveer.triviaapp;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import java.util.ArrayList;

/**
 * Created by Raghuveer on 9/27/2015.
 */
public class Questions {
    int question_no;
    String question;
    ArrayList<String> answer;
    String url;

    public void setQuestion_no(int question_no) {
        this.question_no = question_no;
    }

    public Questions(){
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getQuestion_no() {
        return question_no;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public String toString() {
        return "Questions{" +
                "question_no=" + question_no +
                ", question='" + question + '\'' +
                ", answer=" + answer +
                ", url='" + url + '\'' +
                '}';
    }
}
