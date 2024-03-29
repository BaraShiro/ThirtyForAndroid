package moe.barashiro.thirtyforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Activity for the game.
 * Starts the final score activity.
 *
 * @author  Robert Rosborg
 * @version 1.7
 */
public class GameActivity extends AppCompatActivity {
    private static final String SAVED_GAME_STATE = "savedGameState";
    private static final String SAVED_ROUND_STATE = "savedRoundState";

    private SparseIntArray mWhiteDices;
    private SparseIntArray mRedDices;
    private Map<String, Integer> mRuleNames;
    private boolean[] mDiceMarkedForReroll;
    private Game mGame;
    private boolean mRoundPlayed;

    private ImageButton[] mDiceButtons;
    private TextView mRerollText;
    private Button mRerollButton;
    private TextView mScoreText;
    private Spinner mRuleSpinner;
    private Button mCalculateButton;
    private TextView mRoundText;
    private Button mRoundButton;

    /**
     * Method for
     *
     * @param
     * @return
     */
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }

    /**
     * Method for
     *
     * @param
     * @return
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(SAVED_GAME_STATE, mGame);
        savedInstanceState.putBoolean(SAVED_ROUND_STATE, mRoundPlayed);
    }

    /**
     * Method for creating the activity.
     *
     * @param savedInstanceState A Bundle containing a saved state or null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize mappings.
        initDiceGraphics();
        initRuleNames();

        mGame = new Game();
        mRoundPlayed = false;
        if (savedInstanceState != null) {
            mGame = savedInstanceState.getParcelable(SAVED_GAME_STATE);
            mRoundPlayed = savedInstanceState.getBoolean(SAVED_ROUND_STATE);
        }

        // Set all buttons and text views.
        mRerollButton = (Button) findViewById(R.id.rerollButton);
        mRerollText = (TextView) findViewById(R.id.rerollText);
        mScoreText = (TextView) findViewById(R.id.scoreText);
        mRuleSpinner = (Spinner) findViewById(R.id.ruleSpinner);
        mCalculateButton = (Button) findViewById(R.id.calculateButton);
        mRoundText = (TextView) findViewById(R.id.roundText);
        mRoundButton = (Button) findViewById(R.id.roundButton);
        mDiceButtons = new ImageButton[] {
                (ImageButton) findViewById(R.id.diceOneButton),
                (ImageButton) findViewById(R.id.diceTwoButton),
                (ImageButton) findViewById(R.id.diceThreeButton),
                (ImageButton) findViewById(R.id.diceFourButton),
                (ImageButton) findViewById(R.id.diceFiveButton),
                (ImageButton) findViewById(R.id.diceSixButton)
        };

        // Marked dice are reset on loading a saved instance, this is a design choice.
        mDiceMarkedForReroll = new boolean[] {false, false, false, false, false, false};
        mRerollButton.setEnabled(false);

        // Update everything.
        updateCalcAndRoundButtons();
        updateAllDice();
        updateRerollText();
        updateScoreText();
        updateRoundText();
        updateRuleSpinner();

        // Set all listeners.
        mDiceButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDice(0);
            }
        });

        mDiceButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDice(1);
            }
        });

        mDiceButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDice(2);
            }
        });

        mDiceButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDice(3);
            }
        });

        mDiceButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDice(4);
            }
        });

        mDiceButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDice(5);
            }
        });

        mRerollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.rerollDices(mDiceMarkedForReroll[0], mDiceMarkedForReroll[1], mDiceMarkedForReroll[2], mDiceMarkedForReroll[3], mDiceMarkedForReroll[4], mDiceMarkedForReroll[5]);
                updateAllDice();
                updateRerollText();
                mRerollButton.setEnabled(false);
            }
        });

        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ruleName = String.valueOf(mRuleSpinner.getSelectedItem());
                int ruleNumber = mRuleNames.get(ruleName);
                int _score = mGame.calculateScore(ruleNumber); //  Variable _score never actually used, the score is kept in the Game object.
                mRoundPlayed = true;
                mGame.setRerollsToZero();
                mRerollButton.setEnabled(false);
                updateRerollText();
                updateScoreText();
                updateAllDice();
                updateCalcAndRoundButtons();
            }
        });

        mRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGame.gameOver()){ // If the game is over show the score screen,
                    int[] finalScores = mGame.getScoresPerRule();
                    Intent intent = FinalScoreActivity.newIntent(GameActivity.this, finalScores);
                    startActivity(intent);
                }else { // otherwise advance to the next round.
                    mGame.nextRound();
                    mRoundPlayed = false;
                    if(mGame.gameOver()){
                        mRoundButton.setText(R.string.round_button_score); // Change button text to indicate different behaviour.
                    }
                    updateAllDice();
                    updateRerollText();
                    updateRoundText();
                    updateScoreText();
                    updateRuleSpinner();
                    updateCalcAndRoundButtons();
                }
            }
        });

    }
    /**
     * Method for initialising the graphics for the dice,
     * by mapping numbers to drawables.
     */
    private void initDiceGraphics(){
        mWhiteDices = new SparseIntArray(6);
        mWhiteDices.put(1, R.drawable.ic_dicewhiteone);
        mWhiteDices.put(2, R.drawable.ic_dicewhitetwo);
        mWhiteDices.put(3, R.drawable.ic_dicewhitethree);
        mWhiteDices.put(4, R.drawable.ic_dicewhitefour);
        mWhiteDices.put(5, R.drawable.ic_dicewhitefive);
        mWhiteDices.put(6, R.drawable.ic_dicewhitesix);

        mRedDices = new SparseIntArray(6);
        mRedDices.put(1, R.drawable.ic_diceredone);
        mRedDices.put(2, R.drawable.ic_diceredtwo);
        mRedDices.put(3, R.drawable.ic_diceredthree);
        mRedDices.put(4, R.drawable.ic_diceredfour);
        mRedDices.put(5, R.drawable.ic_diceredfive);
        mRedDices.put(6, R.drawable.ic_diceredsix);
    }

    /**
     * Method for initialising the rule names,
     * by mapping their string name to their position in the rules array.
     */
    private void initRuleNames(){
        mRuleNames = new HashMap<>();
        String[] values = getResources().getStringArray(R.array.rule_array);
        for(int i = 0; i < 10; i++){
            mRuleNames.put(values[i], i);
        }
    }

    /**
     * Method for marking dice for rerolling.
     *
     * @param dice The dice to be rerolled.
     */
    private void markDice(int dice)
    {
        if(mGame.getRerollsLeft() > 0) { // If we have rerolls left,
            if (mDiceMarkedForReroll[dice]) { // and the dice is marked,
                mDiceMarkedForReroll[dice] = false; // unmark it and turn it white.
                updateDiceButtonGraphics(mDiceButtons[dice], mWhiteDices, mGame.getDiceValue(dice));
            } else { // If the dice is not marked,
                mDiceMarkedForReroll[dice] = true; // mark it and turn it red.
                updateDiceButtonGraphics(mDiceButtons[dice], mRedDices, mGame.getDiceValue(dice));
            }

            // Enable or disable the reroll button based on if there are any marked  dice.
            if(someDiceAreMarked()){
                mRerollButton.setEnabled(true);
            } else{
                mRerollButton.setEnabled(false);
            }
        }
    }

    /**
     * Method for setting all dice graphics to the correct drawable.
     */
    private void updateAllDice(){
        int[] diceValues = mGame.getDiceValues();
        for (int i = 0; i < 6; i++){
            updateDiceButtonGraphics(mDiceButtons[i], mWhiteDices, diceValues[i]);
            mDiceMarkedForReroll[i] = false;
        }

    }

    /**
     * Method for checking if there are marked dice.
     *
     * @return True if there are marked dice, else false.
     */
    private boolean someDiceAreMarked(){
        boolean marked = false;
        for (int i = 0; i < 6; i++){
            if(mDiceMarkedForReroll[i]){
                marked = true;
            }
        }
        return marked;
    }

    /**
     * Method for updating the reroll text.
     */
    private void updateRerollText(){
        String newRerollText = getResources().getString(R.string.reroll_text) + " " + mGame.getRerollsLeft();
        mRerollText.setText(newRerollText);
    }

    /**
     * Method for updating the score text.
     */
    private void updateScoreText(){
        String newScoreText = getResources().getString(R.string.score_text) + " " + mGame.getCurrentScore();
        mScoreText.setText(newScoreText);
    }

    /**
     * Method for updating the round text.
     */
    private void updateRoundText(){
        String newRoundText = getResources().getString(R.string.round_text) + " " + mGame.getRound();
        mRoundText.setText(newRoundText);
    }

    /**
     * Method for enabling and disabling the calculate button, the rule spinner,
     * and the round button, based on if the current round has been played.
     */
    private void updateCalcAndRoundButtons(){
        mCalculateButton.setEnabled(!mRoundPlayed);
        mRuleSpinner.setEnabled(!mRoundPlayed);
        mRoundButton.setEnabled(mRoundPlayed);
    }

    /**
     * Method for updating the choices in the rule spinner based on what rules have been used this game.
     */
    private void updateRuleSpinner(){
        ArrayAdapter<String> ruleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, createRuleSpinnerList());
        ruleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRuleSpinner.setAdapter(ruleAdapter);
    }

    /**
     * Method for creating a list of choices for the rule spinner based on what rules have been used this game.

     * @return A list of unused rules that will appear in the rules spinner.
     */
    private List<String> createRuleSpinnerList(){
        List<String> list = new ArrayList<String>();
        boolean[] ruleUsed = mGame.getScoreRulesUsed();
        String[] values = getResources().getStringArray(R.array.rule_array);
        for(int i = 0; i < 10; i++){
            if(!ruleUsed[i]){
                list.add(values[i]);
            }
        }
        return list;
    }

    /**
     * Method for updating the graphics for a dice buttons.
     *
     * @param diceButton The button to update.
     * @param diceGraphics An array of graphics from which the new graphic will be taken.
     * @param diceValue The value of the dice to be represented on the graphics.
     */
    private void updateDiceButtonGraphics(ImageButton diceButton, SparseIntArray diceGraphics, int diceValue){
        diceButton.setImageResource(diceGraphics.get(diceValue));
    }
}