package org.example;

public class FetchException extends RuntimeException {

    public FetchException(Exception cause) {
        super(cause);
    }
}
