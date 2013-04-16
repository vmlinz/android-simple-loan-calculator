package ee.smkv.calc.loan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    public static Loan loan;
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
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        calculator = CALCULATORS[itemPosition];
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
            calculate(null);
        } else if (item == helpMenuItem) {
            startActivity(new Intent(this, TypeHelpActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculate(View view) {
        try {
            Loan loan = new Loan();
            loan.setLoanType(0);
            loan.setAmount(Utils.getNumber((EditText) findViewById(R.id.amountEdit)));
            loan.setInterest(Utils.getNumber((EditText) findViewById(R.id.interestEdit)));

            BigDecimal periodInMonths = ((PeriodChooserFragment) getSupportFragmentManager().findFragmentById(R.id.periodChooserFragment)).getPeriodInMonths();
            loan.setPeriod(periodInMonths.intValue());

            calculator.calculate(loan);
            StartActivity.loan = loan;
            Intent myIntent = new Intent(this, ResultActivity.class);
            startActivity(myIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
        }
    }
}
