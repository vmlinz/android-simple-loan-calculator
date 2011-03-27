package ee.smkv.calc.loan.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class MyTextWatcher implements TextWatcher {

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    public void afterTextChanged(Editable editable) {
        //do nothing
    }

}
