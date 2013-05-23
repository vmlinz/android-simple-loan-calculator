package ee.smkv.calc.loan.export;

import android.content.Context;
import ee.smkv.calc.loan.R;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;


public class TxtExporter {
    final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    final StringBuilder builder = new StringBuilder();
    private final String nr;
    private final String balance;
    private final String principal;
    private final String interest;
    private final String commision;
    private final String total;
    private final String title;
    private final int tableWidth;
    private final int nrLen;
    private final int balanceLen;
    private final int principalLen;
    private final int interestLen;
    private final int commissionLen;
    private final int totalLen;

    public TxtExporter(Loan loan, Context context) {
        nr = context.getString(R.string.paymentNr);
        balance = context.getString(R.string.paymentBalance);
        principal = context.getString(R.string.paymentPrincipal);
        interest = context.getString(R.string.paymentInterest);
        commision = context.getString(R.string.paymentCommission);
        total = context.getString(R.string.paymentTotal);
        title = context.getString(R.string.app_name);

        nrLen = nr.length();
        balanceLen = balance.length();
        principalLen = principal.length();
        interestLen = interest.length();
        commissionLen = commision.length();
        totalLen = total.length();
        tableWidth = nrLen + balanceLen + principalLen + interestLen + commissionLen + totalLen + 15;

        builder.append(title).append('\n')
        .append(' ').append(nr).append(' ').append('|')
        .append(' ').append(balance).append(' ').append('|')
        .append(' ').append(principal).append(' ').append('|')
        .append(' ').append(interest).append(' ').append('|')
        .append(' ').append(commision).append(' ').append('|')
        .append(' ').append(total).append('\n');

        char[] line = new char[tableWidth];
        Arrays.fill(line , '-');
        builder.append(line).append('\n');


        if (loan.hasDownPayment() || loan.hasDisposableCommission()) {
            dataRow(0 , loan.getAmount() , loan.getDownPaymentPayment() , BigDecimal.ZERO , loan.getDisposableCommissionPayment() , loan.getDownPaymentPayment().add(loan.getDisposableCommissionPayment()));
        }

        for (Payment payment : loan.getPayments()) {
            dataRow(payment.getNr() , payment.getBalance() , payment.getPrincipal() , payment.getInterest() , payment.getCommission() , payment.getAmount());
        }

    }

    private void dataRow(Integer nr , BigDecimal balance , BigDecimal principal , BigDecimal interest , BigDecimal commission , BigDecimal total) {
        builder
               .append(' ').append(nr).append(' ').append('|')
               .append(' ').append(number(balance, this.balance.length())).append(' ').append('|')
               .append(' ').append(number(principal, this.principal.length())).append(' ').append('|')
               .append(' ').append(number(interest, this.interest.length())).append(' ').append('|')
               .append(' ').append(number(commission, this.commision.length())).append(' ').append('|')
               .append(' ').append(number(total, this.total.length())).append('\n');

    }

    private String number(BigDecimal number ,int lenght){
        String s = number != null && number.doubleValue() > 0 ? decimalFormat.format(number) : "";

        char[] left = lenght > s.length()? new char[lenght - s.length()] : new char[0];
        Arrays.fill(left , ' ');
        return new String(left) + s;
    }


    public void write(OutputStream out) throws IOException {
        out.write(builder.toString().getBytes());
    }


}
