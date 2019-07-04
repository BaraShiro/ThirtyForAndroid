package moe.barashiro.thirtyforandroid;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Game implements Parcelable {

    private D6[] mDices;
    private boolean[] mScoreRulesUsed;
    private int[] mScoreRules;
    private int mRerollsLeft;
    private int mRound;
    private boolean mGameOver;
    private int[] mScoresPerRule;
    private int[] mScoresPerRound;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public static Creator<Game> getCREATOR() {
        return CREATOR;
    }
    
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // save dice ints
        parcel.writeBooleanArray(mScoreRulesUsed);
        parcel.writeIntArray(mScoreRules);
        parcel.writeInt(mRerollsLeft);
        parcel.writeInt(mRound);
        parcel.writeInt(mGameOver ? 1 : 0); // Workaround for Parcel not having writeBoolean()
        parcel.writeIntArray(mScoresPerRule);
        parcel.writeIntArray(mScoresPerRound);


    }

    public Game(Parcel in) {
        // load dice ints
        mDices = new D6[] {new D6(), new D6(), new D6(), new D6(), new D6(), new D6()};
        rerollAllDices();
        in.readBooleanArray(mScoreRulesUsed);
        in.readIntArray(mScoreRules);
        mRerollsLeft = in.readInt();
        mRound = in.readInt();
        mGameOver = in.readInt() == 1;
        in.readIntArray(mScoresPerRule);
        in.readIntArray(mScoresPerRound);
    }

    public Game(){
        mDices = new D6[] {new D6(), new D6(), new D6(), new D6(), new D6(), new D6()};
        rerollAllDices();
        mScoreRulesUsed = new boolean[] {false, false, false, false, false, false, false, false, false, false};
        mScoreRules = new int[] {3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        mRerollsLeft = 2;
        mRound = 1;
        mGameOver = false;
        mScoresPerRule = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        mScoresPerRound = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public int[] getDiceValues(){
        int[] values = new int[6];
        for (int i = 0; i < 6; i++) {
            values[i] = mDices[i].getValue();
        }
        return values;
    }

    public  int getDiceValue(int dice){
        return mDices[dice].getValue();
    }

    public boolean[] getScoreRulesUsed(){
        return mScoreRulesUsed;
    }

    public int getRerollsLeft() {
        return mRerollsLeft;
    }

    public void setRerollsToZero(){
        mRerollsLeft = 0;
    }

    public boolean gameOver(){
        return mGameOver;
    }

    public int[] getScoresPerRule() {
        return mScoresPerRule;
    }

    public int[] getScoresPerRound() {
        return mScoresPerRound;
    }

    public int getRound(){
        return mRound;
    }

    public  int getCurrentScore(){
        return mScoresPerRound[mRound - 1];
    }

    public void nextRound(){
        if(!mGameOver) {
            mRound++;
            mRerollsLeft = 2;
            rerollAllDices();
            if (mRound >= 10) {
                mGameOver = true;
            }
        }
    }

    public void rerollDices(boolean diceOne, boolean diceTwo, boolean diceTree, // Inelegant but safe as compared to a boolean[]
                     boolean diceFour, boolean diceFive,boolean diceSix){
        boolean[] doRerolls = new boolean[]{diceOne, diceTwo, diceTree, diceFour, diceFive, diceSix};
        for(int i = 0; i < 6; i++){
            if(doRerolls[i]){
                mDices[i].roll();
            }
        }
        mRerollsLeft--;
    }

    private void rerollAllDices(){
        for (D6 dice : mDices){
            dice.roll();
        }
    }

    public int calculateScore(int ruleNumber){
        List<Integer> diceValueList = new LinkedList<>();

        for (D6 dice : mDices){
            diceValueList.add(dice.getValue());
        }
        int score = computeScore(mScoreRules[ruleNumber], diceValueList);
        mScoreRulesUsed[ruleNumber] = true;
        mScoresPerRule[ruleNumber] = score;
        mScoresPerRound[mRound - 1] = score;
        return score;
    }

    private int computeScore(int rule, List<Integer> intList){
        if(rule < 4){
            int out = 0;
            for(Integer i : intList){
                if(i < 4){
                    out += i;
                }
            }
            return out;

        }else{
            List<Integer> out = new LinkedList<>();

            for(int i = 0; i < intList.size(); i++){
                List<Integer> newList = new LinkedList<>(intList);
                int current = newList.get(i);
                newList.remove(i);

                out.add(computeScoreAux(current, newList, rule, 0));
            }
            return Collections.max(out);
        }
    }

    private int computeScoreAux(int current, List<Integer> rest, int rule, int score){
        int savedScore = 0; // The score awarded this iteration.
        int newScore = current + score; // Partial score reached so far.

        if(newScore > rule){ // If we overshoot,
            return 0; // stop exploring this branch.
        }
        else if(newScore == rule){ // If we hit the mark,
            savedScore = newScore; // we are awarded the score,
            score = 0; // and we reset the partial score.
        }
        else{ // If we undershoot,
            score = newScore; // save the partial for next iteration.
        }

        if(rest.size() == 0){ // If we are on the last iteration,
            return savedScore; // return the score awarded this iteration.
        }
        else { // If we are not on the last Iteration.
            List<Integer> out = new LinkedList<>(); // Out list to keep scores.

            for (int i = 0; i < rest.size(); i++) { // For each element in the rest list,
                List<Integer> newRest = new LinkedList<>(rest); // copy the list,
                int newCurrent = newRest.get(i); //  save the element,
                newRest.remove(i); // and remove it from the copy.
                // Create a new branch for all remaining iterations,
                // and add the score of that branch plus the score awarded this iteration to the out list.
                out.add(computeScoreAux(newCurrent, newRest, rule, score) + savedScore);
            }

            return Collections.max(out); // Return the branch with the highest score.
        }
    }
}
