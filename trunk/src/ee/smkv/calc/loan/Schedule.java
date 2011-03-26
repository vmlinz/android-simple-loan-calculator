package ee.smkv.calc.loan;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TableLayout;
import ee.smkv.calc.loan.chart.LoanChart;
import ee.smkv.calc.loan.export.Exporter;
import ee.smkv.calc.loan.export.HtmlScheduleCreator;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * @author Andrei Samkov
 */
public class Schedule extends Activity {
    int mode = BigDecimal.ROUND_HALF_EVEN;
    TableLayout table;
    protected static final String UTF = "UTF-8";
    public static Loan loan;
    WebView webview = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webview = new WebView(this);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webview.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

        webview.addJavascriptInterface(this, "schedule");

        setContentView(webview);
        AsyncTask task = new AsyncTask() {
            private final ProgressDialog dialog = new ProgressDialog(Schedule.this);

            @Override
            protected void onPreExecute() {
                this.dialog.setMessage(getResources().getString(R.string.scheduleLoading));
                this.dialog.show();
            }

            @Override
            protected Object doInBackground(Object... objects) {
                final Loan loan = getLoan();
                webview.loadDataWithBaseURL(null, createHtml(loan), "text/html", UTF, "about:blank");
                return null;
            }

            @Override
            protected void onPostExecute(final Object unused) {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            }

        };
        task.execute();
    }

    private String createHtml(Loan loan) {
        HtmlScheduleCreator creator = new HtmlScheduleCreator(loan, getResources());
        StringBuilder sb = new StringBuilder();
        String script = LoanChart.getScript();
        sb.append("<html><head><style>" +
                "body{background:#FFF;color:#111; font-family:Verdana;}table{border-spacing:0px 0px; font-size:11px; width:100%}td{padding:5px; }th{padding:5px;text-align:left;}.odd{background:#EEE;}.odd th{border-bottom: 1px solid black;} .odd td{border-bottom: 1px solid #CCC;}.even{background:#FAFAFA;}#closeBtn{width:100%;padding:10px} table{border-radius: 5px;}" +
                "</style><script> ")
                .append(script)
                .append("</script>")
                .append("</head><body>");

        try {
            creator.appendHtmlScheduleTable(sb);
            creator.appendHtmlChart(sb, webview.getWidth() - 10, 200);
            creator.appendHtmlButtons(sb);
        } catch (Exception e) {
            Log.v(Schedule.class.getSimpleName(), e.getMessage(), e);
        }


        String html = sb.append("</body></html>").toString();
        return html;
    }


    private String encode(int id) throws UnsupportedEncodingException {
        return getResources().getString(id);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public float getTotalInterestPercent() {
        return Math.round(loan.getTotalInterests().floatValue() * 100 / loan.getTotalAmount().floatValue());
    }

    private Loan getLoan() {
        return loan;
    }


    public String getInterestPointsData() {
        float[] points = LoanChart.getPoints(loan, LoanChart.INTERESTS);
        return Arrays.toString(points);
    }

    public String getPrincipalPointsData() {
        float[] points = LoanChart.getPoints(loan, LoanChart.PRINCIPAL);
        return Arrays.toString(points);
    }

    public String getPaymentPointsData() {
        float[] points = LoanChart.getPoints(loan, LoanChart.PAYMENT);
        return Arrays.toString(points);
    }

    public String getXLabels() {
//        float[] points = LoanChart.getPoints(loan, LoanChart.LABEL);
//        String[] labels = new String[points.length];
//        for (int i = 0; i < points.length; i++) {
//            labels[i] = "" + Float.valueOf(points[i]).intValue();
//        }
//        return Arrays.toString(labels);
        return "[]";
    }

    public String getLegend() {
        return "[\"" +
                getResources().getString(R.string.paymentPrincipal) + "\",\"" +
                getResources().getString(R.string.paymentInterest) + "\",\"" +
                getResources().getString(R.string.paymentTotal) + "\"]";
    }

    public String getPieTitle() {
//        return getResources().getString(R.string.chartTitle);
        return "";
    }

    public String getLoanAmountLabel() {
        return getResources().getString(R.string.amount);
    }

    public String getLoanInterestLabel() {
        return getResources().getString(R.string.paymentInterest);
    }

    public float getLoanAmount() {
        return loan.getAmount().floatValue();
    }

    public float getLoanInterest() {
        return loan.getTotalInterests().floatValue();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedulemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exportEmailMenu:
                Exporter.sendToEmail(loan, getResources(), this);
                break;
            case R.id.exportExcelMenu:
                File file = Exporter.exportToCSVFile(loan, getResources());
                new OkDialogWrapper(this, getResources().getString(R.string.fileCreated) + ' ' + file.getName()).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
