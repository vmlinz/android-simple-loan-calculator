package ee.smkv.calc.loan;

import android.app.Activity;
import android.os.Bundle;


public class ChartActivity extends Activity {
	ChartView chartView;
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.chart);
	    chartView = (ChartView)findViewById(R.id.LoanChart);	    
	  }
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    Loan loan = (Loan)getIntent().getSerializableExtra(Loan.class.getName());
	    chartView.setLoan(loan);
	  }
}
