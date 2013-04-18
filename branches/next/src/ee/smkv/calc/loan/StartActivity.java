package ee.smkv.calc.loan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import ee.smkv.calc.loan.calculators.AnnuityCalculator;
import ee.smkv.calc.loan.calculators.Calculator;
import ee.smkv.calc.loan.calculators.DifferentiatedCalculator;
import ee.smkv.calc.loan.calculators.FixedPaymentCalculator;
import ee.smkv.calc.loan.model.Loan;

import java.math.BigDecimal;


public class StartActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener {
  public static final Calculator[] CALCULATORS = new Calculator[]{new AnnuityCalculator(), new DifferentiatedCalculator(), new FixedPaymentCalculator()};
  public static Loan loan = new Loan();
  public static final String SETTINGS_NAME = StartActivity.class.getName();

  public static StoreManager storeManager;
  private MenuItem calculateMenuItem;
  private MenuItem helpMenuItem;
  private MenuItem settingMenuItem;
  private Calculator calculator = CALCULATORS[0];

  @Override
  public void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.Theme_Sherlock_Light);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    setTitle("");

    Context context = getSupportActionBar().getThemedContext();
    ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.types, R.layout.sherlock_spinner_item);
    list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    getSupportActionBar().setListNavigationCallbacks(list, this);


    storeManager = new StoreManager(PreferenceManager.getDefaultSharedPreferences(this));
    storeManager.loadTextViews(
      (TextView)findViewById(R.id.amountEdit),
      (TextView)findViewById(R.id.interestEdit),
      (TextView)findViewById(R.id.fixedPaymentEdit),
      (TextView)findViewById(R.id.periodMonthEdit),
      (TextView)findViewById(R.id.periodYearEdit),
      (TextView)findViewById(R.id.downPaymentEdit),
      (TextView)findViewById(R.id.disposableCommissionEdit),
      (TextView)findViewById(R.id.monthlyCommissionEdit),
      (TextView)findViewById(R.id.residueEdit)
    );
    storeManager.loadSpinners(
      (Spinner)findViewById(R.id.downPaymentType),
      (Spinner)findViewById(R.id.disposableCommissionType),
      (Spinner)findViewById(R.id.monthlyCommissionType),
      (Spinner)findViewById(R.id.residueType)
    );

    int type = storeManager.getInteger("type", 0);
    getSupportActionBar().setSelectedNavigationItem(type);

    fixInterestlabel();
  }


  @Override
  protected void onPostResume() {
    super.onPostResume();
    fixInterestlabel();
  }

  private void fixInterestlabel() {
    if (isEffectiveRate()) {
      TextView interestLabel = (TextView)findViewById(R.id.interestLabel);
      interestLabel.setText(getString(R.string.effectiveInterestLbl));
    }
    else {
      TextView interestLabel = (TextView)findViewById(R.id.interestLabel);
      interestLabel.setText(getString(R.string.interest));
    }
  }

  private boolean isEffectiveRate() {
    String interestType = PreferenceManager.getDefaultSharedPreferences(this).getString("interestType", "nominal");
    Log.i(StartActivity.class.getName(), interestType);
    return "effective".equals(interestType);
  }


  @Override
  protected void onStop() {
    super.onStop();
    try {
      storeManager.storeTextViews((TextView)findViewById(R.id.amountEdit),
                                  (TextView)findViewById(R.id.interestEdit),
                                  (TextView)findViewById(R.id.fixedPaymentEdit),
                                  (TextView)findViewById(R.id.periodMonthEdit),
                                  (TextView)findViewById(R.id.periodYearEdit),
                                  (TextView)findViewById(R.id.downPaymentEdit),
                                  (TextView)findViewById(R.id.disposableCommissionEdit),
                                  (TextView)findViewById(R.id.monthlyCommissionEdit),
                                  (TextView)findViewById(R.id.residueEdit));
      storeManager.storeSpinners((Spinner)findViewById(R.id.downPaymentType),
                                 (Spinner)findViewById(R.id.disposableCommissionType),
                                 (Spinner)findViewById(R.id.monthlyCommissionType),
                                 (Spinner)findViewById(R.id.residueType));
      storeManager.setInteger("type", getSupportActionBar().getSelectedNavigationIndex());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    calculator = CALCULATORS[itemPosition];
    if (itemPosition == 2) {
      findViewById(R.id.fixedPaymentBlock).setVisibility(View.VISIBLE);
      findViewById(R.id.periodChooserFragment).setVisibility(View.GONE);
    }
    else {
      findViewById(R.id.fixedPaymentBlock).setVisibility(View.GONE);
      findViewById(R.id.periodChooserFragment).setVisibility(View.VISIBLE);
    }
    return true;
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    calculateMenuItem = menu.add(R.string.calc);
    calculateMenuItem
      .setIcon(R.drawable.ic_action_calc)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    helpMenuItem = menu.add(R.string.typeHelpLbl);
    helpMenuItem.setIcon(android.R.drawable.ic_menu_help);
    settingMenuItem = menu.add("Setting");
    settingMenuItem.setIcon(android.R.drawable.ic_menu_manage);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item == calculateMenuItem) {
      calculate(findViewById(R.id.calcButton));
    }
    else if (item == helpMenuItem) {
      startActivity(new Intent(this, TypeHelpActivity.class));
    }
    else if (item == settingMenuItem) {
      startActivity(new Intent(this, SettingsActivity.class));
    }
    return super.onOptionsItemSelected(item);
  }

  public void calculate(View view) {
    CalculateTask task = new CalculateTask(StartActivity.this);
    task.execute();
  }


  @Override
  public void onConfigurationChanged(Configuration newConfig) {

    if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
      setInputType(InputType.TYPE_NULL
        , R.id.amountEdit
        , R.id.interestEdit
        , R.id.periodMonthEdit
        , R.id.periodYearEdit
        , R.id.downPaymentEdit
        , R.id.disposableCommissionEdit
        , R.id.monthlyCommissionEdit
        , R.id.residueEdit
      );

    }
    else {
      setInputType(InputType.TYPE_CLASS_NUMBER
        , R.id.amountEdit
        , R.id.interestEdit
        , R.id.periodMonthEdit
        , R.id.periodYearEdit
        , R.id.downPaymentEdit
        , R.id.disposableCommissionEdit
        , R.id.monthlyCommissionEdit
        , R.id.residueEdit
      );

    }

    super.onConfigurationChanged(newConfig);
  }

  private void setInputType(int type, int... ids) {
    for (int id : ids) {
      ((EditText)findViewById(id)).setInputType(type);
    }
  }


  public class CalculateTask extends AsyncTask<Void, Void, Loan> {

    private final Context context;
    private final ProgressDialog dialog;
    private FieldNumberFormatException numberFormatException;
    private Exception exception;

    public CalculateTask(Context context) {
      this.context = context;
      dialog = new ProgressDialog(this.context);
    }


    @Override
    protected void onPreExecute() {
      this.dialog.setIndeterminate(true);
      this.dialog.setMessage("Calculating loan ...");
      this.dialog.show();
    }

    @Override
    protected Loan doInBackground(Void... params) {
      Loan loan = new Loan();
      try {
        loan.setLoanType(getSupportActionBar().getSelectedNavigationIndex());
        loan.setAmount(Utils.getNumber((EditText)findViewById(R.id.amountEdit)));

        if (isEffectiveRate()) {
          BigDecimal effectiveRate = Utils.getNumber((EditText)findViewById(R.id.interestEdit));
          BigDecimal nominalRate = new BigDecimal(1200 * (Math.pow(1 + effectiveRate.doubleValue() / 100, (double)1 / (double)12) - 1));
          loan.setInterest(nominalRate);

        }
        else {

          loan.setInterest(Utils.getNumber((EditText)findViewById(R.id.interestEdit)));
        }


        loan.setDownPayment(Utils.getNumber((EditText)findViewById(R.id.downPaymentEdit), BigDecimal.ZERO));
        loan.setDownPaymentType(((Spinner)findViewById(R.id.downPaymentType)).getSelectedItemPosition());

        loan.setDisposableCommission(Utils.getNumber((EditText)findViewById(R.id.disposableCommissionEdit), BigDecimal.ZERO));
        loan.setDisposableCommissionType(((Spinner)findViewById(R.id.disposableCommissionType)).getSelectedItemPosition());

        loan.setMonthlyCommission(Utils.getNumber((EditText)findViewById(R.id.monthlyCommissionEdit), BigDecimal.ZERO));
        loan.setMonthlyCommissionType(((Spinner)findViewById(R.id.monthlyCommissionType)).getSelectedItemPosition());

        loan.setResidue(Utils.getNumber((EditText)findViewById(R.id.residueEdit), BigDecimal.ZERO));
        loan.setResidueType(((Spinner)findViewById(R.id.residueType)).getSelectedItemPosition());

        if (calculator instanceof FixedPaymentCalculator) {
          loan.setFixedPayment(Utils.getNumber((EditText)findViewById(R.id.fixedPaymentEdit), BigDecimal.ZERO));
        }
        else {
          BigDecimal periodInMonths = ((PeriodChooserFragment)getSupportFragmentManager().findFragmentById(R.id.periodChooserFragment)).getPeriodInMonths();
          loan.setPeriod(periodInMonths.intValue());
          if (periodInMonths.intValue() == 0) {
            throw new FieldNumberFormatException(R.id.periodMonthEdit, "Period should be greater that zero");
          }
        }

        calculator.calculate(loan);

      }
      catch (FieldNumberFormatException e) {
        numberFormatException = e;


      }
      catch (Exception e) {
        e.printStackTrace();
        exception = e;
      }
      return loan;
    }


    @Override
    protected void onPostExecute(Loan loan) {
      StartActivity.loan = loan;
      if (this.dialog.isShowing()) {
        this.dialog.dismiss();
      }
      if (numberFormatException == null) {
        Intent myIntent = new Intent(StartActivity.this, ResultActivity.class);
        startActivity(myIntent);
      }
      else if (exception != null) {
        Toast.makeText(StartActivity.this, exception.toString(), Toast.LENGTH_SHORT);
      }
      else {
        showError();
      }
    }

    private void showError() {
      AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.Theme_Sherlock_Light_Dialog);
      switch (numberFormatException.getId()) {
        case R.id.amountEdit:
          builder.setMessage(R.string.errorAmount);
          break;
        case R.id.interestEdit:
          builder.setMessage(R.string.errorInterest);
          break;
        case R.id.fixedPaymentEdit:
          builder.setMessage(R.string.errorFixedAmount);
          break;
        case R.id.periodMonthEdit:
        case R.id.periodYearEdit:
          builder.setMessage(R.string.errorPeriod);
          break;
        case R.id.downPaymentEdit:
          builder.setMessage(R.string.errorDownPayment);
          break;
        case R.id.disposableCommissionEdit:
          builder.setMessage(R.string.errorDispCommission);
          break;
        case R.id.monthlyCommissionEdit:
          builder.setMessage(R.string.errorMonthlyCommission);
          break;
        case R.id.residueEdit:
          builder.setMessage(R.string.errorResidue);
          break;
        default:
          builder.setMessage(numberFormatException.getMessage());
      }

      builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          focusOnView(findViewById(numberFormatException.getId()));
        }
      });

      builder.create().show();

    }
  }


  private final void focusOnView(final View view) {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        ScrollView mainScrollView = (ScrollView)findViewById(R.id.mainScrollView);
        mainScrollView.scrollTo(0, view.getBottom());
        view.requestFocus();
      }
    });
  }

}
