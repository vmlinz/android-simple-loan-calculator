package ee.smkv.calc.loan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import ee.smkv.calc.loan.export.Exporter;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.utils.OkDialogWrapper;

import java.io.File;


public abstract class AbstractScheduleActivity extends Activity{
    protected static final String UTF = "UTF-8";
    WebView webview = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webview = new WebView(getActivity());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webview.addJavascriptInterface(getActivity(), "schedule");

        setContentView(webview);
        AsyncTask task = new AsyncTask() {
            private final ProgressDialog dialog = new ProgressDialog(getActivity());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedulemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exportEmailMenu:
                Exporter.sendToEmail(getLoan(), getResources(), getActivity());
                break;
            case R.id.exportExcelMenu:
                File file = Exporter.exportToCSVFile(getLoan(), getResources());
                new OkDialogWrapper(this, getResources().getString(R.string.fileCreated) + ' ' + file.getName()).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    protected String getStyle() {
        return "body{background:#FFF;color:#111; font-family:Verdana;}table{border-spacing:0px 0px; font-size:11px; width:100%}td{padding:5px; }th{padding:5px;text-align:left;}.odd{background:#EEE;}.odd th{border-bottom: 1px solid black;} .odd td{border-bottom: 1px solid #CCC;}.even{background:#FAFAFA;}#closeBtn{width:100%;padding:10px} table{border-radius: 5px;}";
    }

    protected abstract Activity getActivity();

    protected abstract String createHtml(Loan loan);

    protected abstract Loan getLoan();
}
