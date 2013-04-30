package ee.smkv.calc.loan;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;

import java.util.Observable;
import java.util.Observer;

public class ResultFragment extends SherlockFragment implements Observer {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result, container, false);

        int bg = ThemeResolver.isLight(view.getContext()) ? R.drawable.roundedshape : R.drawable.roundedshape_dark;
        view.findViewById(R.id.resultInputLoanData).setBackground(getResources().getDrawable(bg));

        setupResult(view);

        LoanDispatcher.getInstance().addObserver(this);

        return view;
    }

    private void setupResult(View view) {
        if (StartActivity.loan.isCalculated()) {
            Utils.setNumber((TextView) view.findViewById(R.id.resultAmountValue), StartActivity.loan.getAmount());
            Utils.setNumber((TextView) view.findViewById(R.id.resultMonthlyPaymentText) , StartActivity.loan.getMinMonthlyPayment() , StartActivity.loan.getMaxMonthlyPayment());
            Utils.setNumber((TextView) view.findViewById(R.id.resultIterestTotalText) , StartActivity.loan.getTotalInterests());
            Utils.setNumber((TextView) view.findViewById(R.id.resultDownPaymentValue) , StartActivity.loan.getDownPaymentPayment());
            Utils.setNumber((TextView) view.findViewById(R.id.resultCommissionsTotalText) , StartActivity.loan.getCommissionsTotal());
            Utils.setNumber((TextView) view.findViewById(R.id.resultInterestValue) , StartActivity.loan.getInterest());
            Utils.setNumber((TextView) view.findViewById(R.id.effectiveInterestText) , StartActivity.loan.getEffectiveInterestRate());
            Utils.setNumber((TextView) view.findViewById(R.id.resultAmountTotalText) , StartActivity.loan.getTotalAmount());
            Utils.setNumber((TextView) view.findViewById(R.id.resultPeriodValue) , StartActivity.loan.getPeriod());
            Utils.setNumber((TextView) view.findViewById(R.id.resultResidueValue) , StartActivity.loan.getResiduePayment());
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        setupResult(getView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoanDispatcher.getInstance().deleteObserver(this);
    }
}
