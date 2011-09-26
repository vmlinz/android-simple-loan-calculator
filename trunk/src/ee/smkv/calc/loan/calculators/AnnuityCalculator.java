package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static java.math.BigDecimal.*;

public class AnnuityCalculator extends AbstractCalculator {


    public void calculate(Loan loan) {
        BigDecimal amount = calculateAmountWithDownPayment(loan);
        addDisposableCommission(loan, amount);

        BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
        BigDecimal oneAndInterest = ONE.add(interestMonthly);
        BigDecimal powered = ONE.divide(oneAndInterest.pow(loan.getPeriod()), SCALE, MODE);
        BigDecimal divider = ONE.subtract(powered);
        BigDecimal payment = amount.multiply(interestMonthly).divide(divider, SCALE, MODE);

        BigDecimal balance = amount;
        ArrayList<Payment> payments = new ArrayList<Payment>();
        for (int i = 0; i < loan.getPeriod(); i++) {
            BigDecimal interest = balance.multiply(interestMonthly);
            BigDecimal principal = payment.subtract(interest);
            Payment p = new Payment();
            p.setNr(i + 1);
            p.setBalance(balance);
            p.setInterest(interest);
            p.setPrincipal(principal);

            addPaymentWithCommission(loan, p, payment);

            payments.add(p);
            balance = balance.subtract(principal);
        }
        loan.setPayments(payments);
        loan.setEffectiveInterestRate(calculateEffectiveInterestRate(loan));
    }


  public static void main(String[] args) {
    AnnuityCalculator calculator = new AnnuityCalculator();
    double eff = calculator.calcEffRateUsingIterativeApproach(1, 1000, new double[]{600, 600}, 12);
    System.out.println( (eff * 100) + "%" );// Should by ~ 13.07%

    eff = calculator.calcEffRateUsingIterativeApproach(1, 1000, new double[]{1200}, 18);
    System.out.println( (eff * 100) + "%" ); // Should by ~ 12.92%
  }



}
