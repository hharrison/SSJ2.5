

/*
 * Class:        YListChart
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

/**
 * This class extends the class
 * {@link umontreal.iro.lecuyer.charts.XYLineChart XYLineChart}.
 * Each {@link YListChart} object is associated with a
 * {@link umontreal.iro.lecuyer.charts.YListSeriesCollection YListSeriesCollection} data set.
 * The data is given as one or more lists of <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates.
 * The <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates are
 * regularly-spaced multiples of the indices of the data points.
 * 
 */
public class YListChart extends XYLineChart  {




   /**
    * Empty constructor.
    * 
    */
   public YListChart()  {
      super();
      // dataset = new XYListSeriesCollection();
      // init (null, null, null);
   }


   /**
    * Initializes a new <TT>YListChart</TT> instance with set of points <TT>data</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input vectors represents a set of plotting data. More specifically,
    *    each vector <TT>data</TT> represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinates set.
    *    Position in the vector will form the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates.
    *    Indeed, the value <TT>data</TT><SPAN CLASS="MATH">[<I>j</I>]</SPAN> corresponds to the point
    *    
    * <SPAN CLASS="MATH">(<I>j</I> + 1,<texttt>data</texttt>[<I>j</I>])</SPAN> (but rescaled) on the chart.
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
   public YListChart (String title, String XLabel, String YLabel,
                      double[]... data)  {
      super();
      dataset = new YListSeriesCollection(data);
      init (title, XLabel, YLabel);
   }


   /**
    * Similar to the constructor  above.  Except that if <TT>flag</TT> is <TT>true</TT>, the points are
    *  
    * <SPAN CLASS="MATH">(<I>j</I> + 1,<TT>data</TT>[<I>j</I>])</SPAN> for each series;
    *   but if <TT>flag</TT> is <TT>false</TT>,
    *   the points are 
    * <SPAN CLASS="MATH">((<I>j</I> + 1)/<I>n</I>,<TT>data</TT>[<I>j</I>])</SPAN>, where <SPAN CLASS="MATH"><I>n</I></SPAN> is
    *   the number of points of each series in <TT>data</TT>.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param flag to choose the step between <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates
    * 
    *    @param data series of point sets.
    * 
    * 
    */
   public YListChart (String title, String XLabel, String YLabel,
                      boolean flag, double[]... data)  {
      super();
      dataset = new YListSeriesCollection(flag, data);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>YListChart</TT> instance with a set of points
    *    <TT>data</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input vector represents a set of plotting data.
    *    Position in the vector gives the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates of the curve.
    *    The value <TT>data</TT><SPAN CLASS="MATH">[<I>j</I>]</SPAN> corresponds to the point
    *    <SPAN CLASS="MATH">(<I>j</I> + 1</SPAN>, <TT>data</TT><SPAN CLASS="MATH">[<I>j</I>]</SPAN>) (but rescaled on the chart) for the curve.
    *    However, only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>data</TT>
    *     will be considered to plot the curve.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data point set.
    * 
    *    @param numPoints number of points to plot
    * 
    * 
    */
   public YListChart (String title, String XLabel, String YLabel,
                      double[] data, int numPoints)  {
      super();
      dataset = new YListSeriesCollection(data, numPoints);
      init (title, XLabel, YLabel);
   }


   /**
    * Similar to the constructor  above, but the points are 
    * <SPAN CLASS="MATH">(<I>h</I>(<I>j</I> + 1),&nbsp;<TT>data</TT>[<I>j</I>])</SPAN>.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param h step between <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates
    * 
    *    @param data point set.
    * 
    *    @param numPoints number of points to plot
    * 
    * 
    */
   public YListChart (String title, String XLabel, String YLabel,
                      double h, double[] data, int numPoints)  {
      super();
      dataset = new YListSeriesCollection(h, data, numPoints);
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>YListChart</TT> instance with set of points <TT>data</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input vectors represents a set of plotting data. More specifically,
    *    for a <SPAN CLASS="MATH"><I>n</I></SPAN>-row matrix <TT>data</TT>, each row <TT>data</TT>
    * <SPAN CLASS="MATH">[<I>i</I>], <I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>, represents a <SPAN CLASS="MATH"><I>y</I></SPAN>-coordinate set for a curve.
    *    Position in the vector gives the <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates of the curves.
    *    Indeed, the value <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>][<I>j</I>]</SPAN> corresponds to the point
    *    <SPAN CLASS="MATH">(<I>j</I> + 1</SPAN>, <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>][<I>j</I>]</SPAN>) (but rescaled on the chart) for curve <SPAN CLASS="MATH"><I>i</I></SPAN>.
    *    However, only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of each <TT>data</TT><SPAN CLASS="MATH">[<I>i</I>]</SPAN>
    *     will be considered to plot each curve.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param data series of point sets.
    * 
    *    @param numPoints number of points to plot
    * 
    */
   public YListChart (String title, String XLabel, String YLabel,
                      double[][] data, int numPoints)  {
      super();
      dataset = new YListSeriesCollection(data, numPoints);
      init (title, XLabel, YLabel);
   }
}
