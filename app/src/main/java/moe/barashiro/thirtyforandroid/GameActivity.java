package moe.barashiro.thirtyforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
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

public class GameActivity extends AppCompatActivity {

    private SparseIntArray mWhiteDices;
    private SparseIntArray mRedDices;
    private Map<String, Integer> mMethodNames;
    private boolean[] mDiceMarkedForReroll;
    private Game mGame;

    private ImageButton[] mDiceButtons;
    private TextView mRerollText;
    private Button mRerollButton;
    private TextView mRerollsLeftText;
    private TextView mRoundText;
    private TextView mScoreText;
    private Spinner mMethodSpinner;
    private Button mCalculateButton;
    private Button mNext;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mWhiteDices = new SparseIntArray(6);
        mWhiteDices.put(1, R.drawable.white1);
        mWhiteDices.put(2, R.drawable.white2);
        mWhiteDices.put(3, R.drawable.white3);
        mWhiteDices.put(4, R.drawable.white4);
        mWhiteDices.put(5, R.drawable.white5);
        mWhiteDices.put(6, R.drawable.white6);

        mRedDices = new SparseIntArray(6);
        mRedDices.put(1, R.drawable.red1);
        mRedDices.put(2, R.drawable.red2);
        mRedDices.put(3, R.drawable.red3);
        mRedDices.put(4, R.drawable.red4);
        mRedDices.put(5, R.drawable.red5);
        mRedDices.put(6, R.drawable.red6);

        mMethodNames = new HashMap<>();
        String[] values = getResources().getStringArray(R.array.methodArray);
        for(int i = 0; i < 10; i++){
            mMethodNames.put(values[i], i);
        }

        mGame = new Game();
        mDiceMarkedForReroll = new boolean[] {false, false, false, false, false, false};

        mRerollButton = (Button) findViewById(R.id.rerollButton);
        mRerollText = (TextView) findViewById(R.id.rerollText);
        mScoreText = (TextView) findViewById(R.id.scoreText);
        mMethodSpinner = (Spinner) findViewById(R.id.methodSpinner);
        mCalculateButton = (Button) findViewById(R.id.calculateButton);
        mDiceButtons = new ImageButton[] {
                (ImageButton) findViewById(R.id.diceOneButton),
                (ImageButton) findViewById(R.id.diceTwoButton),
                (ImageButton) findViewById(R.id.diceThreeButton),
                (ImageButton) findViewById(R.id.diceFourButton),
                (ImageButton) findViewById(R.id.diceFiveButton),
                (ImageButton) findViewById(R.id.diceSixButton)
        };


        mRerollButton.setEnabled(false);
        updateAllDice();
        updateRerollText();
        updateScoreText(0);
        updateMethodSpinner();




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
                String methodName = String.valueOf(mMethodSpinner.getSelectedItem());
                int methodNumber = mMethodNames.get(methodName);
                int score = mGame.calculateScore(methodNumber);
                mGame.setRerollsToZero();
                updateRerollText();
                updateScoreText(score);
                mCalculateButton.setEnabled(false);
                mMethodSpinner.setEnabled(false);
                mRerollButton.setEnabled(false);
            }
        });

    }

    private void markDice(int dice)
    {
        if(mGame.getRerollsLeft() > 0) {
            if (mDiceMarkedForReroll[dice]) {
                mDiceMarkedForReroll[dice] = false;
                updateDiceButtonGraphics(mDiceButtons[dice], mWhiteDices, mGame.getDiceValue(dice));
            } else {
                mDiceMarkedForReroll[dice] = true;
                updateDiceButtonGraphics(mDiceButtons[dice], mRedDices, mGame.getDiceValue(dice));
            }

            if(someDiceAreMarked()){
                mRerollButton.setEnabled(true);
            } else{
                mRerollButton.setEnabled(false);
            }

        }
        // TODO: Maybe toast "no rerolls left" in else branch
    }

    private void updateAllDice(){
        int[] diceValues = mGame.getDiceValues();
        for (int i = 0; i < 6; i++){
            updateDiceButtonGraphics(mDiceButtons[i], mWhiteDices, diceValues[i]);
            mDiceMarkedForReroll[i] = false;
        }

    }

    private boolean someDiceAreMarked(){
        boolean marked = false;
        for (int i = 0; i < 6; i++){
            if(mDiceMarkedForReroll[i]){
                marked = true;
            }
        }
        return marked;
    }

    private void updateRerollText(){
        String newRerollText = getResources().getString(R.string.rerollText) + " " + mGame.getRerollsLeft();
        mRerollText.setText(newRerollText);
    }

    private void updateScoreText(int score){
        String newScoreText = getResources().getString(R.string.scoreText) + " " + score;
        mScoreText.setText(newScoreText);
    }

    private void updateMethodSpinner(){
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, createMethodSpinnerList());
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMethodSpinner.setAdapter(methodAdapter);
    }

    private List<String> createMethodSpinnerList(){
        List<String> list = new ArrayList<String>();
        boolean[] methodsUsed = mGame.getScoreMethodsUsed();
        String[] values = getResources().getStringArray(R.array.methodArray);
        for(int i = 0; i < 10; i++){
            if(!methodsUsed[i]){
                list.add(values[i]);
            }
        }
        return list;
    }

//    private int methodStringToInt(String method){
//        switch(method) {
//            case "Low":
//                return 0;
//            case "Fours":
//                return 1;
//            case "Fives":
//                return 2;
//            case "Sixes":
//                return 3;
//            case "Sevens":
//                return 4;
//            case "Eight":
//                return 5;
//            case "Nines":
//                return 6;
//            case "Tens":
//                return 7;
//            case "Elevens":
//                return 8;
//            case "Twelves":
//                return 9;
//        }
//        return 0;
//    }

    private void updateDiceButtonGraphics(ImageButton diceButton, SparseIntArray diceGraphics, int diceValue){
        diceButton.setImageResource(diceGraphics.get(diceValue));
    }
}