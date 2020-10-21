package io.finarkein.auth.util;

/**
 * Clock which can be used to get the amount of elapsed milliseconds in system time.
 */
public interface Clock {

    /**
     * Provides the default System implementation of a Clock by using {@link
     * System#currentTimeMillis()}.
     */
    Clock SYSTEM = System::currentTimeMillis;

    /**
     * Returns the current time in milliseconds since midnight, January 1, 1970 UTC, to match the
     * behavior of {@link System#currentTimeMillis()}.
     *
     * @return current time in milliseconds since epoch
     */
    long currentTimeMillis();

}
