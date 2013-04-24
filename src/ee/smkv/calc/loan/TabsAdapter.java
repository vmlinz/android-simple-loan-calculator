package ee.smkv.calc.loan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: andrei
* Date: 20.04.13
* Time: 12:54
* To change this template use File | Settings | File Templates.
*/
class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private final Context context;
    private final ActionBar mActionBar;
    private final ViewPager mPager;
    private List<TabInfo> mTabs = new ArrayList<TabInfo>();

    public TabsAdapter(SherlockFragment fragment, ViewPager pager) {
       super( fragment.getFragmentManager());
        this.mPager = pager;
        this.context = fragment.getActivity();
        this.mActionBar = null;
    }
    public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        this.context = activity;
        this.mActionBar = activity.getSupportActionBar();
        this.mPager = pager;

        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    public void addTab(CharSequence title, Class<? extends Fragment> fragmentClass, Bundle args) {
        final TabInfo tabInfo = new TabInfo(fragmentClass, args);

        if (mActionBar != null) {
            ActionBar.Tab tab = mActionBar.newTab();
            tab.setText(title);
            tab.setTabListener(this);
            tab.setTag(tabInfo);
            mActionBar.addTab(tab);
        }

        mTabs.add(tabInfo);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        final TabInfo tabInfo = mTabs.get(position);
        return (Fragment) Fragment.instantiate(context, tabInfo.fragmentClass.getName(), tabInfo.args);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageSelected(int position) {
        /*
         * Select tab when user swiped
         */
        if (mActionBar != null) {
            mActionBar.setSelectedNavigationItem(position);
        }
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        /*
         * Slide to selected fragment when user selected tab
         */
        TabInfo tabInfo = (TabInfo) tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tabInfo) {
                mPager.setCurrentItem(i);
            }
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
