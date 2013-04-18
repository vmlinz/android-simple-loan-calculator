package ee.smkv.calc.loan;


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
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

public class ChartFragment extends SherlockFragment {
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

    principalRenderer.setColor(getResources().getColor(R.color.principal));
    interestRenderer.setColor(getResources().getColor(R.color.interest));
    commissionRenderer.setColor(getResources().getColor(R.color.commission));

    renderer.setAntialiasing(true);
    renderer.setZoomEnabled(false);
    renderer.setPanEnabled(false);
    renderer.setExternalZoomEnabled(false);
    renderer.setGridColor(getResources().getColor(R.color.Separator_Color));
    renderer.setBackgroundColor(getResources().getColor(R.color.abs__background_holo_light));
    renderer.setMarginsColor(getResources().getColor(R.color.abs__background_holo_light));

    renderer.addSeriesRenderer(0, principalRenderer);
    dataset.addSeries(0, new PrincipalSeries(getString(R.string.paymentPrincipal), loan.getPayments()));

    renderer.addSeriesRenderer(1, interestRenderer);
    dataset.addSeries(1, new InterestSeries(getString(R.string.paymentInterest), loan.getPayments()));

    if (loan.hasAnyCommission()) {
      renderer.addSeriesRenderer(2, commissionRenderer);
      dataset.addSeries(2, new CommissionSeries(getString(R.string.paymentCommission), loan.getPayments()));
    }

    GraphicalView lineChartView = ChartFactory.getLineChartView(view.getContext(), dataset, renderer);
    LinearLayout linePlaceholder = (LinearLayout)view.findViewById(R.id.lineChartPlaceholder);
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
    renderer.setLegendTextSize(getResources().getDimension(R.dimen.abs__action_bar_title_text_size));


    CategorySeries series = new CategorySeries("Pie");
    GraphicalView pieChartView = ChartFactory.getPieChartView(view.getContext(), series, renderer);

    series.add(getString(R.string.paymentPrincipal), loan.getAmount().doubleValue());
    SimpleSeriesRenderer principalRenderer = new SimpleSeriesRenderer();
    principalRenderer.setDisplayChartValues(true);
    renderer.addSeriesRenderer(0, principalRenderer);
    principalRenderer.setColor(getResources().getColor(R.color.principal));


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


    LinearLayout placeholder = (LinearLayout)view.findViewById(R.id.pieChartPlaceholder);
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
}
