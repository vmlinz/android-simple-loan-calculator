package ee.smkv.calc.loan.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Samkov
 */
public class Loan implements Serializable{
  private static final long serialVersionUID = 1L;
  private int loanType = 0;
  private BigDecimal amount;
  private BigDecimal interest;
  private BigDecimal fixedPayment;
  private Integer period;
  private List<Payment> payments = new ArrayList<Payment>();


  public void reset() {
    getPayments().clear();
    amount = BigDecimal.ZERO;
    interest = BigDecimal.ZERO;
    fixedPayment = BigDecimal.ZERO;
    period = 0;
  }

  public BigDecimal getTotalAmount() {
    BigDecimal total = BigDecimal.ZERO;
    for (Payment payment : payments) {
      if (payment.getAmount() != null) {
        total = total.add(payment.getAmount());
      }
    }
    return total;
  }

  public BigDecimal getTotalInterests() {
    BigDecimal total = BigDecimal.ZERO;
    for (Payment payment : payments) {
      if (payment.getInterest() != null) {
        total = total.add(payment.getInterest());
      }
    }
    return total;
  }

  public BigDecimal getMaxMonthlyPayment() {
    BigDecimal max = BigDecimal.ZERO;
    for (Payment payment : payments) {
      if (payment.getAmount() != null) {
        max = payment.getAmount().compareTo(max) > 0 ? payment.getAmount() : max;
      }
    }
    return max;
  }

  public BigDecimal getMinMonthlyPayment() {
    BigDecimal min = BigDecimal.ZERO;
    for (Payment payment : payments) {
      if (payment.getAmount() != null) {
        if (min == BigDecimal.ZERO) {
          min = payment.getAmount();
        }
        min = payment.getAmount().compareTo(min) < 0 ? payment.getAmount() : min;
      }
    }
    return min;
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
    return payments;
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

    Loan loan = (Loan)o;

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
