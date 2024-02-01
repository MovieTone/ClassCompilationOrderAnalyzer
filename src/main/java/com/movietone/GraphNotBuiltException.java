package com.movietone;

/**
 * Signals that graph is not yet built
 */
public class GraphNotBuiltException extends Exception {

    // exception message
    private static final String EXCEPTION_MESSAGE = "Build The main.java.com.movietone.Graph First";

    public GraphNotBuiltException() {
        super(EXCEPTION_MESSAGE);
    }

}
