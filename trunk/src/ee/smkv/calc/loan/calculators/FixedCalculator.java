package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.Calculator;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;

public class FixedCalculator implements Calculator {
	


  public void calculate(Loan loan){

		BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
		BigDecimal monthlyAmount = loan.getFixedPayment();
		BigDecimal currentAmount = loan.getAmount();
		BigDecimal ma = monthlyAmount;
    BigDecimal interest = null;
    BigDecimal amount= null;
    int i = 0;

    if( loan.getAmount().divide(loan.getFixedPayment() ,0,MODE).intValue() > 1000){
      throw new RuntimeException("Too small fixed payment part. Count of payments is over 1000 and mobile device can't calculate too big periods.");
    }

		while(currentAmount.compareTo(BigDecimal.ZERO) > 0){

      if(currentAmount.compareTo(ma)< 0){
          ma = currentAmount;
      }

			interest =  currentAmount.multiply(interestMonthly);
      amount   =  interest.add(ma) ;

      Payment payment = new Payment();
      payment.setNr(i+1);
      payment.setInterest(interest);
      payment.setPrincipal(ma);
      payment.setBalance(currentAmount);
      payment.setAmount(amount);

      loan.getPayments().add(payment);

      currentAmount = currentAmount.subtract(ma);
      i++;
    }

    loan.setPeriod(i);
  }
 
}
