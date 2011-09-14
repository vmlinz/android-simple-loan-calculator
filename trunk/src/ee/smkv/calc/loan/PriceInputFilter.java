package ee.smkv.calc.loan;

import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;


public class PriceInputFilter implements InputFilter {

  public CharSequence filter(CharSequence source, int start, int end,  Spanned dest, int dstart, int dend) {

    String checkedText = dest.toString() + source.toString();
    String pattern = getPattern();

    if (!Pattern.matches(pattern, checkedText)) {
      return "";
    }

    return null;
  }

  private String getPattern() {
    return "[0-9]+([.,]{1}||[.,]{1}[0-9]{})?";
  }
}