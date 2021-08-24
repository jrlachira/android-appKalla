package org.kalla.enterprise.movil.cron;

class AlwaysTrueValueMatcher implements ValueMatcher {

    /**
     * Always true!
     */
    public boolean match(int value) {
        return true;
    }

}
