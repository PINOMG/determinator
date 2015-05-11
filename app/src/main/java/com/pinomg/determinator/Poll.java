package com.pinomg.determinator;

import java.io.Serializable;

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
            "Answerd",
            "Finished",
            "Archived"
         };

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

    // TODO: Implement!
    public Integer getStatus() {
        return STATUS_PENDING;
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

