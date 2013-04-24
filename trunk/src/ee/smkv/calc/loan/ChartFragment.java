package ee.smkv.calc.loan;


import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;
import ee.smkv.calc.loan.model.Loan;
import ee.smkv.calc.loan.model.Payment;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PieChart;
import org.achartengine.chart.RangeBarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ChartFragment extends SherlockFragment implements Observer {

    PieChart pieChart;
    RangeBarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart, container, false);

        Loan loan = StartActivity.loan;

        if (loan.isCalculated()) {
            addPieChart(view, loan);
            addBarChart(view, loan);
        }
        LoanDispatcher.getInstance().addObserver(this);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoanDispatcher.getInstance().deleteObserver(this);
    }


    private void addBarChart(View view, Loan loan) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        SimpleSeriesRenderer principalRenderer = new SimpleSeriesRenderer();
        SimpleSeriesRenderer interestRenderer = new SimpleSeriesRenderer();
        SimpleSeriesRenderer commissionRenderer = new SimpleSeriesRenderer();


        principalRenderer.setColor(getResources().getColor(R.color.principal));
        interestRenderer.setColor(getResources().getColor(R.color.interest));
        commissionRenderer.setColor(getResources().getColor(R.color.commission));


        renderer.setAntialiasing(true);
        renderer.setZoomEnabled(false, false);
        renderer.setPanEnabled(false, false);
        renderer.setExternalZoomEnabled(false);
        renderer.setShowGrid(false);
        renderer.setGridColor(getResources().getColor(R.color.Separator_Color));
        renderer.setBackgroundColor(getResources().getColor(R.color.abs__background_holo_light));
        renderer.setMarginsColor(getResources().getColor(R.color.abs__background_holo_light));
        renderer.setAxisTitleTextSize(getResources().getDimension(R.dimen.abs__action_bar_subtitle_text_size));
        renderer.setInScroll(false);
        //renderer.setBarSpacing(0.5);

        renderer.setLabelsColor(Color.BLACK);


        renderer.addSeriesRenderer(0, principalRenderer);
        dataset.addSeries(0, new PrincipalSeries(getString(R.string.paymentPrincipal), loan.getPayments()).toXYSeries());

        renderer.addSeriesRenderer(1, interestRenderer);
        dataset.addSeries(1, new InterestSeries(getString(R.string.paymentInterest), loan.getPayments()).toXYSeries());

        if (loan.hasAnyCommission()) {
            renderer.addSeriesRenderer(2, commissionRenderer);
            dataset.addSeries(2, new CommissionSeries(getString(R.string.paymentCommission), loan.getPayments()).toXYSeries());
        }

        barChart = new RangeBarChart(dataset, renderer, BarChart.Type.STACKED);
        GraphicalView lineChartView = new GraphicalView(view.getContext(), barChart);

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
        renderer.setMargins(new int[]{10, 10, 10, 10});
        CategorySeries series = new CategorySeries("Pie");

        BigDecimal totalAmount = loan.getTotalAmount();

        series.add(getString(R.string.paymentPrincipal) + " (" + Utils.percent(loan.getAmount(), totalAmount) + ")", loan.getAmount().doubleValue());
        SimpleSeriesRenderer principalRenderer = new SimpleSeriesRenderer();
        principalRenderer.setDisplayChartValues(true);
        principalRenderer.setColor(getResources().getColor(R.color.principal));
        renderer.addSeriesRenderer(0, principalRenderer);


        series.add(getString(R.string.paymentInterest) + " (" + Utils.percent(loan.getTotalInterests(), totalAmount) + ")", loan.getTotalInterests().doubleValue());
        SimpleSeriesRenderer interestRenderer = new SimpleSeriesRenderer();
        interestRenderer.setDisplayChartValues(true);
        interestRenderer.setColor(getResources().getColor(R.color.interest));
        renderer.addSeriesRenderer(1, interestRenderer);


        if (loan.hasAnyCommission()) {
            series.add(getString(R.string.paymentCommission) + " (" + Utils.percent(loan.getCommissionsTotal(), totalAmount) + ")", loan.getCommissionsTotal().doubleValue());
            SimpleSeriesRenderer commissionRenderer = new SimpleSeriesRenderer();
            commissionRenderer.setDisplayChartValues(true);
            commissionRenderer.setColor(getResources().getColor(R.color.commission));
            renderer.addSeriesRenderer(2, commissionRenderer);
        }


        pieChart = new PieChart(series, renderer) {
            @Override
            public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
                setCenterX(width / 2);
                setCenterY(height / 2);
                super.draw(canvas, x, y, width, height, paint);
            }
        };
        GraphicalView pieChartView = new GraphicalView(view.getContext(), pieChart);
        LinearLayout placeholder = (LinearLayout) view.findViewById(R.id.pieChartPlaceholder);
        placeholder.addView(pieChartView);
    }

    @Override
    public void update(Observable observable, Object data) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Loan loan = StartActivity.loan;
                if (pieChart != null) {
                    ((LinearLayout) getView().findViewById(R.id.pieChartPlaceholder)).removeViewAt(0);
                    ((LinearLayout) getView().findViewById(R.id.lineChartPlaceholder)).removeViewAt(0);
                }
                if (loan.isCalculated()) {
                    addPieChart(getView(), loan);
                    addBarChart(getView(), loan);
                }
            }
        });

    }


  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (getView().findViewById(R.id.chartLayout) != null) {
      switch (newConfig.orientation){
        case Configuration.ORIENTATION_LANDSCAPE:
          ((LinearLayout)getView().findViewById(R.id.chartLayout)).setOrientation(LinearLayout.HORIZONTAL);
          break;
        case Configuration.ORIENTATION_PORTRAIT:
          ((LinearLayout)getView().findViewById(R.id.chartLayout)).setOrientation(LinearLayout.VERTICAL);
          break;
      }
    }

  }

  private class PrincipalSeries extends RangeCategorySeries {
        public PrincipalSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                double shift = 0;
                add(shift, shift + payment.getPrincipal().doubleValue());
            }
        }
    }

    private class InterestSeries extends RangeCategorySeries {
        public InterestSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                double shift = payment.getPrincipal().doubleValue();
                add(shift, shift + payment.getInterest().doubleValue());
            }
        }
    }

    private class CommissionSeries extends RangeCategorySeries {
        public CommissionSeries(String title, List<Payment> payments) {
            super(title);
            for (Payment payment : payments) {
                double shift = payment.getPrincipal().doubleValue() + payment.getInterest().doubleValue();
                add(shift, shift + payment.getCommission().doubleValue());
            }
        }
    }


}
