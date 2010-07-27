package ee.smkv.calc.loan;


import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ChartView extends View {
	Loan loan;
	Paint paint = new Paint();
	int width = 300, heigh = 200;
	static final int INTERST = 1 , PRINCIPAL = 2 , AMOUNT = 3;
	static final String TAG = ChartView.class.getSimpleName();
	static final DecimalFormat FORMAT = new DecimalFormat("0.00"); 
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
		paint.setAntiAlias(true);
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = getMeasuredWidth();
		heigh = getMeasuredHeight() - 40;
		if(loan != null){				
			drawLines(canvas);
			drawGrid(canvas);
			drawLegend(canvas);
		}
	}

	private void drawLegend(Canvas canvas) {
		paint.setTextSize(12f);
    paint.setStrokeWidth(0.25f);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.MAGENTA);
		canvas.drawText(getResources().getString(R.string.paymentTotal), 20, heigh + 12 , paint);
		paint.setColor(Color.YELLOW);
    canvas.drawText(getResources().getString(R.string.paymentPrincipal), 20, heigh + 26 , paint);
    paint.setColor(Color.RED);
    canvas.drawText(getResources().getString(R.string.paymentInterest), 20, heigh + 40 , paint);


    paint.setStrokeWidth(2f);
    paint.setColor(Color.MAGENTA);
    canvas.drawLine(5,heigh + 10 ,15 , heigh + 10, paint);
    paint.setColor(Color.YELLOW);
    canvas.drawLine(5,heigh + 24 ,15 , heigh + 24, paint);
    paint.setColor(Color.RED);
    canvas.drawLine(5,heigh + 38 ,15 , heigh + 38, paint);
	}

	private void drawGrid(Canvas canvas) {
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2f);
		canvas.drawRect(0, 0, width, heigh, paint);
		
		paint.setStrokeWidth(0.25f);
		paint.setTextSize(10f);		
		
		float max = loan.getMaxMonthlyPayment().floatValue()* 1.1f; // adds 10 %
		
		int gridSize = 50;
		int xCount = (int)( 1.0f * width / gridSize);
		int yCount = (int)( 1.0f * heigh / gridSize);
		float xStep = width / xCount;
		float yStep = heigh / yCount;
		Path grid = new Path();
		for( int i = 1 ;i < xCount; i++ ){
			grid.moveTo(i * xStep, 0);
			grid.lineTo(i * xStep, heigh);
		}
		for( int i = 1 ;i < yCount; i++ ){
			grid.moveTo(0, i * yStep);
			grid.lineTo(width, i * yStep);
			float label = max -  (((i * yStep) / heigh) * max);
      paint.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawText(FORMAT.format(label), 3, i * yStep - 3, paint);
      paint.setStyle(Paint.Style.STROKE);
		}
		canvas.drawPath(grid, paint);
	}

	private void drawLines(Canvas canvas) {
		float gridXStep = width / (loan.getPeriod() - 1 );
		float max = loan.getMaxMonthlyPayment().floatValue()* 1.1f; // adds 10 %
		Path principal = getPath(PRINCIPAL , loan , max , gridXStep );
		Path interst = getPath(INTERST ,loan , max, gridXStep);
		Path amount = getPath(AMOUNT ,loan , max, gridXStep);
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3.0f);
		paint.setColor(Color.MAGENTA);
		canvas.drawPath(amount, paint);
		paint.setColor(Color.YELLOW);
		canvas.drawPath(principal, paint);
		paint.setColor(Color.RED);
		canvas.drawPath(interst, paint);
	}
	
	
	private Path getPath(int type, Loan loan, float max , float gridXStep) {
		Path line = new Path();
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
			float y = heigh - (f / max * heigh);
			Log.d(TAG, type + ": draw line x = " + x + " y = " + y);
			if(x == 0){
				line.moveTo(x, y);
			}else{
				line.lineTo(x, y );
			}
			x = x + gridXStep;
		}
		return line;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}
}
