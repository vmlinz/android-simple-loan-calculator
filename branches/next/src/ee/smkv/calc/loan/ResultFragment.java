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

        Utils.setNumber((TextView) view.findViewById(R.id.resultAmountValue) , StartActivity.loan.getAmount());
        Utils.setNumber((TextView) view.findViewById(R.id.resultMonthlyPaymentText) , StartActivity.loan.getMinMonthlyPayment() , StartActivity.loan.getMaxMonthlyPayment());
        Utils.setNumber((TextView) view.findViewById(R.id.resultIterestTotalText) , StartActivity.loan.getTotalInterests());
        Utils.setNumber((TextView) view.findViewById(R.id.resultDownPaymentValue) , StartActivity.loan.getDownPaymentPayment());
        Utils.setNumber((TextView) view.findViewById(R.id.resultCommissionsTotalText) , StartActivity.loan.getCommissionsTotal());
        Utils.setNumber((TextView) view.findViewById(R.id.resultInterestValue) , StartActivity.loan.getInterest());
        Utils.setNumber((TextView) view.findViewById(R.id.effectiveInterestText) , StartActivity.loan.getEffectiveInterestRate());
        Utils.setNumber((TextView) view.findViewById(R.id.resultAmountTotalText) , StartActivity.loan.getTotalAmount());
        Utils.setNumber((TextView) view.findViewById(R.id.resultPeriodValue) , StartActivity.loan.getPeriod());
        Utils.setNumber((TextView) view.findViewById(R.id.resultResidueValue) , StartActivity.loan.getResiduePayment());

        return view;
    }
}
