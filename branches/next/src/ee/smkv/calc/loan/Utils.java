package ee.smkv.calc.loan;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {
    public static BigDecimal getNumber(EditText editText) {
        return new BigDecimal(editText.getText().toString());
    }

    public static BigDecimal getNumber(EditText editText, BigDecimal defaultNumber) {
        try {
            return new BigDecimal(editText.getText().toString());
        } catch (Exception e) {
            return defaultNumber;
        }
    }

    public static void setNumber(EditText editText, BigDecimal number) {
        editText.setText(number.toPlainString());
    }

    public static void setNumber(TextView textView, BigDecimal number) {
        if (number == null) {
            number = BigDecimal.ZERO;
        }
        textView.setText(number.setScale(2, RoundingMode.HALF_EVEN).toPlainString());
    }

    public static void setNumber(TextView textView, Integer number) {
        if (number == null) {
            number = 0;
        }
        textView.setText(number.toString());
    }

    public static void setNumber(TextView textView, BigDecimal min, BigDecimal max) {
        if (min == null) {
            min = BigDecimal.ZERO;
        }
        if (max == null) {
            max = BigDecimal.ZERO;
        }
        if (min.compareTo(max) == 0) {
            setNumber(textView, min);
        } else {

            textView.setText(
                    min.setScale(2, RoundingMode.HALF_EVEN).toPlainString() +
                            " - " +
                            max.setScale(2, RoundingMode.HALF_EVEN).toPlainString()
            );
        }
    }
}
