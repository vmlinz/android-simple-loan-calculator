package ee.smkv.calc.loan.calculators;

import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.math.BigDecimal;

public abstract class AbstractCalculator implements Calculator {

    protected void addPaymentWithCommission(Loan loan, Payment p, BigDecimal payment) {
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
    }

    protected void addDisposableCommission(Loan loan, BigDecimal amount) {
        BigDecimal disposableCommission = loan.getDisposableCommission();
        if (disposableCommission != null && disposableCommission.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getDisposableCommissionType() == Loan.PERCENT) {
                disposableCommission = disposableCommission.multiply(amount).divide(new BigDecimal("100"), SCALE, MODE);
            }
            loan.setDisposableCommissionPayment(disposableCommission);
        }
    }

    protected BigDecimal calculateAmountWithDownPayment(Loan loan) {
        BigDecimal amount = loan.getAmount();

        BigDecimal downPayment = loan.getDownPayment();
        if (downPayment != null && downPayment.compareTo(BigDecimal.ZERO) != 0) {
            if (loan.getDownPaymentType() == Loan.PERCENT) {
                downPayment = downPayment.multiply(amount).divide(new BigDecimal("100"), SCALE, MODE);
            }
            loan.setDownPaymentPayment(downPayment);
            amount = amount.subtract(downPayment);
        }
        return amount;
    }

}
