package ee.smkv.calc.loan;

import ee.smkv.calc.loan.model.Loan;

import java.math.BigDecimal;

public interface Calculator {
  int MODE = BigDecimal.ROUND_HALF_UP;
  int SCALE = 8;
	void calculate(Loan loan);
}
