package ee.smkv.calc.loan;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TableLayout;
import ee.smkv.calc.loan.chart.LoanChart;
import ee.smkv.calc.loan.export.HtmlScheduleCreator;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;


/**
 * @author Andrei Samkov
 */
public class Schedule extends Activity {
  int mode = BigDecimal.ROUND_HALF_EVEN;
  TableLayout table;
  protected static final String UTF = "UTF-8";
  public static Loan loan;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final WebView webview = new WebView(this);
    WebSettings webSettings = webview.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setSupportZoom(false);

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
        //        webview.loadData( createHtml(loan), "text/html", UTF);
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
    sb.append("<html><head><style>" +
              "body{background:#000000;color:#ffffff;}" +
              "table{border-spacing:0px 0px; font-size:11px; width:100%}" +
              "td{padding:5px; }" +
              "th{padding:5px;text-align:left;}" +
              ".odd{background:#555555;}" +
              ".even{background:#777777;}" +
              ".bars td{vertical-align:bottom; width:30%}" +
              ".bar{width:100%;background:#CCCCCC;}" +
              "#closeBtn{width:100%;padding:10px}" +
              "</style><script>")
      .append(LoanChart.getScript())
      .append("</script>")
      .append("</head><body>");

    try {
      creator.appendHtmlScheduleTable(sb);
      //creator.appendHtmlBars(sb);
      creator.appendHtmlChart(sb);
      creator.appendHtmlButtons(sb);
    }
    catch (Exception e) {
      Log.v(Schedule.class.getSimpleName(), e.getMessage(), e);
    }


    return sb.append("</body></html>").toString();
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


}
