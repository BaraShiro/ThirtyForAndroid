package moe.barashiro.thirtyforandroid;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * A class for simulating a Thirty game.
 *
 * @author  Robert Rosborg
 * @version 1.5
 */
public class Game implements Parcelable {

    private D6[] mDices;
    private boolean[] mScoreRulesUsed;
    private int[] mScoreRules;
    private int mRerollsLeft;
    private int mRound;
    private boolean mGameOver;
    private int[] mScoresPerRule;
    private int[] mScoresPerRound;

    /**
     * Method for describing contents of the parcel.
     *
     * @return 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Creator for the Game class.
     */
    public static final Creator<Game> CREATOR = new Creator<Game>() {

        /**
         * Method for creating a Game object from a Parcel
         *
         * @param in The parcel to creaste the Game object from.
         * @return A new Game object created from the Parcel.
         */
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        /**
         * Method for creating an array of Game objects
         *
         * @param size The size of the return array.
         * @return An array of Game objects.
         */
        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    /**
     * Getter for the creator.
     *
     * @return The creator for the Game class.
     */
    public static Creator<Game> getCREATOR() {
        return CREATOR;
    }

    /**
     * Method for writing data to a Parcel.
     *
     * @param out The Parcel to write to.
     * @param flags Bit flags for the Parcel.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        // Instead of saving the dice we save the dice values.
        int[] diceValues = new int[6];
        for(int i = 0; i < 6; i++){
            diceValues[i] = mDices[i].getValue();
        }
        out.writeIntArray(diceValues);
        out.writeBooleanArray(mScoreRulesUsed);
        out.writeIntArray(mScoreRules);
        out.writeInt(mRerollsLeft);
        out.writeInt(mRound);
        out.writeInt(mGameOver ? 1 : 0); // Workaround for Parcel not having writeBoolean()
        out.writeIntArray(mScoresPerRule);
        out.writeIntArray(mScoresPerRound);
    }

    /**
     * Constructor for the Game class.
     *
     * @param in The parcel to create the Game object from.
     */
    public Game(Parcel in) {
        int[] diceValues = new int[6];
        in.readIntArray(diceValues);
        // We only saved the dice values so we need to recreate the dice.
        mDices = new D6[] {new D6(diceValues[0]), new D6(diceValues[1]), new D6(diceValues[2]),
                            new D6(diceValues[3]), new D6(diceValues[4]), new D6(diceValues[5])};
        in.readBooleanArray(mScoreRulesUsed);
        in.readIntArray(mScoreRules);
        mRerollsLeft = in.readInt();
        mRound = in.readInt();
        mGameOver = in.readInt() == 1;
        in.readIntArray(mScoresPerRule);
        in.readIntArray(mScoresPerRound);
    }

    /**
     * Constructor for the Game class.
     */
    public Game(){
        mDices = new D6[] {new D6(), new D6(), new D6(), new D6(), new D6(), new D6()};
        rerollAllDices(); // TODO: Fix so dices are rolled on creation
        mScoreRulesUsed = new boolean[] {false, false, false, false, false, false, false, false, false, false};
        mScoreRules = new int[] {3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        mRerollsLeft = 2;
        mRound = 1;
        mGameOver = false;
        mScoresPerRule = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        mScoresPerRound = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    /**
     * Getter for the dice values.
     *
     * @return An array with the values of the dice.
     */
    public int[] getDiceValues(){
        int[] values = new int[6];
        for (int i = 0; i < 6; i++) {
            values[i] = mDices[i].getValue();
        }
        return values;
    }

    /**
     * Getter for a specific dice value.
     *
     * @param dice The dice we get the value from.
     * @return The value of dice.
     */
    public  int getDiceValue(int dice){
        return mDices[dice].getValue();
    }

    /**
     * Getter for what score rules have been used.
     *
     * @return An array of booleans indicating what rules have been used.
     */
    public boolean[] getScoreRulesUsed(){
        return mScoreRulesUsed;
    }

    /**
     * Getter for the number of rerolls left.
     *
     * @return The number of rerolls left.
     */
    public int getRerollsLeft() {
        return mRerollsLeft;
    }

    /**
     * Setter for thenumber of rerolls left.
     * Sets the number of rerolls left to 0.
     */
    public void setRerollsToZero(){
        mRerollsLeft = 0;
    }

    /**
     * Getter for the game over state.
     *
     * @return A boolean indicating if the game is over.
     */
    public boolean gameOver(){
        return mGameOver;
    }

    /**
     * Getter for the scores sorted by rule.
     *
     * @return An array of scores sorted by rule.
     */
    public int[] getScoresPerRule() {
        return mScoresPerRule;
    }

    /**
     * Getter for the scores sorted by round.
     *
     * @return An array of scores sorted by round.
     */    public int[] getScoresPerRound() {
        return mScoresPerRound;
    }

    /**
     * Getter for round.
     *
     * @return The number of the current round.
     */
    public int getRound(){
        return mRound;
    }

    /**
     * Getter for the current score.
     *
     * @return The score for this round.
     */
    public  int getCurrentScore(){
        return mScoresPerRound[mRound - 1];
    }

    /**
     * Method for advancing the game one round.
     */
    public void nextRound(){
        if(!mGameOver) {
            mRound++; // Advance the round,
            mRerollsLeft = 2; // reset the rerolls,
            rerollAllDices(); // roll all dices,
            if (mRound >= 10) { // and check for game over state. // TODO: remove magical numbers
                mGameOver = true;
            }
        }
    }

    /**
     * Method for rerolling some dices.
     *
     * @param diceOne Indicates if the first dice should be rerolled or not.
     * @param diceTwo  Indicates if the second dice should be rerolled or not.
     * @param diceTree  Indicates if the third dice should be rerolled or not.
     * @param diceFour  Indicates if the fourth dice should be rerolled or not.
     * @param diceFive Indicates if the fifth dice should be rerolled or not.
     * @param diceSix Indicates if the sixth dice should be rerolled or not.
     */
    public void rerollDices(boolean diceOne, boolean diceTwo, boolean diceTree, // Inelegant but safe as compared to a boolean[]
                     boolean diceFour, boolean diceFive, boolean diceSix){
        boolean[] doRerolls = new boolean[]{diceOne, diceTwo, diceTree, diceFour, diceFive, diceSix};
        for(int i = 0; i < 6; i++){
            if(doRerolls[i]){ // Only reroll marked dice.
                mDices[i].roll();
            }
        }
        mRerollsLeft--;
    }

    /**
     * Method for rerolling all dice.
     */
    private void rerollAllDices(){
        for (D6 dice : mDices){
            dice.roll();
        }
    }

    /**
     * Method for calculating the score this round.
     *
     * @param ruleNumber The number of the rule to be used.
     * @return The score this round.
     */
    public int calculateScore(int ruleNumber){
        List<Integer> diceValueList = new LinkedList<>();

        for (D6 dice : mDices){
            diceValueList.add(dice.getValue());
        }
        int score = computeScore(mScoreRules[ruleNumber], diceValueList);
        mScoreRulesUsed[ruleNumber] = true; // Mark rule as used.
        mScoresPerRule[ruleNumber] = score; // Save the score.
        mScoresPerRound[mRound - 1] = score;
        return score;
    }

    /**
     * Method for computing the best score possible for a given set of dice.
     *
     * @param rule The number the dice need to add up to to award points.
     * @param intList A list of numbers representing the values of dice.
     * @return The best score possible.
     */
    private int computeScore(int rule, List<Integer> intList){
        if(rule < 4){ // We are using the Low rule,
            int out = 0;
            for(Integer diceValue : intList){ // So we simply add up all dice < 3.
                if(diceValue < 4){
                    out += diceValue;
                }
            }
            return out; // Return the score.

        }else{ // We are not using the Low rule.
            List<Integer> out = new LinkedList<>(); // Create a list to gather results in.

            for(int i = 0; i < intList.size(); i++){ // For all possible starting numbers,
                List<Integer> newList = new LinkedList<>(intList); // copy the list,
                int current = newList.get(i); // save the staring number,
                newList.remove(i); // and remove it from the list.

                out.add(computeScoreAux(current, newList, rule, 0)); // Add the maximum score for that branch in the results list.
            }
            return Collections.max(out); // Return the score from the branch with the highest score.
        }
    }

    /**
     * Helper method for computeScore().
     *
     * @param current The current dice value.
     * @param rest The rest of the dice values.
     * @param rule The number the dice need to add up to to award points.
     * @param score Accumulator for dice values.
     * @return The best score possible.
     */
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

            return Collections.max(out); // Return the score from the branch with the highest score.
        }
    }
}
