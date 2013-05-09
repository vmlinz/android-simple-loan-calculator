package ee.smkv.calc.loan;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import ee.smkv.calc.loan.export.ExportDialog;

public class ResultActivity extends TabSwipeActivity {
    MenuItem addToCompareMenuItem;
    MenuItem openCompareMenuItem;
    MenuItem exportMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeResolver.getActivityTheme(this));
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

        int orientation = getResources().getConfiguration().orientation;
        addToCompareMenuItem = menu.add(R.string.addToCompare);
        addToCompareMenuItem
                .setIcon(ThemeResolver.getAddToCompareIcon(this))
                .setShowAsAction( orientation == Configuration.ORIENTATION_PORTRAIT ?  MenuItem.SHOW_AS_ACTION_IF_ROOM : MenuItem.SHOW_AS_ACTION_NEVER );

        openCompareMenuItem = menu.add(R.string.viewCompare);
        openCompareMenuItem
                .setIcon(R.drawable.ic_action_compare);

        exportMenuItem = menu.add(R.string.exportToEmail);
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
            return true;
        }
        if(item == openCompareMenuItem){
            Intent myIntent = new Intent(this, CompareActivity.class);
            startActivity(myIntent);
            return true;
        }
        if(item == exportMenuItem){
            ExportDialog dialog = new ExportDialog(this, ThemeResolver.getDialogTheme(this));
            dialog.show();
            return true;
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch(newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                addToCompareMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                addToCompareMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                break;
        }
    }
}
