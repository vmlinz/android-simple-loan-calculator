package ee.smkv.calc.loan;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;

public class ResultFragment extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result, container, false);

        Utils.setNumber((TextView) view.findViewById(R.id.resultAmountText) , StartActivity.loan.getAmount());
        Utils.setNumber((TextView) view.findViewById(R.id.resultMonthlyPaymentText) , StartActivity.loan.getMinMonthlyPayment() , StartActivity.loan.getMaxMonthlyPayment());
        Utils.setNumber((TextView) view.findViewById(R.id.resultIterestTotalText) , StartActivity.loan.getTotalInterests());
        Utils.setNumber((TextView) view.findViewById(R.id.resultDownPaymentTotalText) , StartActivity.loan.getDownPaymentPayment());
        Utils.setNumber((TextView) view.findViewById(R.id.resultCommissionsTotalText) , StartActivity.loan.getMonthlyCommissionPayment());
        Utils.setNumber((TextView) view.findViewById(R.id.nominalInterestText) , StartActivity.loan.getInterest());
        Utils.setNumber((TextView) view.findViewById(R.id.effectiveInterestText) , StartActivity.loan.getEffectiveInterestRate());
        Utils.setNumber((TextView) view.findViewById(R.id.resultAmountTotalText) , StartActivity.loan.getTotalAmount());
        Utils.setNumber((TextView) view.findViewById(R.id.resultPeriodTotalText) , StartActivity.loan.getPeriod());

        return view;
    }
}
