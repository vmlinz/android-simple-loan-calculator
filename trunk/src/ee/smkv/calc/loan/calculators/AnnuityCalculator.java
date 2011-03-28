package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AnnuityCalculator implements Calculator {


    public void calculate(Loan loan) {
        BigDecimal amount = loan.getAmount();

        BigDecimal downPayment = loan.getDownPayment();
        if (downPayment != null && downPayment.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getDownPaymentType() == Loan.PERCENT) {
                downPayment = downPayment.multiply(amount).divide(new BigDecimal("100"), SCALE, MODE);
            }
            loan.setDownPaymentPayment(downPayment);
            amount = amount.subtract(downPayment);
        }

        BigDecimal disposableCommission = loan.getDisposableCommission();
        if (disposableCommission != null && disposableCommission.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getDisposableCommissionType() == Loan.PERCENT) {
                disposableCommission = disposableCommission.multiply(amount).divide(new BigDecimal("100"), SCALE, MODE);
            }
            loan.setDisposableCommissionPayment(disposableCommission);
        }

        BigDecimal one = BigDecimal.ONE;
        BigDecimal interestMonthly = loan.getInterest().divide(new BigDecimal("1200"), SCALE, MODE);
        BigDecimal oneAndInterest = one.add(interestMonthly);
        BigDecimal powered = one.divide(oneAndInterest.pow(loan.getPeriod()), SCALE, MODE);
        BigDecimal divider = one.subtract(powered);
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

            payments.add(p);
            balance = balance.subtract(principal);
        }
        loan.setPayments(payments);
    }


}
