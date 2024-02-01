package com.movietone;

/**
 * Signals that cycle is detected in a graph
 */
public class CycleDetectedException extends Exception {

    // exception message
    private static final String EXCEPTION_MESSAGE = "Cycle Detected!";

    public CycleDetectedException() {
        super(EXCEPTION_MESSAGE);
    }

}
