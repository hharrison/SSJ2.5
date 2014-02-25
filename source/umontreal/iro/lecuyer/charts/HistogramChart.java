

/*
 * Class:        HistogramChart
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       
 * @since

 * SSJ is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License (GPL) as published by the
 * Free Software Foundation, either version 3 of the License, or
 * any later version.

 * SSJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * A copy of the GNU General Public License is available at
   <a href="http://www.gnu.org/licenses">GPL licence site</a>.
 */

package umontreal.iro.lecuyer.charts;

import   umontreal.iro.lecuyer.stat.TallyStore;

import   org.jfree.chart.ChartFactory;
import   org.jfree.chart.ChartPanel;
import   org.jfree.chart.axis.NumberAxis;
import   org.jfree.chart.plot.XYPlot;
import   org.jfree.chart.plot.PlotOrientation;
import   org.jfree.data.statistics.HistogramBin;

import   cern.colt.list.DoubleArrayList;

import   java.util.ListIterator;
import   java.util.Locale;
import   java.util.Formatter;
import   javax.swing.JFrame;

/**
 * This class provides tools to create and manage histograms.
 * The {@link HistogramChart} class is the simplest way to produce histograms.
 * Each {@link HistogramChart} object is linked with an
 * {@link umontreal.iro.lecuyer.charts.HistogramSeriesCollection HistogramSeriesCollection} dataset.
 * 
 */
public class HistogramChart extends XYChart  {

   protected void init (String title, String XLabel, String YLabel) {
      // create the chart...
      chart = ChartFactory.createXYLineChart(
         title,                    // chart title
         XLabel,                   // x axis label
         YLabel,                   // y axis label
         dataset.getSeriesCollection(), // data
         PlotOrientation.VERTICAL,
         true,                     // include legend
         true,                     // tooltips
         false                     // urls
      );
      ((XYPlot)chart.getPlot()).setRenderer(dataset.getRenderer());
      //Initialize axis variables
      XAxis = new Axis( (NumberAxis)((XYPlot) chart.getPlot()).getDomainAxis(),
            Axis.ORIENTATION_HORIZONTAL );
      YAxis = new Axis( (NumberAxis)((XYPlot) chart.getPlot()).getRangeAxis() ,
            Axis.ORIENTATION_VERTICAL );
      setAutoRange(false, true, true, true);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with an empty data set.
    * 
    */
   public HistogramChart ()  {
      super();
      dataset = new HistogramSeriesCollection();
      init (null, null, null);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with input <TT>data</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT> a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input parameter <TT>data</TT> represents a collection of observation sets.
    *    Therefore <TT>data</TT>
    * <SPAN CLASS="MATH">[<I>i</I>], <I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>, is used to plot the
    *      <SPAN CLASS="MATH"><I>i</I></SPAN>th histogram.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series of point sets.
    * 
    * 
    */
   public HistogramChart (String title, String XLabel, String YLabel,
                          double[]... data)  {
      super();
      dataset = new HistogramSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with input <TT>data</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT> a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input parameter <TT>data</TT> represents an observation set.
    *   Only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>data</TT> will
    *   be considered to plot the histogram.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series of point sets.
    * 
    *    @param numPoints Number of points to plot
    * 
    * 
    */
   public HistogramChart (String title, String XLabel, String YLabel,
                          double[] data, int numPoints)  {
      super();
      double[] datan = new double[numPoints];
      System.arraycopy (data, 0, datan, 0, numPoints);
      dataset = new HistogramSeriesCollection(datan);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with data <TT>data</TT>.
    *    Each <TT>DoubleArrayList</TT> input parameter represents a collection of
    *    observation sets.
    *    {@link cern.colt.list.DoubleArrayList DoubleArrayList} is from the Colt library
    *    and is used to store data.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series of observation sets.
    * 
    * 
    */
   public HistogramChart (String title, String XLabel, String YLabel,
                          DoubleArrayList... data)  {
      super();
      dataset = new HistogramSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with data arrays contained in each
    *    {@link umontreal.iro.lecuyer.stat.TallyStore TallyStore} object.
    *    The input parameter <TT>tallies</TT> represents a collection of observation sets.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param tallies series of observation sets.
    * 
    * 
    */
   public HistogramChart (String title, String XLabel, String YLabel,
                          TallyStore... tallies)  {
      super();
      dataset = new HistogramSeriesCollection(tallies);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with data <TT>data</TT>.
    *    The input parameter <TT>data</TT> represents a set of plotting data.
    *    {@link umontreal.iro.lecuyer.charts.CustomHistogramDataset CustomHistogramDataset} is a
    *    <TT>JFreeChart</TT>-like container class that stores and manages
    *     observation sets.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series collection.
    * 
    * 
    */
   public HistogramChart (String title, String XLabel, String YLabel,
                          CustomHistogramDataset data)  {
      super();
      dataset = new HistogramSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>HistogramChart</TT> instance with data <TT>count</TT>
    *    and <TT>bound</TT>. The adjacent categories (or bins) are specified as
    *    non-overlapping intervals: bin[j] contains the values in the interval
    *    [<TT>bound[j]</TT>, <TT>bound[j+1]</TT>], and <TT>count[j]</TT> is the
    *    number of such values. Thus the length of <TT>bound</TT> must be equal to
    *    the length of <TT>count</TT> plus one: the last value of <TT>bound</TT>
    *    is the right boundary of the last bin.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param count the number of observation between each bound.
    * 
    *    @param bound the bounds of the observations
    * 
    */
    public HistogramChart (String title, String XLabel, String YLabel,
                           int[] count, double[] bound)  {
      super();
      if (bound.length != count.length + 1)
         throw new IllegalArgumentException (
            "bound.length must be equal to count.length + 1");
      int sum = 0;
      for (int i = 0 ; i < count.length ; i++) sum +=count[i];
      double[] data = new double [sum];
      int k = 0;
      for (int i = 0 ; i < count.length ; i++)
         for (int j = 0 ; j < count[i] ; j++)
            data[k++] = bound[i];

      dataset = new HistogramSeriesCollection(data, sum);
      init (title, XLabel, YLabel);
   }


   public void setAutoRange (boolean right, boolean top)  {
         throw new UnsupportedOperationException(
            "You can't use setAutoRange with HistogramChart class, use setAutoRange().");
   }
   public void setManuelRange (double [] range, boolean right, boolean top) {
         throw new UnsupportedOperationException(
            "You can't use setManuelRange with HistogramChart class, use setManuelRange(range).");
   }


   /**
    * Returns the chart's dataset.
    * 
    * @return the chart's dataset.
    * 
    */
   public HistogramSeriesCollection getSeriesCollection()  {
      return (HistogramSeriesCollection)dataset;
   }


   /**
    * Links a new dataset to the current chart.
    * 
    * @param dataset new dataset.
    * 
    * 
    */
   public void setSeriesCollection (HistogramSeriesCollection dataset)  {
      this.dataset = dataset;
   }


   /**
    * Synchronizes <SPAN CLASS="MATH"><I>x</I></SPAN>-axis ticks to the <SPAN CLASS="MATH"><I>s</I></SPAN>-th histogram bins.
    * 
    * @param s selects histogram used to define ticks.
    * 
    * 
    */
   public void setTicksSynchro (int s)  {
      if (((CustomHistogramDataset)this.dataset.getSeriesCollection()).getBinWidth(s) == -1){
         DoubleArrayList newTicks = new DoubleArrayList();
         ListIterator binsIter = ((HistogramSeriesCollection)this.dataset).getBins(s).listIterator();

         int i = 0;
         HistogramBin prec = (HistogramBin)binsIter.next();
         double var;
         newTicks.add(prec.getStartBoundary());
         newTicks.add(var = prec.getEndBoundary());
         HistogramBin temp;
         while(binsIter.hasNext()) {
            temp = (HistogramBin)binsIter.next();
            if(temp.getStartBoundary() != var) {
               newTicks.add(var = temp.getStartBoundary());
            } else if(temp.getEndBoundary() != var) {
               newTicks.add(var = temp.getEndBoundary());
            }
         }
         XAxis.setLabels(newTicks.elements());
      }
      else {
         XAxis.setLabels(((CustomHistogramDataset)this.dataset.getSeriesCollection()).getBinWidth(s));
      }
   }


   /**
    * Displays chart on the screen using Swing.
    *    This method creates an application containing a chart panel displaying
    *    the chart. The created frame is positioned on-screen, and displayed before
    *    it is returned. The <TT>width</TT> and the <TT>height</TT>
    *    of the chart are measured in pixels.
    * 
    * @param width frame width in pixels.
    * 
    *    @param height frame height in pixels.
    * 
    *    @return frame containing the chart.
    * 
    */
   public JFrame view (int width, int height)  {
      JFrame myFrame;
      if(chart.getTitle() != null)
         myFrame = new JFrame("HistogramChart from SSJ: " + chart.getTitle().getText());
      else
         myFrame = new JFrame("HistogramChart from SSJ");
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
      myFrame.setContentPane(chartPanel);
      myFrame.pack();
      myFrame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
      myFrame.setLocationRelativeTo (null);
      myFrame.setVisible(true);
      return myFrame;
   }


   public String toLatex (double width, double height)  {
      double xunit, yunit;
      double[] save = new double[4];

      if (dataset.getSeriesCollection().getSeriesCount() == 0)
         throw new IllegalArgumentException("Empty chart");
      if (YAxis.getTwinAxisPosition() < 0)
         YAxis.setTwinAxisPosition(0);

      // Calcul des parametres d'echelle et de decalage
      double XScale = computeXScale(XAxis.getTwinAxisPosition());
      double YScale = computeYScale(YAxis.getTwinAxisPosition());

      // taille d'une unite en x et en cm dans l'objet "tikzpicture"
      xunit = width / ( (Math.max(XAxis.getAxis().getRange().getUpperBound(), XAxis.getTwinAxisPosition()) * XScale) - (Math.min(XAxis.getAxis().getRange().getLowerBound(), XAxis.getTwinAxisPosition()) * XScale) );
      // taille d'une unite en y et en cm dans l'objet "tikzpicture"
     yunit = height / ( (Math.max(YAxis.getAxis().getRange().getUpperBound(), YAxis.getTwinAxisPosition()) * YScale) - (Math.min(YAxis.getAxis().getRange().getLowerBound(), YAxis.getTwinAxisPosition()) * YScale) );

      Formatter formatter = new Formatter(Locale.US);

      /*Entete du document*/
      if (latexDocFlag) {
         formatter.format("\\documentclass[12pt]{article}%n%n");
         formatter.format("\\usepackage{tikz}%n\\usetikzlibrary{plotmarks}%n\\begin{document}%n%n");
      }
      if (chart.getTitle() != null)
         formatter.format("%% PGF/TikZ picture from SSJ: %s%n", chart.getTitle().getText());
      else
         formatter.format("%% PGF/TikZ picture from SSJ %n");
      formatter.format("%% XScale = %s,  YScale = %s,  XShift = %s,  YShift = %s%n", XScale, YScale, XAxis.getTwinAxisPosition(), YAxis.getTwinAxisPosition());
      formatter.format("%% Therefore, thisFileXValue = (originalSeriesXValue+XShift)*XScale%n");
      formatter.format("%%        and thisFileYValue = (originalSeriesYValue+YShift)*YScale%n%n");
      if (chart.getTitle() != null)
         formatter.format("\\begin{figure}%n");
      formatter.format("\\begin{center}%n");
      formatter.format("\\begin{tikzpicture}[x=%scm, y=%scm]%n", xunit, yunit);
      formatter.format("\\footnotesize%n");
      if (grid)
         formatter.format("\\draw[color=lightgray] (%s, %s) grid[xstep = %s, ystep=%s] (%s, %s);%n",
            (Math.min(XAxis.getAxis().getRange().getLowerBound(), XAxis.getTwinAxisPosition())-XAxis.getTwinAxisPosition()) * XScale,
            (Math.min(YAxis.getAxis().getRange().getLowerBound(), YAxis.getTwinAxisPosition())-YAxis.getTwinAxisPosition()) * YScale,
            xstepGrid*XScale, ystepGrid*YScale,
            (Math.max(XAxis.getAxis().getRange().getUpperBound(), XAxis.getTwinAxisPosition())-XAxis.getTwinAxisPosition()) * XScale,
            (Math.max(YAxis.getAxis().getRange().getUpperBound(), YAxis.getTwinAxisPosition())-YAxis.getTwinAxisPosition()) * YScale );
      setTick0Flags();
      formatter.format("%s", XAxis.toLatex(XScale) );
      formatter.format("%s", YAxis.toLatex(YScale) );

      formatter.format("%s", dataset.toLatex(
         XScale, YScale,
         XAxis.getTwinAxisPosition(), YAxis.getTwinAxisPosition(),
         XAxis.getAxis().getLowerBound(), XAxis.getAxis().getUpperBound(),
         YAxis.getAxis().getLowerBound(), YAxis.getAxis().getUpperBound()));

      formatter.format("\\end{tikzpicture}%n");
      formatter.format("\\end{center}%n");
      if (chart.getTitle() != null) {
         formatter.format("\\caption{");
         formatter.format(chart.getTitle().getText());
         formatter.format("}%n\\end{figure}%n");
      }
      if (latexDocFlag)
         formatter.format("\\end{document}%n");
      return formatter.toString();
   }


}
