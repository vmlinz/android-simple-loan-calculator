package ee.smkv.calc.loan;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ResultActivity extends TabSwipeActivity {
    MenuItem addToCompareMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);
        setTitle(getResources().getStringArray(R.array.shorttypes)[StartActivity.loan.getLoanType()]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        addTab(getString(R.string.tabResult), ResultFragment.class, new Bundle());
        addTab(getString(R.string.tabSchedule), ScheduleFragment.class, new Bundle());
        addTab(getString(R.string.tabChart), ChartFragment.class, new Bundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addToCompareMenuItem = menu.add(R.string.addToCompare);
        addToCompareMenuItem
                .setIcon(R.drawable.ic_action_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);


        }

        if(item == addToCompareMenuItem){
            StartActivity.storeManager.addLoan( StartActivity.loan);
            Intent myIntent = new Intent(this, CompareActivity.class);
            startActivity(myIntent);
        }
        return false;
    }
}
