

/*
 * Class:        PiecewiseLinearEmpiricalDist
 * Description:  piecewise-linear empirical distribution
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

package umontreal.iro.lecuyer.probdist;

import java.util.Formatter;
import java.util.Locale;
import umontreal.iro.lecuyer.util.Num;
import umontreal.iro.lecuyer.util.PrintfFormat;
import java.util.Arrays;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;


/**
 * Extends the class {@link ContinuousDistribution} for a piecewise-linear 
 * approximation of the <SPAN  CLASS="textit">empirical</SPAN> distribution function, 
 * based on the observations 
 * <SPAN CLASS="MATH"><I>X</I><SUB>(1)</SUB>,..., <I>X</I><SUB>(n)</SUB></SPAN> (sorted by increasing order), 
 * and defined as follows (e.g.,).
 * The distribution function starts at <SPAN CLASS="MATH"><I>X</I><SUB>(1)</SUB></SPAN> and climbs linearly by <SPAN CLASS="MATH">1/(<I>n</I> - 1)</SPAN>
 * between any two successive observations.  The density is 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 1/[(<I>n</I> - 1)(<I>X</I><SUB>(i+1)</SUB> - <I>X</I><SUB>(i)</SUB>)] for <I>X</I><SUB>(i)</SUB>&nbsp;&lt;=&nbsp;<I>x</I> &lt; <I>X</I><SUB>(i+1)</SUB> and <I>i</I> = 1, 2,..., <I>n</I> - 1.
 * </DIV><P></P>
 * The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">0</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &lt; <I>X</I><SUB>(1)</SUB>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">(<I>i</I> - 1)/(<I>n</I> - 1) + (<I>x</I> - <I>X</I><SUB>(i)</SUB>)/[(<I>n</I> - 1)(<I>X</I><SUB>(i+1)</SUB> - <I>X</I><SUB>(i)</SUB>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>X</I><SUB>(i)</SUB>&nbsp;&lt;=&nbsp;<I>x</I> &lt; <I>X</I><SUB>(i+1)</SUB>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; elsewhere,</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * whose inverse is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>X</I><SUB>(i)</SUB> + ((<I>n</I> - 1)<I>u</I> - <I>i</I> + 1)(<I>X</I><SUB>(i+1)</SUB> - <I>X</I><SUB>(i)</SUB>)
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH">(<I>i</I> - 1)/(<I>n</I> - 1)&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;<I>i</I>/(<I>n</I> - 1)</SPAN> and 
 * <SPAN CLASS="MATH"><I>i</I> = 1,..., <I>n</I> - 1</SPAN>.
 * 
 */
public class PiecewiseLinearEmpiricalDist extends ContinuousDistribution {
   private double[] sortedObs;
   private double[] diffObs;
   private int n = 0;
   private double sampleMean;
   private double sampleVariance;
   private double sampleStandardDeviation;


   /**
    * Constructs a new piecewise-linear distribution using
    *   all the observations stored in <TT>obs</TT>.
    *   These observations are copied into an internal array and then sorted.
    * 
    */
   public PiecewiseLinearEmpiricalDist (double[] obs) {
      if (obs.length <= 1)
         throw new IllegalArgumentException ("Two or more observations are needed");
      // sortedObs = obs;
      n = obs.length;
      sortedObs = new double[n];
      System.arraycopy (obs, 0, sortedObs, 0, n);
      init();
   }


   /**
    * Constructs a new empirical distribution using
    *   the observations read from the reader <TT>in</TT>. This constructor
    *   will read the first <TT>double</TT> of each line in the stream.
    *   Any line that does not start with a <TT>+</TT>, <TT>-</TT>, or a decimal digit,
    *   is ignored.  The file is read until its end.
    *   One must be careful about lines starting with a blank.
    *   This format is the same as in UNURAN.
    * 
    */
   public PiecewiseLinearEmpiricalDist (Reader in) throws IOException {
      BufferedReader inb = new BufferedReader (in);
      double[] data = new double[5];
      String li;
      while ((li = inb.readLine()) != null) {
         // look for the first non-digit character on the read line
         int index = 0;
         while (index < li.length() &&
            (li.charAt (index) == '+' || li.charAt (index) == '-' ||
             li.charAt (index) == 'e' || li.charAt (index) == 'E' ||
             li.charAt (index) == '.' || Character.isDigit (li.charAt (index))))
           ++index; 

         // truncate the line
         li = li.substring (0, index);
         if (!li.equals ("")) {
            try {
               data[n++] = Double.parseDouble (li);
               if (n >= data.length) {
                  double[] newData = new double[2*n];
                  System.arraycopy (data, 0, newData, 0, data.length);
                  data = newData;
               }
            }
            catch (NumberFormatException nfe) {}
         }
      }
      sortedObs = new double[n];
      System.arraycopy (data, 0, sortedObs, 0, n);
      init();
   }


   public double density (double x) {
      // This is implemented via a linear search: very inefficient!!!
      if (x < sortedObs[0] || x >= sortedObs[n-1])
         return 0;
      for (int i = 0; i < (n-1); i++) {
         if (x >= sortedObs[i] && x < sortedObs[i+1])
            return 1.0 / ((n-1)*diffObs[i]);
      }
      throw new IllegalStateException();
   }

   public double cdf (double x) {
      // This is implemented via a linear search: very inefficient!!!
      if (x <= sortedObs[0])
         return 0;
      if (x >= sortedObs[n-1])
         return 1;
      for (int i = 0; i < (n-1); i++) {
         if (x >= sortedObs[i] && x < sortedObs[i+1])
            return i/(n-1.0) + (x - sortedObs[i])/((n-1.0)*diffObs[i]);
      }
      throw new IllegalStateException();
   }

   public double barF (double x) {
      // This is implemented via a linear search: very inefficient!!!
      if (x <= sortedObs[0])
         return 1;
      if (x >= sortedObs[n-1])
         return 0;
      for (int i = 0; i < (n-1); i++) {
         if (x >= sortedObs[i] && x < sortedObs[i+1])
            return (n-1.0-i)/(n-1.0) - (x - sortedObs[i])/((n-1.0)*diffObs[i]);
      }
      throw new IllegalStateException();
   }

   public double inverseF (double u) {
      if (u < 0 || u > 1)
         throw new IllegalArgumentException ("u is not in [0,1]");
      if (u <= 0.0)
         return sortedObs[0];
      if (u >= 1.0)
         return sortedObs[n-1];
      double p = (n - 1)*u;
      int i = (int)Math.floor (p);
      if (i == (n-1))
         return sortedObs[n-1];
      else
         return sortedObs[i] + (p - i)*diffObs[i];
   }

   public double getMean() {
      return sampleMean;
   }

   public double getVariance() {
      return sampleVariance;
   }

   public double getStandardDeviation() {
      return sampleStandardDeviation;
   }

   private void init() {
      Arrays.sort (sortedObs);
      // n = sortedObs.length;
      diffObs = new double[sortedObs.length];
      double sum = 0.0;
      for (int i = 0; i < diffObs.length-1; i++) {
         diffObs[i] = sortedObs[i+1] - sortedObs[i];
         sum += sortedObs[i];
      }
      diffObs[n-1] = 0.0;  // Can be useful in case i=n-1 in inverseF.
      sum += sortedObs[n-1];
      sampleMean = sum / n;
      sum = 0.0;
      for (int i = 0; i < n; i++) {
         double coeff = (sortedObs[i] - sampleMean);
         sum += coeff*coeff;
      }
      sampleVariance = sum / (n-1);
      sampleStandardDeviation = Math.sqrt (sampleVariance);
      supportA = sortedObs[0]*(1.0 - Num.DBL_EPSILON);
      supportB = sortedObs[n-1]*(1.0 + Num.DBL_EPSILON);
   }

   /**
    * Returns <SPAN CLASS="MATH"><I>n</I></SPAN>, the number of observations.
    * 
    */
   public int getN() {
      return n;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>X</I><SUB>(i)</SUB></SPAN>.
    * 
    */
   public double getObs (int i) {
      return sortedObs[i];
   }


   /**
    * Returns the sample mean of the observations.
    * 
    */
   public double getSampleMean() {
      return sampleMean;
   }


   /**
    * Returns the sample variance of the observations.
    * 
    */
   public double getSampleVariance() {
      return sampleVariance;
   }


   /**
    * Returns the sample standard deviation of the observations.
    * 
    */
   public double getSampleStandardDeviation() {
      return sampleStandardDeviation;
   }


   /**
    * Return a table containing parameters of the current distribution.
    * 
    */
   public double[] getParams () {
      double[] retour = new double[n];
      System.arraycopy (sortedObs, 0, retour, 0, n);
      return retour;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString () {
      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb, Locale.US);
      formatter.format(getClass().getSimpleName() + PrintfFormat.NEWLINE);
      for(int i = 0; i<n; i++) {
         formatter.format("%f%n", sortedObs[i]);
      }
      return sb.toString();
   }

}
