

/*
 * Class:        XYLineChart
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

import   org.jfree.chart.*;
import   org.jfree.chart.axis.*;
import   org.jfree.chart.plot.*;
import   org.jfree.chart.renderer.xy.*;
import   org.jfree.data.xy.*;
import   org.jfree.data.category.*;
import   org.jfree.chart.renderer.category.*;
import   java.util.Locale;
import   java.util.Formatter;
import   java.lang.Math;
import   java.awt.*;
import   java.awt.geom.*;
import   cern.colt.list.DoubleArrayList;
import   javax.swing.JFrame;

/**
 * This class provides tools to create and manage curve plots. Using the
 * {@link XYLineChart} class is the simplest way to produce curve plots only.
 * Each {@link XYLineChart} object is linked with a
 * {@link umontreal.iro.lecuyer.charts.XYListSeriesCollection XYListSeriesCollection} data set.
 * 
 */
public class XYLineChart extends XYChart  {

   protected void init (String title, String XLabel, String YLabel) {
      // create the chart...
      chart = ChartFactory.createXYLineChart (
         title,                    // chart title
         XLabel,                   // x axis label
         YLabel,                   // y axis label
         dataset.getSeriesCollection(), // data
         PlotOrientation.VERTICAL,
         false,                    // include legend
         true,                     // tooltips
         false                     // urls
      );

      if (null != title) {
         if (title.startsWith("cdf") || title.startsWith("prob") || title.startsWith("density"))
            setprobFlag (true);
      }

      ((XYPlot)chart.getPlot()).setRenderer(dataset.getRenderer());
      // Initialize axis variables
      initAxis();
   }

   protected void initAxis(){
      XAxis = new Axis((NumberAxis)((XYPlot)chart.getPlot()).getDomainAxis(),
                        Axis.ORIENTATION_HORIZONTAL);
      YAxis = new Axis((NumberAxis)((XYPlot)chart.getPlot()).getRangeAxis(),
                        Axis.ORIENTATION_VERTICAL);
      setAutoRange(true, true);
   }



   /**
    * Initializes a new <TT>XYLineChart</TT> instance with an empty data set.
    * 
    */
   public XYLineChart()  {
      super();
      dataset = new XYListSeriesCollection();
      init (null, null, null);
   }


   /**
    * Initializes a new <TT>XYLineChart</TT> instance with sets of points <TT>data</TT>.
    * <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of the
    * <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT> a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * The input parameter <TT>data</TT>  represents a set of plotting data.
    * 
    * <P>
    * For example, if one <SPAN CLASS="MATH"><I>n</I></SPAN>-row matrix <TT>data1</TT> is given as argument
    *  <TT>data</TT>, then the first row <TT>data1</TT><SPAN CLASS="MATH">[0]</SPAN> represents the
    *  <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinate vector, and every other row <TT>data1</TT>
    * <SPAN CLASS="MATH">[<I>i</I>], <I>i</I> = 1,&#8230;, <I>n</I> - 1</SPAN>, represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinate set for a curve.
    *   Therefore matrix <TT>data1</TT><SPAN CLASS="MATH">[<I>i</I>][<I>j</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>,  corresponds
    *    to <SPAN CLASS="MATH"><I>n</I> - 1</SPAN> curves, all with the same <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates.
    * 
    * <P>
    * However, one may want to plot several curves with different <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates.
    *   In that case, one should give the curves as matrices with two rows.
    * For examples, if the argument <TT>data</TT> is made of three 2-row matrices
    * <TT>data1</TT>, <TT>data2</TT> and <TT>data3</TT>, then they represents
    *  three different curves, <TT>data*</TT><SPAN CLASS="MATH">[0]</SPAN> being the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates,
    *  and  <TT>data*</TT><SPAN CLASS="MATH">[1]</SPAN> the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates of the curves.
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
   public XYLineChart (String title, String XLabel, String YLabel,
                       double[][]... data)  {
      super();
      dataset = new XYListSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>XYLineChart</TT> instance with sets of points <TT>data</TT>.
    * <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of the
    * <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT> a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *  If <TT>data</TT> is a <SPAN CLASS="MATH"><I>n</I></SPAN>-row matrix,
    *  then the first row <TT>data</TT><SPAN CLASS="MATH">[0]</SPAN> represents the
    *  <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinate vector, and every other row <TT>data</TT>
    * <SPAN CLASS="MATH">[<I>i</I>], <I>i</I> = 1,&#8230;, <I>n</I> - 1</SPAN>, represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinate set of points.
    *   Therefore matrix <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>][&nbsp;]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>,  corresponds
    *    to <SPAN CLASS="MATH"><I>n</I> - 1</SPAN> curves, all with the same <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates.
    *   However, only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>data</TT> will
    *   be considered to plot each curve.
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
   public XYLineChart (String title, String XLabel, String YLabel,
                       double[][] data, int numPoints)  {
      super();
      dataset = new XYListSeriesCollection(data, numPoints);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>XYLineChart</TT> instance using subsets of <TT>data</TT>.
    * <TT>data[x][.]</TT> will form the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates and
    * <TT>data[y][.]</TT> will form the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates of the chart.
    * <TT>title</TT> sets a title, <TT>XLabel</TT> is a short description of the
    * <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT> is a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * Warning: if the new <SPAN CLASS="MATH"><I>x</I></SPAN>-axis coordinates are not monotone increasing, then
    * they will automatically be sorted in increasing order so the points will
    * be reordered, but the original <TT>data</TT> is not changed.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series of point sets.
    * 
    *    @param x Index of data forming the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates
    * 
    *    @param y Index of data forming the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates
    * 
    * 
    */
   public XYLineChart (String title, String XLabel, String YLabel,
                       double[][] data, int x, int y)  {
      super();
      int len = data[0].length;
      double[][] proj = new double[2][len];
      for (int i = 0; i < len; i++) {
         proj[0][i] = data[x][i];
         proj[1][i] = data[y][i];
      }
      dataset = new XYListSeriesCollection(proj);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>XYLineChart</TT> instance with data <TT>data</TT>.
    *    The input parameter <TT>data</TT> represents a set of plotting data. A
    *    {@link cern.colt.list.DoubleArrayList DoubleArrayList} from the Colt library is
    *    used to store the data. The description is similar to the
    *    constructor  {@link YListChart} with <TT>double[]... data</TT>.
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
   public XYLineChart (String title, String XLabel, String YLabel,
                       DoubleArrayList... data)  {
      super();
      dataset = new XYListSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>XYLineChart</TT> instance with data <TT>data</TT>.
    *    The input parameter <TT>data</TT> represents a set of plotting data.
    *    {@link org.jfree.data.xy.XYSeriesCollection XYSeriesCollection} is a
    *    <TT>JFreeChart</TT> container class to store <SPAN CLASS="MATH"><I>XY</I></SPAN> plots.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series collection.
    * 
    */
   public XYLineChart (String title, String XLabel, String YLabel,
                       XYSeriesCollection data)  {
      super();
      dataset = new XYListSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Adds a data series into the series collection. Vector <TT>x</TT> represents
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates and vector <TT>y</TT> represents the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates of
    *    the series. <TT>name</TT> and <TT>plotStyle</TT> are the name and the plot
    *    style associated to the series.
    * 
    * @param x <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> coordinates.
    * 
    *    @param y <SPAN CLASS="MATH"><I>y</I><SUB>i</SUB></SPAN> coordinates.
    * 
    *    @param name Name of the series.
    * 
    *    @param plotStyle Plot style of the series.
    * 
    *    @return Integer that represent the new point set's position in the JFreeChart <TT>XYSeriesCollection</TT> object.
    * 
    */
   public int add (double[] x, double[] y, String name, String plotStyle)  {
      int seriesIndex = add(x,y);
      getSeriesCollection().setName(seriesIndex, name);
      getSeriesCollection().setPlotStyle (seriesIndex, plotStyle);
      return seriesIndex;
   }


   /**
    * Adds a data series into the series collection. Vector <TT>x</TT> represents
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates and vector <TT>y</TT> represents the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates of
    *    the series.
    * 
    * @param x <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> coordinates.
    * 
    *    @param y <SPAN CLASS="MATH"><I>y</I><SUB>i</SUB></SPAN> coordinates.
    * 
    *    @return Integer that represent the new point set's position in the JFreeChart <TT>XYSeriesCollection</TT> object.
    * 
    */
   public int add (double[] x, double[] y)  {
      int seriesIndex = getSeriesCollection().add(x,y);
      initAxis();
      return seriesIndex;
   }


   /**
    * Adds a data series into the series collection. Vector <TT>x</TT> represents
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates and vector <TT>y</TT> represents the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates of
    *    the series. Only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>x</TT> and
    *    <TT>y</TT> will be taken into account for the new series.
    * 
    * @param x <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> coordinates.
    * 
    *    @param y <SPAN CLASS="MATH"><I>y</I><SUB>i</SUB></SPAN> coordinates.
    * 
    *    @param numPoints Number of points to add
    * 
    *    @return Integer that represent the new point set's position in the JFreeChart <TT>XYSeriesCollection</TT> object.
    * 
    */
   public int add (double[] x, double[] y, int numPoints)  {
      int seriesIndex = getSeriesCollection().add(x, y, numPoints);
      initAxis();
      return seriesIndex;
   }


   /**
    * Adds the new collection of data series <TT>data</TT> into the series collection.
    *    If <TT>data</TT> is a <SPAN CLASS="MATH"><I>n</I></SPAN>-row matrix,
    *  then the first row <TT>data</TT><SPAN CLASS="MATH">[0]</SPAN> represents the
    *  <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinate vector, and every other row <TT>data</TT>
    * <SPAN CLASS="MATH">[<I>i</I>], <I>i</I> = 1,&#8230;, <I>n</I> - 1</SPAN>, represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinate set of points.
    *   Therefore matrix <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>][&nbsp;]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>,  corresponds
    *    to <SPAN CLASS="MATH"><I>n</I> - 1</SPAN> curves, all with the same <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates.
    * 
    * @param data series of point sets.
    * 
    * 
    */
   public int add (double[][] data)  {
      int seriesIndex = getSeriesCollection().add(data);
      initAxis();
      return seriesIndex;
   }


   /**
    * Adds the new collection of data series <TT>data</TT> into the series collection.
    *    If <TT>data</TT> is a <SPAN CLASS="MATH"><I>n</I></SPAN>-row matrix,
    *  then the first row <TT>data</TT><SPAN CLASS="MATH">[0]</SPAN> represents the
    *  <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinate vector, and every other row <TT>data</TT>
    * <SPAN CLASS="MATH">[<I>i</I>], <I>i</I> = 1,&#8230;, <I>n</I> - 1</SPAN>, represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinate set of points.
    *   Therefore matrix <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>][&nbsp;]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>,  corresponds
    *    to <SPAN CLASS="MATH"><I>n</I> - 1</SPAN> curves, all with the same <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates.
    *   However, only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>data</TT> will
    *   be taken into account for the new series.
    * 
    * @param data series of point sets.
    * 
    *    @param numPoints Number of points to plot
    * 
    * 
    */
   public int add (double[][] data, int numPoints)  {
      int seriesIndex = getSeriesCollection().add(data, numPoints);
      initAxis();
      return seriesIndex;
   }


   /**
    * Returns the chart's dataset.
    * 
    * @return the chart's dataset.
    * 
    */
   public XYListSeriesCollection getSeriesCollection()  {
      return (XYListSeriesCollection)dataset;
   }


   /**
    * Links a new dataset to the current chart.
    * 
    * @param dataset new dataset.
    * 
    * 
    */
   public void setSeriesCollection (XYListSeriesCollection dataset)  {
      this.dataset = dataset;
   }


   /**
    * Synchronizes <SPAN CLASS="MATH"><I>X</I></SPAN>-axis ticks to the <SPAN CLASS="MATH"><I>s</I></SPAN>-th series <SPAN CLASS="MATH"><I>x</I></SPAN>-values.
    * 
    * @param s series used to define ticks.
    * 
    * 
    */
   public void setTicksSynchro (int s)  {
      XYSeriesCollection seriesCollection =
          (XYSeriesCollection)this.dataset.getSeriesCollection();
      double[] values = new double[seriesCollection.getItemCount(s)];

      for(int i = 0; i < seriesCollection.getItemCount(s); i++)
         values[i] = seriesCollection.getXValue(s, i);

      XAxis.setLabels(values);
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
    *    @return frame containing the chart.;
    * 
    */
   public JFrame view (int width, int height)  {
      JFrame myFrame;
      if(chart.getTitle() != null)
         myFrame = new JFrame("XYLineChart from SSJ: " + chart.getTitle().getText());
      else
         myFrame = new JFrame("XYLineChart from SSJ");
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
      myFrame.setContentPane(chartPanel);
      myFrame.pack();
      myFrame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
      myFrame.setLocationRelativeTo (null);
      myFrame.setVisible(true);
      return myFrame;
   }

   /**
    * Displays bar chart on the screen using Swing.
    *    This method creates an application containing a bar chart panel displaying
    *    the chart.  The created frame is positioned on-screen, and displayed before
    *    it is returned. The <TT>width</TT> and the <TT>height</TT>
    *    of the chart are measured in pixels.
    * 
    * @param width frame width in pixels.
    * 
    *    @param height frame height in pixels.
    * 
    *    @return frame containing the bar chart.;
    * 
    */
   public JFrame viewBar (int width, int height)  {
      JFrame myFrame;
      if (chart.getTitle() != null)
         myFrame = new JFrame("XYLineChart from SSJ: " + chart.getTitle().getText());
      else
         myFrame = new JFrame("XYLineChart from SSJ");

      XYPlot plot = (XYPlot) chart.getPlot();

      //Create the bar
      plot.setDataset(0, dataset.getSeriesCollection());
      final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
      renderer.setSeriesPaint(0,Color.ORANGE);
      renderer.setSeriesShape(0,new Line2D.Double(0, 0, 0 , 1000));
      plot.setRenderer(0, renderer);

      //Create the points
      plot.setDataset(1, dataset.getSeriesCollection());
      final XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(false, true);
      renderer2.setSeriesPaint(0,Color.ORANGE);
      renderer2.setSeriesShape(0,new Ellipse2D.Double(-2.0,-2.0,4.0,4.0));
      plot.setRenderer(1, renderer2);

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
      double xunit=0, yunit=0;
      double[] save = new double[4];

      if (dataset.getSeriesCollection().getSeriesCount() == 0)
         throw new IllegalArgumentException("Empty chart");

      //Calcul des parametres d'echelle et de decalage
      double XScale = computeXScale(XAxis.getTwinAxisPosition());
      double YScale = computeYScale(YAxis.getTwinAxisPosition());

      // taille d'une unite en x et en cm dans l'objet "tikzpicture"
      xunit = width / ((Math.max(XAxis.getAxis().getRange().getUpperBound(), XAxis.getTwinAxisPosition()) * XScale) - (Math.min(XAxis.getAxis().getRange().getLowerBound(), XAxis.getTwinAxisPosition()) * XScale));
      // taille d'une unite en y et en cm dans l'objet "tikzpicture"
      yunit = height / ((Math.max(YAxis.getAxis().getRange().getUpperBound(), YAxis.getTwinAxisPosition()) * YScale) - (Math.min(YAxis.getAxis().getRange().getLowerBound(), YAxis.getTwinAxisPosition()) * YScale));

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
