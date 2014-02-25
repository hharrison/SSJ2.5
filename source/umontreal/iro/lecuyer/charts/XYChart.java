

/*
 * Class:        XYChart
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
import java.io.*;
import org.jfree.chart.JFreeChart;
import javax.swing.JFrame;

/**
 * This class provides tools to create charts from data in a simple way. Its main
 * feature is to produce
 *  TikZ/PGF (see WWW link <TT><A NAME="tex2html1"
 *   HREF="http://sourceforge.net/projects/pgf/">http://sourceforge.net/projects/pgf/</A></TT>)
 *  compatible source code which can be included
 * in <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> documents, but it can also produce charts in other formats.
 * One can easily create a new chart, and customize its appearance using methods
 * of this class, with the encapsulated
 * {@link umontreal.iro.lecuyer.charts.SSJXYSeriesCollection SSJXYSeriesCollection} object
 * representing the data, and the two
 * {@link umontreal.iro.lecuyer.charts.Axis Axis} objects representing the axes.
 * All these classes depend on the <TT>JFreeChart</TT> API (see WWW link
 * <TT><A NAME="tex2html2"
 *   HREF="http://www.jfree.org/jfreechart/">http://www.jfree.org/jfreechart/</A></TT>) which provides tools to build charts with
 * Java, to draw them, and export them to files. However, only basic features are
 * used here.
 * 
 * <P>
 * Moreover, <TT>XYChart</TT> provides methods to plot data using a MATLAB friendly
 * syntax. None of these methods provides new features; they just propose a
 * different syntax to create charts. Therefore some features are unavailable
 * when using these methods only.
 * 
 */
public abstract class XYChart  {
   protected Axis XAxis;
   protected Axis YAxis;

   protected SSJXYSeriesCollection dataset;
   protected JFreeChart chart;
   protected boolean latexDocFlag = true;

   protected boolean autoRange;
   protected double[] manualRange;

   protected boolean grid = false;
   protected double xstepGrid;
   protected double ystepGrid;

   // this flag is set true when plotting probabilities. In that case,
   // y is always >= 0.
   protected boolean probFlag = false;

   protected double chartMargin = 0.02;   // margin around the chart



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
    * Returns the chart's domain axis (<SPAN CLASS="MATH"><I>x</I></SPAN>-axis) object.
    * 
    * @return chart's domain axis (<SPAN CLASS="MATH"><I>x</I></SPAN>-axis) object.
    * 
    */
   public Axis getXAxis()  {
      return XAxis;
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
    * Must be set <TT>true</TT> when plotting probabilities,
    *   <TT>false</TT> otherwise.
    * 
    * @param flag <TT>true</TT> for plotting probabilities
    * 
    * 
    */
   public void setprobFlag (boolean flag)  {
      probFlag = flag;
   }


   /**
    * The <SPAN CLASS="MATH"><I>x</I></SPAN> and the <SPAN CLASS="MATH"><I>y</I></SPAN> ranges of the chart are set automatically.
    * 
    */
   public void setAutoRange()  {
      setAutoRange (false, false, true, true);
   }


   /**
    * The <SPAN CLASS="MATH"><I>x</I></SPAN> and the <SPAN CLASS="MATH"><I>y</I></SPAN> ranges of the chart are set automatically.
    *  If <TT>right</TT> is <TT>true</TT>, the vertical axis will be on the left of
    *  the points, otherwise on the right. If <TT>top</TT> is <TT>true</TT>,
    *  the horizontal axis  will be under the points, otherwise above the points.
    * 
    * @param right true if the x-values on the right of axis.
    * 
    *    @param top true if the y-values on the top of axis.
    * 
    * 
    */
   public void setAutoRange (boolean right, boolean top)  {
      setAutoRange (false, false, right, top);
   }

   private double[] adjustRangeBounds (double bmin, double bmax) {
      // resets chart lower and upper bounds to round values.
      // Returns corrected [lowerBound, upperBound]

      double del = (bmax - bmin)/20.0;    // Choose 20 intervals to round
      int a = (int) Math.floor(0.5 + Math.log10(del));
      double d = Math.pow(10.0, (double) a);     // power of 10
      double lower = d*Math.ceil((bmin - del)/d);
      if (lower > bmin)
         lower -= d;
      if (0 == Math.abs(bmin))
         lower = 0;
      double upper = d*Math.floor((bmax + del)/d);
      if (upper < bmax)
         upper += d;
      double [] range = new double[2];
      range[0] = lower;
      range[1] = upper;
      return range;
   }

   protected void setAutoRange (boolean xZero, boolean yZero, boolean right, boolean top) {
      // see description of setAxesZero
      autoRange = true;
      double BorneMin = (dataset.getDomainBounds())[0];
      double BorneMax = (dataset.getDomainBounds())[1];
      double del;
      if (BorneMax - BorneMin < 1)
         del = (BorneMax - BorneMin) * chartMargin;
      else
         del = chartMargin;
      if (BorneMin < 0.0) BorneMin *= 1.0 + del;
      else BorneMin *= 1.0 - del;
      if (BorneMax < 0.0) BorneMax *= 1.0 - del;
      else BorneMax *= 1.0 + del;
      double [] newRange = new double[2];
      newRange = adjustRangeBounds (BorneMin, BorneMax);
      if (probFlag && (BorneMin == 0.0))
         newRange[0] = 0.0;
      XAxis.getAxis().setLowerBound(newRange[0]);
      XAxis.getAxis().setUpperBound(newRange[1]);

      BorneMin = (dataset.getRangeBounds())[0];
      BorneMax = (dataset.getRangeBounds())[1];
      if (BorneMax - BorneMin < 1)
         del = (BorneMax - BorneMin) * chartMargin;
      else
         del = chartMargin;
      if (BorneMin < 0.0) BorneMin *= 1.0 + del;
      else BorneMin *= 1.0 - del;
      if (BorneMax < 0.0) BorneMax *= 1.0 - del;
      else BorneMax *= 1.0 + del;
      newRange = adjustRangeBounds (BorneMin, BorneMax);
      if (probFlag && (newRange[0] <= 0.0))   // probabilities are always >= 0
         newRange[0] = 0.0;
      YAxis.getAxis().setLowerBound(newRange[0]);
      YAxis.getAxis().setUpperBound(newRange[1]);

      if (xZero)
         XAxis.setTwinAxisPosition(0);
      else {
         if (right)
            XAxis.setTwinAxisPosition(XAxis.getAxis().getLowerBound());
         else
            XAxis.setTwinAxisPosition(XAxis.getAxis().getUpperBound());
      }

      if (yZero)
         YAxis.setTwinAxisPosition(0);
      else {
         if (top)
            YAxis.setTwinAxisPosition(YAxis.getAxis().getLowerBound());
         else
            YAxis.setTwinAxisPosition(YAxis.getAxis().getUpperBound());
      }
   }

   /**
    * The <SPAN CLASS="MATH"><I>x</I></SPAN> and the <SPAN CLASS="MATH"><I>y</I></SPAN> ranges of the chart are set automatically.
    * If <TT>xZero</TT> is <TT>true</TT>, the vertical axis will pass through the
    * point <SPAN CLASS="MATH">(0, <I>y</I>)</SPAN>. If <TT>yZero</TT> is <TT>true</TT>, the horizontal axis
    * will pass through the point <SPAN CLASS="MATH">(<I>x</I>, 0)</SPAN>.
    * 
    * @param xZero true if vertical axis passes through point 0
    * 
    *   @param yZero true if horizontal axis passes through point 0
    * 
    * 
    */
   public void setAutoRange00 (boolean xZero, boolean yZero)  {
      setAutoRange (xZero, yZero, true, true);
   }


   /**
    * Sets the <SPAN CLASS="MATH"><I>x</I></SPAN> and <SPAN CLASS="MATH"><I>y</I></SPAN> ranges of the chart  using the format: <TT>range =
    *   [xmin, xmax, ymin, ymax]</TT>. 
    * @param range new axis ranges.
    * 
    * 
    */
   public void setManualRange (double[] range)  {
      setManualRange (range, false, false, true, true);
   }


   /**
    * Sets the <SPAN CLASS="MATH"><I>x</I></SPAN> and <SPAN CLASS="MATH"><I>y</I></SPAN> ranges of the chart  using the format: <TT>range =
    *   [xmin, xmax, ymin, ymax]</TT>.
    *   If <TT>right</TT> is <TT>true</TT>, the vertical axis will be on the left of
    *  the points, otherwise on the right. If <TT>top</TT> is <TT>true</TT>,
    *  the horizontal axis  will be under the points, otherwise above the points.
    * 
    * @param range new axis ranges.
    * 
    *    @param right true if the x-values on the right.
    * 
    *    @param top true if the y-values on the top.
    * 
    * 
    */
   public void setManualRange (double[] range, boolean right, boolean top)  {
      setManualRange (range, false, false, right, top);
   }


   private void setManualRange (double[] range, boolean xZero, boolean yZero,
                                boolean right, boolean top) {
      if (range.length != 4)
         throw new IllegalArgumentException (
             "range must have the format: [xmin, xmax, ymin, ymax]");
      autoRange = false;
      XAxis.getAxis().setLowerBound(Math.min(range[0],range[1]));
      XAxis.getAxis().setUpperBound(Math.max(range[0],range[1]));
      YAxis.getAxis().setLowerBound(Math.min(range[2],range[3]));
      YAxis.getAxis().setUpperBound(Math.max(range[2],range[3]));

      if (xZero)
         XAxis.setTwinAxisPosition(0);
      else {
         if (right)
            XAxis.setTwinAxisPosition(XAxis.getAxis().getLowerBound());
         else
            XAxis.setTwinAxisPosition(XAxis.getAxis().getUpperBound());
      }

      if (yZero)
         YAxis.setTwinAxisPosition(0);
      else {
         if (top)
            YAxis.setTwinAxisPosition(YAxis.getAxis().getLowerBound());
         else
            YAxis.setTwinAxisPosition(YAxis.getAxis().getUpperBound());
      }
   }

   /**
    * Sets the <SPAN CLASS="MATH"><I>x</I></SPAN> and <SPAN CLASS="MATH"><I>y</I></SPAN> ranges of the chart using the format: <TT>range =
    *   [xmin, xmax, ymin, ymax]</TT>.
    *  If <TT>xZero</TT> is <TT>true</TT>, the vertical axis will pass through the
    * point <SPAN CLASS="MATH">(0, <I>y</I>)</SPAN>. If <TT>yZero</TT> is <TT>true</TT>, the horizontal axis
    * will pass through the point <SPAN CLASS="MATH">(<I>x</I>, 0)</SPAN>.
    * 
    * @param xZero true if vertical axis passes through point 0
    * 
    *   @param yZero true if horizontal axis passes through point 0
    * 
    * 
    */
   public void setManualRange00 (double[] range, boolean xZero, boolean yZero)  {
      setManualRange (range, xZero, yZero, true, true);
   }


   /**
    * Returns the chart margin, which is the fraction by which the chart
    *     is enlarged on its borders. The default value is <SPAN CLASS="MATH">0.02</SPAN>.
    * 
    */
   public double getChartMargin()  {
      return chartMargin;
   }


   /**
    * Sets the chart margin to <TT>margin</TT>. It is the fraction by
    *     which the chart is enlarged on its borders.
    *    Restriction: 
    * <SPAN CLASS="MATH"><texttt>margin</texttt>&nbsp;&gt;=&nbsp; 0</SPAN>.
    * 
    * @param margin margin percentage amount.
    * 
    * 
    */
   public void setChartMargin (double margin)  {
      if (margin < 0.0)
         throw new IllegalArgumentException ("margin < 0");
      chartMargin = margin;
   }


   /**
    * Synchronizes <SPAN CLASS="MATH"><I>x</I></SPAN>-axis ticks to the <SPAN CLASS="MATH"><I>s</I></SPAN>-th series <SPAN CLASS="MATH"><I>x</I></SPAN>-values.
    * 
    * @param s series.
    * 
    */
   public abstract void setTicksSynchro (int s);


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
      this.xstepGrid = xstep;
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
    * Exports the chart to a <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> source code using PGF/TikZ.
    *    This method constructs and returns a string that can be written to
    *    a <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> document to render the plot. <TT>width</TT> and <TT>height</TT>
    *    represents the width and the height of the produced chart. These dimensions
    *    do not take into account the axes and labels extra space. The <TT>width</TT>
    *    and the <TT>height</TT> of the chart are measured in centimeters.
    * 
    * @param width Chart's width in centimeters.
    * 
    *    @param height Chart's height in centimeters.
    * 
    *    @return LaTeX source code.
    * 
    */
   public abstract String toLatex (double width, double height);


   /**
    * Transforms the chart to <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> form and writes it in file <TT>fileName</TT>.
    *    The chart's width and height (in centimeters) are <TT>width</TT> and <TT>height</TT>.
    * 
    */
   public void toLatexFile (String fileName, double width, double height)  {
      String output = toLatex(width, height);
      Writer file = null;
      try {
         file = new FileWriter(fileName);
         file.write(output);
         file.close();
     } catch (IOException e) {
         System.err.println ("   toLatexFile:  cannot write to  " + fileName);
         e.printStackTrace();
         try {
            if (file != null)
               file.close();
         } catch (IOException ioe) {}
      }
  }


   /**
    * Flag to remove the <code>\documentclass</code> (and other) commands in the
    * created <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> files.
    * If <TT>flag</TT> is <TT>true</TT>,  then when charts are translated into
    * <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> form, it will be as a self-contained file that can be directly
    * compiled with <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN>. However, in this form, the file cannot be included in
    * another <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> file without causing compilation errors because of the multiple
    * instructions <code>\documentclass</code> and <code>\begin{document}</code>.
    * By setting <TT>flag</TT> to <TT>false</TT>, these instructions will be
    * removed from the <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> chart files, which can then be included in a master
    * <SPAN CLASS="logo,LaTeX">L<SUP><SMALL>A</SMALL></SUP>T<SMALL>E</SMALL>X</SPAN> file. By default, the flag is <TT>true</TT>.
    * 
    */
   public void setLatexDocFlag (boolean flag)  {
      latexDocFlag = flag;
   }



   protected void setTick0Flags() {
      // Set flag true if first or last label is on perpendicular axis.
      // The label will be moved a little to the right (x-label), or above
      // (y-label) to prevent it from being on the perpendicular axis.
      // But it is unnecessary when graph begins or ends where label is;
      // in this case, flag is false.
      // We cannot put this method in Axis because it depends on the
      // other axis.
     double minAxis = Math.min (XAxis.getAxis().getRange().getLowerBound(),
                                                   XAxis.getTwinAxisPosition());
     double maxAxis = Math.max (XAxis.getAxis().getRange().getUpperBound(),
                                                     XAxis.getTwinAxisPosition());
     if (XAxis.getTwinAxisPosition() == minAxis ||
         XAxis.getTwinAxisPosition() == maxAxis)
        YAxis.setTick0Flag(false);
     else
        YAxis.setTick0Flag(true);

     minAxis = Math.min (YAxis.getAxis().getRange().getLowerBound(),
                                                   YAxis.getTwinAxisPosition());
     maxAxis = Math.max (YAxis.getAxis().getRange().getUpperBound(),
                                                     YAxis.getTwinAxisPosition());
     if (YAxis.getTwinAxisPosition() == minAxis ||
         YAxis.getTwinAxisPosition() == maxAxis)
        XAxis.setTick0Flag(false);
     else
        XAxis.setTick0Flag(true);
   }


   protected double computeXScale (double position) {
      double[] bounds = new double[2];
      bounds[0] = XAxis.getAxis().getLowerBound();
      bounds[1] = XAxis.getAxis().getUpperBound();

      if (position < bounds[0])
         bounds[0] = position;
      if (position > bounds[1])
         bounds[1] = position;
      bounds[0] -= position;
      bounds[1] -= position;
      return computeScale (bounds);
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
