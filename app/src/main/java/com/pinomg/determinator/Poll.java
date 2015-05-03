package com.pinomg.determinator;

/**
 * The question class. Holds all information about a question.
 */
public class Poll {
    public String question;
    public String alternativeOne;
    public String alternativeTwo;

    public Poll (String question, String a1, String a2) {
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
    }

    //This is required by the adapter for output in a list.
    public String toString(){
        return this.question;
    }
}

