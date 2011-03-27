package ee.smkv.calc.loan.export;

import android.content.res.Resources;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;
import ee.smkv.calc.loan.R;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author samko
 */
public class TextScheduleCreator {

  int mode = BigDecimal.ROUND_HALF_EVEN;
  Resources resources;
  Loan loan;

  public TextScheduleCreator(Loan loan, Resources resources) {
    this.loan = loan;
    this.resources = resources;
  }

  public void appendTextScheduleTable(StringBuilder sb) {
    BigDecimal max = loan.getMaxMonthlyPayment();
    BigDecimal min = loan.getMinMonthlyPayment();
    String monthlyPayment = "";
    String amountTotal = "";
    String interestTotal = "";
    String periodTotal = "";
    if (max.compareTo(min) == 0) {
      monthlyPayment = max.setScale(2, mode).toPlainString();
    }
    else {
      monthlyPayment = max.setScale(2, mode).toPlainString() + " - " + min.setScale(2, mode).toPlainString();
    }


    amountTotal = loan.getTotalAmount().setScale(2, mode).toPlainString();
    interestTotal = loan.getTotalInterests().setScale(2, mode).toPlainString();
    periodTotal = loan.getPeriod().toString();


    sb
      .append(encode(R.string.app_name))
      .append('\n')
      .append('\n')
      .append(encode(R.string.type))
      .append(": ")
      .append(resources.getStringArray(R.array.types)[loan.getLoanType()])
      .append('\n')
      .append(encode(R.string.MonthlyAmountLbl))
      .append(": ")
      .append(monthlyPayment)
      .append('\n')
      .append(encode(R.string.IterestTotalLbl))
      .append(": ")
      .append(interestTotal)
      .append('\n')
      .append(encode(R.string.AmountTotalLbl))
      .append(": ")
      .append(amountTotal)
      .append('\n')
      .append(encode(R.string.paymentsCount))
      .append(": ")
      .append(periodTotal)
      .append('\n')
      .append('\n');


    String pnr = encode(R.string.paymentNr);
    String bal = encode(R.string.paymentBalance);
    String pri = encode(R.string.paymentPrincipal);
    String inr = encode(R.string.paymentInterest);
    String tor = encode(R.string.paymentTotal);
    sb
      .append(pnr)
      .append(' ').append('|').append(' ')
      .append(bal)
      .append(' ').append('|').append(' ')
      .append(pri)
      .append(' ').append('|').append(' ')
      .append(inr)
      .append(' ').append('|').append(' ')
      .append(tor)
      .append("\n");
    int i = 0;
    for (Payment payment : loan.getPayments()) {
      sb
        .append( pad(payment.getNr().toString(), pnr.length()))
        .append(' ').append('|').append(' ')
        .append(pad(payment.getBalance().setScale(2, mode).toPlainString(), bal.length()))
        .append(' ').append('|').append(' ')
        .append(pad(payment.getPrincipal().setScale(2, mode).toPlainString(), pri.length()))
        .append(' ').append('|').append(' ')
        .append(pad(payment.getInterest().setScale(2, mode).toPlainString(), inr.length()))
        .append(' ').append('|').append(' ')
        .append(pad(payment.getAmount().setScale(2, mode).toPlainString(), tor.length()))
        .append('\n');
    }
  }

  private String pad(String s, int length) {
    if ( s.length() < length ){
      char[] spaces = new char[length - s.length()];
      Arrays.fill(spaces , ' ');
      return new String(spaces) + s;
    }
    return s;
  }


  private String encode(int id) {
    return resources.getString(id);
  }


}
