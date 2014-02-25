

/*
 * Class:        GumbelDist
 * Description:  Gumbel distribution
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
 * Extends the class {@link ContinuousDistribution} for
 * the  <SPAN  CLASS="textit">Gumbel</SPAN> distribution, with location parameter
 * <SPAN CLASS="MATH"><I>&#948;</I></SPAN> and scale parameter 
 * <SPAN CLASS="MATH"><I>&#946;</I>&#8800; 0</SPAN>. Using the notation 
 * <SPAN CLASS="MATH"><I>z</I> = (<I>x</I> - <I>&#948;</I>)/<I>&#946;</I></SPAN>,
 * it has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>e</I><SUP>-z</SUP><I>e</I><SUP>-e<SUP>-z</SUP></SUP>/| <I>&#946;</I>|,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * and distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>e</I><SUP>-e<SUP>-z</SUP></SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>&#946;</I> &gt; 0
 * </DIV><P></P>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1 - <I>e</I><SUP>-e<SUP>-z</SUP></SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>&#946;</I> &lt; 0.
 * </DIV><P></P>
 * 
 */
public class GumbelDist extends ContinuousDistribution {
   private double delta;
   private double beta;

   private static class FunctionPlus implements MathFunction {
      // when beta > 0
      protected int n;
      protected double mean;
      protected double[] x;
      private double minx;   // min of all {x[i]}

      public FunctionPlus (double[] y, int n, double mean, double minx) {
         this.n = n;
         this.mean = mean;
         this.x = y;
         this.minx = minx;
      }

      public double evaluate (double lam) {
         if (lam <= 0.0) return 1.0e100;
         double tem;
         double sum2 = 0.0;
         double sum1 = 0.0;

         for (int i = 0; i < n; i++) {
            tem = Math.exp (-(x[i] - minx)* lam);
            sum1 += tem;
            sum2 += x[i] * tem;
         }

         return (mean - 1/lam) * sum1 - sum2;
      }
   }


   private static class FunctionMinus implements MathFunction {
      // when beta < 0
      protected int n;
      protected double mean;
      protected double[] x;
      protected double xmax;

      public FunctionMinus (double[] y, int n, double mean, double xmax) {
         this.n = n;
         this.mean = mean;
         this.x = y;
         this.xmax = xmax;
      }

      public double evaluate (double lam) {
         if (lam >= 0.0) return 1.0e100;
         double tem;
         double sum2 = 0.0;
         double sum1 = 0.0;

         for (int i = 0; i < n; i++) {
            tem = Math.exp ((xmax - x[i]) * lam);
            sum1 += tem;
            sum2 += x[i] * tem;
         }

         return (mean - 1/lam) * sum1 - sum2;
      }
   }


   /**
    * Constructor for the standard
    *    Gumbel distribution with parameters  <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = 1 and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = 0.
    * 
    */
   public GumbelDist() {
      setParams (1.0, 0.0);
   }


   /**
    * Constructs a <TT>GumbelDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT> and  <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT>.
    * 
    */
   public GumbelDist (double beta, double delta) {
      setParams (beta, delta);
   }


   public double density (double x) {
      return density (beta, delta, x);
   }

   public double cdf (double x) {
      return cdf (beta, delta, x);
   }

   public double barF (double x) {
      return barF (beta, delta, x);
   }

   public double inverseF (double u) {
      return inverseF (beta, delta, u);
   }

   public double getMean() {
      return GumbelDist.getMean (beta, delta);
   }

   public double getVariance() {
      return GumbelDist.getVariance (beta, delta);
   }

   public double getStandardDeviation() {
      return GumbelDist.getStandardDeviation (beta, delta);
   }

   /**
    * Computes and returns the density function.
    * 
    */
   public static double density (double beta, double delta, double x) {
      if (beta == 0)
         throw new IllegalArgumentException ("beta = 0");
      final double z = (x - delta)/beta;
      if (z <= -10.0)
         return 0.0;
      double t = Math.exp (-z);
      return  t * Math.exp (-t)/Math.abs(beta);
   }


   /**
    * Computes and returns  the distribution function.
    * 
    */
   public static double cdf (double beta, double delta, double x) {
      if (beta == 0)
         throw new IllegalArgumentException ("beta = 0");
      final double z = (x - delta)/beta;
      if (beta > 0.) {
         if (z <= -7.0)
            return 0.0;
         return Math.exp (-Math.exp (-z));
      } else {   // beta < 0
          if (z <= -7.0)
            return 1.0;
         return -Math.expm1 (-Math.exp (-z));
     }
   }


   /**
    * Computes and returns  the complementary distribution function <SPAN CLASS="MATH">1 - <I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double barF (double beta, double delta, double x) {
      if (beta == 0)
         throw new IllegalArgumentException ("beta = 0");
      final double z = (x - delta)/beta;
      if (beta > 0.) {
         if (z <= -7.0)
            return 1.0;
         return -Math.expm1 (-Math.exp (-z));
      } else {   // beta < 0
          if (z <= -7.0)
            return 0.0;
         return Math.exp (-Math.exp (-z));
      }
   }


   /**
    * Computes and returns the inverse distribution function.
    * 
    */
   public static double inverseF (double beta, double delta, double u) {
       if (u < 0.0 || u > 1.0)
          throw new IllegalArgumentException ("u not in [0, 1]");
      if (beta == 0)
         throw new IllegalArgumentException ("beta = 0");
       if (u >= 1.0)
           return Double.POSITIVE_INFINITY;
       if (u <= 0.0)
           return Double.NEGATIVE_INFINITY;
       if (beta > 0.)
          return delta - Math.log (-Math.log (u))*beta;
       else
          return delta - Math.log (-Math.log1p(-u))*beta;
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#946;</I>, <I>&#948;</I>)</SPAN> of the Gumbel distribution,
    *    <SPAN  CLASS="textit">assuming that <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN></SPAN>, and
    *    using the maximum likelihood method with the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&delta;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 1)
         throw new IllegalArgumentException ("n <= 1");
      int i;
      double par[] = new double[2];

      double xmin = Double.MAX_VALUE;
      double sum = 0;
      for (i = 0; i < n; i++) {
         sum += x[i];
         if (x[i] < xmin)
            xmin = x[i];
      }
      double mean = sum / (double) n;

      sum = 0;
      for (i = 0; i < n; i++)
         sum += (x[i] - mean) * (x[i] - mean);
      double variance = sum / (n - 1.0);

      FunctionPlus func = new FunctionPlus (x, n, mean, xmin);

      double lam = 1.0 / (0.7797*Math.sqrt (variance));
      final double EPS = 0.02;
      double a = (1.0 - EPS)*lam - 5.0;
      if (a <= 0)
         a = 1e-15;
      double b = (1.0 + EPS)*lam + 5.0;
      lam = RootFinder.brentDekker (a, b, func, 1e-8);
      par[0] = 1.0 / lam;

      sum = 0;
      for (i = 0; i < n; i++)
           sum += Math.exp (-(x[i] - xmin) * lam);
      par[1] = xmin - Math.log (sum/n) / lam;
      return par;
   }


   /**
    * Similar to {@link #getMLE getMLE}, but <SPAN  CLASS="textit">for the case <SPAN CLASS="MATH"><I>&#946;</I> &lt; 0</SPAN></SPAN>.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&delta;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLEmin (double[] x, int n) {
      if (n <= 1)
         throw new IllegalArgumentException ("n <= 1");

      int i;
      double par[] = new double[2];
      double xmax = -Double.MAX_VALUE;
      double sum = 0.0;
      for (i = 0; i < n; i++) {
         sum += x[i];
         if (x[i] > xmax)
            xmax = x[i];
      }
      double mean = sum / (double) n;

      sum = 0.0;
      for (i = 0; i < n; i++)
         sum += (x[i] - mean) * (x[i] - mean);
      double variance = sum / (n - 1.0);

      FunctionMinus func = new FunctionMinus (x, n, mean, xmax);

      double lam = -1.0 / (0.7797*Math.sqrt (variance));
      final double EPS = 0.02;
      double a = (1.0 + EPS)*lam - 2.0;
      double b = (1.0 - EPS)*lam + 2.0;
      if (b >= 0)
         b = -1e-15;
      lam = RootFinder.brentDekker (a, b, func, 1e-12);
      par[0] = 1.0 / lam;

      sum = 0.0;
      for (i = 0; i < n; i++)
         sum += Math.exp ((xmax - x[i]) * lam);
      par[0] = 1.0 / lam;
      par[1] = xmax - Math.log (sum / n) / lam;

      return par;
   }


   /**
    * Creates a new instance of an Gumbel distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> estimated using the maximum likelihood method based
    *     on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>,  <SPAN  CLASS="textit">assuming that <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN></SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static GumbelDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new GumbelDist (parameters[0], parameters[1]);
   }


   /**
    * Similar to {@link #getInstanceFromMLE getInstanceFromMLE}, but <SPAN  CLASS="textit">for the case <SPAN CLASS="MATH"><I>&#946;</I> &lt; 0</SPAN></SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static GumbelDist getInstanceFromMLEmin (double[] x, int n) {
      double parameters[] = getMLEmin (x, n);
      return new GumbelDist (parameters[0], parameters[1]);
   }


   /**
    * Returns the mean,  
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#948;</I> + <I>&#947;&#946;</I></SPAN>,
    *    of the Gumbel distribution with parameters <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>,
    *   where 
    * <SPAN CLASS="MATH"><I>&#947;</I> = 0.5772156649015329</SPAN> is the Euler-Mascheroni constant.
    * 
    * @return the mean of the Extreme Value distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#948;</I> + <I>&#947;</I>*<I>&#946;</I></SPAN>
    * 
    */
   public static double getMean (double beta, double delta) {
     if (beta == 0.0)
         throw new IllegalArgumentException ("beta = 0");

      return delta + Num.EULER * beta;
   }


   /**
    * Returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#960;</I><SUP>2</SUP><I>&#946;</I><SUP>2</SUP>/6</SPAN> of the Gumbel distribution with parameters  <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the variance of the Gumbel distribution 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = ()<I>&#960;&#946;</I>)<SUP>2</SUP>/6</SPAN>
    * 
    */
   public static double getVariance (double beta, double delta) {
     if (beta == 0.0)
         throw new IllegalArgumentException ("beta = 0");

      return Math.PI * Math.PI * beta * beta / 6.0;
   }


   /**
    * Returns the standard deviation
    *    of the Gumbel distribution with parameters <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the standard deviation of the Gumbel distribution
    * 
    */
   public static double getStandardDeviation (double beta, double delta) {
     if (beta == 0.0)
         throw new IllegalArgumentException ("beta = 0");

      return  Math.sqrt(getVariance (beta, delta));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
    * 
    */
   public double getDelta() {
      return delta;
   }



   /**
    * Sets the parameters  <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
    * 
    */
   public void setParams (double beta, double delta) {
     if (beta == 0)
         throw new IllegalArgumentException ("beta = 0");
      this.delta  = delta;
      this.beta = beta;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {beta, delta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : beta = " + beta + ", delta = " + delta;
   }

}
