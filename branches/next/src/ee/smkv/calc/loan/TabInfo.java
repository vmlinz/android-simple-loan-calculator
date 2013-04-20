package ee.smkv.calc.loan;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
* Created with IntelliJ IDEA.
* User: andrei
* Date: 20.04.13
* Time: 12:57
* To change this template use File | Settings | File Templates.
*/
class TabInfo {
    public final Class<? extends Fragment> fragmentClass;
    public final Bundle args;

    public TabInfo(Class<? extends Fragment> fragmentClass,
                   Bundle args) {
        this.fragmentClass = fragmentClass;
        this.args = args;
    }
}
