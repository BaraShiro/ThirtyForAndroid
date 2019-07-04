package moe.barashiro.thirtyforandroid;

import java.util.Random;

public class D6 {
    private int mValue;
    private Random mRandom;

    public D6(int  value){
        mValue = value;
        mRandom = new Random();
    }

    public D6(){
        mValue = 1;
        mRandom = new Random();
    }

    public void roll(){
        mValue = mRandom.nextInt(6) + 1; // Get an int between 0 + 1 and 5 + 1.
    }

    public int getValue(){
        return mValue;
    }
}
