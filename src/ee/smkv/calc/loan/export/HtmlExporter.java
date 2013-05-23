package ee.smkv.calc.loan.export;

import android.content.Context;
import ee.smkv.calc.loan.R;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;


public class HtmlExporter {
    final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    final StringBuilder builder = new StringBuilder();

    public HtmlExporter(Loan loan, Context context) {
        builder.append("<html><head><title>")
                .append(context.getString(R.string.app_name))
                .append("</title></head><body><h1>")
                .append(context.getString(R.string.app_name))
                .append("</h1><table><tr><th>")
                .append(context.getString(R.string.paymentNr))
                .append("</th><th>")
                .append(context.getString(R.string.paymentBalance))
                .append("</th><th>")
                .append(context.getString(R.string.paymentPrincipal))
                .append("</th><th>")
                .append(context.getString(R.string.paymentInterest))
                .append("</th><th>")
                .append(context.getString(R.string.paymentCommission))
                .append("</th><th>")
                .append(context.getString(R.string.paymentTotal))
                .append("</th></tr>")
        ;

        if (loan.hasDownPayment() || loan.hasDisposableCommission()) {
            dataRow(0 , loan.getAmount() , loan.getDownPaymentPayment() , BigDecimal.ZERO , loan.getDisposableCommissionPayment() , loan.getDownPaymentPayment().add(loan.getDisposableCommissionPayment()));
        }

        for (Payment payment : loan.getPayments()) {
            dataRow(payment.getNr() , payment.getBalance() , payment.getPrincipal() , payment.getInterest() , payment.getCommission() , payment.getAmount());
        }

        builder.append("</body></html>");
    }

    private void dataRow(Integer nr , BigDecimal balance , BigDecimal principal , BigDecimal interest , BigDecimal commission , BigDecimal total) {
        builder.append("<tr><td align=\"right\">");
        builder.append(nr);
        builder.append("</td><td align=\"right\">");
        number(balance);
        builder.append("</td><td align=\"right\">");
        number(principal);
        builder.append("</td><td align=\"right\">");
        number(interest);
        builder.append("</td><td align=\"right\">");
        number(commission);
        builder.append("</td><td align=\"right\">");
        number(total);
        builder.append("</td></tr>");


    }

    private void number(BigDecimal number){
        builder.append( number != null && number.doubleValue() > 0 ? decimalFormat.format(number) : "");
    }


    public void write(OutputStream out) throws IOException {
        out.write(builder.toString().getBytes());
    }


}
