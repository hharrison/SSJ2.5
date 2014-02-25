

/*
 * Class:        Tally
 * Description:  statistical collector 
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

package umontreal.iro.lecuyer.stat;

import umontreal.iro.lecuyer.util.PrintfFormat;
import umontreal.iro.lecuyer.probdist.StudentDist;
import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.probdist.ChiSquareDist;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This type of statistical collector takes a sequence of real-valued
 * observations and can return the average,
 * the variance, a confidence interval for the theoretical mean, etc.
 * Each call to {@link #add add} provides a new observation.
 * When the broadcasting to observers is activated,
 * the method {@link #add add} will also pass this new information to its
 * registered observers.
 * This type of collector does not memorize the individual observations,
 * but only their number, sum, sum of squares, maximum, and minimum.
 * The subclass {@link TallyStore} offers a collector that memorizes
 * the observations.
 * 
 */
public class Tally extends StatProbe implements Cloneable {
   private int numObs;
   private double sumSquares;
   private final boolean isStable = true;  // = true: use a numerically stable
                                   // form for the average and the variance.
                                   // = false: use the old unstable forms.
   private double curAverage;  // The average of the first numObs observations
   private double curSum2;     // The sum (xi - average)^2 of the first numObs
                               // observations.
   private Logger log = Logger.getLogger ("umontreal.iro.lecuyer.stat");

   private static enum CIType {CI_NONE, CI_NORMAL, CI_STUDENT};

   protected CIType confidenceInterval = CIType.CI_NONE;
   protected double level = 0.95;
   protected int digits = 3;



   /**
    * Constructs a new unnamed <TT>Tally</TT> statistical probe.
    * 
    */
   public Tally()  {
      super();
      init();
   } 


   /**
    * Constructs a new <TT>Tally</TT> statistical probe with
    *    name <TT>name</TT>.
    *  
    * @param name name of the tally
    * 
    */
   public Tally (String name)  {
      super();
      this.name = name;
      init();
   } 


   public void init() {
       maxValue = Double.NEGATIVE_INFINITY;
       minValue = Double.POSITIVE_INFINITY;
       sumValue = 0.0;
       sumSquares = 0.0;
       curAverage = 0.0;
       curSum2 = 0.0;
       numObs = 0;
   }

   /**
    * Gives a new observation <TT>x</TT> to the statistical collector.
    *    If broadcasting to observers is activated for this object,
    *    this method also transmits the new information to the
    *    registered observers by invoking the method
    *    {@link #notifyListeners notifyListeners}.
    *  
    * @param x observation being added to this tally
    * 
    * 
    */
   public void add (double x)  {
      if (collect) {
         if (x < minValue) minValue = x;
         if (x > maxValue) maxValue = x;
         numObs++;
         sumValue += x;
         sumSquares += x * x;
         curSum2 += (numObs - 1)*(x - curAverage)*(x - curAverage) / numObs;
         curAverage += (x - curAverage)/numObs;
      }
      notifyListeners (x);
   }


   /**
    * Returns the number of observations given to this probe
    *    since its last initialization.
    *   
    * @return the number of collected observations
    * 
    */
   public int numberObs()  {
      return numObs;
   } 


   /**
    * Returns the average value of the observations since the last
    *    initialization.
    * 
    */
   public double average()  {
      if (numObs < 1) {
         //System.err.println (
         //    "******* Tally " + name + ":   calling average() with " + numObs +
         //    " Observation");
         log.logp (Level.WARNING, "Tally", "average",
            "Tally " + name + ":   calling average() with " + numObs +
             " observation");
         return Double.NaN;
      }
      if (!isStable)
         return sumValue / (double) numObs;
      else
         return curAverage;
   }


   /**
    * Returns the sample variance of the observations since the last
    *    initialization.
    *    This returns <TT>Double.NaN</TT>
    *    if the tally contains less than two observations.
    *  
    * @return the variance of the observations
    * 
    */
   public double variance()  {
      // throws NumberObservationException {
      // if (numObs < 2) throw NumberObservationException;
      if (numObs < 2) {
         //System.err.println (
         //    "******* Tally " + name + ":   calling variance() with " + numObs +
         //    " Observation");
         log.logp (Level.WARNING, "Tally", "variance",
            "Tally " + name + ":   calling variance() with " + numObs +
             " observation");
         return Double.NaN;
      }
      if (!isStable) {
         double temp = sumSquares - (sumValue * sumValue)/(double)numObs;
         temp = temp / (numObs-1);
         if (temp < 0.0) return 0.0;
         return temp;
      } else
         return curSum2 / (numObs-1);
   }


   /**
    * Returns the sample standard deviation of the observations
    *    since the last initialization.
    *    This returns <TT>Double.NaN</TT>
    *    if the tally contains less than two observations.
    *  
    * @return the standard deviation of the observations
    * 
    */
   public double standardDeviation()  {
      return Math.sqrt (variance());
   }


   /**
    * Computes a confidence interval on the mean.
    *   Returns, in elements 0 and 1 of the array
    *   object <TT>centerAndRadius[]</TT>, the center and half-length (radius)
    *   of a confidence interval on the true mean of the random variable <SPAN CLASS="MATH"><I>X</I></SPAN>,
    *   with confidence level <TT>level</TT>, assuming that the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *   given to this collector are independent and identically distributed
    *   (i.i.d.) copies of <SPAN CLASS="MATH"><I>X</I></SPAN>, and that <SPAN CLASS="MATH"><I>n</I></SPAN> is large enough for the central limit
    *   theorem to hold.  This confidence interval is computed based on the statistic
    *   
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>Z</I> = bar(X)<SUB>n</SUB>-<I>&#956;</I>/(<I>S</I><SUB>n, x</SUB>/(n)<SUP>1/2</SUP>)
    * </DIV><P></P>
    * where <SPAN CLASS="MATH"><I>n</I></SPAN> is the number of observations given to this collector since its
    *   last initialization, 
    * <SPAN CLASS="MATH">bar(X)<SUB>n</SUB> =</SPAN> <TT>average()</TT> is the average of these
    *   observations, <SPAN CLASS="MATH"><I>S</I><SUB>n, x</SUB> =</SPAN> <TT>standardDeviation()</TT> is the empirical
    *   standard  deviation.  Under the assumption that the observations
    *   of <SPAN CLASS="MATH"><I>X</I></SPAN> are  i.i.d. and <SPAN CLASS="MATH"><I>n</I></SPAN> is large,
    *   <SPAN CLASS="MATH"><I>Z</I></SPAN> has the standard normal distribution.
    *   The confidence interval given by this method is valid <SPAN  CLASS="textit">only if</SPAN>
    *   this assumption is approximately verified.
    *  
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @param centerAndRadius array of size 2 in which are returned the center
    *         and radius of the confidence interval, respectively
    * 
    * 
    */
   public void confidenceIntervalNormal (double level,
                                         double[] centerAndRadius)  {
      // Must return an array object, cannot return 2 doubles directly
      double z;
      if (numObs < 2) throw new RuntimeException (
          "Tally " + name +
          ": Calling confidenceIntervalStudent with < 2 Observations");
      centerAndRadius[0] =  average();
      z = NormalDist.inverseF01 (0.5 * (level + 1.0));
      centerAndRadius[1] = z * Math.sqrt (variance() / (double)numObs);
   }


   /**
    * Computes a confidence interval on the mean.
    *   Returns, in elements 0 and 1 of the array
    *   object <TT>centerAndRadius[]</TT>, the center and half-length (radius)
    *   of a confidence interval on the true mean of the random variable <SPAN CLASS="MATH"><I>X</I></SPAN>,
    *   with confidence level <TT>level</TT>, assuming that the observations
    *   given to this collector are independent and identically distributed
    *   (i.i.d.) copies of <SPAN CLASS="MATH"><I>X</I></SPAN>, and that <SPAN CLASS="MATH"><I>X</I></SPAN> has the normal distribution.
    *   This confidence interval is computed based on the statistic
    *   
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>T</I> = bar(X)<SUB>n</SUB>-<I>&#956;</I>/(<I>S</I><SUB>n, x</SUB>/(n)<SUP>1/2</SUP>)
    * </DIV><P></P>
    * where <SPAN CLASS="MATH"><I>n</I></SPAN> is the number of observations given to this collector since its
    *   last initialization, 
    * <SPAN CLASS="MATH">bar(X)<SUB>n</SUB> =</SPAN> <TT>average()</TT> is the average of these
    *   observations, <SPAN CLASS="MATH"><I>S</I><SUB>n, x</SUB> =</SPAN> <TT>standardDeviation()</TT> is the empirical
    *   standard  deviation.  Under the assumption that the observations
    *   of <SPAN CLASS="MATH"><I>X</I></SPAN> are  i.i.d. and normally distributed,
    *   <SPAN CLASS="MATH"><I>T</I></SPAN> has the Student distribution with <SPAN CLASS="MATH"><I>n</I> - 1</SPAN> degrees of freedom.
    *   The confidence interval given by this method is valid <SPAN  CLASS="textit">only if</SPAN>
    *   this assumption is approximately verified, or if <SPAN CLASS="MATH"><I>n</I></SPAN> is large enough
    *   so that <SPAN CLASS="MATH">bar(X)<SUB>n</SUB></SPAN> is approximately normally distributed.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @param centerAndRadius array of size 2 in which are returned the center
    *         and radius of the confidence interval, respectively
    * 
    * 
    */
   public void confidenceIntervalStudent (double level,
                                          double[] centerAndRadius)  {
      // Must return an array object, cannot return 2 doubles directly
      double t;
      if (numObs < 2) throw new RuntimeException (
          "Tally " + name +
          ": Calling confidenceIntervalStudent with < 2 Observations");
      centerAndRadius[0] =  average();
      t = StudentDist.inverseF (numObs - 1, 0.5 * (level + 1.0));
      centerAndRadius[1] = t * Math.sqrt (variance() / (double)numObs);
   }


   /**
    * Similar to {@link #confidenceIntervalNormal confidenceIntervalNormal}.
    *    Returns the confidence interval in a formatted string of the form 
    * <BR>
    * <DIV CLASS="centerline" ID="par423" ALIGN="CENTER">
    * ``<TT>95% confidence interval for mean (normal): (32.431, 32.487)</TT>'',</DIV>
    * using <SPAN CLASS="MATH"><I>d</I></SPAN> fractional decimal digits.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @param d number of fractional decimal digits
    * 
    *    @return a confidence interval formatted as a string
    * 
    */
   public String formatCINormal (double level, int d)  {
      PrintfFormat str = new PrintfFormat();
      double ci[] = new double[2];
      confidenceIntervalNormal (level, ci);
      str.append ("  " + (100*level) + "%");
      str.append (" confidence interval for mean (normal): (");
      str.append (7 + d, d-1, d, ci[0] - ci[1]).append (',');
      str.append (7 + d, d-1, d, ci[0] + ci[1]).append (" )" + PrintfFormat.NEWLINE);
      return str.toString();
}


   /**
    * Equivalent to <TT>formatCINormal (level, 3)</TT>.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @return a confidence interval formatted as a string
    * 
    */
   public String formatCINormal (double level)  {
      return formatCINormal (level, 3);
   }


   /**
    * Similar to {@link #confidenceIntervalStudent confidenceIntervalStudent}.
    *    Returns the confidence interval in a formatted string of the form
    * <BR>  <DIV CLASS="centerline" ID="par425" ALIGN="CENTER">
    * ``<TT>95% confidence interval for mean (student): (32.431, 32.487)</TT>'',</DIV>
    * using <SPAN CLASS="MATH"><I>d</I></SPAN> fractional decimal digits.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @param d number of fractional decimal digits
    * 
    *    @return a confidence interval formatted as a string
    * 
    */
   public String formatCIStudent (double level, int d)  {
      PrintfFormat str = new PrintfFormat();
      double ci[] = new double[2];
      confidenceIntervalStudent (level, ci);
      str.append ("  " + (100*level) + "%");
      str.append (" confidence interval for mean (student): (");
      str.append (7 + d, d, d-1, ci[0] - ci[1]).append (',');
      str.append (7 + d, d, d-1, ci[0] + ci[1]).append (" )" + PrintfFormat.NEWLINE);
      return str.toString();
   }


   /**
    * Equivalent to <TT>formatCIStudent (level, 3)</TT>.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @return a confidence interval formatted as a string
    * 
    */
   public String formatCIStudent (double level)  {
      return formatCIStudent (level, 3);
   }


   /**
    * Computes a confidence interval on the variance.
    *   Returns, in elements 0 and 1 of array <TT>interval</TT>, the
    *   left and right boundaries <SPAN CLASS="MATH">[<I>I</I><SUB>1</SUB>, <I>I</I><SUB>2</SUB>]</SPAN> of a confidence interval on the true
    *  variance <SPAN CLASS="MATH"><I>&#963;</I><SUP>2</SUP></SPAN> of the random variable <SPAN CLASS="MATH"><I>X</I></SPAN>, with confidence level
    *   <TT>level</TT>, assuming that the observations
    *   given to this collector are independent and identically distributed
    *   (i.i.d.) copies of <SPAN CLASS="MATH"><I>X</I></SPAN>, and that <SPAN CLASS="MATH"><I>X</I></SPAN> has the normal distribution.
    *   This confidence interval is computed based on the statistic
    *   
    * <SPAN CLASS="MATH"><I>&#967;</I><SUP>2</SUP><SUB>n-1</SUB> = (<I>n</I> - 1)<I>S</I><SUP>2</SUP><SUB>n</SUB>/<I>&#963;</I><SUP>2</SUP></SPAN>
    *   where <SPAN CLASS="MATH"><I>n</I></SPAN> is the number of observations given to this collector since its
    *   last initialization, and <SPAN CLASS="MATH"><I>S</I><SUP>2</SUP><SUB>n</SUB> =</SPAN> <TT>variance()</TT> is the empirical
    *   variance of these observations.  Under the assumption that the observations
    *   of <SPAN CLASS="MATH"><I>X</I></SPAN> are  i.i.d. and normally distributed,
    *   
    * <SPAN CLASS="MATH"><I>&#967;</I><SUP>2</SUP><SUB>n-1</SUB></SPAN> has the chi-square distribution with <SPAN CLASS="MATH"><I>n</I> - 1</SPAN> degrees of freedom.
    *   Given the <TT>level</TT> 
    * <SPAN CLASS="MATH">= 1 - <I>&#945;</I></SPAN>,
    *   one has 
    * <SPAN CLASS="MATH"><I>P</I>[<I>&#967;</I><SUP>2</SUP><SUB>n-1</SUB> &lt; <I>x</I><SUB>1</SUB>] = <I>P</I>[<I>&#967;</I><SUP>2</SUP><SUB>n-1</SUB> &gt; <I>x</I><SUB>2</SUB>] = <I>&#945;</I>/2</SPAN>
    *   and 
    * <SPAN CLASS="MATH">[<I>I</I><SUB>1</SUB>, <I>I</I><SUB>2</SUB>] = [(<I>n</I> - 1)<I>S</I><SUP>2</SUP><SUB>n</SUB>/<I>x</I><SUB>2</SUB>, &nbsp;(<I>n</I> - 1)<I>S</I><SUP>2</SUP><SUB>n</SUB>/<I>x</I><SUB>1</SUB>]</SPAN>.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @param interval array of size 2 in which are returned the left
    *         and right boundaries of the confidence interval, respectively
    * 
    * 
    */
   public void confidenceIntervalVarianceChi2 (double level,
                                               double[] interval)  {
      // Must return an array object, cannot return 2 doubles directly
      if (numObs < 2) throw new RuntimeException (
          "Tally " + name +
          ":   calling confidenceIntervalVarianceChi2 with < 2 observations");
      double w = (numObs - 1)*variance();
      double x2 = ChiSquareDist.inverseF (numObs - 1, 0.5 * (1.0 + level));
      double x1 = ChiSquareDist.inverseF (numObs - 1, 0.5 * (1.0 - level));
      interval[0] = w / x2;
      interval[1] = w / x1;
   }


   /**
    * Similar to {@link #confidenceIntervalVarianceChi2 confidenceIntervalVarianceChi2}.
    *    Returns the confidence interval in a formatted string of the form 
    * <BR>
    * <DIV CLASS="centerline" ID="par441" ALIGN="CENTER">
    * ``<TT>95.0% confidence interval for variance (chi2):
    *    ( 510.642,  519.673 )</TT>'',</DIV>
    * using <SPAN CLASS="MATH"><I>d</I></SPAN> fractional decimal digits.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true variance
    * 
    *    @param d number of fractional decimal digits
    * 
    *    @return a confidence interval formatted as a string
    * 
    */
   public String formatCIVarianceChi2 (double level, int d)  {
      PrintfFormat str = new PrintfFormat();
      double ci[] = new double[2];
      confidenceIntervalVarianceChi2 (level, ci);
      str.append ("  " + (100*level) + "%");
      str.append (" confidence interval for variance (chi2): (");
      str.append (7 + d, d, d-1, ci[0]).append (',');
      str.append (7 + d, d, d-1, ci[1]).append (" )" + PrintfFormat.NEWLINE);
      return str.toString();
}


   /**
    * Returns a formatted string that contains a report on this probe.
    *  
    * @return a statistical report formatted as a string
    * 
    */
   public String report()  {
      return report(level, digits);
   }


   /**
    * Returns a formatted string that contains a report on this probe
    *   with a confidence interval level <TT>level</TT> using <SPAN CLASS="MATH"><I>d</I></SPAN>
    *   fractional decimal digits.
    *  
    * @param level desired probability that the confidence
    *                  interval covers the true mean
    * 
    *    @param d number of fractional decimal digits
    * 
    *    @return a statistical report formatted as a string
    * 
    */
   public String report (double level, int d)  {
      PrintfFormat str = new PrintfFormat();
      str.append ("REPORT on Tally stat. collector ==> " + name);
      str.append (PrintfFormat.NEWLINE + "    num. obs.      min          max        average     standard dev." + PrintfFormat.NEWLINE);
      str.append (7 + d, (int)numObs);   str.append (" ");
      str.append (9 + d, d, d-1, (double)minValue);   str.append (" ");
      str.append (9 + d, d, d-1, (double)maxValue);   str.append (" ");
      str.append (9 + d, d, d-1, (double)average());  str.append (" ");
      str.append (9 + d, d, d-1, standardDeviation());
      str.append (PrintfFormat.NEWLINE);

      switch (confidenceInterval) {
         case CI_NORMAL:
            str.append (formatCINormal (level, d));
            break;
         case CI_STUDENT:
            str.append (formatCIStudent (level, d));
            break;
      }

      return str.toString();
   }


   public String shortReportHeader() {
      PrintfFormat pf = new PrintfFormat();
      if (showNobs)
         pf.append (-8, "num obs.").append ("  ");
      pf.append (-8, "   min").append ("   ");
      pf.append (-8, "   max").append ("   ");
      pf.append (-8, "   average").append ("   ");
      pf.append (-8, "   std. dev.");
      if (confidenceInterval != CIType.CI_NONE)
         pf.append ("   ").append (-12, "conf. int.");

      return pf.toString();
   }

   /**
    * Formats and returns a short
    *  statistical report for this tally.
    *  The returned single-line report contains the minimum value,
    *  the maximum value,
    *  the average, and the standard deviation, in that order, separated
    *  by three spaces.  If the number of observations is shown in the short report,
    *  a column containing the
    *  number of observations in this tally is added.
    * 
    * @return the string containing the report.
    * 
    */
   public String shortReport() {
      PrintfFormat pf = new PrintfFormat();
      if (showNobs)
         pf.append (-8, numberObs());
      pf.append (9, 3, 2, min()).append ("   ");
      pf.append (9, 3, 2, max()).append ("   ");
      pf.append (10, 3, 2, average()).append ("   ");
      if (numberObs() >= 2)
         pf.append (11, 3, 2, standardDeviation());
      else
         pf.append (11, "---");

      if (confidenceInterval != CIType.CI_NONE) {
         double[] ci = new double[2];
         switch (confidenceInterval) {
         case CI_NORMAL:
            confidenceIntervalNormal (level, ci);
            break;
         case CI_STUDENT:
            confidenceIntervalStudent (level, ci);
            break;
         }
         pf.append ("   ").append ((100*level) + "% (");
         pf.append (9, 3, 2, ci[0] - ci[1]).append (',');
         pf.append (9, 3, 2, ci[0] + ci[1]).append (")");
      }

      return pf.toString();
   }


   /**
    * Returns a formatted string that contains a report on this probe (as in
    *     {@link #report report}), followed by a confidence interval (as in
    *     {@link #formatCIStudent formatCIStudent}), using <SPAN CLASS="MATH"><I>d</I></SPAN> fractional decimal digits.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @param d number of fractional decimal digits
    * 
    *    @return a statistical report with a confidence interval, formatted as a string
    * 
    */
    public String reportAndCIStudent (double level, int d)  {
      CIType oldCIType = confidenceInterval;

      try {
         confidenceInterval = CIType.CI_STUDENT;
         return report(level, d);
      } finally {
         confidenceInterval = oldCIType;
      }
  }


   /**
    * Same as {@link #reportAndCIStudent reportAndCIStudent} <TT>(level, 3)</TT>.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    *    @return a statistical report with a confidence interval, formatted as a string
    * 
    */
   public String reportAndCIStudent (double level)  {
      return reportAndCIStudent (level, 3);
  }


   /**
    * Returns the level of confidence for the intervals on the mean displayed in
    *    reports.  The default confidence level is 0.95.
    * 
    * @return desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    */
   public double getConfidenceLevel() {
      return level;
   }


   /**
    * Sets the level of confidence for the intervals on the mean displayed in
    *    reports.
    * 
    * @param level desired probability that the (random) confidence
    *         interval covers the true mean (a constant)
    * 
    * 
    */
   public void setConfidenceLevel (double level) {
      if (level < 0.0)
         throw new IllegalArgumentException("level < 0");
      if (level >= 1.0)
         throw new IllegalArgumentException("level >= 1");
      this.level = level;
   }


   /**
    * Indicates that no confidence interval needs to be printed in
    *    reports formatted by {@link #report report}, and {@link #shortReport shortReport}.
    *    This restores the default behavior of the reporting system.
    * 
    */
   public void setConfidenceIntervalNone() {
      confidenceInterval = CIType.CI_NONE;
   }


   /**
    * Indicates that a confidence interval on the true mean, based on the
    *   central limit theorem, needs to be included in reports formatted by
    *  {@link #report report} and {@link #shortReport shortReport}. The confidence interval is
    *  formatted using {@link #formatCINormal formatCINormal}.
    * 
    */
   public void setConfidenceIntervalNormal() {
      confidenceInterval = CIType.CI_NORMAL;
   }


   /**
    * Indicates that a confidence interval on the true mean, based on the
    *   normality assumption, needs to be included in
    *    reports formatted by {@link #report report} and {@link #shortReport shortReport}.
    *    The confidence interval is formatted using {@link #formatCIStudent formatCIStudent}.
    * 
    */
   public void setConfidenceIntervalStudent() {
      confidenceInterval = CIType.CI_STUDENT;
   }


   /**
    * Determines if the number of observations must be displayed in reports.
    *   By default, the number of observations is displayed.
    * 
    * @param showNumObs the value of the indicator.
    * 
    * 
    */
   public void setShowNumberObs (boolean showNumObs) {
      showNobs = showNumObs;
   }


   /**
    * Clones this object.
    * 
    */
   public Tally clone() {
      try {
         return (Tally)super.clone();
      } catch (CloneNotSupportedException e) {
         throw new IllegalStateException ("Tally can't clone");
      }
   }

}
