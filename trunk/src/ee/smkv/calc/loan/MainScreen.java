package ee.smkv.calc.loan;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;


public class MainScreen extends Activity implements AdapterView.OnItemSelectedListener {
  public static final String SETTINGS_NAME = MainScreen.class.getName();
  public static StoreManager storeManager;
  public static final Calculator[] CALCULATORS = new Calculator[]{
    new AnnuityCalculator(), new DifferentiatedCalculator(), new FixedCalculator()
  };

  TextView
    fixedPaymentLabel, periodLabel, periodTotalLabel, monthlyAmountLabel,
    monthlyAmountVText, amountTotalVText, interestTotalVText;

  EditText amountEText, interestEText, fixedPaymentEText;
  Spinner periodSpinner, typeSpinner;
  Button calculateButton, scheduleButton, typeHelpButton, typeHelpCloseButton;
  ScrollView scrollView;
  LinearLayout resultContainer;

  Loan loan = new Loan();
  Calculator calculator;


  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    init();
    registerEventListeners();
    storeManager = new StoreManager(getSharedPreferences(SETTINGS_NAME, 0));
    storeManager.loadTextViews(amountEText, interestEText, fixedPaymentEText);
    storeManager.loadSpinners(typeSpinner, periodSpinner);

  }


  @Override
  protected void onStop() {
    super.onStop();
    storeManager.storeTextViews(amountEText, interestEText, fixedPaymentEText);
    storeManager.storeSpinners(typeSpinner, periodSpinner);
  }


  private void registerEventListeners() {
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View arg0) {
        calculate();
        scrollView.scrollTo(resultContainer.getLeft(), resultContainer.getTop());
      }
    });

    scheduleButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View arg0) {
        showSchedule();
      }
    });

    typeHelpButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View arg0) {
        Intent typeHelp = new Intent(MainScreen.this, TypeHelpActivity.class);
        startActivity(typeHelp);
      }
    });
    periodSpinner.setOnItemSelectedListener(this);
    typeSpinner.setOnItemSelectedListener(this);
  }

  private void changeCalculatorType() {
    setTitle((String)typeSpinner.getSelectedItem());

    int selected = typeSpinner.getSelectedItemPosition();
    calculator = CALCULATORS[selected];
    boolean fixed = selected == 2;

    fixedPaymentLabel.setVisibility(!fixed ? View.GONE : View.VISIBLE);
    fixedPaymentEText.setVisibility(!fixed ? View.GONE : View.VISIBLE);
    periodLabel.setVisibility(fixed ? View.GONE : View.VISIBLE);
    periodSpinner.setVisibility(fixed ? View.GONE : View.VISIBLE);
  }

  private void showSchedule() {
    Intent scheduleActivityIntent = new Intent(MainScreen.this, Schedule.class);
    scheduleActivityIntent.putExtra(Loan.class.getName(), loan);
    startActivity(scheduleActivityIntent);
  }


  private void init() {
    amountEText = (EditText)findViewById(R.id.Amount);
    interestEText = (EditText)findViewById(R.id.Interest);
    periodSpinner = (Spinner)findViewById(R.id.Spinner01);
    monthlyAmountVText = (TextView)findViewById(R.id.MonthlyAmount);
    amountTotalVText = (TextView)findViewById(R.id.AmountTotal);
    interestTotalVText = (TextView)findViewById(R.id.IterestTotal);
    calculateButton = (Button)findViewById(R.id.Calc);
    scrollView = (ScrollView)findViewById(R.id.ScrollView);
    typeSpinner = (Spinner)findViewById(R.id.Type);
    fixedPaymentLabel = (TextView)findViewById(R.id.fixedPaymentLbl);
    periodLabel = (TextView)findViewById(R.id.periodLbl);
    fixedPaymentEText = (EditText)findViewById(R.id.fixedPaymentText);
    periodTotalLabel = (TextView)findViewById(R.id.PeriodTotal);
    monthlyAmountLabel = (TextView)findViewById(R.id.MonthlyAmountLbl);
    scheduleButton = (Button)findViewById(R.id.ScheduleButton);
    typeHelpButton = (Button)findViewById(R.id.TypeHelp);
    typeHelpCloseButton = (Button)findViewById(R.id.TypeHelpClose);
    resultContainer = (LinearLayout)findViewById(R.id.resultContainer);

    calculateButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.calculator), null, null, null);
    scheduleButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.table), null, null, null);
    typeHelpButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.help), null, null, null);
    typeHelpButton.setText("");
  }


  protected void showError(Exception e) {
    new ErrorDialogWrapper(this, e).show();
  }


  private void calculate() {
    try {
      loan = new Loan();
      loadLoanDataFromUI();
      calculator.calculate(loan);
      showCalculatedData();
      scheduleButton.setEnabled(true);
    }
    catch (Exception e) {
      showError(e);
      e.printStackTrace();
    }
  }

  private void showCalculatedData() {
    int mode = BigDecimal.ROUND_HALF_UP;
    String monthlyPayment = "";
    BigDecimal max = loan.getMaxMonthlyPayment();
    BigDecimal min = loan.getMinMonthlyPayment();
    if (max.compareTo(min) == 0) {
      monthlyPayment = max.setScale(2, mode).toPlainString();
    }
    else {
      monthlyPayment = max.setScale(2, mode).toPlainString() + " - " + min.setScale(2, mode).toPlainString();
    }
    monthlyAmountVText.setText(monthlyPayment);

    amountTotalVText.setText(loan.getTotalAmount().setScale(2, mode).toPlainString());
    interestTotalVText.setText(loan.getTotalInterests().setScale(2, mode).toPlainString());
    periodTotalLabel.setText(loan.getPeriod().toString());
  }

  private void loadLoanDataFromUI() {
    loan.setLoanType( typeSpinner.getSelectedItemPosition() );
    loan.setAmount(getNumber(amountEText, R.string.errorAmount));
    loan.setInterest(getNumber(interestEText, R.string.errorInterest));
    loan.setPeriod(getPeriod(periodSpinner).intValue());
    if (fixedPaymentEText.getVisibility() == View.VISIBLE) {
      loan.setFixedPayment(getNumber(fixedPaymentEText, R.string.errorFixedAmount));
    }
  }

  private BigDecimal getPeriod(Spinner periodSpinner) {
    try {
      boolean months = periodSpinner.getSelectedItemPosition() < 5;
      String period = (String)periodSpinner.getSelectedItem();
      DecimalFormat format = new DecimalFormat("#0");
      format.setParseBigDecimal(true);
      BigDecimal num = (BigDecimal)format.parse(period);
      return months ? num : num.multiply(new BigDecimal(12));
    }
    catch (ParseException e) {
      throw new RuntimeException("unable parse the period ");
    }

  }

  private BigDecimal getNumber(EditText text, int errorCode) {
    BigDecimal decimal;
    try {
      decimal = new BigDecimal(text.getText().toString());
    }
    catch (Exception e) {
      throw new RuntimeException("" + getResources().getText(errorCode));
    }
    if (BigDecimal.ZERO.compareTo(decimal) == 0) {
      throw new RuntimeException("" + getResources().getText(errorCode));
    }
    return decimal;
  }

  private boolean isRequiredRecalculation() {
    return
      amountEText.getText() != null && amountEText.getText().toString().trim().length() > 0
      && interestEText.getText() != null && interestEText.getText().toString().trim().length() > 0
      && (typeSpinner.getSelectedItemPosition() != 2 ||
          (fixedPaymentEText.getText() != null && fixedPaymentEText.getText().toString().trim().length() > 0));
  }

  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    changeCalculatorType();
    if (isRequiredRecalculation()) {
      calculate();
    }
  }

  public void onNothingSelected(AdapterView<?> adapterView) {
    //ignore
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.mainmenu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.addToCompareMenu:
        if (loan != null) {
          storeManager.addLoan(loan);
          openCompareActivity();
        }
        break;
      case R.id.viewCompareMenu:
        openCompareActivity();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void openCompareActivity() {
    Intent compareActivityIntent = new Intent(MainScreen.this, CompareActivity.class);
    startActivity(compareActivityIntent);
  }
}