

/*
 * Class:        QQPlot
 * Description:  qq-plot
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        May 2011

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
   import umontreal.iro.lecuyer.probdist.ContinuousDistribution;
   import java.util.Arrays;



/**
 * This class implements <SPAN  CLASS="textit">QQ-plot</SPAN> (or quantile-quantile plot)
 * objects that compare two probability distributions.
 * The data is given as a list of <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates 
 * <SPAN CLASS="MATH">(<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>n</SUB>)</SPAN>,
 * and one is given a reference continuous probability distribution  <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
 * One first sorts the <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> in ascending order, then noted <SPAN CLASS="MATH"><I>x</I><SUB>(i)</SUB></SPAN>, and
 * plots the points 
 * <SPAN CLASS="MATH">(<I>F</I><SUP>-1</SUP>(<I>p</I><SUB>i</SUB>), <I>x</I><SUB>(i)</SUB>)</SPAN>, where
 * 
 * <SPAN CLASS="MATH"><I>i</I> = 1, 2,&#8230;, <I>n</I></SPAN> and  
 * <SPAN CLASS="MATH"><I>p</I><SUB>i</SUB> = (<I>i</I> - 1/2)/<I>n</I></SPAN>,
 * to see if the data <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> comes from the reference distribution  <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
 * The graph of the straight line <SPAN CLASS="MATH"><I>y</I> = <I>x</I></SPAN> is also plotted for comparison.
 * 
 */
public class QQPlot extends XYLineChart  {
   private double[][] Q;        // data points
   private double[][] Lin;      // line y = x

   private void initLinear (double a, double b)
   {
      // line y = x in [a, b] by steps of h
      int m = 100;
      double h = (b - a)/ m;
      Lin = new double[2][m+1];
      for (int i = 0; i <= m; i++)
         Lin[0][i] = Lin[1][i] = a + h * i;
   }


   private void initPoints (ContinuousDistribution dist, double[] data,
                            int numPoints)
   {
      int i;
      double p;
      Q = new double[2][numPoints];     // q_i = cdf^(-1)(p_i)

      for (i = 0; i < numPoints; i++)
         Q[1][i] = data[i];
      Arrays.sort(Q[1]);
      for (i = 0; i < numPoints; i++) {
         p = (i + 0.5)/numPoints;
         Q[0][i] = dist.inverseF(p);
      }
   }


   /**
    * Constructs a new <TT>QQPlot</TT> instance using the points <TT>X</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The plot is a QQ-plot of the points
    *    
    * <SPAN CLASS="MATH">(<I>F</I><SUP>-1</SUP>(<I>p</I><SUB>i</SUB>), <I>x</I><SUB>(i)</SUB>)</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1, 2,&#8230;, <I>n</I></SPAN>, where  
    * <SPAN CLASS="MATH"><I>p</I><SUB>i</SUB> = (<I>i</I> - 1/2)/<I>n</I></SPAN>,
    *    <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB> =</SPAN><TT> X[<SPAN CLASS="MATH"><I>i</I></SPAN>-1]</TT>, <SPAN CLASS="MATH"><I>x</I><SUB>(i)</SUB></SPAN> are the  sorted points,
    *     and 
    * <SPAN CLASS="MATH"><I>x</I> = <I>F</I><SUP>-1</SUP>(<I>p</I>) =</SPAN><TT> dist.inverseF(<SPAN CLASS="MATH"><I>p</I></SPAN>)</TT>. The points <TT>X</TT> are not  sorted.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param dist Reference distribution
    * 
    *    @param X points.
    * 
    * 
    */
   public QQPlot (String title, String XLabel, String YLabel,
                  ContinuousDistribution dist, double[] X)  {
      this (title, XLabel, YLabel, dist, X, X.length);
   }


   /**
    * Similar to the constructor {@link #QQPlot(String,String,String,ContinuousDistribution,double[]) QQPlot}<TT>(title, XLabel, YLabel, dist, X)</TT> above, except that only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>X</TT>
    *     are plotted.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param dist Reference distribution
    * 
    *    @param X point set.
    * 
    *    @param numPoints number of points to plot
    * 
    * 
    */
   public QQPlot (String title, String XLabel, String YLabel,
                  ContinuousDistribution dist, double[] X, int numPoints)  {
      super();
      initPoints (dist, X, numPoints);
      initLinear (Q[1][0], Q[1][numPoints-1]);
      dataset = new XYListSeriesCollection(Q, Lin);
      // --- dashed line for y = x
      ((XYListSeriesCollection)dataset).setDashPattern(1, "dashed");
      init (title, XLabel, YLabel);
   }


   /**
    * Constructs a new <TT>QQPlot</TT> instance.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input vectors in <TT>data</TT> represents several sets of <SPAN CLASS="MATH"><I>x</I></SPAN>-points.
    *    <SPAN CLASS="MATH"><I>r</I></SPAN> determine the set of points to be plotted in the QQ-plot, that is,
    *    one will plot only the points <TT>data[r][i]</TT>,
    *    for 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;,(<I>n</I> - 1)</SPAN> and a given <SPAN CLASS="MATH"><I>r</I></SPAN>, where <SPAN CLASS="MATH"><I>n</I></SPAN> is the number
    *    of points in set <SPAN CLASS="MATH"><I>r</I></SPAN>. The points are assumed to follow the distribution
    *   <TT>dist</TT>.
    * 
    * @param title chart title.
    * 
    *    @param XLabel Label on <SPAN CLASS="MATH"><I>x</I></SPAN>-axis.
    * 
    *    @param YLabel Label on <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    * 
    *    @param dist Reference distribution
    * 
    *    @param data series of point sets.
    * 
    *    @param r set of points to plot
    * 
    */
   public QQPlot (String title, String XLabel, String YLabel,
                  ContinuousDistribution dist, double[][] data, int r)  {
      this (title, XLabel, YLabel, dist, data[r], data[r].length);
   }
}
