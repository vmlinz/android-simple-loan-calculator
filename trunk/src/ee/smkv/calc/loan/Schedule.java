package ee.smkv.calc.loan;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TableLayout;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;



/**
 * @author Andrei Samkov
 */
public class Schedule extends Activity {
  int mode = BigDecimal.ROUND_HALF_EVEN;
  TableLayout table;
  protected static final String UTF = "UTF-8";


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final WebView webview = new WebView(this);
    WebSettings webSettings = webview.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setSupportZoom(false);

    webview.addJavascriptInterface(this , "schedule");

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
        webview.loadDataWithBaseURL (null, createHtml(loan), "text/html", UTF, "about:blank");
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
              "</style></head><body>");

    try {
      appendHtmlScheduleTable(loan, sb);
      appendHtmlBars(loan, sb);

      sb.append("<button id='closeBtn' onclick='window.schedule.finish();'>")
      .append(encode(R.string.close))
      .append("</button>");
    }
    catch (Exception e) {
      Log.v(Schedule.class.getSimpleName() , e.getMessage(), e);
    }



    return sb.append("</body></html>").toString();
  }

  private String encode(int id) throws UnsupportedEncodingException {
    //return URLEncoder.encode(getResources().getString(id),UTF).replaceAll("\\+"," ");
    return getResources().getString(id);
  }

  private void appendHtmlScheduleTable(Loan loan, StringBuilder sb) throws UnsupportedEncodingException {
    sb
      .append("<table><tr class=\"odd\"><th>")
      .append(encode(R.string.paymentNr))
      .append("</th><th>")
      .append(encode(R.string.paymentBalance))
      .append("</th><th>")
      .append(encode(R.string.paymentPrincipal))
      .append("</th><th>")
      .append(encode(R.string.paymentInterest))
      .append("</th><th>")
      .append(encode(R.string.paymentTotal))
      .append("</th></tr>");
    int i = 0;
    for (Payment payment : loan.getPayments()) {
      sb.append("<tr class=\"")
        .append(i++ % 2 == 0 ? "even" : "odd")
        .append("\"><td>")
        .append(payment.getNr().toString())
        .append("</td><td>")
        .append(payment.getBalance().setScale(2, mode).toPlainString())
        .append("</td><td>")
        .append(payment.getPrincipal().setScale(2, mode).toPlainString())
        .append("</td><td>")
        .append(payment.getInterest().setScale(2, mode).toPlainString())
        .append("</td><td>")
        .append(payment.getAmount().setScale(2, mode).toPlainString())
        .append("</td></tr>");
    }
    sb
      .append("<tr><th>&nbsp;</th><th>&nbsp;</th><th>")
      .append(loan.getAmount().setScale(2, mode).toPlainString())
      .append("</th><th>")
      .append(loan.getTotalInterests().setScale(2, mode).toPlainString())
      .append("</th><th>")
      .append(loan.getTotalAmount().setScale(2, mode).toPlainString())
      .append("</th></tr>")
      .append("</table>");
  }


  private void appendHtmlBars(Loan loan, StringBuilder sb) throws UnsupportedEncodingException {

    BigDecimal amount = loan.getAmount().multiply(new BigDecimal("200")).divide(loan.getTotalAmount() ,0 , mode);
    BigDecimal interest = loan.getTotalInterests().multiply(new BigDecimal("200")).divide(loan.getTotalAmount() ,0 , mode);

    sb
      .append("<table class='bars' height='200px'>" +
              "</tr>" +
              "<tr>" +
              "<td><table class='bar' height='" + amount.toPlainString() +"px'><tr><td></td></tr></table></td>" +
              "<td><table class='bar' height='" + interest.toPlainString() +"px'><tr><td></td></tr></table></td>" +
              "<td><table class='bar' height='200px'><tr><td></td></tr></table></td>" +
              "</tr>")
      .append("<tr><th>")
      .append(encode(R.string.chartAmount)).append("<br />")
      .append(loan.getAmount().setScale(2, mode).toPlainString())
      .append("</th><th>")
      .append(encode(R.string.chartInterest)).append("<br />")
      .append(loan.getTotalInterests().setScale(2, mode).toPlainString())
      .append("</th><th>")
      .append(encode(R.string.chartTotal)).append("<br />")
      .append(loan.getTotalAmount().setScale(2, mode).toPlainString())
      .append("</th></tr>")
      .append("</table>");

  }

  @Override
  protected void onResume() {
    super.onResume();

  }


  private Loan getLoan() {
    return (Loan)getIntent().getSerializableExtra(Loan.class.getName());
  }

}
