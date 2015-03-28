package com.republicofgavin.creditcardholder.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.republicofgavin.creditcardholder.R;

/**
 * Activity for holding the {@link com.republicofgavin.creditcardholder.fragments.AddCreditCardFragment}
 *
 * @author Gavin McNeely
 */
public class AddCreditCardActivity extends Activity {

    private static final String TAG = AddCreditCardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_credit_card, menu);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        return true;
    }
}
