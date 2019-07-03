package moe.barashiro.thirtyforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private SparseIntArray whiteDices;
    private SparseIntArray redDices;
    private boolean[] diceMarkedForReroll;

    private Game mGame;
    private ImageButton[] mDiceButtons;
    private Button mReroll;
    private TextView mRerollsLeft;
    private TextView mRound;
    private Button mCalculate;
    private Button mNext;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        whiteDices = new SparseIntArray(6);
        whiteDices.put(1, R.drawable.white1);
        whiteDices.put(2, R.drawable.white2);
        whiteDices.put(3, R.drawable.white3);
        whiteDices.put(4, R.drawable.white4);
        whiteDices.put(5, R.drawable.white5);
        whiteDices.put(6, R.drawable.white6);

        redDices = new SparseIntArray(6);
        redDices.put(1, R.drawable.red1);
        redDices.put(2, R.drawable.red2);
        redDices.put(3, R.drawable.red3);
        redDices.put(4, R.drawable.red4);
        redDices.put(5, R.drawable.red5);
        redDices.put(6, R.drawable.red6);

        diceMarkedForReroll = new boolean[] {false, false, false, false, false, false};

        mGame = new Game();

        mDiceButtons = new ImageButton[] {
                (ImageButton) findViewById(R.id.diceOneButton),
                (ImageButton) findViewById(R.id.diceTwoButton),
                (ImageButton) findViewById(R.id.diceThreeButton),
                (ImageButton) findViewById(R.id.diceFourButton),
                (ImageButton) findViewById(R.id.diceFiveButton),
                (ImageButton) findViewById(R.id.diceSixButton)
        };
        mReroll = (Button) findViewById(R.id.reroll);

        updateAllDice();


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

        mReroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGame.rerollDices(diceMarkedForReroll[0], diceMarkedForReroll[1],diceMarkedForReroll[2],diceMarkedForReroll[3],diceMarkedForReroll[4],diceMarkedForReroll[5]);
                updateAllDice();
                if(mGame.getRerollsLeft() < 1) {
                    mReroll.setEnabled(false);
                }
            }
        });

    }

    private void markDice(int dice)
    {
        if(diceMarkedForReroll[dice]){
            diceMarkedForReroll[dice] = false;
            updateDiceButtonGraphics(mDiceButtons[dice], whiteDices, mGame.getDiceValue(dice));
        }
        else{
            diceMarkedForReroll[dice] = true;
            updateDiceButtonGraphics(mDiceButtons[dice], redDices, mGame.getDiceValue(dice));
        }
    }

    private void updateAllDice(){
        int[] diceValues = mGame.getDiceValues();
        for (int i = 0; i < 6; i++){
            updateDiceButtonGraphics(mDiceButtons[i], whiteDices, diceValues[i]);
            diceMarkedForReroll[i] = false;
        }
    }

    private void updateDiceButtonGraphics(ImageButton diceButton, SparseIntArray diceGraphics, int diceValue){
        diceButton.setImageResource(diceGraphics.get(diceValue));
    }
}