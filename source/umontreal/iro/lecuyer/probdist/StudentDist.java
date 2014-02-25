
/*
 * Class:        StudentDistDist
 * Description:  Student-t distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        March 2009

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
import umontreal.iro.lecuyer.functions.MathFunction;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <SPAN  CLASS="textit">Student</SPAN> <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution
 * with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom, where <SPAN CLASS="MATH"><I>n</I></SPAN> is a positive integer.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = [<I>&#915;</I>((<I>n</I> + 1)/2)/(<I>&#915;</I>(<I>n</I>/2)(&pi;n)<SUP>1/2</SUP>)][1 + <I>x</I><SUP>2</SUP>/<I>n</I>]<SUP>-(n+1)/2</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined in
 * {@link GammaDist}.
 * 
 */
public class StudentDist extends ContinuousDistribution {
   protected int n;
   private double factor;
   private static final int NLIM1 = 100000;
/*
   private static double cdfPeizer (int n, double x) {
      // Peizer-Pratt normal approximation for the cdf (n, u)
      // \cite{tPEI68a}
      double v = Math.log1p(x*x/n) / (n - 5.0/6.0);
      double z = -(n - 2.0/3.0 + 0.1/n) * Math.sqrt(v);
      double u = NormalDist.cdf01 (z);
      if (x >= 0.0)
         return 1.0 - u;
      return u;
   }

   private static double invPeizer (int n, double u) {
      // Peizer-Pratt normal approximation for the inverseF (n, u)
      // \cite{tPEI68a}
      double z = NormalDist.inverseF01 (u);
      double q = z / (n - 2.0/3.0 + 0.1/n);
      double v = q*q*(n - 5.0/6.0);
      double t = Math.sqrt(n * Math.expm1(v));
      if (u >= 0.5)
         return t;
      else
         return -t;
   }
*/

   private static double cdfGaver (int n, double x) {
      // Gaver-Kafadar normal approximation for the cdf
      // \cite{tGAV84a}
      double v = Math.log1p(x * x / n) / (n - 1.5);
      double z = -(n - 1) * Math.sqrt(v);
      double u = NormalDist.cdf01 (z);
      if (x >= 0.0)
         return 1.0 - u;
      return u;
   }


   private static double invGaver (int n, double u) {
      // Gaver-Kafadar normal approximation for the inverse
      // \cite{tGAV84a}
      double z = NormalDist.inverseF01 (u);
      double q = z / (n - 1.0);
      double v = q * q * (n - 1.5);
      double t = Math.sqrt(n * Math.expm1(v));
      if (u >= 0.5)
         return t;
      else
         return -t;
   }


   private static class Function implements MathFunction {
      private int n;
      private double[] xi;

      public Function (double[] x, int n) {
         this.n = n;
         this.xi = new double[n];
         System.arraycopy (x, 0, this.xi, 0, n);
      }

      public double evaluate (double x) {
         if (x <= 0.0)
            return 1e200;
         double sum = 0.0;
         for (int i = 0; i < n; i++)
            sum += Math.log (density ((int) Math.round (x), xi[i]));
         return sum;
      }
   }


   /**
    * Constructs a <TT>StudentDist</TT> object with <TT>n</TT> degrees of freedom.
    * 
    */
   public StudentDist (int n) {
     setN (n);
   }


   public double density (double x) {
      return factor*Math.pow (1.0 / (1.0 + x*x/n), (n + 1)/2.0);
   }

   public double cdf (double x) {
      return cdf (n, x);
   }

   public double barF (double x) {
      return barF (n, x);
   }

   public double inverseF (double u) {
      return inverseF (n, u);
   }

   public double getMean() {
      return StudentDist.getMean (n);
   }

   public double getVariance() {
      return StudentDist.getVariance (n);
   }

   public double getStandardDeviation() {
      return StudentDist.getStandardDeviation (n);
   }

   /**
    * Computes the density function of a
    *    Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom.
    * 
    */
   public static double density (int n, double x) {
      double factor = Num.gammaRatioHalf(n/2.0)/ Math.sqrt (n*Math.PI);
      return factor*Math.pow (1.0 / (1.0 + x*x/n), (n + 1)/2.0);
   }


   /**
    * Computes the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution function <SPAN CLASS="MATH"><I>u</I> = <I>F</I>(<I>x</I>)</SPAN> with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom.
    *   Gives 13 decimal digits of precision for 
    * <SPAN CLASS="MATH"><I>n</I>&nbsp;&lt;=&nbsp;10<SUP>5</SUP></SPAN>.
    *  For <SPAN CLASS="MATH"><I>n</I> &gt; 10<SUP>5</SUP></SPAN>, gives  at least 6 decimal digits of precision everywhere, and
    *  at least 9 decimal digits of precision for all 
    * <SPAN CLASS="MATH"><I>u</I> &gt; 10<SUP>-15</SUP></SPAN>.
    * 
    */
   public static double cdf (int n, double x) {
      if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");
      if (n == 1)
         return CauchyDist.cdf(0, 1, x);

      if (x > 1.0e10)
         return 1.0;
      if (n > NLIM1)
         return cdfGaver(n, x);

      double r = Math.abs(x);
      if (r < 1.0e15)
         r = Math.sqrt (n + x*x);

      double z;
      if (x >= 0.0)
         z = 0.5*(1.0 + x/r);
      else
         z = 0.5*n/(r*(r - x));

      if (n == 2)
         return z;
      return BetaSymmetricalDist.cdf (0.5*n, 15, z);
   }


   /**
    * Same as {@link #cdf(int,double) cdf}<TT>(n, x)</TT>.
    * 
    */
   @Deprecated
   public static double cdf2 (int n, int d, double x) {
      if (d <= 0)
         throw new IllegalArgumentException ("student2:   d <= 0");
      return cdf (n, x);
   }


   /**
    * Computes the complementary distribution function 
    * <SPAN CLASS="MATH"><I>v</I> = bar(F)(<I>x</I>)</SPAN>
    * with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom. Gives 13 decimal digits of precision for 
    * <SPAN CLASS="MATH"><I>n</I>&nbsp;&lt;=&nbsp;10<SUP>5</SUP></SPAN>.
    *  For <SPAN CLASS="MATH"><I>n</I> &gt; 10<SUP>5</SUP></SPAN>, gives  at least 6 decimal digits of precision everywhere, and
    *  at least 9 decimal digits of precision for all 
    * <SPAN CLASS="MATH"><I>v</I> &gt; 10<SUP>-15</SUP></SPAN>.
    * 
    */
   public static double barF (int n, double x) {
      if (n < 1)
        throw new IllegalArgumentException ("n < 1");
      if (n == 1)
         return CauchyDist.barF(0, 1, x);

      if (n == 2) {
         double z = Math.abs(x);
         if (z < 1.0e10)
            z = Math.sqrt(2.0 + x*x);
         if (x <= 0.) {
            if (x < -1.0e10)
               return 1.0;
            return 0.5* (1.0 - x / z);
         } else
            return 1.0 / (z * (z + x));
      }

      return cdf (n, -x);
   }


   /**
    * Returns the inverse 
    * <SPAN CLASS="MATH"><I>x</I> = <I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN> of
    *   Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution function with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom.
    *   Gives 13 decimal digits of precision for 
    * <SPAN CLASS="MATH"><I>n</I>&nbsp;&lt;=&nbsp;10<SUP>5</SUP></SPAN>,
    *   and at least 9 decimal digits of precision for <SPAN CLASS="MATH"><I>n</I> &gt; 10<SUP>5</SUP></SPAN>.
    * 
    */
   public static double inverseF (int n, double u) {
        if (n < 1)
            throw new IllegalArgumentException ("Student:   n < 1");
        if (u > 1.0 || u < 0.0)
            throw new IllegalArgumentException ("Student:   u not in [0, 1]");
        if (u <= 0.0)
           return Double.NEGATIVE_INFINITY;
        if (u >= 1.0)
           return Double.POSITIVE_INFINITY;

        if (1 == n)
           return CauchyDist.inverseF(0, 1, u);

        if (2 == n)
           return (2.0*u - 1.0) / Math.sqrt(2.0*u*(1.0 - u));

        if (n > NLIM1)
           return invGaver(n, u);
        double z = BetaSymmetricalDist.inverseF (0.5*n, u);
        return (z - 0.5) * Math.sqrt(n / (z*(1.0 - z)));
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>m</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>. The estimate is returned in a one-element
    *    array.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param m the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH">hat(n)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int m) {
      double sum = 0.0;
      double[] parameters = new double[1];

      if (m <= 0)
         throw new IllegalArgumentException ("m <= 0");

      double var = 0.0;
      for (int i = 0; i < m; i++)
         var += x[i] * x[i];
      var /= (double) m;

      Function f = new Function (x, m);

      double n0 = Math.round ((2.0 * var) / (var - 1.0));
      double fn0 = f.evaluate (n0);
      double min = fn0;
      double fn1 = f.evaluate (n0 + 1.0);
      double fn_1 = f.evaluate (n0 - 1.0);

      parameters[0] = n0;

      if (fn_1 > fn0) {
         double n = n0 - 1.0;
         double y;
         while (((y = f.evaluate (n)) > min) && (n >= 1.0)) {
            min = y;
            parameters[0] = n;
            n -= 1.0;
         }

      } else if (fn1 > fn0) {
         double n = n0 + 1.0;
         double y;
         while ((y = f.evaluate (n)) > min) {
            min = y;
            parameters[0] = n;
            n += 1.0;
         }
      }
      return parameters;
   }


   /**
    * Creates a new instance of a Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>m</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param m the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static StudentDist getInstanceFromMLE (double[] x, int m) {
      double parameters[] = getMLE (x, m);
      return new StudentDist ((int) parameters[0]);
   }


   /**
    * Returns the mean <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = 0</SPAN> of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution with
    *  parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the mean of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = 0</SPAN>
    * 
    */
   public static double getMean (int n) {
     if (n < 2)
        throw new IllegalArgumentException ("n <= 1");
      return 0;
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>n</I>/(<I>n</I> - 2)</SPAN>
    *    of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the variance of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>n</I>/(<I>n</I> - 2)</SPAN>
    * 
    */
   public static double getVariance (int n) {
      if (n < 3)
         throw new IllegalArgumentException("n <= 2");
      return (n / (n - 2.0));
   }


   /**
    * Computes and returns the standard deviation
    *    of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the standard deviation of the Student <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution
    * 
    */
   public static double getStandardDeviation (int n) {
      return Math.sqrt (StudentDist.getVariance (n));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> associated with this object.
    * 
    */
   public int getN() {
      return n;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> associated with this object.
    * 
    */
   public void setN (int n) {
     if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");
      this.n = n;
      factor = Num.gammaRatioHalf(n/2.0) / Math.sqrt (n*Math.PI);
   }


   /**
    * Return a table containing the parameter of the current distribution.
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {n};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : n = " + n;
   }

}
