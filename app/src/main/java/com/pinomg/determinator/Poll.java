package com.pinomg.determinator;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Poll class,
 *
 * A Poll is a collection of a question, two answer alternatives, some anwserers
 * and maybe a result. A Poll can have four different statuses:
 *  1. PENDING - Incoming poll which the user haven't viewed
 *  2. ANSWERED - Poll viewed and answered by the user, but no result yet
 *  3. FINISHED - Result is available but haven't been viewed by user
 *  4. ARCHIVED - Polls answer been viewed by user, and now nothing more can happen with the poll.
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

    private Integer id;
    private String question;
    private String alternativeOne;
    private String alternativeTwo;
    private ArrayList<User> answerers; // TODO: Maybe implement with set instead of ArrayList?
    private int result = 0; //Poll result
    private int answer; //Users answer

    /**
     * Simple constructor
     * @param question
     * @param alternativeOne
     * @param alternativeTwo
     */
    public Poll(String question, String alternativeOne, String alternativeTwo) {
        this.question = question;
        this.alternativeOne = alternativeOne;
        this.alternativeTwo = alternativeTwo;
    }

    public Poll(Integer id, String question, String alternativeOne, String alternativeTwo) {
        this.id = id;
        this.question = question;
        this.alternativeOne = alternativeOne;
        this.alternativeTwo = alternativeTwo;
    }

    public Poll(String question, String alternativeOne, String alternativeTwo, List<User> answerers) {
        this.question = question;
        this.alternativeOne = alternativeOne;
        this.alternativeTwo = alternativeTwo;
        setAnswerers(answerers);
    }

    public Poll(Integer id, String question, String alternativeOne, String alternativeTwo, List<User> answerers) {
        this.id = id;
        this.question = question;
        this.alternativeOne = alternativeOne;
        this.alternativeTwo = alternativeTwo;
        setAnswerers(answerers);
    }

    /**
     * Returns polls server id
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for question
     * @param question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Getter for question
     * @return Polls question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setter for alternative one.
     * @param text
     */
    public void setAlternativeOne(String text) {
        this.alternativeOne = text;
    }

    /**
     * Getter for alternative one
     * @return Polls alternative one
     */
    public String getAlternativeOne() {
        return alternativeOne;
    }

    /**
     * Setter for alternative two.
     * @param text
     */
    public void setAlternativeTwo(String text) {
        this.alternativeTwo = text;
    }

    /**
     * Getter for alternative two
     * @return Polls alternative two
     */
    public String getAlternativeTwo() {
        return alternativeTwo;
    }

    /**
     * Sets what the logged in user answered
     * @param answer
     */
    public void setAnswer(int answer) {
        if(result == 1 || result == 2) {
            throw new IllegalArgumentException("Result must be 1 or 2!");
        } else {
            this.answer = answer;
        }
    }

    /**
     * @return Users answer to polls question
     */
    public int getAnswer() {
        return answer;
    }

    /**
     * Setter for polls result
     * @param result 0 - no result, 1 - first alternative, 2 - second alternative
     */
    public void setResult(int result) {
        if(result < 0 || result > 2) {
            throw new IllegalArgumentException("Result must be 0,1 or 2!");
        } else {
            this.result = result;
        }
    }

    /**
     * @return Polls result int, 0,1 or 2. See setResult for details
     */
    public int getResult() {
        return result;
    }

    /**
     * Returns the text for the winning alternative or null if no result are in yet.
     * @return Text of winning alternative or null
     */
    public String getResultText() {
        if(result == 1) {
            return alternativeOne;
        } else if(result == 2) {
            return alternativeTwo;
        } else {
            return null;
        }
    }


    /**
     * Sets list of answerers
     * @param answerers
     */
    public void setAnswerers(List<User> answerers) {
        if(answerers == null) {
            throw new IllegalArgumentException("Answerers can't be set to null pointer!");
        } else {
            this.answerers = new ArrayList<>(answerers);
        }
    }

    /**
     * @return List of answerers
     */
    public List<User> getAnswerers() {
        return new ArrayList<>(answerers);
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

    public String getResult() {
        if(result == 1) {
            return alternativeOne;
        } else if(result == 2) {
            return alternativeTwo;
        } else {
            return null;
        }
    }

    //This is required by the adapter for output in a list.
    public String toString(){
        return this.question;
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
        // TODO: Not ideal implementation..
        return (question + alternativeOne + alternativeTwo).hashCode();
    }


    public static String getStatusText(int status) {
        return TEXT_STATUS[status];
    }
}

