

/*
 * Class:        CategoryChart
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

import org.jfree.chart.JFreeChart;
import javax.swing.JFrame;

/**
 * This class provides tools to create charts from data in a simple way. Its main
 * feature is to produce TikZ/PGF (see WWW link <TT><A NAME="tex2html1"
 *   HREF="http://sourceforge.net/projects/pgf/">http://sourceforge.net/projects/pgf/</A></TT>) compatible source code which can be included
 * in <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> documents, but it can also produce charts in other formats.
 * One can easily create a new chart, and customize its appearance using methods
 * of this class, with the encapsulated
 * {@link umontreal.iro.lecuyer.charts.SSJCategorySeriesCollection SSJCategorySeriesCollection} object
 * representing the data, and an {@link umontreal.iro.lecuyer.charts.Axis Axis}
 * object representing the axis.
 * All these classes depend on the <TT>JFreeChart</TT> API (see WWW link
 * <TT><A NAME="tex2html2"
 *   HREF="http://www.jfree.org/jfreechart/">http://www.jfree.org/jfreechart/</A></TT>) which provides tools to build charts with
 * Java, to draw them, and export them to files. However, only basic features are
 * used here.
 * 
 * <P>
 * Moreover, <TT>CategoryChart</TT> provides methods to plot data using a MATLAB friendly
 * syntax. None of these methods provides new features; they just propose a
 * different syntax to create charts. Therefore some features are unavailable
 * when using these methods only.
 * 
 */
public abstract class CategoryChart  {

   protected Axis YAxis;
   protected SSJCategorySeriesCollection dataset;
   protected JFreeChart chart;
   protected boolean latexDocFlag = true;

   protected boolean autoRange;
   protected double[] manualRange;

   protected boolean grid = false;
   protected double ystepGrid;

   final protected double BOR = 0.1;


   /**
    * Returns the <TT>JFreeChart</TT> object associated with this chart.
    * 
    * @return the associated JFreeChart object.
    * 
    */
   public JFreeChart getJFreeChart()  {
      return chart;
   }


   /**
    * Returns the chart's range axis (<SPAN CLASS="MATH"><I>y</I></SPAN>-axis) object.
    * 
    * @return chart's range axis (<SPAN CLASS="MATH"><I>y</I></SPAN>-axis) object.
    * 
    */
   public Axis getYAxis()  {
      return YAxis;
   }


   public abstract JFrame view (int width, int height);


   /**
    * Gets the current chart title.
    * 
    * @return Chart title.
    * 
    */
   public String getTitle()  {
      return chart.getTitle().getText();
   }


   /**
    * Sets a title to this chart. This title will appear on the chart displayed
    *  by method {@link #view view}.
    * 
    * @param title chart title.
    * 
    * 
    */
   public void setTitle (String title)  {
      chart.setTitle(title);
   }


   /**
    * Sets chart <SPAN CLASS="MATH"><I>y</I></SPAN> range to automatic values.
    * 
    */
   public void setAutoRange ()  {
      autoRange = true;

      double BorneMin = Math.abs((dataset.getRangeBounds())[0]);
      double BorneMax = Math.abs((dataset.getRangeBounds())[1]);

      double max = Math.max(BorneMin,BorneMax) * BOR;
      YAxis.getAxis().setLowerBound(BorneMin - max);
      YAxis.getAxis().setUpperBound(BorneMax + max);
      YAxis.setLabelsAuto();
   }


   /**
    * Sets new <SPAN CLASS="MATH"><I>y</I></SPAN>-axis bounds, using the format:
    * <TT>range</TT> = [<TT>ymin, ymax</TT>].
    * 
    * @param range new axis ranges.
    * 
    * 
    */
   private void setManualRange (double[] range)   {
      if(range.length != 2)
         throw new IllegalArgumentException (
             "range must have the format: [ymin, ymax]");
      autoRange = false;
      YAxis.getAxis().setLowerBound(Math.min(range[0],range[1]));
      YAxis.getAxis().setUpperBound(Math.max(range[0],range[1]));
   }


   /**
    * Puts a grid on the background. It is important to note that the grid is
    *    always shifted in such a way that it contains the axes. Thus, the grid does
    *    not always have an intersection at the corner points; this occurs
    *    only if the corner points are multiples of the steps: <TT>xstep</TT>
    *    and <TT>ystep</TT> sets the step in each direction.
    * 
    * @param xstep sets the step in the x-direction.
    * 
    *    @param ystep sets the step in the y-direction.
    * 
    * 
    */
   public void enableGrid (double xstep, double ystep)  {
      this.grid = true;
      this.ystepGrid = ystep;
   }


   /**
    * Disables the background grid.
    * 
    */
   public void disableGrid()  {
      this.grid = false;
   }


   /**
    * Transforms the chart into <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> form and returns it as a <TT>String</TT>.
    * 
    */
   public abstract String toLatex (double width, double height);


   /**
    * Same as in {@link XYChart}.
    * 
    */
   public void setLatexDocFlag (boolean flag)  {
      latexDocFlag = flag;
   }



   protected double computeYScale (double position) {
      double[] bounds = new double[2];
      bounds[0] = YAxis.getAxis().getLowerBound();
      bounds[1] = YAxis.getAxis().getUpperBound();

      if (position < bounds[0])
         bounds[0] = position;
      if (position > bounds[1])
         bounds[1] = position;
      bounds[0] -= position;
      bounds[1] -= position;
      return computeScale (bounds);
   }

   protected double computeScale (double[] bounds) {
      int tenPowerRatio = 0;
      // echelle < 1 si les valeurs sont grandes
      while (bounds[1] > 1000 || bounds[0] < -1000) {
         bounds[1] /= 10;
         bounds[0] /= 10;
         tenPowerRatio++;
      }
      // echelle > 1 si les valeurs sont petites
      while (bounds[1] < 100 && bounds[0] > -100) {
         bounds[1] *= 10;
         bounds[0] *= 10;
         tenPowerRatio--;
      }
      return 1/Math.pow(10, tenPowerRatio);
   }
}
