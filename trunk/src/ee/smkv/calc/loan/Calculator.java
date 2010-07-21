package ee.smkv.calc.loan;

import java.math.BigDecimal;

public interface Calculator {
  int MODE = BigDecimal.ROUND_HALF_UP;
	void calculate(Loan loan);
}
