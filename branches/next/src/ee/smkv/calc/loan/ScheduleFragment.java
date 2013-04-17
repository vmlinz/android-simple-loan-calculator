package ee.smkv.calc.loan;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends SherlockFragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.schedule, container, false);

    Loan loan = StartActivity.loan;

    CreateScheduleTableTask scheduleTableTask = new CreateScheduleTableTask(view.getContext());

    List<Payment> payments = loan.getPayments();
    Payment[] array = new Payment[payments.size()];
    scheduleTableTask.execute(payments.toArray(array));

    return view;
  }


  public class CreateScheduleTableTask extends AsyncTask<Payment, Integer, List<View>> {

    private final Context context;
    private final ProgressDialog dialog;

    public CreateScheduleTableTask(Context context) {
      this.context = context;
      dialog = new ProgressDialog(this.context);
    }


    @Override
    protected void onPreExecute() {
      this.dialog.setIndeterminate(false);
      this.dialog.setMessage("Creating payments table...");
      this.dialog.show();
    }

    @Override
    protected List<View> doInBackground(Payment... payments) {
      this.dialog.setMax(payments.length);
      List<View> list = new ArrayList<View>(payments.length);
      for (Payment payment : payments) {
        View tableRow = LayoutInflater.from(context).inflate(R.layout.schedule_row, null);
        Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentNr), payment.getNr());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentBalance), payment.getBalance());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentPrincipal), payment.getPrincipal());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentInterest), payment.getInterest());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentCommission), payment.getCommission());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.schedulePaymentAmount), payment.getAmount());
        list.add(tableRow);
        this.publishProgress(payment.getNr());
      }

      return list;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      this.dialog.setProgress( values[0]);
    }

    @Override
    protected void onPostExecute(List<View> views) {
      TableLayout table = (TableLayout)getView().findViewById(R.id.scheduleTable);
      for(View view : views){
        table.addView(view);
      }
      if (this.dialog.isShowing()) {
        this.dialog.dismiss();
      }
    }
  }

}
