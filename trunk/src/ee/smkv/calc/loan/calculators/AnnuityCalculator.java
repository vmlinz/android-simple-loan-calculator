package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.Calculator;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;

public class AnnuityCalculator implements Calculator {


  public void calculate(Loan loan) {
    BigDecimal one = BigDecimal.ONE;
    BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
    BigDecimal oneAndInterest = one.add(interestMonthly);
    BigDecimal powered = one.divide(oneAndInterest.pow(loan.getPeriod()), SCALE, MODE);
    BigDecimal divider = one.subtract(powered);
    BigDecimal payment = loan.getAmount().multiply(interestMonthly).divide(divider, SCALE, MODE);

    BigDecimal balance = loan.getAmount();
    for (int i = 0; i < loan.getPeriod(); i++) {
      BigDecimal interest = balance.multiply(interestMonthly);
      BigDecimal principal = payment.subtract(interest);
      Payment p = new Payment();
      p.setNr(i + 1);
      p.setBalance(balance);
      p.setInterest(interest);
      p.setPrincipal(principal);
      p.setAmount(payment);

      loan.getPayments().add(p);
      balance = balance.subtract(principal);
    }

  }


}
