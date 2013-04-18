package ee.smkv.calc.loan;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PieChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.*;

import java.util.List;

public class ChartFragment extends SherlockFragment {

    PieChart pieChart;
    LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart, container, false);

        Loan loan = StartActivity.loan;

        addPieChart(view, loan);
        addLineChart(view, loan);



        return view;
    }



    private void addLineChart(View view, Loan loan) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        XYSeriesRenderer principalRenderer = new XYSeriesRenderer();
        XYSeriesRenderer interestRenderer = new XYSeriesRenderer();
        XYSeriesRenderer commissionRenderer = new XYSeriesRenderer();
        XYSeriesRenderer paymentRenderer = new XYSeriesRenderer();

        paymentRenderer.setColor(getResources().getColor(R.color.payment));
        paymentRenderer.setLineWidth(2);
        paymentRenderer.setFillBelowLine(true);
        paymentRenderer.setFillBelowLineColor(getResources().getColor(R.color.paymentFill));

        principalRenderer.setColor(getResources().getColor(R.color.principal));
        principalRenderer.setLineWidth(2);
        principalRenderer.setFillBelowLine(true);
        principalRenderer.setFillBelowLineColor(getResources().getColor(R.color.principalFill));

        interestRenderer.setColor(getResources().getColor(R.color.interest));
        interestRenderer.setLineWidth(2);
        interestRenderer.setFillBelowLine(true);
        interestRenderer.setFillBelowLineColor(getResources().getColor(R.color.interestFill));

        commissionRenderer.setColor(getResources().getColor(R.color.commission));
        commissionRenderer.setLineWidth(2);
        commissionRenderer.setFillBelowLine(true);
        commissionRenderer.setFillBelowLineColor(getResources().getColor(R.color.commissionFill));


        renderer.setAntialiasing(true);
        renderer.setZoomEnabled(false , false);
        renderer.setPanEnabled(false , false);
        renderer.setExternalZoomEnabled(false);
        renderer.setGridColor(getResources().getColor(R.color.Separator_Color));
        renderer.setBackgroundColor(getResources().getColor(R.color.abs__background_holo_light));
        renderer.setMarginsColor(getResources().getColor(R.color.abs__background_holo_light));
        renderer.setAxisTitleTextSize(getResources().getDimension(R.dimen.abs__action_bar_subtitle_text_size));
        renderer.setInScroll(false);

        renderer.addSeriesRenderer(0, paymentRenderer);
        dataset.addSeries(0, new PaymentSeries(getString(R.string.paymentTotal), loan.getPayments()));

        renderer.addSeriesRenderer(1, principalRenderer);
        dataset.addSeries(1, new PrincipalSeries(getString(R.string.paymentPrincipal), loan.getPayments()));

        renderer.addSeriesRenderer(2, interestRenderer);
        dataset.addSeries(2, new InterestSeries(getString(R.string.paymentInterest), loan.getPayments()));

        if (loan.hasAnyCommission()) {
            renderer.addSeriesRenderer(3, commissionRenderer);
            dataset.addSeries(3, new CommissionSeries(getString(R.string.paymentCommission), loan.getPayments()));
        }

        lineChart = new LineChart(dataset,renderer);
        GraphicalView lineChartView = new GraphicalView(view.getContext(), lineChart);

        LinearLayout linePlaceholder = (LinearLayout) view.findViewById(R.id.lineChartPlaceholder);
        linePlaceholder.addView(lineChartView);
    }

    private void addPieChart(View view, Loan loan) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setAntialiasing(true);
        renderer.setExternalZoomEnabled(false);
        renderer.setPanEnabled(false);
        renderer.setZoomEnabled(false);

        renderer.setLabelsColor(getResources().getColor(R.color.abs__bright_foreground_holo_light));
        renderer.setLabelsTextSize(getResources().getDimension(R.dimen.abs__action_bar_subtitle_text_size));
        renderer.setBackgroundColor(getResources().getColor(R.color.abs__background_holo_light));
        renderer.setMargins(new int[]{10,10,10,10});
        CategorySeries series = new CategorySeries("Pie");

        series.add(getString(R.string.paymentPrincipal), loan.getAmount().doubleValue());
        SimpleSeriesRenderer principalRenderer = new SimpleSeriesRenderer();
        principalRenderer.setDisplayChartValues(true);
        principalRenderer.setColor(getResources().getColor(R.color.principal));
        renderer.addSeriesRenderer(0, principalRenderer);



        series.add(getString(R.string.paymentInterest), loan.getTotalInterests().doubleValue());
        SimpleSeriesRenderer interestRenderer = new SimpleSeriesRenderer();
        interestRenderer.setDisplayChartValues(true);
        interestRenderer.setColor(getResources().getColor(R.color.interest));
        renderer.addSeriesRenderer(1, interestRenderer);


        if (loan.hasAnyCommission()) {
            series.add(getString(R.string.paymentCommission), loan.getCommissionsTotal().doubleValue());
            SimpleSeriesRenderer commissionRenderer = new SimpleSeriesRenderer();
            commissionRenderer.setDisplayChartValues(true);
            commissionRenderer.setColor(getResources().getColor(R.color.commission));
            renderer.addSeriesRenderer(2, commissionRenderer);
        }

        pieChart = new PieChart(series , renderer){
            @Override
            public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
                setCenterX( width/2);
                setCenterY( height/2);
                super.draw(canvas, x, y, width, height, paint);
            }
        };
        GraphicalView pieChartView = new GraphicalView(view.getContext(), pieChart);
        LinearLayout placeholder = (LinearLayout) view.findViewById(R.id.pieChartPlaceholder);
        placeholder.addView(pieChartView);
    }


    private class PrincipalSeries extends XYSeries {
        public PrincipalSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                add(payment.getNr().doubleValue(), payment.getPrincipal().doubleValue());
            }
        }
    }

    private class InterestSeries extends XYSeries {
        public InterestSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                add(payment.getNr().doubleValue(), payment.getInterest().doubleValue());
            }
        }
    }

    private class CommissionSeries extends XYSeries {
        public CommissionSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                add(payment.getNr().doubleValue(), payment.getCommission().doubleValue());
            }
        }
    }

    private class PaymentSeries extends XYSeries {
        public PaymentSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                add(payment.getNr().doubleValue(), payment.getAmount().doubleValue());
            }
        }
    }
}
