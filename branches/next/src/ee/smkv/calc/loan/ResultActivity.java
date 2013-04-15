package ee.smkv.calc.loan;

import android.os.Bundle;
import com.actionbarsherlock.view.MenuItem;

public class ResultActivity extends TabSwipeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        addTab(getString(R.string.tabResult), ResultFragment.class, new Bundle());
        addTab(getString(R.string.tabSchedule), ScheduleFragment.class, new Bundle());
        addTab(getString(R.string.tabChart), ChartFragment.class, new Bundle());
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
