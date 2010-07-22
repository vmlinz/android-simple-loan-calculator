package ee.smkv.calc.loan;

import java.math.BigDecimal;

public class FixedCalculator implements Calculator {
	


  public void calculate(Loan loan){

		BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
		BigDecimal monthlyAmount = loan.getFixedPayment();
		BigDecimal currentAmount = loan.getAmount();
		BigDecimal ma = monthlyAmount;
    int i = 0;
		while(currentAmount.compareTo(BigDecimal.ZERO) > 0){

      if(currentAmount.compareTo(ma)< 0){
          ma = currentAmount;
      }

			BigDecimal interest =  currentAmount.multiply(interestMonthly);
      BigDecimal amount=  interest.add(ma) ;

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
