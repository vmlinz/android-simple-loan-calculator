package ee.smkv.calc.loan;


import ee.smkv.calc.loan.model.Loan;

import java.util.Observable;

public class LoanDispatcher extends Observable {
    private static LoanDispatcher INSTANCE = new LoanDispatcher();

    private LoanDispatcher() {
    }

   public static LoanDispatcher getInstance(){
       return INSTANCE;
   }

   public void dispatch(Loan loan){
       setChanged();
       notifyObservers(loan);
   }

}
