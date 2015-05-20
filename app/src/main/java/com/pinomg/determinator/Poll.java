package com.pinomg.determinator;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The question class. Holds all information about a question.
 */
public class Poll implements Serializable {


    public static int STATUS_PENDING  = 0; // Not viewed
    public static int STATUS_ANSWERED = 1; // Viewed and answered, no result yet
    public static int STATUS_FINISHED = 2; // Result is available
    public static int STATUS_ARCHIVED = 3; // Result viewed

    private static String[] TEXT_STATUS = {
            "Pending",
            "Answered",
            "Finished",
            "Archived"
         };

    private int id;
    public String question;
    public String alternativeOne;
    public String alternativeTwo;
    public LinkedList<Friend> friends;
    public int result; //Poll result
    public int answer; //Users answer

    public Poll (String question, String a1, String a2, LinkedList<Friend> friends) {
        this.friends = friends;
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
        this.result = 0;
    }

    public Poll (int id, String question, String a1, String a2, LinkedList<Friend> friends, int result, int answer) {
        this.friends = friends;
        this.id = id;
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
        this.result = result;
        this.answer = answer;
    }

    // TODO: Implement!
    public Integer getStatus() {
        if (this.result == 0 && this.answer == 0) {
            return STATUS_PENDING;
        } else if (this.result == 0 && this.answer > 0) {
            return STATUS_ANSWERED;
        } else
            return STATUS_FINISHED;
    }

    //This is required by the adapter for output in a list.
    public String toString(){
        return this.question;
    }


    //How to "un"-serialize an object from json-code
    public static Poll serialize (JSONObject json) throws JSONException {

        String alternative_one = json.getString("alternative_one");
        String alternative_two = json.getString("alternative_two");

        String question = json.getString("question");
        int id = json.getInt("id");
        int result = json.getInt("result");
        int answer = json.getInt("answer");

        Poll p = new Poll(id, question, alternative_one, alternative_two, null, result, answer);

        Log.d("Poll:", "Created poll " + id);
        return p;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else {
            Poll tmp = (Poll) o;
            return this.question.equals(tmp.question)
                    && this.alternativeOne.equals(tmp.alternativeOne)
                    && this.alternativeTwo.equals(tmp.alternativeTwo);
        }
    }

    @Override
    public int hashCode() {
        return (Integer.toString(id)).hashCode();
    }


    public static String getStatusText(int status) {
        return TEXT_STATUS[status];
    }
}

