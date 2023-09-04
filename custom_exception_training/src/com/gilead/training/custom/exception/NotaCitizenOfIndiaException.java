package com.gilead.training.custom.exception;

public class NotaCitizenOfIndiaException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NotaCitizenOfIndiaException(String message) {
        super(message);
    }
}
