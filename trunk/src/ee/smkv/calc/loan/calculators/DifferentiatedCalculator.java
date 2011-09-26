package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DifferentiatedCalculator extends AbstractCalculator {
    private static final BigDecimal TWO = new BigDecimal(2);

    public void calculate(Loan loan) {
        BigDecimal amount = calculateAmountWithDownPayment(loan);
        addDisposableCommission(loan, amount);

        BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
        BigDecimal monthlyAmount = amount.divide(new BigDecimal(loan.getPeriod()), SCALE, MODE);
        BigDecimal currentAmount = amount;
        ArrayList<Payment> payments = new ArrayList<Payment>();
        for (int i = 0; i < loan.getPeriod(); i++) {
            BigDecimal interest = currentAmount.multiply(interestMonthly);
            BigDecimal payment = interest.add(monthlyAmount);

            Payment p = new Payment();
            p.setNr(i + 1);
            p.setInterest(interest);
            p.setPrincipal(monthlyAmount);
            p.setBalance(currentAmount);

            addPaymentWithCommission(loan, p, payment);

            payments.add(p);

            currentAmount = currentAmount.subtract(monthlyAmount);
        }
        loan.setPayments(payments);
        loan.setEffectiveInterestRate(calculateEffectiveInterestRate(loan));
    }



}
