package ee.smkv.calc.loan.chart;

import ee.smkv.calc.loan.Loan;
import ee.smkv.calc.loan.Payment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author samko
 */
public class LoanChart {
  public final static int INTERESTS = 0, PRINCIPAL = 1, PAYMENT = 2 , LABEL = 3;

  public static String getScript() {
      StringBuilder sb = new StringBuilder();
      sb.append(new String(convertStreamToBytes(LoanChart.class.getResourceAsStream("RGraph.common.core.js"))));
      sb.append(new String(convertStreamToBytes(LoanChart.class.getResourceAsStream("RGraph.line.js"))));
      sb.append(new String(convertStreamToBytes(LoanChart.class.getResourceAsStream("RGraph.pie.js"))));
      sb.append(new String(convertStreamToBytes(LoanChart.class.getResourceAsStream("chart.js"))));
    return sb.toString();
  }

  public static float[] getPoints(Loan loan, int type) {
    int m = 12;
    int size = loan.getPeriod() > m ? (loan.getPeriod() == 18 ? 9 : m) : loan.getPeriod();
    int step = loan.getPeriod() > size ? loan.getPeriod() / size : 1;
    float[] points = new float[size];
    int i = 0, c = 0, nr = 0;
    float s = 0;
    for (Payment p : loan.getPayments()) {
      i++;
      switch (type) {
        case INTERESTS:
          s += p.getInterest().floatValue();
          break;
        case PRINCIPAL:
          s += p.getPrincipal().floatValue();
          break;
        case PAYMENT:
          s += p.getAmount().floatValue();
          break;
      }

      c++;
      if (i % step == 0) {
          if (type == LABEL) {
              points[nr++] = i;
          }else{
              points[nr++] = s / c;
              c = 0;
              s = 0;
          }
      }
    }
    return points;
  }

  public static byte[] convertStreamToBytes(InputStream is) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[1024];
      int read = 0;
      while ((read = is.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
    }
    catch (Exception e) {

    }
    finally {
      try {
        is.close();
      }
      catch (IOException e) {

      }
    }
    return out.toByteArray();
  }

}
