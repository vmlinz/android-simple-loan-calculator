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
      //sb.append(new String(convertStreamToBytes(LoanChart.class.getResourceAsStream("chart.js"))));

      sb.append("\n" +
              "\n" +
              "//  --------------\n" +
              "function myDrawPie( title , amount , amountLbl , interest , interestLbl){\n" +
              "    var pie = new RGraph.Pie('pie', [amount ,interest]); // Create the pie object\n" +
              "    pie.Set('chart.labels', [amountLbl, interestLbl]);\n" +
              "    pie.Set('chart.gutter', 45);\n" +
              "    pie.Set('chart.highlight.style', '3d'); // Defaults to 3d anyway; can be 2d or 3d\n" +
              "    pie.Set('chart.linewidth', 2);\n" +
              "    pie.Set('chart.labels.sticks', true);\n" +
              "    pie.Set('chart.strokestyle', '#CCC');\n" +
              "    pie.Set('chart.colors', ['#009933','#ff3300']);\n" +
              "    pie.Set('chart.text.color', '#F26A00');\n" +
              "    pie.Set('chart.title.color', '#F26A00');\n" +
              "    pie.Set('chart.title',title);\n" +
              "    pie.Draw();\n" +
              "}\n" +
              "\n" +
              "function myDrawLine(principalData , interestData , paymentData ,xLabels){\n" +
              "    var line = new RGraph.Line(\"line\", [principalData,interestData,paymentData]);\n" +
              "    line.Set('chart.background.barcolor1', 'black');\n" +
              "    line.Set('chart.background.barcolor2', 'black');\n" +
              "    line.Set('chart.background.grid.color', '#777');\n" +
              "    line.Set('chart.background.grid.width', 0.5);\n" +
              "    line.Set('chart.colors', ['rgba(255,0,0,1)','rgba(0,255,0,1)','rgba(0,0,255,1)']);\n" +
              "    line.Set('chart.linewidth', 3);\n" +
              "    //line.Set('chart.filled', true);\n" +
              "    line.Set('chart.hmargin', 5);\n" +
              "    line.Set('chart.labels', xLabels);\n" +
              "    line.Set('chart.gutter', 20);\n" +
              "    line.Set('chart.text.size', 8);\n" +
              "    line.Set('chart.text.color','#F26A00');\n" +
              "    line.Draw();\n" +
              "}\n" +
              "\n" +
              "function myDrayAll(){\n" +
              "    try{\n" +
              "        log('3');\n" +
              "        myDrawPie(\n" +
              "            window.schedule.getPieTitle(),\n" +
              "            window.schedule.getLoanAmount(),\n" +
              "            window.schedule.getLoanAmountLabel(),\n" +
              "            window.schedule.getLoanInterest(),\n" +
              "            window.schedule.getLoanInterestLabel()\n" +
              "        );\n" +
              "        log('4');\n" +
              "        myDrawLine(\n" +
              "            window.schedule.getPrincipalPointsData(),\n" +
              "            window.schedule.getInterestPointsData(),\n" +
              "            window.schedule.getPaymentPointsData(),\n" +
              "            window.schedule.getXLabels()\n" +
              "        ); \n" +
              "    }catch( err ){\n" +
              "        log(err.description);\n" +
              "    }\n" +
              "\n" +
              "}\n" +
              "\n" +
              "window.onload = function (){\n" +
              "  log('2');\n" +
              " myDrayAll();\n" +
              "};\n" +
              "\n" +
              "\n" +
              "\n" +
              "function log(s){\n" +
              " console.info(s);\n" +
              "}");
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
