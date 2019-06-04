package com.estt.tool.test;

import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.estt.tool.test.service.EndPointSmokeTestService;

/**
 * Created by saurabh.yagnik on 2016/10/12.
 */
@Component
public class MainApplication {
	
	// variables for piechart render
	private PieChart pieChart;

	@Autowired
	private EndPointSmokeTestService endPointSmokeTestService;

	/**
	 * This is the main entry point for end point smoke test tool, accepts two
	 * argument
	 * 
	 * @param args: args[0] : configuration file directory path, args[1]: mandatory field, pass userid to track and fetch data who initiated process,
	 * 			 args[2] : value must be "piechart" optional to check real time pie chart
	 * @throws Exception : throws exception of arguments are not enough
	 */
	public static void main(String[] args) throws Exception {

		@SuppressWarnings("resource")
		MainApplication mainApplication = new ClassPathXmlApplicationContext("applicationContext.xml")
				.getBean(MainApplication.class);

		if (args.length == 0) {
			System.out.println("Please provide configuration and out directories path");
			System.exit(1);
		}
		mainApplication.testEndPoints(args);
	}

	public void testEndPoints(String... args) throws Exception {
		if (args.length == 3 && "piechart".equals(args[2])) {
			// start pie chart
			startPieChart();
		}
		endPointSmokeTestService.testEndPoints(args);
	}

	public void startPieChart() {

		final SwingWrapper<PieChart> swingWrapper = new SwingWrapper<PieChart>(getChart());
		swingWrapper.displayChart("Smoke Test");

		// Simulate a data feed
		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

				updateData();

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						swingWrapper.repaintChart();
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
	}

	public PieChart getChart() {

		// Create Chart
		pieChart = new PieChartBuilder().width(1000).height(700).theme(ChartTheme.Matlab).title("Smoke Test Pie Chart")
				.build();

		// Customize Chart
		pieChart.getStyler().setLegendVisible(false);
		pieChart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
		pieChart.getStyler().setAnnotationDistance(1.22);
		pieChart.getStyler().setPlotContentSize(.7);
		pieChart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Pie);

		Map<String, Number> pieData = endPointSmokeTestService.getStatusData();
		for (Entry<String, Number> entry : pieData.entrySet()) {
			pieChart.addSeries(entry.getKey(), entry.getValue());
		}
		return pieChart;
	}

	public void updateData() {

		Map<String, Number> pieData = endPointSmokeTestService.getStatusData();
		for (Entry<String, Number> entry : pieData.entrySet()) {
			if (entry.getKey().equals("SUCCESS")) {
				pieChart.getSeriesMap().get(entry.getKey()).setFillColor(Color.GREEN);
			} else {
				pieChart.getSeriesMap().get(entry.getKey()).setFillColor(Color.RED);
			}
			pieChart.updatePieSeries(entry.getKey(), entry.getValue());

		}
	}
}
