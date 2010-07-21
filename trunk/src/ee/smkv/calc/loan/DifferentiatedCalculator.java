package ee.smkv.calc.loan;

import java.math.BigDecimal;

public class DifferentiatedCalculator implements Calculator{
	

	public void calculate(Loan loan){

		BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), 8, MODE);
		BigDecimal monthlyAmount = loan.getAmount().divide(new BigDecimal(loan.getPeriod()) , 8 , MODE);
		BigDecimal currentAmount = loan.getAmount();
		for(int i = 0 ; i < loan.getPeriod() ; i++){
			BigDecimal interest =  currentAmount.multiply(interestMonthly);
      BigDecimal amount=  interest.add(monthlyAmount) ;

      Payment payment = new Payment();
      payment.setNr(i + 1);
      payment.setInterest(interest.setScale(2,MODE));
      payment.setPrincipal(monthlyAmount.setScale(2 , MODE));
      payment.setBalance(currentAmount.setScale(2 , MODE));
      payment.setAmount(amount.setScale(2 , MODE));

      loan.getPayments().add(payment);

      currentAmount = currentAmount.subtract(monthlyAmount);
    }

	}


	
	

}
