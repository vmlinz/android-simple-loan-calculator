package ee.smkv.calc.loan;

import java.math.BigDecimal;

public class AnnuityCalculator implements Calculator {


  public void calculate(Loan loan) {
    BigDecimal one = BigDecimal.ONE;
    BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), 8, MODE);
    BigDecimal oneAndInterest = one.add(interestMonthly);
    BigDecimal powered = one.divide(oneAndInterest.pow(loan.getPeriod()), 8, MODE);
    BigDecimal divider = one.subtract(powered);
    BigDecimal payment = loan.getAmount().multiply(interestMonthly).divide(divider, 2, MODE);

    BigDecimal balance = loan.getAmount();
    for (int i = 0; i < loan.getPeriod(); i++) {
      BigDecimal interest = balance.multiply(interestMonthly);
      BigDecimal principal = payment.subtract(interest);
      Payment p = new Payment();
      p.setNr(i + 1);
      p.setBalance(balance.setScale(2, MODE));
      p.setInterest(interest.setScale(2, MODE));
      p.setPrincipal(principal.setScale(2, MODE));
      p.setAmount(payment.setScale(2, MODE));

      loan.getPayments().add(p);
      balance = balance.subtract(principal);
    }

  }


}
