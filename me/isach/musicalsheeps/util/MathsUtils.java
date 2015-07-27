package me.isach.musicalsheeps.util;

import java.util.Random;

/**
 * Created by sacha on 22/07/15.
 */
public class MathsUtils {

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    /**
     * Checks if a {@link java.lang.String} is an {@link java.lang.Integer}.
     *
     * @param supposedInteger the supposed {@link java.lang.Integer}.
     * @return {@code true} if the supposed integer is an {@link java.lang.Integer}, {@code false} otherwise.
     */
    public static boolean isInt(String supposedInteger) {
        try {
            Integer.parseInt(supposedInteger);
        } catch (Exception exc) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a {@link java.lang.Character} is an {@link java.lang.Integer}.
     *
     * @param supposedInteger the supposed {@link java.lang.Integer}.
     * @return {@code true} if the supposed integer is an {@link java.lang.Integer}, {@code false} otherwise.
     */
    public static boolean isInt(Character supposedInteger) {
        return isInt(String.valueOf(supposedInteger));
    }

}
