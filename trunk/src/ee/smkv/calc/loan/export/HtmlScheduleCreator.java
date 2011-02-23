package ee.smkv.calc.loan.export;

import android.content.res.Resources;
import ee.smkv.calc.loan.Loan;
import ee.smkv.calc.loan.Payment;
import ee.smkv.calc.loan.R;
import ee.smkv.calc.loan.chart.LoanChart;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author samko
 */
public class HtmlScheduleCreator {

  int mode = BigDecimal.ROUND_HALF_EVEN;
  Resources resources;
  Loan loan;

  public HtmlScheduleCreator(Loan loan, Resources resources) {
    this.loan = loan;
    this.resources = resources;
  }

  public void appendHtmlScheduleTable(StringBuilder sb) {
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


  public void appendHtmlBars(StringBuilder sb) {

    BigDecimal amount = loan.getAmount().multiply(new BigDecimal("200")).divide(loan.getTotalAmount(), 0, mode);
    BigDecimal interest = loan.getTotalInterests().multiply(new BigDecimal("200")).divide(loan.getTotalAmount(), 0, mode);

    sb
      .append("<table class='bars' height='200px'>" +
              "</tr>" +
              "<tr>" +
              "<td><table class='bar' height='" + amount.toPlainString() + "px'><tr><td></td></tr></table></td>" +
              "<td><table class='bar' height='" + interest.toPlainString() + "px'><tr><td></td></tr></table></td>" +
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


  public void appendHtmlButtons(StringBuilder sb) {
    sb.append("<button id='closeBtn' onclick='window.schedule.finish();'>")
      .append("<img align=\"absmiddle\" src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAABWtJREFUWIWll/tvFFUUxz9zZ3a724YCTQuy4aVYi6RAhYRkodI0gUQwmpSgPAqJ0loCiQE0MeEv8BcVEiOJvDRCJVECSnyEaKxtKqs10BZQIWt4VhZoA2Vpmd2dx/WH3Rm3Zbc74Pen3XPPPd/vPXPvufcoPALqplJXP5X68BTC5UHKKydQCRAdJDqgMxCJEWnro629j3avMZVCDjNKmdFcTfPrc3i91E+pl6DxFPFP/uST/efZfzXO1ccWsPFZNu6qY5df4PdCPBopm9SOdnYc+otDjyRgfgXzd9exe+EkFmbbRWiu0GoaNK1quaaMq1DE5CoBYN+6aMv7/dK8+INp9hw37Rvn7Ox5p29zens723v76S0oYEmIJa0v0FoWoMyxqTMWqUWrdxepT4XVwusG61LESh7dnrSudlmO7V6Ke43f09jxDx15BcyvYP7XL/G1S676Cbz6YcBX2+LLzWSAUEEROYeNzr1G4os3E1gpAIYMhlZ8xYrsTIwQ0LaaNiftSkmZEnzjWFCtrPO06nyw/u609H2rdDnULyH9OeqPUu+Mu8HXz2Z9SzUtaauf4i3fFDvkRs9x7JsXUJ941hNptr8omy7UWbWq0XXIQFqESgjdfsDt7n663QyESgh1N9Id1AgCBNbuCfie3+IDGP5gKfb1Myj+YorWfYyvpqEgefLIZmTqAWLaAkreSn9y49QBI9HanHD85nzGnL4h+gRAUzVNDrk6faHqkBs9x9Pk0gbbInlkM0bP8YLk2BaKtLGvn3H9fYubfOqsWjfjzdU0AwiArfPY6gwUrXq/KDuo4i8GXzD9ZwwR2eRpxmB6bhaKXn7Xje0KWDadZSU+SgDE1BqRvel8NQ0Urfs4vdMd5BDxEDmAUB/6ZOrTtaoIzRUApX5Kw1MIa+EphF3CBavTx802QWiuCGAkQUaEAy/kDrTqF7XUjXMpgJVPslIseoJFrsKZmUKTIS+UicThTSQPb/JMDqDNfdkNHp5CWHNuNACntObC6ExIywQzs6l9gbToAuSjOSonUCnKg5Q7BmVcxZiXk5MJKW0wEyhSgpRgJEDano6pUlLmckwsYqI2lnPeIKP+y8cJkoEY0BlwA93vHzOWu9sVkU67oiAVBbQAiiIK1gkAOXzH5bib5K6IDhJ1DPati3buaTmOmtDAXwz+YhQ1k0gPxSqbIzpIVHTdpMsxWFciVq5Jec/5hoMENhwsWCeyYf7xren8jsSIiEiMiEt05qjhmTyz4bwWK1fA2ROugJNXOCl+vMaPwwbDAHZfj21F210mrxXOqwjr707LeS3FU8Q7b9ApAPacZY/jlDz2djJbsUw9AEPPS55PhEzeR+qDI3ySX73jxt5/nv2QuYwOnOeAbqIDWNdOW0bnXsMJKqYtQCrCU5F5SASK62/8+qlhXU7vMUtiOQJcrJ/N+vhW4vGtxOPbgnHz8m+mzCDVfUymuo9Jrxjtb17uMuPbgnEn/mtzeM3hHVFTOl6ho6aCGhj5JJP6oFSCEwr2ELlgRTssfV+D7pz/nn56ln7JUmd8xHuvd4DeNc+wxifwYegYv39uiPEhoT61+LHehcapA4Z+cK1OcggA3UTfeJKNsWFiOQXEholFYkRWzmRlUCOItDDPnTCt6M+WmFQlxMRpeS+rEau+FLGSrU3J1E+7U8j0CbqT4M6a71iTXXfgURuTyVVCe261ps1erinB8YqYWpNuTPp6bKnfk+aFH0yz+6g5uqI+UmOSjZa5tLz3PO95WXU+7PyFnR/18lG+cc/NaeNsGssD/13dY2EgwUDrBVr/d3M6GrUhauunpdvzUj+l88qZB3B2gLPxFPFIjEjbddo6b9DpNea/akXrK4/1JNMAAAAASUVORK5CYII='/> ")
      .append(encode(R.string.close))
      .append("</button>");
  }

  public void appendHtmlChart(StringBuilder sb) {
    sb.append("<canvas id=\"pipe\" width=\"300\" height=\"150\">Pipe diagramm</canvas>")
      .append("<canvas id=\"line\" width=\"300\" height=\"150\">Loan amotrization</canvas>")
      .append("<script> drawPipe(window.schedule.getTotalInterestPercent() , 'Total interests' , 'Loan amount');drawLines(")
      .append(Arrays.toString(getIPoints())).append(',')
      .append(Arrays.toString(getPPoints())).append(',')
      .append(Arrays.toString(getAPoints()))
      .append(");</script>");
  }


  private String encode(int id) {
    return resources.getString(id);
  }

  public float[] getIPoints() {
    return LoanChart.getPoints(loan, LoanChart.INTERESTS);
  }

  public float[] getPPoints() {
    return LoanChart.getPoints(loan, LoanChart.PRINCIPAL);
  }

  public float[] getAPoints() {
    return LoanChart.getPoints(loan, LoanChart.PAYMENT);
  }

}
