package com.sofia.uni.fmi.ai.kmedians.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LinePlot extends JFrame {
    public LinePlot(List<Double> metrics, String title) {
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i <= metrics.size() - 1; i++) {
            series.add(i + 1, metrics.get(i));
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createScatterPlot("", "X-Axis", "Y-Axis", dataset);
        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickUnit(new NumberTickUnit(2));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(new NumberTickUnit(2));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
        plot.setBackgroundPaint(new Color(235, 235, 235));

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
}
