package com.pinomg.determinator;

import java.io.Serializable;

/**
 * The question class. Holds all information about a question.
 */
public class Poll implements Serializable {
    public int id;
    public String question;
    public String alternativeOne;
    public String alternativeTwo;

    public Poll (String question, String a1, String a2) {
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
    }

    public Poll (int id, String question, String a1, String a2) {
        this.id = id;
        this.question = question;
        this.alternativeOne = a1;
        this.alternativeTwo = a2;
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
}

