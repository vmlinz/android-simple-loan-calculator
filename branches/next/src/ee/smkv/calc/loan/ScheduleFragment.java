package ee.smkv.calc.loan;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import ee.smkv.calc.loan.model.Payment;

public class ScheduleFragment extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule, container, false);
        TableLayout table = (TableLayout)view.findViewById(R.id.scheduleTable);
        for(Payment payment : StartActivity.loan.getPayments()){
            View tableRow = inflater.inflate(R.layout.schedule_row, container);
            Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentNr) , payment.getNr());
            Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentBalance) , payment.getBalance());
            Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentPrincipal) , payment.getPrincipal());
            Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentInterest) , payment.getInterest());
            Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentCommission) , payment.getCommission());
            Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentAmount) , payment.getAmount());
            table.addView(tableRow);
        }

        return view;
    }
}
