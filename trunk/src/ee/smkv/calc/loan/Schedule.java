package ee.smkv.calc.loan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Andrei Samkov
 */
public class Schedule extends Activity {
  int mode = BigDecimal.ROUND_HALF_EVEN;
  TableLayout table;
  TableRow header, footer;
  Button closeButton, chartButton;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.schedule2);
    //    table = (TableLayout)findViewById(R.id.ScheduleTable);
    //    header = (TableRow)findViewById(R.id.HeaderRow);
    //    footer = (TableRow)findViewById(R.id.FooterRow);
    closeButton = (Button)findViewById(R.id.scheduleClose);
    closeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.close), null, null, null);
    closeButton.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {
        finish();
      }
    });
    chartButton = (Button)findViewById(R.id.chartButton);
    chartButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.chart), null, null, null);
    chartButton.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {
        Loan loan = getLoan();
        Intent chart = new Intent(Schedule.this, ChartActivity.class);
        chart.putExtra(Loan.class.getName(), loan);
        startActivity(chart);
      }
    });

    final Loan loan = getLoan();
    //showSchedule(loan);

    AsyncTask task = new AsyncTask() {
      @Override
      protected Object doInBackground(Object... objects) {

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (Payment payment : loan.getPayments()) {
          HashMap<String, String> map = new HashMap<String, String>();
          map.put("nr", payment.getNr().toString());
          map.put("balance", payment.getBalance().setScale(2, mode).toPlainString());
          map.put("principal", payment.getPrincipal().setScale(2, mode).toPlainString());
          map.put("interest", payment.getInterest().setScale(2, mode).toPlainString());
          map.put("amount", payment.getAmount().setScale(2, mode).toPlainString());
          mylist.add(map);
        }

        ListView list = (ListView)findViewById(R.id.SCHEDULE);
        SimpleAdapter mSchedule = new SimpleAdapter(Schedule.this, mylist, R.layout.schedulerow,
                                                    new String[]{"nr", "balance", "principal", "interest", "amount"},
                                                    new int[]{R.id.NR_CELL, R.id.BALANCE_CELL, R.id.PRINCIPAL_CELL, R.id.INTEREST_CELL, R.id.PAYMENT_CELL});
        list.setAdapter(mSchedule);

        return null;
      }
    };

    task.execute();
  }

  @Override
  protected void onResume() {
    super.onResume();

  }


  private Loan getLoan() {
    return (Loan)getIntent().getSerializableExtra(Loan.class.getName());
  }


  private void showSchedule(Loan loan) {
    int pos = 1;
    for (Payment payment : loan.getPayments()) {
      addPaymentToTable(payment, pos++);
    }
    showTotal(loan, pos);
  }

  private void showTotal(Loan loan, int pos) {
    TableRow row = new TableRow(table.getContext());

    row.setPadding(2, 4, 2, 4);
    table.addView(row, pos);

    TextView nr = new TextView(row.getContext());
    TextView balance = new TextView(row.getContext());
    TextView principal = new TextView(row.getContext());
    TextView interest = new TextView(row.getContext());
    TextView amount = new TextView(row.getContext());

    principal.setText(loan.getAmount().setScale(2, mode).toPlainString());
    principal.setTextColor(getResources().getColor(R.color.result));
    principal.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

    interest.setText(loan.getTotalInterests().setScale(2, mode).toPlainString());
    interest.setTextColor(getResources().getColor(R.color.result));
    interest.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

    amount.setText(loan.getTotalAmount().setScale(2, mode).toPlainString());
    amount.setTextColor(getResources().getColor(R.color.result));
    amount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

    row.addView(nr);
    row.addView(balance);
    row.addView(principal);
    row.addView(interest);
    row.addView(amount);

  }

  private void addPaymentToTable(Payment payment, int pos) {
    TableRow row = new TableRow(table.getContext());
    row.setBackgroundColor(getResources().getColor(pos % 2 == 0 ? R.color.odd : R.color.even));
    row.setPadding(2, 4, 2, 4);
    table.addView(row, pos);
    TextView nr = new TextView(row.getContext());
    TextView balance = new TextView(row.getContext());
    TextView principal = new TextView(row.getContext());
    TextView interest = new TextView(row.getContext());
    TextView amount = new TextView(row.getContext());

    nr.setText(payment.getNr().toString());
    nr.setTextColor(Color.WHITE);

    balance.setText(payment.getBalance().setScale(2, mode).toPlainString());
    balance.setTextColor(Color.WHITE);

    principal.setText(payment.getPrincipal().setScale(2, mode).toPlainString());
    principal.setTextColor(Color.WHITE);

    interest.setText(payment.getInterest().setScale(2, mode).toPlainString());
    interest.setTextColor(Color.WHITE);

    amount.setText(payment.getAmount().setScale(2, mode).toPlainString());
    amount.setTextColor(Color.WHITE);

    row.addView(nr);
    row.addView(balance);
    row.addView(principal);
    row.addView(interest);
    row.addView(amount);
  }
}
