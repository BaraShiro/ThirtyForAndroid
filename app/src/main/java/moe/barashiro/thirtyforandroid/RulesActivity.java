package moe.barashiro.thirtyforandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Activity for showing the game rules.
 *
 * @author  Robert Rosborg
 * @version 1.0
 */
public class RulesActivity extends AppCompatActivity {
    private static final String RULES = "moe.barashiro.thirtyforandroid.rules";

    /**
     * Method for creating a new Intent for starting the RulesActivity.
     *
     * @param packageContext The context for the intent.
     * @return A new Intent for starting the RulesActivity.
     */
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RulesActivity.class);
        return intent;
    }
    /**
     * Method for creating the activity.
     *
     * @param savedInstanceState A Bundle containing a saved state or null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }
}
