

/*
 * Class:        YListSeriesCollection
 * Description:  Lists of y-coordinates of charts
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

import   org.jfree.data.xy.XYSeries;
import   org.jfree.data.xy.XYSeriesCollection;
import   org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


/**
 * This class extends
 * {@link umontreal.iro.lecuyer.charts.XYListSeriesCollection XYListSeriesCollection}.
 * The data is given as lists of <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates. The <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates are
 * regularly spaced multiples of the indices of the data points.
 * 
 */
public class YListSeriesCollection extends XYListSeriesCollection  {

   private void initYListSeries (double h, double[] data, int numPoints)
   {
      renderer = new XYLineAndShapeRenderer(true, false);
      seriesCollection = new XYSeriesCollection();

      XYSeriesCollection tempSeriesCollection =
         (XYSeriesCollection)seriesCollection;
      XYSeries serie = new XYSeries(" ");
      for (int j = 0; j < numPoints; j++)
         serie.add(h*(j+1), data[j]);
      tempSeriesCollection.addSeries(serie);

      // set default colors
      renderer.setSeriesPaint(0, getDefaultColor(0));

      // set default plot style
      plotStyle = new String[1];
      marksType = new String[1];
      dashPattern = new String[1];
      marksType[0] = " ";
      plotStyle[0] = "smooth";
      dashPattern[0] = "solid";
   }


   private void initYListSeries (boolean flag, double[]... data)
   {
      // if flag = true, h = 1; else h = 1/numPoints
      double h;
      renderer = new XYLineAndShapeRenderer(true, false);
      seriesCollection = new XYSeriesCollection();

      XYSeriesCollection tempSeriesCollection =
         (XYSeriesCollection)seriesCollection;
      for (int i = 0; i < data.length; i ++) {
         XYSeries serie = new XYSeries(" ");
         if (flag)
            h = 1;
         else
            h = 1.0 / data[i].length;
         for (int j = 0; j < data[i].length; j++)
            serie.add(h*(j+1), data[i][j]);
         tempSeriesCollection.addSeries(serie);
      }

      final int s = tempSeriesCollection.getSeriesCount();

      // set default colors
      for(int i = 0; i < s; i++) {
         renderer.setSeriesPaint(i, getDefaultColor(i));
      }

      // set default plot style
      plotStyle = new String[s];
      marksType = new String[s];
      dashPattern = new String[s];
      for (int i = 0; i < s; i++) {
         marksType[i] = " ";
         plotStyle[i] = "smooth";
         dashPattern[i] = "solid";
      }
   //   dashPattern[s-1] = "dashed";     // for the line y = x
   }


   /**
    * Creates a new <TT>YListSeriesCollection</TT> instance with default
    *    parameters and given data series. The input vectors represent sets of
    *    plotting data. More specifically, each vector <TT>data</TT> represents
    *    a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates set.
    *    Position in the vector will form the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates. Indeed the value
    *    <TT>data</TT><SPAN CLASS="MATH">[<I>j</I>]</SPAN> corresponds to the point
    *    
    * <SPAN CLASS="MATH">(<I>j</I> + 1,<TT>data</TT>[<I>j</I>])</SPAN> on the chart.
    * 
    * @param data series of point sets.
    * 
    * 
    */
   public YListSeriesCollection (double[]... data)  {
      initYListSeries (true, data);
   }


   /**
    * Similar to the constructor  above, except that if <TT>flag</TT> is <TT>true</TT>, the points are
    *  
    * <SPAN CLASS="MATH">(<I>j</I> + 1,<TT>data</TT>[<I>j</I>])</SPAN> for each series;
    *   but if <TT>flag</TT> is <TT>false</TT>,
    *   the points are 
    * <SPAN CLASS="MATH">((<I>j</I> + 1)/<I>n</I>,<TT>data</TT>[<I>j</I>])</SPAN>, where <SPAN CLASS="MATH"><I>n</I></SPAN> is
    *   the number of points of each series in <TT>data</TT>.
    * 
    * @param flag to choose the step between <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates
    * 
    *    @param data series of point sets.
    * 
    * 
    */
   public YListSeriesCollection (boolean flag, double[]... data)  {
      initYListSeries (flag, data);
   }


   /**
    * Creates a new <TT>YListSeriesCollection</TT> instance with default
    *    parameters and one data series.
    *    The vector <TT>data</TT> represents the <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinate of the points,
    *    and position in the vector represents the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinate.
    *   However, only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>data</TT> will
    *   be considered in the series.
    *   Thus the coordinates of the points are given by
    *    
    * <SPAN CLASS="MATH">(<I>j</I>,&nbsp;<TT>data</TT>[<I>j</I> - 1])</SPAN>,
    *     for 
    * <SPAN CLASS="MATH"><I>j</I> = 1, 2,&#8230;,<texttt>numPoints</texttt></SPAN>.
    * 
    * @param data point set.
    * 
    *    @param numPoints Number of points to plot
    * 
    * 
    */
   public YListSeriesCollection (double[] data, int numPoints)  {
      initYListSeries (1, data, numPoints);
   }


   /**
    * Similar to the constructor {@link #YListSeriesCollection(double[],int) YListSeriesCollection}<TT>(data, numPoints)</TT> above,  but the points are 
    * <SPAN CLASS="MATH">(<I>hj</I>,&nbsp;<TT>data</TT>[<I>j</I> - 1])</SPAN>,
    *     for 
    * <SPAN CLASS="MATH"><I>j</I> = 1, 2,&#8230;,<texttt>numPoints</texttt></SPAN>.
    * 
    * @param h step between <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates
    * 
    *    @param data point set.
    * 
    *    @param numPoints Number of points to plot
    * 
    * 
    */
   public YListSeriesCollection (double h, double[] data, int numPoints)  {
      initYListSeries (h, data, numPoints);
   }


   /**
    * Creates a new <TT>YListSeriesCollection</TT> instance with default
    *    parameters and given data series. The matrix <TT>data</TT> represents a
    *    set of plotting data. More specifically, each row of <TT>data</TT>
    *    represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates set.
    *    Position in the vector will form the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates. Indeed, for each serie
    *    <SPAN CLASS="MATH"><I>i</I></SPAN>, the value <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>][<I>j</I>]</SPAN> corresponds to the point
    *    
    * <SPAN CLASS="MATH">(<I>j</I> + 1,<TT>data</TT>[<I>j</I>])</SPAN> on the chart.
    *   However, only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>data</TT> will
    *   be considered for each series of points.
    * 
    * @param data series of point sets.
    * 
    *    @param numPoints Number of points to plot
    * 
    */
   public YListSeriesCollection (double[][] data, int numPoints)  {
      renderer = new XYLineAndShapeRenderer(true, false);
      seriesCollection = new XYSeriesCollection();

      XYSeriesCollection tempSeriesCollection =
         (XYSeriesCollection)seriesCollection;
      for (int i = 0; i < data.length; i ++) {
         XYSeries serie = new XYSeries(" ");
         for (int j = 0; j < numPoints; j++)
            serie.add(j + 1, data[i][j]);
         tempSeriesCollection.addSeries(serie);
      }

      final int s = tempSeriesCollection.getSeriesCount();

      // set default colors
      for (int i = 0; i < s; i++) {
         renderer.setSeriesPaint(i, getDefaultColor(i));
      }

      // set default plot style
      plotStyle = new String[s];
      marksType = new String[s];
      dashPattern = new String[s];
      for (int i = 0; i < s; i++) {
         marksType[i] = " ";
         plotStyle[i] = "smooth";
         dashPattern[i] = "solid";
      }
   }
}
