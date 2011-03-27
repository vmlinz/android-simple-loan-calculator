package ee.smkv.calc.loan.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Samkov
 */
public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;
    private int loanType = 0;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal interest = BigDecimal.ZERO;
    private BigDecimal fixedPayment = BigDecimal.ZERO;
    private Integer period = 0;
    private List<Payment> payments = new ArrayList<Payment>();

    private BigDecimal totalInterests = BigDecimal.ZERO;
    private BigDecimal minimalPayment = BigDecimal.ZERO;
    private BigDecimal maximalPayment = BigDecimal.ZERO;

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        totalInterests = BigDecimal.ZERO;
        minimalPayment = BigDecimal.ZERO;
        maximalPayment = BigDecimal.ZERO;
        for (Payment payment : payments) {
           totalInterests = totalInterests.add(payment.getInterest());
           if (minimalPayment.equals(BigDecimal.ZERO)){
               minimalPayment = payment.getAmount();
           }else{
               minimalPayment = minimalPayment.min( payment.getAmount());
           }
           maximalPayment = maximalPayment.max(payment.getAmount());
        }
    }

    public BigDecimal getTotalAmount() {
        return amount.add(totalInterests);
    }

    public BigDecimal getTotalInterests() {
        return totalInterests;
    }

    public BigDecimal getMaxMonthlyPayment() {
        return maximalPayment;
    }


    public BigDecimal getMinMonthlyPayment() {
        return minimalPayment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public List<Payment> getPayments() {
        return new ArrayList<Payment>(payments);
    }

    public BigDecimal getFixedPayment() {
        return fixedPayment;
    }

    public void setFixedPayment(BigDecimal fixedPayment) {
        this.fixedPayment = fixedPayment;
    }

    public int getLoanType() {
        return loanType;
    }

    public void setLoanType(int loanType) {
        this.loanType = loanType;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "amount=" + amount +
                ", interest=" + interest +
                ", fixedPayment=" + fixedPayment +
                ", period=" + period +
                ", payments=" + payments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Loan loan = (Loan) o;

        if (loanType != loan.loanType) {
            return false;
        }
        if (amount != null ? !amount.equals(loan.amount) : loan.amount != null) {
            return false;
        }
        if (fixedPayment != null ? !fixedPayment.equals(loan.fixedPayment) : loan.fixedPayment != null) {
            return false;
        }
        if (interest != null ? !interest.equals(loan.interest) : loan.interest != null) {
            return false;
        }
        if (period != null ? !period.equals(loan.period) : loan.period != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = loanType;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (interest != null ? interest.hashCode() : 0);
        result = 31 * result + (fixedPayment != null ? fixedPayment.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }
}
