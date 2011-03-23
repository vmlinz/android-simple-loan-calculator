package ee.smkv.calc.loan;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.PeriodicSync;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ee.smkv.calc.loan.export.CSVScheduleCreator;
import ee.smkv.calc.loan.export.TextScheduleCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
            monthlyAmountVText, amountTotalVText, interestTotalVText,
            periodLblYear, periodLblMonth;

    EditText amountEText, interestEText, fixedPaymentEText, periodYear, periodMonth;
    Spinner typeSpinner;
    Button calculateButton, scheduleButton, typeHelpButton, typeHelpCloseButton,
            periodIncYear, periodDecYear, periodIncMonth, periodDecMonth;
    ScrollView scrollView;
    LinearLayout resultContainer;

    Loan loan = new Loan();
    Calculator calculator;

    int periodInMonths = 0;


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
        storeManager.loadTextViews(amountEText, interestEText, fixedPaymentEText, periodYear, periodMonth);
        storeManager.loadSpinners(typeSpinner);
        if (periodYear.getText().toString() == null || periodYear.getText().toString().length() == 0) {
            periodYear.setText("0");
        }
        if (periodMonth.getText().toString() == null || periodMonth.getText().toString().length() == 0) {
            periodMonth.setText("0");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        storeManager.storeTextViews(amountEText, interestEText, fixedPaymentEText, periodYear, periodMonth);
        storeManager.storeSpinners(typeSpinner);
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

        View.OnClickListener periodListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (view == periodIncYear) {
                    periodYear.setText("" + (getInteger(periodYear) + 1));
                } else if (view == periodDecYear) {
                    periodYear.setText("" + (getInteger(periodYear) - 1));
                } else if (view == periodIncMonth) {
                    periodMonth.setText("" + (getInteger(periodMonth) + 1));
                } else if (view == periodDecMonth) {
                    periodMonth.setText("" + (getInteger(periodMonth) - 1));
                }
            }
        };

        periodIncYear.setOnClickListener(periodListener);
        periodDecYear.setOnClickListener(periodListener);
        periodIncMonth.setOnClickListener(periodListener);
        periodDecMonth.setOnClickListener(periodListener);

        periodYear.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changePeriod(periodYear);
            }
        });
        periodMonth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changePeriod(periodMonth);
            }
        });


        typeSpinner.setOnItemSelectedListener(this);
    }

    private int getInteger(EditText editText) {
        String s = editText.getText().toString().replace(',', '.');
        if (s != null && s.trim().length() != 0) {
            try {
                return new BigDecimal(s).intValue();
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private void changePeriod(EditText editable) {

        if (editable == periodYear) {
            int value = getInteger(periodYear);
            if (value > 50) {
                periodYear.setText("50");
                periodLblYear.setText(getResources().getString(R.string.periodYear5));
            } else if (value < 0) {
                periodYear.setText("0");
                periodLblYear.setText(getResources().getString(R.string.periodYear0));
            } else if (value == 1) {
                periodLblYear.setText(getResources().getString(R.string.periodYear1));
            } else if ((value > 1 && value < 5) ||(value > 21 && value < 25) ||(value > 31 && value < 35) ||(value > 41 && value < 45) ) {
                periodLblYear.setText(getResources().getString(R.string.periodYear2));
            } else {
                periodLblYear.setText(getResources().getString(R.string.periodYear5));
            }

        } else  if (editable == periodMonth) {
            int value = getInteger(periodMonth);
            if (value > 12) {
                periodMonth.setText("12");
                periodLblMonth.setText(getResources().getString(R.string.periodMonth5));
            } else if (value < 0) {
                periodMonth.setText("0");
                periodLblMonth.setText(getResources().getString(R.string.periodMonth0));
            } else if (value == 1) {
                periodLblMonth.setText(getResources().getString(R.string.periodMonth1));
            } else if (value > 1 && value < 5) {
                periodLblMonth.setText(getResources().getString(R.string.periodMonth2));
            } else {
                periodLblMonth.setText(getResources().getString(R.string.periodMonth5));
            }
        }

    }


    private void changeCalculatorType() {
        setTitle((String) typeSpinner.getSelectedItem());

        int selected = typeSpinner.getSelectedItemPosition();
        calculator = CALCULATORS[selected];
        boolean fixed = selected == 2;

        fixedPaymentLabel.setVisibility(!fixed ? View.GONE : View.VISIBLE);
        fixedPaymentEText.setVisibility(!fixed ? View.GONE : View.VISIBLE);
        periodLabel.setVisibility(fixed ? View.GONE : View.VISIBLE);
    }

    private void showSchedule() {
        Intent scheduleActivityIntent = new Intent(MainScreen.this, Schedule.class);
        Schedule.loan = loan;
        startActivity(scheduleActivityIntent);
    }


    private void init() {
        amountEText = (EditText) findViewById(R.id.Amount);
        interestEText = (EditText) findViewById(R.id.Interest);
        monthlyAmountVText = (TextView) findViewById(R.id.MonthlyAmount);
        amountTotalVText = (TextView) findViewById(R.id.AmountTotal);
        interestTotalVText = (TextView) findViewById(R.id.IterestTotal);
        calculateButton = (Button) findViewById(R.id.Calc);
        scrollView = (ScrollView) findViewById(R.id.ScrollView);
        typeSpinner = (Spinner) findViewById(R.id.Type);
        fixedPaymentLabel = (TextView) findViewById(R.id.fixedPaymentLbl);
        periodLabel = (TextView) findViewById(R.id.periodLbl);
        fixedPaymentEText = (EditText) findViewById(R.id.fixedPaymentText);
        periodTotalLabel = (TextView) findViewById(R.id.PeriodTotal);
        monthlyAmountLabel = (TextView) findViewById(R.id.MonthlyAmountLbl);
        scheduleButton = (Button) findViewById(R.id.ScheduleButton);
        typeHelpButton = (Button) findViewById(R.id.TypeHelp);
        typeHelpCloseButton = (Button) findViewById(R.id.TypeHelpClose);
        resultContainer = (LinearLayout) findViewById(R.id.resultContainer);

        calculateButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.calculator), null, null, null);
        scheduleButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.table), null, null, null);
        typeHelpButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.help), null, null, null);
        typeHelpButton.setText("");

        periodIncYear = (Button) findViewById(R.id.periodIncYear);
        periodDecYear = (Button) findViewById(R.id.periodDecYear);
        periodIncMonth = (Button) findViewById(R.id.periodIncMonth);
        periodDecMonth = (Button) findViewById(R.id.periodDecMonth);


        periodLblYear = (TextView) findViewById(R.id.periodLblYear);
        periodLblMonth = (TextView) findViewById(R.id.periodLblMonth);

        periodYear = (EditText) findViewById(R.id.periodYear);
        periodMonth = (EditText) findViewById(R.id.periodMonth);
    }


    protected void showError(Exception e) {
        new ErrorDialogWrapper(this, e).show();
    }


    private void calculate() {
        try {
            loan = new Loan();
            loadLoanDataFromUI();
            if (periodInMonths > 0) {
                calculator.calculate(loan);
                showCalculatedData();
                scheduleButton.setEnabled(true);
                Toast.makeText(this, getResources().getText(R.string.msgCalculated), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
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
        } else {
            monthlyPayment = max.setScale(2, mode).toPlainString() + " - " + min.setScale(2, mode).toPlainString();
        }
        monthlyAmountVText.setText(monthlyPayment);

        amountTotalVText.setText(loan.getTotalAmount().setScale(2, mode).toPlainString());
        interestTotalVText.setText(loan.getTotalInterests().setScale(2, mode).toPlainString());
        periodTotalLabel.setText(loan.getPeriod().toString());
    }

    private void loadLoanDataFromUI() {
        loan.setLoanType(typeSpinner.getSelectedItemPosition());
        loan.setAmount(getNumber(amountEText, R.string.errorAmount));
        loan.setInterest(getNumber(interestEText, R.string.errorInterest));

        periodInMonths = getInteger(periodYear) * 12 + getInteger(periodMonth);

        loan.setPeriod(periodInMonths);
        if (fixedPaymentEText.getVisibility() == View.VISIBLE) {
            loan.setFixedPayment(getNumber(fixedPaymentEText, R.string.errorFixedAmount));
        }
    }

    private BigDecimal getNumber(EditText text, int errorCode) {
        BigDecimal decimal;
        try {
            String val = text.getText().toString();
            val = val.replace(',', '.');
            decimal = new BigDecimal(val);
        } catch (Exception e) {
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
                        (fixedPaymentEText.getText() != null && fixedPaymentEText.getText().toString().trim().length() > 0) &&
                periodInMonths > 0);
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
            case R.id.exportEmailMenu:
                sendToEmail();
                break;
            case R.id.exportExcelMenu:
                File file = exportToCSVFile();
                new OkDialogWrapper(this, getResources().getString(R.string.fileCreated) + file.getName()).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void sendToEmail() {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/text");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " - export");

        StringBuilder sb = new StringBuilder();
        new TextScheduleCreator(loan, getResources()).appendTextScheduleTable(sb);
        emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportToCSVFile()));
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.sendEmail)));
    }

    private File exportToCSVFile() {
        try {
            CSVScheduleCreator csvScheduleCreator = new CSVScheduleCreator(loan, getResources());
            csvScheduleCreator.assertDataWriteEnabled();
            String fileName = csvScheduleCreator.getFileName();
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + File.separator + fileName);

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), CSVScheduleCreator.ENCODING);
            csvScheduleCreator.createSchedule(writer);
            writer.flush();
            writer.close();
            return file;
        } catch (Exception e) {
            showError(e);
            e.printStackTrace();
            return null;
        }
    }

    private void openCompareActivity() {
        Intent compareActivityIntent = new Intent(MainScreen.this, CompareActivity.class);
        startActivity(compareActivityIntent);
    }
}