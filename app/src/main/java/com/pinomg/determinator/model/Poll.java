package com.pinomg.determinator.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * Poll class,
 *
 * A Poll is a collection of a question, two answer alternatives, some answerers
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
    private LinkedList<User> answerers;
    private int result = 0; //Poll result
    private int answer; //Users answer

    /**
     * Simple constructor
     * @param question The question the poll asks.
     * @param alternativeOne The first alternative.
     * @param alternativeTwo The second alternative.
     */
    public Poll(String question, String alternativeOne, String alternativeTwo) {
        this.question = question;
        this.alternativeOne = alternativeOne;
        this.alternativeTwo = alternativeTwo;
    }

    /**
     * Deeper constructor, used by the api when fetching polls
     * @param id The id of the poll, used by the api
     * @param question The question the poll asks.
     * @param a1 The first alternative.
     * @param a2 The second alternative.
     * @param result The result of the poll, if finished: 1 or 2. If not finished: 0.
     * @param answer The users answer to the poll, if answered: 1 or 2. If not: 0.
     */

    public Poll (int id, String question, String a1, String a2, int result, int answer) {
        this.id = id;
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
        this.result = result;
        this.answer = answer;
    }

    /**
     * Even deeper constructor, allowing to add the answerers in the poll object.
     * @param id The id of the poll, used by the api
     * @param question The question the poll asks.
     * @param a1 The first alternative.
     * @param a2 The second alternative.
     * @param answerers A list of users the poll is sent to.
     * @param result The result of the poll, if finished: 1 or 2. If not finished: 0.
     * @param answer The users answer to the poll, if answered: 1 or 2. If not: 0.
     */
    public Poll (int id, String question, String a1, String a2, LinkedList<User> answerers, int result, int answer) {
        this.answerers = answerers;
        this.id = id;
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
        this.result = result;
        this.answer = answer;
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
     * @param question The question the poll asks.
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
     * @param text The alternative one string
     */
    public void setAlternativeOne(String text) {
        this.alternativeOne = text;
    }

    /**
     * Getter for alternative one
     * @return The Poll's alternative one
     */
    public String getAlternativeOne() {
        return alternativeOne;
    }

    /**
     * Setter for alternative two.
     * @param text The alternative two string
     */
    public void setAlternativeTwo(String text) {
        this.alternativeTwo = text;
    }

    /**
     * Getter for alternative two
     * @return The Poll's alternative two
     */
    public String getAlternativeTwo() {
        return alternativeTwo;
    }

    /**
     * Sets what the logged in user answered
     * @param answer 1 or 2, depending which alternative chosen.
     */
    public void setAnswer(int answer) {
        if(result == 1 || result == 2) {
            throw new IllegalArgumentException("Result must be 1 or 2!");
        } else {
            this.answer = answer;
        }
    }

    /**
     * @return Users answer to polls question. 1 or 2.
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
     * @param answerers list of users the poll is sent to.
     */
    public void setAnswerers(LinkedList<User> answerers) {
        if(answerers == null) {
            throw new IllegalArgumentException("Answerers can't be set to null pointer!");
        } else {
            this.answerers = answerers;
        }
    }

    /**
     * @return List of answerers
     */
    public List<User> getAnswerers() {
        if ( this.answerers != null )
            return new LinkedList<>(answerers);
        else
            return new LinkedList<>();
    }

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
        // TODO: Better implementation with respect to server id, answerers etc.
        return (question + alternativeOne + alternativeTwo).hashCode();
    }


    public static String getStatusText(int status) {
        return TEXT_STATUS[status];
    }
}

