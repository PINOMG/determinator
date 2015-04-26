package com.pinomg.determinator;

/**
 * The question class. Holds all information about a question.
 */
public class Question {
    public String question;
    public String answerOne;
    public String answerTwo;

    //This is required by the adapter for output in a list.
    public String toString(){
        return this.question;
    }
}
