package moe.barashiro.thirtyforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RulesActivity extends AppCompatActivity {
    private static final String RULES = "moe.barashiro.thirtyforandroid.rules";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RulesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }
}
