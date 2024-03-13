package com.ec.viamatica.exceptions;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException(String msg) {
        super(msg);
    }
}
