package moe.barashiro.thirtyforandroid;

import java.util.Random;
/**
 * A class for simulating a six sided dice.
 *
 * @author  Robert Rosborg
 * @version 1.1
 */
public class D6 {
    private int mValue;
    private Random mRandom;

    /**
     * Constructor for D6.
     *
     * @param value The value to assign to the new D6.
     */
    public D6(int  value){
        mValue = value;
        mRandom = new Random();
    }

    /**
     * Constructor for D6.
     * Sets mValue to 1.
     */
    public D6(){
        mValue = 1; // TODO: Rand a number instead.
        mRandom = new Random();
    }

    /**
     * Method for rolling the D6.
     * Sets the value to a random int between 1 and 6.
     */
    public void roll(){
        mValue = mRandom.nextInt(6) + 1; // Get an int between 0 + 1 and 5 + 1.
    }

    /**
     * Getter for value.
     *
     * @return The value of the D6.
     */
    public int getValue(){
        return mValue;
    }
}
