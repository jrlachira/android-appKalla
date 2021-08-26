package org.kalla.enterprise.movil.cron;

interface ValueMatcher {

    /**
     * Validate the given integer value against a set of rules.
     *
     * @param value
     *            The value.
     * @return true if the given value matches the rules of the ValueMatcher,
     *         false otherwise.
     */
    public boolean match(int value);

}
