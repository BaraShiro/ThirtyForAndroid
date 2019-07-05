package moe.barashiro.thirtyforandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mThrityText;
    private TextView mForAndroidText;
    private Button mStartButton;
    private Button mRulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mThrityText = (TextView) findViewById(R.id.thirty);
        mForAndroidText = (TextView) findViewById(R.id.for_android);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        mRulesButton = (Button) findViewById(R.id.rules_button);
        mRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RulesActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
}
