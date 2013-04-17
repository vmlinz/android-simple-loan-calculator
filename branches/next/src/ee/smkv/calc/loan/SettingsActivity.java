package ee.smkv.calc.loan;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class SettingsActivity extends SherlockPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);


        }
        return false;
    }
}
