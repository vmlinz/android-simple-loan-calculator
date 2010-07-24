package ee.smkv.calc.loan;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {
	Loan loan;
	Paint paint = new Paint();
	int width = 300, heigh = 200;
	static final int INTERST = 1 , PRINCIPAL = 2 , AMOUNT = 3;
	public ChartView(Context context) {
		super(context);
		init();
	}
	
	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		paint.setColor(0xCCCCCC00);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = canvas.getWidth();
		heigh = canvas.getHeight();
		if(loan != null){
			canvas.drawRect(0, 0, width, heigh, paint);
			int max = loan.getMaxMonthlyPayment().intValue();
			Path principal = getPath(PRINCIPAL , loan , max);
			Path interst = getPath(INTERST ,loan , max);
			Path amount = getPath(AMOUNT ,loan , max);
			//paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setColor(Color.BLUE);
			canvas.drawPath(amount, paint);
			paint.setColor(Color.YELLOW);
			canvas.drawPath(principal, paint);
			paint.setColor(Color.RED);
			canvas.drawPath(interst, paint);
			
		}
	}
	
	
	private Path getPath(int type, Loan loan, int max) {
		Path line = new Path();
		float xStep = width / loan.getPeriod();
		float x = 0;
		for(Payment payment : loan.getPayments()){
			float f = 0;
			switch (type) {
			case INTERST:
				f = payment.getInterest().floatValue();
				break;
			case PRINCIPAL:
				f = payment.getPrincipal().floatValue();
				break;
			case AMOUNT:
				f = payment.getAmount().floatValue();
				break;

			default:
				break;
			}
			float y = f / max * heigh;
			line.lineTo(x, heigh - y );
			x = x + xStep;
		}
		return line;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}
}
