package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;

public abstract class AbstractCalculator implements Calculator {

    protected void addPaymentWithCommission(Loan loan, Payment p, BigDecimal payment) {
        BigDecimal monthlyCommission = loan.getMonthlyCommission();
        if (monthlyCommission != null && monthlyCommission.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getMonthlyCommissionType() == Loan.PERCENT) {
                monthlyCommission = monthlyCommission.multiply(payment).divide(new BigDecimal("100"), SCALE, MODE);
            }
            p.setCommission(monthlyCommission);
            p.setAmount(payment.add(monthlyCommission));
        } else {
            p.setAmount(payment);
        }
    }

    protected void addDisposableCommission(Loan loan, BigDecimal amount) {
        BigDecimal disposableCommission = loan.getDisposableCommission();
        if (disposableCommission != null && disposableCommission.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getDisposableCommissionType() == Loan.PERCENT) {
                disposableCommission = disposableCommission.multiply(amount).divide(new BigDecimal("100"), SCALE, MODE);
            }
            loan.setDisposableCommissionPayment(disposableCommission);
        }
    }

    protected BigDecimal calculateAmountWithDownPayment(Loan loan) {
        BigDecimal amount = loan.getAmount();

        BigDecimal downPayment = loan.getDownPayment();
        if (downPayment != null && downPayment.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getDownPaymentType() == Loan.PERCENT) {
                downPayment = downPayment.multiply(amount).divide(new BigDecimal("100"), SCALE, MODE);
            }
            loan.setDownPaymentPayment(downPayment);
            amount = amount.subtract(downPayment);
        }
        return amount;
    }

  /**
   * Calculation of effective rate of loan using iterative approach: <br /><br />
   * <code>loanAmount = SUM(i=1 to i=payments.length) { payments[i] / (( 1 + effectiveRate ) ^ (i/12)) }</code>
   * <br /><br />
   * https://www.riigiteataja.ee/akt/189731
   * @param loan Loan
   * @return Effective rate
   */
  protected BigDecimal calculateEffectiveInterestRate(Loan loan) {

    double loanAmount = loan.getAmount().doubleValue() -
                        (loan.getDownPaymentPayment()!= null ? loan.getDownPaymentPayment().doubleValue() : 0)-
                        (loan.getDisposableCommissionPayment()!= null ? loan.getDisposableCommissionPayment().doubleValue() : 0);
    double realInterest = loan.getInterest().doubleValue() / 100;

    double[] payments = new double[loan.getPeriod()];
    int i = 0;
    for (Payment payment : loan.getPayments()) {
      payments[i++] = payment.getAmount().doubleValue();
    }

    double x = calcEffRateUsingIterativeApproach(realInterest, loanAmount, payments , 1);
    return new BigDecimal(x * 100);

  }

  /**
   *
   * @param realInterest Real interest rates, using it only for choosing right start and end points
   * @param loanAmount  Loan amount
   * @param payments Payments
   * @param periodBetweenPayments months between payments, usually is it 1
   * @return effective rate
   */
  protected double calcEffRateUsingIterativeApproach(double realInterest, double loanAmount, double[] payments , int periodBetweenPayments) {
    int i;
    double x1 = 0;
    double x2 = realInterest * 10; // x10 greater

    for (i = 0; i < 20; i++) {
      double x = (x1 + x2) / 2; //average
      double a = calcLoanAmountUsingEffectiveRate(payments, x , periodBetweenPayments);

      if (loanAmount < a) {
        x1 = x;
      }
      else {
        x2 = x;
      }
    }
    return (x1 + x2) / 2 ;
  }

  /**
   * Calculate amount using effective rate and payments
   * @param payments Payments
   * @param effectiveRate Effective Rate
   * @param periodBetweenPayments months between payments, usually is it 1
   * @return loan amount
   */
  private double calcLoanAmountUsingEffectiveRate(double[] payments, double effectiveRate, int periodBetweenPayments) {
    double result = 0;

    for (int i = 0 ; i < payments.length ; i ++){
      result += payments[i] / ( Math.pow(1 + effectiveRate, ((double )(i + 1)* periodBetweenPayments) / (double )12));
    }

    return result;
  }





}
