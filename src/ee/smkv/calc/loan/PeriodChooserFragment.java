package ee.smkv.calc.loan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;

import java.math.BigDecimal;

public class PeriodChooserFragment extends SherlockFragment implements View.OnClickListener {
    EditText years, months;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.period_chooser, container, false);

        months = (EditText) inflate.findViewById(R.id.periodMonthEdit);
        years = (EditText) inflate.findViewById(R.id.periodYearEdit);

        inflate.findViewById(R.id.periodMonthMinusButton).setOnClickListener(this);
        inflate.findViewById(R.id.periodMonthPlusButton).setOnClickListener(this);
        inflate.findViewById(R.id.periodYearMinusButton).setOnClickListener(this);
        inflate.findViewById(R.id.periodYearPlusButton).setOnClickListener(this);

        EditText monthEdit = (EditText) inflate.findViewById(R.id.periodMonthEdit);
        EditText yearMonth = (EditText) inflate.findViewById(R.id.periodYearEdit);
        monthEdit.addTextChangedListener(new NumberTextWatcher(12, monthEdit));
        yearMonth.addTextChangedListener(new NumberTextWatcher(50, yearMonth));


        return inflate;
    }

    public void incrementYear(View view) {
        BigDecimal y = Utils.getNumber(years, BigDecimal.ZERO);
        if (y.compareTo(new BigDecimal("50")) < 0) {
            Utils.setNumber(years, y.intValue() + 1);
        }
    }

    public void decrementYear(View view) {
        BigDecimal y = Utils.getNumber(years, BigDecimal.ZERO);
        if (y.compareTo(BigDecimal.ZERO) > 0) {
            Utils.setNumber(years, y.intValue() - 1);
        }
    }

    public void incrementMonth(View view) {
        BigDecimal y = getNumber();
        if (y.compareTo(new BigDecimal("12")) < 0) {
            Utils.setNumber(months, y.intValue() + 1);
        } else{
            Utils.setNumber(months, BigDecimal.ZERO);
            incrementYear(view);
        }
    }

    public void decrementMonth(View view) {
        BigDecimal y = getNumber();
        if (y.compareTo(BigDecimal.ZERO) > 0) {
            Utils.setNumber(months, y.intValue() - 1);
        }
    }

    private BigDecimal getNumber() {
        try {
            return Utils.getNumber(months, BigDecimal.ZERO);
        } catch (FieldNumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.periodYearPlusButton:
                incrementYear(v);
                break;
            case R.id.periodYearMinusButton:
                decrementYear(v);
                break;
            case R.id.periodMonthPlusButton:
                incrementMonth(v);
                break;
            case R.id.periodMonthMinusButton:
                decrementMonth(v);
                break;
        }
    }


    public BigDecimal getPeriodInMonths(){
        return Utils.getNumber(years , BigDecimal.ZERO).multiply(new BigDecimal("12")).add(getNumber());
    }


    private static class NumberTextWatcher implements TextWatcher {
        int max;
        EditText editText;
        public NumberTextWatcher(int max, EditText editText) {
            this.max = max;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                try {
                    BigDecimal number = Utils.getNumber(editText);
                    if (number.intValue() > max){
                        editText.setText("" + max);
                    }
                    if (number.intValue() < 0){
                        editText.setText("");
                    }
                } catch (FieldNumberFormatException e) {
                    editText.setText("");
                }
            }
        }
    }
}
