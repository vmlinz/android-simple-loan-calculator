package ee.smkv.calc.loan;

import java.math.BigDecimal;

public class FixedCalculator implements Calculator {
	


  public void calculate(Loan loan){

		BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), 8, MODE);
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
      payment.setInterest(interest.setScale(2,MODE));
      payment.setPrincipal(ma.setScale(2 , MODE));
      payment.setBalance(currentAmount.setScale(2 , MODE));
      payment.setAmount(amount.setScale(2 , MODE));

      loan.getPayments().add(payment);

      currentAmount = currentAmount.subtract(ma);
      i++;
    }

    loan.setPeriod(i);
  }
 
}
