package com.movietone;

/**
 * Signals that class name given is not valid
 */
public class InvalidClassNameException extends Exception {

    // exception message
    private static final String EXCEPTION_MESSAGE = "Class Name is Invalid!";

    public InvalidClassNameException() {
        super(EXCEPTION_MESSAGE);
    }

}
