package ee.smkv.calc.loan;

import java.math.BigDecimal;

public class DifferentiatedCalculator implements Calculator{
	

	public void calculate(Loan loan){

		BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
		BigDecimal monthlyAmount = loan.getAmount().divide(new BigDecimal(loan.getPeriod()) , SCALE , MODE);
		BigDecimal currentAmount = loan.getAmount();
		for(int i = 0 ; i < loan.getPeriod() ; i++){
			BigDecimal interest =  currentAmount.multiply(interestMonthly);
      BigDecimal amount=  interest.add(monthlyAmount) ;

      Payment payment = new Payment();
      payment.setNr(i + 1);
      payment.setInterest(interest);
      payment.setPrincipal(monthlyAmount);
      payment.setBalance(currentAmount);
      payment.setAmount(amount);

      loan.getPayments().add(payment);

      currentAmount = currentAmount.subtract(monthlyAmount);
    }

	}


	
	

}
