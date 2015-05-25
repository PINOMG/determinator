package com.pinomg.determinator.model;

import junit.framework.TestCase;

/**
 * Created by olle on 25/05/15.
 */
public class PollTest extends TestCase {

    private final String DEFAULT_QUESTION = "question";
    private final String DEFAULT_ALT_1 = "alt1";
    private final String DEFAULT_ALT_2 = "alt2";

    private Poll poll;

    public void setUp() throws Exception {
        super.setUp();
        poll = new Poll(DEFAULT_QUESTION, DEFAULT_ALT_1, DEFAULT_ALT_2);
    }

    /**
     * Test getting a poll's question
     */
    public void testGetQuestion() {
        assertEquals(poll.getQuestion(), DEFAULT_QUESTION);
    }

    /**
     * Test setting a poll's question
     */
    public void testSetQuestion() {
        poll.setQuestion("Changed");
        assertEquals(poll.getQuestion(),"Changed");
    }

    /**
     * Test getting a poll's alternative one
     */
    public void testGetAlt1() {
        assertEquals(poll.getAlternativeOne(), DEFAULT_ALT_1);
    }

    /**
     * Test setting a poll's alternative one
     */
    public void testSetAlt1() {
        poll.setAlternativeOne("changed alt1");
        assertEquals(poll.getAlternativeOne(), "changed alt1");
    }

    /**
     * Test getting a poll's alternative two
     */
    public void testGetAlt2() {
        assertEquals(poll.getAlternativeTwo(), DEFAULT_ALT_2);
    }

    /**
     * Test setting a poll's alternative two
     */
    public void testSetAlt2() {
        poll.setAlternativeTwo("changed alt2");
        assertEquals(poll.getAlternativeTwo(), "changed alt2");
    }

    /**
     * Test a poll's status
     */
    public void testGetStatus() {

        // Should be pending when first created
        assertEquals((int) poll.getStatus(), Poll.STATUS_PENDING);

        // After answered it should be answered
        poll.setAnswer(1);
        assertEquals((int) poll.getStatus(), Poll.STATUS_ANSWERED);

        // After the result has been set it should be finished
        poll.setResult(1);
        assertEquals((int) poll.getStatus(), Poll.STATUS_FINISHED);

    }

    /**
     * Test setting a poll's result
     */
    public void testSetResult() {
        try {
            poll.setResult(3);
            fail("Should have thrown IllegalArgumentException!");
        } catch(IllegalArgumentException e) {
            poll.setResult(1);
            assertEquals(poll.getResult(), 1);
        }
    }
}