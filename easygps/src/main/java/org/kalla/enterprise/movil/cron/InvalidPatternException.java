package org.kalla.enterprise.movil.cron;

public class InvalidPatternException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Package-reserved construction.
     */
    InvalidPatternException() {
    }

    /**
     * Package-reserved construction.
     */
    InvalidPatternException(String message) {
        super(message);
    }

}
