

/*
 * Class:        PPPlot
 * Description:  pp-plot
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
 * This class implements <SPAN  CLASS="textit">PP-plot</SPAN> (or probability-probability plot)
 * objects that compare two probability distributions.
 * The data is given as a list of <SPAN CLASS="MATH"><I>x</I></SPAN>-coordinates 
 * <SPAN CLASS="MATH">(<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>n</SUB>)</SPAN>,
 * and one is given a reference continuous probability distribution  <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
 * One first sorts the <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> in ascending order, then noted <SPAN CLASS="MATH"><I>x</I><SUB>(i)</SUB></SPAN>, and
 * plots the points 
 * <SPAN CLASS="MATH">(<I>i</I>/<I>n</I>, <I>F</I>(<I>x</I><SUB>(i)</SUB>))</SPAN>, 
 * <SPAN CLASS="MATH"><I>i</I> = 1, 2,&#8230;, <I>n</I></SPAN>, to see if
 * the data <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> comes from the reference distribution  <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
 * The graph of the straight line <SPAN CLASS="MATH"><I>y</I> = <I>x</I></SPAN> is also plotted for comparison.
 * 
 */
public class PPPlot extends XYLineChart  {
   private double[][] U;        // u_i = cdf(x_i)
   private double[][] Lin;      // line y = x

   private void initLinear ()
   {
      // line y = x in [0, 1] by steps of h
      int m = 100;
      double h = 1.0 / m;
      Lin = new double[2][m+1];
      for (int i = 0; i <= m; i++)
         Lin[0][i] = Lin[1][i] = h * i;
   }


   private void initPoints (ContinuousDistribution dist, double[] data,
                            int numPoints)
   {
      int i;
      U = new double[2][numPoints];

      for (i = 0; i < numPoints; i++)
         U[1][i] = dist.cdf(data[i]);
      Arrays.sort(U[1]);
      for (i = 0; i < numPoints; i++)
         U[0][i] = (double) (i+1)/numPoints;
   }


   /**
    * Initializes a new <TT>PPPlot</TT> instance using the points <TT>X</TT>.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The plot is a PP-plot of the points
    *    
    * <SPAN CLASS="MATH">(<I>i</I>/<I>n</I>, <I>F</I>(<I>x</I><SUB>(i)</SUB>)</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1, 2,&#8230;, <I>n</I></SPAN>, where <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB> =</SPAN><TT> X[<SPAN CLASS="MATH"><I>i</I></SPAN>-1]</TT>,
    *    <SPAN CLASS="MATH"><I>x</I><SUB>(i)</SUB></SPAN> are the  sorted points,  and <SPAN CLASS="MATH"><I>F</I>(<I>x</I>) =</SPAN><TT> dist.cdf(<SPAN CLASS="MATH"><I>x</I></SPAN>)</TT>.
    *    The points <TT>X</TT> are not  sorted.
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
   public PPPlot (String title, String XLabel, String YLabel,
                  ContinuousDistribution dist, double[] X)  {
      this (title, XLabel, YLabel, dist, X, X.length);
   }


   /**
    * Similar to the constructor
    *   {@link #PPPlot(String,String,String,ContinuousDistribution,double[]) PPPlot}
    *   <TT>(title, XLabel, YLabel, dist, X)</TT>
    *    above, except that only <SPAN  CLASS="textit">the first</SPAN> <TT>numPoints</TT> of <TT>X</TT>
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
   public PPPlot (String title, String XLabel, String YLabel,
                  ContinuousDistribution dist, double[] X, int numPoints)  {
      super();
      initPoints (dist, X, numPoints);
      initLinear ();
      dataset = new XYListSeriesCollection(U, Lin);
      // --- dashed line for y = x
      ((XYListSeriesCollection)dataset).setDashPattern(1, "dashed");
      init (title, XLabel, YLabel);
   }


   /**
    * Initializes a new <TT>PPPlot</TT> instance.
    *    <TT>title</TT> is a title, <TT>XLabel</TT> is a short description of
    *    the <SPAN CLASS="MATH"><I>x</I></SPAN>-axis, and <TT>YLabel</TT>  a short description of the <SPAN CLASS="MATH"><I>y</I></SPAN>-axis.
    *    The input vectors in <TT>data</TT> represents several sets of <SPAN CLASS="MATH"><I>x</I></SPAN>-points.
    *    <SPAN CLASS="MATH"><I>r</I></SPAN> determine the set of points to be plotted in the PP-plot, that is,
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
   public PPPlot (String title, String XLabel, String YLabel,
                  ContinuousDistribution dist, double[][] data, int r)  {
      this (title, XLabel, YLabel, dist, data[r], data[r].length);
   }
}
