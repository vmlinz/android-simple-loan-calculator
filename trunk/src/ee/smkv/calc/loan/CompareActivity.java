package ee.smkv.calc.loan;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import ee.smkv.calc.loan.model.Loan;

import java.util.Set;

public class CompareActivity extends SherlockActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    setTheme(ThemeResolver.getActivityTheme(this));
    super.onCreate(savedInstanceState);
    setContentView(R.layout.compare);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);


    LinearLayout header = (LinearLayout)findViewById(R.id.compareHeader);
    final int bg = ThemeResolver.isLight(this) ? R.drawable.row_border : R.drawable.row_border_dark;

    applyBackground(header, bg);

    if (StartActivity.storeManager != null) {
      Set<Loan> loans = StartActivity.storeManager.getLoans();
      final LinearLayout placeholder = (LinearLayout)findViewById(R.id.compareDataPlaceholder);
      for (final Loan loan : loans) {
        final ViewGroup tableRow = (ViewGroup)LayoutInflater.from(getApplicationContext()).inflate(R.layout.compare_row, null);

        ((TextView)tableRow.findViewById(R.id.compareTypeData)).setText(getResources().getStringArray(R.array.shorttypes)[loan.getLoanType()]);
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareAmountData), loan.getAmount());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareInterestData), loan.getInterest());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareEffectiveInterestData), loan.getEffectiveInterestRate());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.comparePeriodData), loan.getPeriod());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.comparePaymentMaxData), loan.getMaxMonthlyPayment());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.comparePaymentMinData), loan.getMinMonthlyPayment());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareInterestTotalData), loan.getTotalInterests());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareAmountTotalData), loan.getTotalAmount());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareDownPaymentData), loan.getDownPaymentPayment());
        Utils.setNumber((TextView)tableRow.findViewById(R.id.compareResidueData), loan.getResiduePayment());

        View close = tableRow.findViewById(R.id.compareRemoveLoan);
        applyBackground(tableRow, bg);
        close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            StartActivity.storeManager.removeLoan(loan);
            placeholder.removeView(tableRow);
          }
        });

        placeholder.addView(tableRow);
      }
    }
  }

  private void applyBackground(ViewGroup viewGroup, int bg) {
    Drawable background = getResources().getDrawable(bg);
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      if (viewGroup.getChildAt(i) instanceof TextView) {
        viewGroup.getChildAt(i).setBackgroundDrawable(background);
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return (true);


    }
    return false;
  }
}
