package ee.smkv.calc.loan.chart;

import ee.smkv.calc.loan.Loan;

/**
 * @author samko
 */
public class LoanChart {
  private final Loan loan;
  private final int width;
  private final int height;

  public LoanChart(Loan loan, int width, int height) {
    this.loan = loan;
    this.width = width;
    this.height = height;
  }
  
  public String toHtml(){
    return "";
  }
}
