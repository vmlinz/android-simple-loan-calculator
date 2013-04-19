package ee.smkv.calc.loan;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    public static BigDecimal getNumber(EditText editText) throws FieldNumberFormatException {
    BigDecimal number = getNumber(editText, null);
    if (number == null) {
      throw new FieldNumberFormatException(editText.getId(), "Number if empty");
    }
    return number;
  }

  public static BigDecimal getNumber(EditText editText, BigDecimal defaultNumber) throws FieldNumberFormatException {

    try {
      Editable text = editText.getText();
      if (text == null || text.length() == 0) {
        return defaultNumber;
      }
      return new BigDecimal(text.toString().replace(',', '.'));
    }
    catch (FieldNumberFormatException e) {
      throw e;
    }
    catch (NumberFormatException e) {
      throw new FieldNumberFormatException(editText.getId(), e.getMessage());
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
    }
    else {

      textView.setText(
        min.setScale(2, RoundingMode.HALF_EVEN).toPlainString() +
        " - " +
        max.setScale(2, RoundingMode.HALF_EVEN).toPlainString()
      );
    }
  }

    public static String percent(BigDecimal value ,BigDecimal total){
        BigDecimal percent = value.multiply(HUNDRED).divide(total, 2, RoundingMode.HALF_EVEN);
        return percent.toPlainString() + "%";
    }
}
