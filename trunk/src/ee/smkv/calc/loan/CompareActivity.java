package ee.smkv.calc.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import ee.smkv.calc.loan.model.Loan;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Andrei Samkov
 */
public class CompareActivity extends Activity implements View.OnClickListener {
  Set<Loan> loans;
  int mode = BigDecimal.ROUND_HALF_EVEN;
  private LinearLayout container;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.compare);

    container = (LinearLayout)findViewById(R.id.loansContainer);

    Button closeButton = (Button)findViewById(R.id.closeCompare);
    closeButton.setOnClickListener(this);
    closeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.close), null, null, null);

    Button cleanButton = (Button)findViewById(R.id.cleanCompare);
    cleanButton.setOnClickListener(this);
    cleanButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.clean), null, null, null);

    if (MainActivity.storeManager != null) {
      loans = MainActivity.storeManager.getLoans();
    }
    showLoans(loans);

  }

  private void showLoans(Set<Loan> loans) {

    for (Loan loan : loans) {
      if(loan != null){
        container.addView(createLoanView(container, loan), container.getChildCount());
      }
    }
  }

  private View createLoanView(final LinearLayout container, final Loan loan) {
    final LinearLayout cell = new LinearLayout(container.getContext());
    cell.setOrientation(LinearLayout.VERTICAL);
    appendField(cell, getResources().getStringArray(R.array.shorttypes)[loan.getLoanType()]);
    if(loan.getAmount() != null) appendField(cell, loan.getAmount().setScale(2, mode).toPlainString());
    if(loan.getInterest() != null)appendField(cell, loan.getInterest().setScale(2, mode).toPlainString());
    if(loan.getPeriod() != null)appendField(cell, loan.getPeriod().toString());
    appendField(cell, loan.getMaxMonthlyPayment().setScale(2, mode).toPlainString());
    appendField(cell, loan.getMinMonthlyPayment().setScale(2, mode).toPlainString());
    appendField(cell, loan.getTotalInterests().setScale(2, mode).toPlainString());
    appendField(cell, loan.getTotalAmount().setScale(2, mode).toPlainString());


    LinearLayout buttonBar = new LinearLayout(cell.getContext());

    ImageButton removeBtn = new ImageButton(buttonBar.getContext());
    removeBtn.setImageResource(R.drawable.minus);
    removeBtn.setPadding(5, 5, 5, 5);
    removeBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        MainActivity.storeManager.removeLoan(loan);
        container.removeView(cell);
      }
    });
    buttonBar.addView(removeBtn);


    ImageButton scheduleBtn = new ImageButton(buttonBar.getContext());
    scheduleBtn.setImageResource(R.drawable.tablesmall);
    scheduleBtn.setPadding(5, 5, 5, 5);
    scheduleBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent scheduleActivityIntent = new Intent(CompareActivity.this, ScheduleActivity.class);
        ScheduleActivity.loan = loan;
        startActivity(scheduleActivityIntent);
      }
    });
    buttonBar.addView(scheduleBtn);
    cell.addView(buttonBar);
    return cell;
  }

  private void appendField(LinearLayout cell, String t) {
    TextView item = new TextView(cell.getContext());
    item.setText(t);
    item.setPadding(5, 5, 5, 5);
    cell.addView(item);
    View line = new View(cell.getContext());
    line.setBackgroundColor(getResources().getColor(R.color.border));
    line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT , 1));
    cell.addView(line);
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.closeCompare:
        finish();
        break;
      case R.id.cleanCompare:
        clearAll();
        break;

    }
  }

  private void clearAll() {
    try {

        for( int i = container.getChildCount() ; i > 1 ; i--){
          container.removeViewAt(i-1);
        }  
        MainActivity.storeManager.removeLoans(loans);

    }
    catch (Exception e) {
      Log.v(CompareActivity.class.getSimpleName(), e.getMessage(), e);
    }
  }

}
