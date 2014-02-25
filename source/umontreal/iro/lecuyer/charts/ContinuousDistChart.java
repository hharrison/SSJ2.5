

/*
 * Class:        ContinuousDistChart
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        May 2008

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

import org.jfree.chart.JFreeChart;
import javax.swing.JFrame;



/**
 * This class provides tools to plot the density and the cumulative probability
 *  of a continuous probability distribution.
 * 
 */
public class ContinuousDistChart  {
   protected ContinuousDistribution dist;
   protected double a,b;
   protected int m;
   protected XYLineChart cdfChart;
   protected XYLineChart densityChart;

   private void init() {
      double[][] cdf = new double[2][m+1];
      double[][] density = new double[2][m+1];
      double h = (b - a) / m;
      double x;
      int coex = 0;

      try {
         for (int i = 0; i <= m; i++) {
            x = a + i*h;
            cdf[0][i] = x;
            cdf[1][i] = dist.cdf (x);
         }
         cdfChart = new XYLineChart("cdf: " + dist.toString(), "", "", cdf);
      } catch (UnsupportedOperationException e) {
         coex++;
         System.err.println (e);
//         e.printStackTrace();
      }

      try {
         for (int i = 0; i <= m; i++) {
            x = a + i*h;
            density[0][i] = x;
            density[1][i] = dist.density (x);
         }
         densityChart = new XYLineChart("density: " + dist.toString(),
                                        "", "", density);
      } catch (UnsupportedOperationException e) {
         System.err.println (e);
         if (coex == 1)
            throw e;
      }
      cdfChart.setprobFlag (true);
      densityChart.setprobFlag (true);
   }


   /**
    * Constructor for a new <TT>ContinuousDistChart</TT> instance. It will plot the
    * continuous distribution <TT>dist</TT> over the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>,
    *   using <SPAN CLASS="MATH"><I>m</I> + 1</SPAN> equidistant sample points.
    * 
    * @param dist continuous distribution to plot
    * 
    *    @param a lower bound of interval
    * 
    *    @param b upper bound of interval
    * 
    *    @param m number of steps
    * 
    */
   public ContinuousDistChart (ContinuousDistribution dist, double a,
                               double b, int m)  {
      this.dist = dist;
      this.a = a;
      this.b = b;
      this.m = m;
      init();
   }


   /**
    * Displays a chart of the cumulative distribution function (cdf) on the screen
    *    using Swing. This method creates an application containing a chart panel
    *    displaying the chart. The created frame is positioned on-screen, and
    *    displayed before it is returned. The <TT>width</TT> and the <TT>height</TT>
    *    of the chart are measured in pixels.
    * 
    * @param width frame width in pixels
    * 
    *    @param height frame height in pixels
    * 
    *    @return frame containing the chart
    * 
    */
   public JFrame viewCdf (int width, int height)  {
      return cdfChart.view(width, height);
   }


   /**
    * Similar to {@link #viewCdf viewCdf}, but for the probability density instead
    *    of the cdf.
    * 
    * @param width frame width in pixels
    * 
    *    @param height frame height in pixels
    * 
    *    @return frame containing the chart
    * 
    */
   public JFrame viewDensity (int width, int height)  {
      return densityChart.view(width, height);
   }


   /**
    * Exports a chart of the cdf to a <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> source code using PGF/TikZ.
    *    This method constructs and returns a string that can be written to
    *    a <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> document to render the plot. <TT>width</TT> and <TT>height</TT>
    *    represents the width and the height of the produced chart. These dimensions
    *    do not take into account the axes and labels extra space. The <TT>width</TT>
    *    and the <TT>height</TT> of the chart are measured in centimeters.
    * 
    * @param width Chart's width in centimeters
    * 
    *    @param height Chart's height in centimeters
    * 
    *    @return LaTeX source code
    * 
    */
   public String toLatexCdf (int width, int height)  {
      return cdfChart.toLatex(width, height);
   }


   /**
    * Similar to {@link #toLatexCdf toLatexCdf}, but for the probability density instead
    *    of the cdf.
    * 
    * @param width Chart's width in centimeters
    * 
    *    @param height Chart's height in centimeters
    * 
    *    @return LaTeX source code
    * 
    */
   public String toLatexDensity (int width, int height)  {
      return densityChart.toLatex(width, height);
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> for this object.
    * 
    * @param a lower bound of interval
    * 
    *    @param b upper bound of interval
    * 
    *    @param m number of points in the plot minus one
    * 
    * 
    */
   public void setParam (double a, double b, int m)  {
      this.a = a;
      this.b = b;
      this.m = m;
      init();
   }


}
