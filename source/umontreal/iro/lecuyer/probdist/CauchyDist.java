
/*
 * Class:        CauchyDist
 * Description:  Cauchy distribution
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

import umontreal.iro.lecuyer.util.Misc;
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>Cauchy</EM> distribution
 * with location parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
 * and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#946;</I>/(<I>&#960;</I>[(<I>x</I> - <I>&#945;</I>)<SUP>2</SUP> + <I>&#946;</I><SUP>2</SUP>]) for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1/2 + arctan((<I>x</I> - <I>&#945;</I>)/<I>&#946;</I>)/<I>&#960;</I>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;,
 * </DIV><P></P>
 * and its inverse is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>&#945;</I> + <I>&#946;</I>tan(<I>&#960;</I>(<I>u</I> - 1/2)).&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0 &lt; <I>u</I> &lt; 1.
 * </DIV><P></P>
 * 
 */
public class CauchyDist extends ContinuousDistribution {
   private double alpha;
   private double beta;

   private static class Optim implements Uncmin_methods
   {
      private int n;
      private double[] xi;

      public Optim (double[] x, int n)
      {
         this.n = n;
         this.xi = new double[n];
         System.arraycopy (x, 0, this.xi, 0, n);
      }

      public double f_to_minimize (double[] p)
      {
         double sum = 0.0;

         if (p[2] <= 0.0)               // barrier at 0
            return 1.0e200;

         for (int i = 0; i < n; i++)
            sum -= Math.log (density (p[1], p[2], xi[i]));

         return sum;
      }

      public void gradient (double[] x, double[] g)
      {
      }

      public void hessian (double[] x, double[][] h)
      {
      }
   }



   /**
    * Constructs a <TT>CauchyDist</TT> object
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I> = 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN>.
    * 
    */
   public CauchyDist() {
      setParams (0.0, 1.0);
   }


   /**
    * Constructs a <TT>CauchyDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>.
    * 
    */
   public CauchyDist (double alpha, double beta) {
      setParams (alpha, beta);
   }


   public double density (double x) {
      return density (alpha, beta, x);
   }

   public double cdf (double x) {
      return cdf (alpha, beta, x);
   }

   public double barF (double x) {
      return barF (alpha, beta, x);
   }

   public double inverseF (double u){
      return inverseF (alpha, beta, u);
   }

   public double getMean() {
      return CauchyDist.getMean (alpha, beta);
   }

   public double getVariance() {
      return CauchyDist.getVariance (alpha, beta);
   }

   public double getStandardDeviation() {
      return CauchyDist.getStandardDeviation (alpha, beta);
   }

   /**
    * Computes the density function.
    * 
    */
   public static double density (double alpha, double beta, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      double t = (x - alpha)/beta;
      return 1.0/(beta * Math.PI*(1 + t*t));
   }


   /**
    * Computes the  distribution function.
    * 
    */
   public static double cdf (double alpha, double beta, double x) {
      // The integral was computed analytically using Mathematica
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      double z = (x - alpha)/beta;
      if (z < -0.5)
         return Math.atan(-1.0/z)/Math.PI;
      return Math.atan(z)/Math.PI + 0.5;
   }


   /**
    * Computes the complementary distribution.
    * 
    */
   public static double barF (double alpha, double beta, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      double z = (x - alpha)/beta;
      if (z > 0.5)
         return Math.atan(1./z)/Math.PI;
      return 0.5 - Math.atan(z)/Math.PI;
   }


   /**
    * Computes the inverse of the distribution.
    * 
    */
   public static double inverseF (double alpha, double beta, double u) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
     if (u < 0.0 || u > 1.0)
        throw new IllegalArgumentException ("u must be in [0,1]");
     if (u <= 0.0)
        return Double.NEGATIVE_INFINITY;
     if (u >= 1.0)
        return Double.POSITIVE_INFINITY;
     if (u < 0.5)
        return alpha - 1.0/Math.tan (Math.PI*u) * beta;
     return alpha + Math.tan (Math.PI*(u - 0.5)) * beta;
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#946;</I>)</SPAN> of the Cauchy distribution
    *   using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      double sum = 0.0;

      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      Optim system = new Optim (x, n);

      double[] parameters = new double[2];
      double[] xpls = new double[3];
      double[] param = new double[3];
      double[] fpls = new double[3];
      double[] gpls = new double[3];
      int[] itrcmd = new int[2];
      double[][] a = new double[3][3];
      double[] udiag = new double[3];

      param[1] = EmpiricalDist.getMedian (x, n);

      int m = Math.round ((float) n / 4.0f);
      double q3 = Misc.quickSelect (x, n, 3 * m);
      double q1 = Misc.quickSelect (x, n, m);
      param[2] = (q3 - q1) / 2.0;

      Uncmin_f77.optif0_f77 (2, param, system, xpls, fpls, gpls, itrcmd, a, udiag);

      for (int i = 0; i < 2; i++)
         parameters[i] = xpls[i+1];

      return parameters;
   }


   /**
    * Creates a new instance of a Cauchy distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>
    *    estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static CauchyDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new CauchyDist (parameters[0], parameters[1]);
   }


   /**
    * Throws an exception since the mean does not exist.
    * 
    * @exception UnsupportedOperationException the mean of the Cauchy distribution is undefined.
    * 
    * 
    */
   public static double getMean (double alpha, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      throw new UnsupportedOperationException("Undefined mean");
   }


   /**
    * Returns <SPAN CLASS="MATH">&#8734;</SPAN> since the variance does not exist.
    * 
    * @return <SPAN CLASS="MATH">&#8734;</SPAN>.
    * 
    */
   public static double getVariance (double alpha, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      return Double.POSITIVE_INFINITY;
   }


   /**
    * Returns <SPAN CLASS="MATH">&#8734;</SPAN> since the standard deviation does not exist.
    * 
    * @return <SPAN CLASS="MATH">&#8734;</SPAN>
    * 
    */
   public static double getStandardDeviation (double alpha, double beta) {
      return Double.POSITIVE_INFINITY;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#945;</I></SPAN> for this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#946;</I></SPAN> for this object.
    * 
    */
   public double getBeta() {
      return beta;
   }



   /**
    * Sets the value of the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> for this object.
    * 
    */
   public void setParams (double alpha, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      this.alpha = alpha;
      this.beta = beta;
   }


   /**
    * Return a table containing parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha, beta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : alpha = " + alpha + ", beta = " + beta;
   }

}
