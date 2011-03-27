package ee.smkv.calc.loan.export;

import android.content.res.Resources;
import android.os.Environment;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;
import ee.smkv.calc.loan.R;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

/**
 * @author samko
 */
public class CSVScheduleCreator {
  final static int MODE = BigDecimal.ROUND_HALF_EVEN;
  public final static String ENCODING = "UTF-16LE";
  Resources resources;
  Loan loan;

  public CSVScheduleCreator(Loan loan, Resources resources) {
    this.loan = loan;
    this.resources = resources;
  }


  public void assertDataWriteEnabled(){

    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
      // OK
    }
    else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      // We can only read the media
      throw new RuntimeException("We can only read the media"); //TODO localize message
    }
    else {
     throw new RuntimeException("media not available"); //TODO localize message
    }

  }


  public void createSchedule(Writer out) throws IOException {
    out.write('\uFEFF');
    out.write(encode(R.string.paymentNr));
    char tab = '\t';
    char ln = '\n';
    out.write(tab);
    out.write(encode(R.string.paymentBalance));
    out.write(tab);
    out.write(encode(R.string.paymentPrincipal));
    out.write(tab);
    out.write(encode(R.string.paymentInterest));
    out.write(tab);
    out.write(encode(R.string.paymentTotal));
    out.write(ln);

    int i = 0;
    for (Payment payment : loan.getPayments()) {
      out.write(encode(payment.getNr().toString()));
      out.write(tab);
      out.write(encode(payment.getBalance().setScale(2, MODE).toPlainString()));
      out.write(tab);
      out.write(encode(payment.getPrincipal().setScale(2, MODE).toPlainString()));
      out.write(tab);
      out.write(encode(payment.getInterest().setScale(2, MODE).toPlainString()));
      out.write(tab);
      out.write(encode(payment.getAmount().setScale(2, MODE).toPlainString()));
      out.write(ln);
    }

  }


  private char[] encode(int id) {
//    try {
//      return resources.getString(id).getBytes(ENCODING);
//    }
//    catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
    return resources.getString(id).toCharArray();
  }

  private char[] encode(String text) {
//    try {
//      return text.getBytes(ENCODING);
//    }
//    catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
    return text.toCharArray();
  }

  public String getFileName() {
    return "loan-"+loan.getAmount()+"-"+loan.getInterest()+"%-"+loan.getPeriod()+"M.csv";
  }
}
