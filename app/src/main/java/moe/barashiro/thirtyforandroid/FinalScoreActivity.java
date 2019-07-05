package moe.barashiro.thirtyforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalScoreActivity extends AppCompatActivity {
    private static final String FINAL_SCORES = "moe.barashiro.thirtyforandroid.final_scores";

    private int[] mFinalScores;
    private TextView[] mScoreTexts;
    private TextView mTotalScore;


    public static Intent newIntent(Context packageContext, int[] finalScores) {
        Intent intent = new Intent(packageContext, FinalScoreActivity.class);
        intent.putExtra(FINAL_SCORES, finalScores);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        mFinalScores = getIntent().getIntArrayExtra(FINAL_SCORES);
        mScoreTexts = new TextView[]{
                (TextView) findViewById(R.id.scoreScreenLowScoreText),
                (TextView) findViewById(R.id.scoreScreenFoursScoreText),
                (TextView) findViewById(R.id.scoreScreenFivesScoreText),
                (TextView) findViewById(R.id.scoreScreenSixesScoreText),
                (TextView) findViewById(R.id.scoreScreenSevensScoreText),
                (TextView) findViewById(R.id.scoreScreenEightsScoreText),
                (TextView) findViewById(R.id.scoreScreenNinesScoreText),
                (TextView) findViewById(R.id.scoreScreenTensScoreText),
                (TextView) findViewById(R.id.scoreScreenElevensScoreText),
                (TextView) findViewById(R.id.scoreScreenTwelvesScoreText),
        };
        mTotalScore = (TextView) findViewById(R.id.scoreScreenTotalScoreText);
        int total = 0;
        for(int i = 0; i < 10; i++){
            mScoreTexts[i].setText(Integer.toString(mFinalScores[i]));
            total += mFinalScores[i];
        }
        mTotalScore.setText(Integer.toString(total));


    }
}
