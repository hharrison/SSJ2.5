

/*
 * Class:        LogarithmicDist
 * Description:  logarithmic distribution
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

import umontreal.iro.lecuyer.util.Num;
import umontreal.iro.lecuyer.util.RootFinder;
import umontreal.iro.lecuyer.functions.MathFunction;


/**
 * Extends the class {@link DiscreteDistributionInt} for
 * the <EM>logarithmic</EM> distribution. It has shape parameter
 * <SPAN CLASS="MATH"><I>&#952;</I></SPAN>, where 
 * <SPAN CLASS="MATH">0 &lt; <I>&#952;</I> &lt; 1</SPAN>.
 * Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = - <I>&#952;</I><SUP>x</SUP>/(<I>x</I> log(1 - <I>&#952;</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 1, 2, 3,...
 * </DIV><P></P>
 * Its distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = -1/log(1-<I>&#952;</I>)&sum;<SUB>i=1</SUB><SUP>x</SUP><I>&#952;</I><SUP>i</SUP>/<I>i</I>, &amp;  for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * 
 */
public class LogarithmicDist extends DiscreteDistributionInt {

   private double theta;
   private double t;

   private static class Function implements MathFunction {
      protected double mean;

      public Function (double mean) {
         this.mean = mean;
      }

      public double evaluate (double x) {
         if (x <= 0.0 || x >= 1.0) return 1.0e200;
         return (x + mean * (1.0 - x) * Math.log1p (-x));
      }
   }


   /**
    * Constructs a logarithmic distribution with parameter <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN>
    *   <TT>theta</TT>.
    * 
    */
   public LogarithmicDist (double theta) {
      setTheta (theta);
   }


   public double prob (int x) {
      if (x < 1)
         return 0;
      return t*Math.pow (theta, x)/x;
   }

   public double cdf (int x) {
      if (x < 1)
         return 0;
      double res = prob (1);
      double term = res;
      for (int i = 2; i <= x; i++) {
         term *= theta;
         res += term/i;
      }
      return res;
   }

   public double barF (int x) {
      if (x <= 1)
         return 1.0;
      double res = prob (x);
      double term = res;
      int i = x + 1;
      while (term > EPSILON) {
         term *= theta*(i-1)/i;
         res += term;
      }
      return res;
   }

   public int inverseFInt (double u) {
      return inverseF (theta, u);
   }

   public double getMean() {
      return LogarithmicDist.getMean (theta);
   }

   public double getVariance() {
      return LogarithmicDist.getVariance (theta);
   }

   public double getStandardDeviation() {
      return LogarithmicDist.getStandardDeviation (theta);
   }

   /**
    * Computes the logarithmic probability <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double prob (double theta, int x) {
      if (theta <= 0 || theta >= 1)
         throw new IllegalArgumentException ("theta not in range (0,1)");
      if (x < 1)
         return 0;
      return -1.0/Math.log1p(-theta) * Math.pow (theta, x)/x;
   }


   /**
    * Computes the distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double cdf (double theta, int x) {
      if (theta <= 0 || theta >= 1)
         throw new IllegalArgumentException ("theta not in range (0,1)");
      if (x < 1)
         return 0;
      double res = prob (theta, 1);
      double term = res;
      for (int i = 2; i <= x; i++) {
         term *= theta;
         res += term/i;
      }
      return res;
   }


   /**
    * Computes the complementary distribution function.
    * <SPAN  CLASS="textit">WARNING:</SPAN> The complementary distribution function is defined as 
    * 
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>]</SPAN>.
    * 
    */
   public static double barF (double theta, int x) {
      if (theta <= 0 || theta >= 1)
         throw new IllegalArgumentException ("theta not in range (0,1)");
      if (x <= 1)
         return 1.0;
      double res = prob (theta, x);
      double term = res;
      int i = x + 1;
      while (term > EPSILON) {
         term *= theta*(i-1)/i;
         res += term;
      }
      return res;
   }


   public static int inverseF (double theta, double u) {
      throw new UnsupportedOperationException();
   }

   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>&#952;</I></SPAN> of the logarithmic distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations 
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimate is returned in element 0
    *    of the returned array.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameter [
    * <SPAN CLASS="MATH">hat(&thetas;)</SPAN>]
    * 
    */
   public static double[] getMLE (int[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[];
      parameters = new double[1];
      double sum = 0.0;
      for (int i = 0; i < n; i++) {
         sum += x[i];
      }

      double mean = (double) sum / (double) n;

      Function f = new Function (mean);
      parameters[0] = RootFinder.brentDekker (1e-15, 1.0-1e-15, f, 1e-7);

      return parameters;
   }


   /**
    * Creates a new instance of a logarithmic distribution with parameter
    *    <SPAN CLASS="MATH"><I>&#952;</I></SPAN> estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static LogarithmicDist getInstanceFromMLE (int[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new LogarithmicDist (parameters[0]);
   }


   /**
    * Computes and returns the mean
    * of the logarithmic distribution with parameter <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT>.
    * 
    * @return the mean of the logarithmic distribution
    *     
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = - <I>&#952;</I>/((1 - <I>&#952;</I>)<I>ln</I>(1 - <I>&#952;</I>))</SPAN>
    * 
    */
   public static double getMean (double theta) {
      if (theta <= 0.0 || theta >= 1.0)
         throw new IllegalArgumentException ("theta not in range (0,1)");

      return ((-1 / Math.log1p(-theta)) * (theta / (1 - theta)));
   }


   /**
    * Computes and returns the variance
    * of the logarithmic distribution with parameter <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT>.
    * 
    * @return the variance of the logarithmic distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = - <I>&#952;</I>(<I>&#952;</I> + <I>ln</I>(1 - <I>&#952;</I>))/((1 - <I>&#952;</I>)<SUP>2</SUP>(<I>ln</I>(1 - <I>&#952;</I>))<SUP>2</SUP>)</SPAN>
    * 
    */
   public static double getVariance (double theta) {
      if (theta <= 0.0 || theta >= 1.0)
         throw new IllegalArgumentException ("theta not in range (0,1)");

      double v = Math.log1p(-theta);
      return ((-theta * (theta + v)) / ((1 - theta) * (1 - theta) * v * v));
   }


   /**
    * Computes and returns the standard deviation of the
    *    logarithmic distribution with parameter <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT>.
    * 
    * @return the standard deviation of the logarithmic distribution
    * 
    */
   public static double getStandardDeviation (double theta) {
      return Math.sqrt (LogarithmicDist.getVariance (theta));
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#952;</I></SPAN> associated with this object.
    * 
    */
   public double getTheta() {
      return theta;
   }



   /**
    * Sets the <SPAN CLASS="MATH"><I>&#952;</I></SPAN> associated with this object.
    * 
    */
   public void setTheta (double theta) {
      if (theta <= 0 || theta >= 1)
         throw new IllegalArgumentException ("theta not in range (0,1)");
      this.theta = theta;
      t = -1.0/Math.log1p (-theta);
      supportA = 1;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {theta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : theta = " + theta;
   }

}
