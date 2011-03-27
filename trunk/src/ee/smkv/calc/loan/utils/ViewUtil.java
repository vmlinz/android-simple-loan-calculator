package ee.smkv.calc.loan.utils;

import android.widget.EditText;

import java.math.BigDecimal;

public class ViewUtil {

    private ViewUtil() {
    }

    public static int getIntegerValue(EditText editText) throws EditTextNumberFormatException {
        return getBigDecimalValue(editText).intValue();
    }

    public static BigDecimal getBigDecimalValue(EditText editText) throws EditTextNumberFormatException {
        String value = editText.getText().toString().replace(',', '.');
        if (value != null && value.trim().length() > 0) {
            try {
                return new BigDecimal(value);
            } catch (Exception e) {
                throw new EditTextNumberFormatException(editText, e);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }
}
