package ee.smkv.calc.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        return inflate;
    }

    public void incrementYear(View view) {
        BigDecimal y = Utils.getNumber(years);
        if (y.compareTo(new BigDecimal("50")) < 0) {
            Utils.setNumber(years, y.add(BigDecimal.ONE));
        }
    }

    public void decrementYear(View view) {
        BigDecimal y = Utils.getNumber(years);
        if (y.compareTo(BigDecimal.ZERO) > 0) {
            Utils.setNumber(years, y.subtract(BigDecimal.ONE));
        }
    }

    public void incrementMonth(View view) {
        BigDecimal y = Utils.getNumber(months);
        if (y.compareTo(new BigDecimal("12")) < 0) {
            Utils.setNumber(months, y.add(BigDecimal.ONE));
        }
    }

    public void decrementMonth(View view) {
        BigDecimal y = Utils.getNumber(months);
        if (y.compareTo(BigDecimal.ZERO) > 0) {
            Utils.setNumber(months, y.subtract(BigDecimal.ONE));
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
        return Utils.getNumber(years , BigDecimal.ZERO).multiply(new BigDecimal("12")).add(Utils.getNumber(months , BigDecimal.ZERO));
    }
}
