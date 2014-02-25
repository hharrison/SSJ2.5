
/*
 * Class:        WeibullDist
 * Description:  Weibull distribution
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
 * This class extends the class {@link ContinuousDistribution} for
 * the <EM>Weibull</EM> distribution with shape parameter
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>, location parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, and scale parameter
 * 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * The density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#945;&#955;</I><SUP><I>&#945;</I></SUP>(<I>x</I> - <I>&#948;</I>)<SUP><I>&#945;</I>-1</SUP><I>e</I><SUP>-(<I>&#955;</I>(x-<I>&#948;</I>))<SUP><I>&#945;</I></SUP></SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; <I>&#948;</I>.
 * </DIV><P></P>
 * the distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1 - <I>e</I><SUP>-(<I>&#955;</I>(x-<I>&#948;</I>))<SUP><I>&#945;</I></SUP></SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; <I>&#948;</I>,
 * </DIV><P></P>
 * and the inverse distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = (- ln(1 - <I>u</I>))<SUP>1/<I>&#945;</I></SUP>/<I>&#955;</I> + <I>&#948;</I>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I> &lt; 1.
 * </DIV><P></P>
 * 
 */
public class WeibullDist extends ContinuousDistribution {
   private double alpha;
   private double lambda;
   private double delta;

   private static class Function implements MathFunction {
      private int n;
      private double xi[];
      private double lnXi[];
      private double sumLnXi = 0.0;
      private final double LN_EPS = Num.LN_DBL_MIN - Num.LN2;

      public Function (double x[], int n)
      {
         this.n = n;
         this.xi = new double[n];
         this.lnXi = new double[n];

         for (int i = 0; i < n; i++)
         {
            this.xi[i] = x[i];
            if (x[i] > 0.0)
               this.lnXi[i] = Math.log (x[i]);
            else
               this.lnXi[i] = LN_EPS;
            sumLnXi += this.lnXi[i];
         }
      }

      public double evaluate (double x)
      {
         if (x <= 0.0) return 1.0e200;
         double sumXiLnXi = 0.0;
         double sumXi = 0.0;
         double xalpha;

         for (int i = 0; i < n; i++)
         {
            xalpha = Math.pow (this.xi[i], x);
            sumXiLnXi += xalpha * lnXi[i];
            sumXi += xalpha;
         }

         return (x * (n * sumXiLnXi - sumLnXi * sumXi) - n * sumXi);
      }
   }




   /**
    * Constructs a <TT>WeibullDist</TT> object with parameters
    *     <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = 1, and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = 0.
    * 
    */
   public WeibullDist (double alpha) {
      setParams (alpha, 1.0, 0.0);
   }


   /**
    * Constructs a <TT>WeibullDist</TT> object with parameters
    *      <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>,
    *      <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>, and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT>.
    * 
    */
   public WeibullDist (double alpha, double lambda, double delta) {
      setParams (alpha, lambda, delta);
   }


   public double density (double x) {
      return density (alpha, lambda, delta, x);
   }

   public double cdf (double x) {
      return cdf (alpha, lambda, delta, x);
   }

   public double barF (double x) {
      return barF (alpha, lambda, delta, x);
   }

   public double inverseF (double u) {
      return inverseF (alpha, lambda, delta, u);
   }

   public double getMean() {
      return WeibullDist.getMean (alpha, lambda, delta);
   }

   public double getVariance() {
      return WeibullDist.getVariance (alpha, lambda, delta);
   }

   public double getStandardDeviation() {
      return WeibullDist.getStandardDeviation (alpha, lambda, delta);
   }

   /**
    * Computes the density function.
    * 
    */
   public static double density (double alpha, double lambda,
                                 double delta, double x) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0.0)
        throw new IllegalArgumentException ("lambda <= 0");
      if (x <= delta)
         return 0.0;
      double y = Math.log(lambda*(x - delta)) * alpha;
      if (y >= 7.0)
         return 0.0;
      y = Math.exp(y);

      return alpha * (y / (x - delta)) * Math.exp(-y);
   }


   /**
    * Same as <TT>density (alpha, 1, 0, x)</TT>.
    * 
    */
   public static double density (double alpha, double x) {
      return density (alpha, 1.0, 0.0, x);
   }


   /**
    * Computes the distribution function.
    * 
    */
   public static double cdf (double alpha, double lambda,
                             double delta, double x) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0.0)
        throw new IllegalArgumentException ("lambda <= 0");
      if (x <= delta)
         return 0.0;
      if ((lambda*(x - delta) >= XBIG) && (alpha >= 1.0))
         return 1.0;
      double y = Math.log(lambda*(x - delta)) * alpha;
      if (y >= 3.65)
         return 1.0;
      y = Math.exp(y);
      return -Math.expm1 (-y);   // in JDK-1.5
   }


   /**
    * Same as <TT>cdf (alpha, 1, 0, x)</TT>.
    * 
    */
   public static double cdf (double alpha, double x) {
      return cdf (alpha, 1.0, 0.0, x);
   }


   /**
    * Computes  the complementary distribution function.
    * 
    */
   public static double barF (double alpha, double lambda,
                              double delta, double x) {
      if (alpha <= 0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0)
        throw new IllegalArgumentException ("lambda <= 0");
      if (x <= delta)
         return 1.0;
      if (alpha >= 1.0 && x >= Num.DBL_MAX_EXP*2)
         return 0.0;

      double temp = Math.log (lambda*(x - delta)) * alpha;
      if (temp >= Num.DBL_MAX_EXP * Num.LN2)
         return 0.0;
      temp = Math.exp(temp);
      return Math.exp (-temp);
   }


   /**
    * Same as <TT>barF (alpha, 1, 0, x)</TT>.
    * 
    */
   public static double barF (double alpha, double x) {
      return barF (alpha, 1.0, 0.0, x);
   }


   /**
    * Computes  the inverse of the distribution function.
    * 
    */
   public static double inverseF (double alpha, double lambda,
                                  double delta, double u) {
        double t;
        if (alpha <= 0.0)
            throw new IllegalArgumentException ("alpha <= 0");
        if (lambda <= 0.0)
            throw new IllegalArgumentException ("lambda <= 0");

        if (u < 0.0 || u > 1.0)
            throw new IllegalArgumentException ("u not in [0, 1]");
        if (u <= 0.0)
           return 0.0;
        if (u >= 1.0)
           return Double.POSITIVE_INFINITY;

        t = -Math.log1p (-u);
        if (Math.log (t)/Math.log (10) >= alpha*Num.DBL_MAX_10_EXP)
           throw new ArithmeticException
              ("inverse function cannot be positive infinity");

        return Math.pow (t, 1.0/alpha)/lambda + delta;
   }


   /**
    * Same as <TT>inverseF (alpha, 1, 0, x)</TT>.
    * 
    */
   public static double inverseF (double alpha, double x) {
      return inverseF (alpha, 1.0, 0.0, x);
   }

   private static double[] getMaximumLikelihoodEstimate (double[] x, int n,
                                                         double delta) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (delta != 0.0)
         throw new IllegalArgumentException ("delta must be equal to 0");
// Verifier cette fonction si delta != 0.

      final double LN_EPS = Num.LN_DBL_MIN - Num.LN2;
      double sumLn = 0.0;
      double sumLn2 = 0.0;
      double lnxi;
      for (int i = 0; i < n; i++) {
         if (x[i] <= delta)
            lnxi = LN_EPS;
         else
            lnxi = Math.log (x[i]);
         sumLn += lnxi;
         sumLn2 += lnxi * lnxi;
      }

      double alpha0 = Math.sqrt ((double) n / ((6.0 / (Math.PI * Math.PI)) *
                  (sumLn2 - sumLn * sumLn / (double) n)));
      double a = alpha0 - 20.0;
      if (a <= delta)
         a = delta + 1.0e-5;

      double param[] = new double[3];
      param[2] = 0.0;
      Function f = new Function (x, n);
      param[0] = RootFinder.brentDekker (a, alpha0 + 20.0, f, 1e-5);

      double sumXalpha = 0.0;
      for (int i = 0; i < n; i++)
         sumXalpha += Math.pow (x[i], param[0]);
      param[1] = Math.pow ((double) n / sumXalpha, 1.0 / param[0]);

      return param;
   }

   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#955;</I>)</SPAN> of the Weibull  distribution,
    *    assuming that 
    * <SPAN CLASS="MATH"><I>&#948;</I> = 0</SPAN>,
    *     using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameter [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&lambda;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&delta;)</SPAN> = 0]
    * 
    */
   public static double[] getMLE (double[] x, int n)
   {
      return getMaximumLikelihoodEstimate (x, n, 0.0);
   }


   /**
    * Creates a new instance of a Weibull distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>,
    *    <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and 
    * <SPAN CLASS="MATH"><I>&#948;</I> = 0</SPAN>
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
   public static WeibullDist getInstanceFromMLE (double[] x, int n) {
      double param[] = getMLE (x, n);
      return new WeibullDist (param[0], param[1], param[2]);
   }


   /**
    * Computes and returns the mean
    * of the Weibull distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the mean of the Weibull distribution
    *     
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#948;</I> + <I>&#915;</I>(1 + 1/<I>&#945;</I>)/<I>&#955;</I></SPAN>
    * 
    */
   public static double getMean (double alpha, double lambda, double delta) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0.0)
        throw new IllegalArgumentException ("lambda <= 0");

      return (delta + Math.exp (Num.lnGamma(1.0 + 1.0 / alpha)) / lambda);
   }


   /**
    * Computes and returns the variance
    * of the Weibull distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the variance of the Weibull distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = 1/<I>&#955;</I><SUP>2</SUP>| <I>&#915;</I>(2/<I>&#945;</I> +1) - <I>&#915;</I><SUP>2</SUP>(1/<I>&#945;</I> + 1)|</SPAN>
    * 
    */
   public static double getVariance (double alpha, double lambda,
                                     double delta) {
      double gAlpha;

      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0.0)
        throw new IllegalArgumentException ("lambda <= 0");

      gAlpha = Math.exp (Num.lnGamma (1.0 / alpha + 1.0));

      return (Math.abs (Math.exp (Num.lnGamma(2 / alpha + 1)) - gAlpha * gAlpha) / (lambda * lambda));
   }


   /**
    * Computes and returns the standard deviation
    *    of the Weibull distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the standard deviation of the Weibull distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double lambda,
                                              double delta) {
      return Math.sqrt (WeibullDist.getVariance (alpha, lambda, delta));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    */
   public double getDelta() {
      return delta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> for this
    *    object.
    * 
    */
   public void setParams (double alpha, double lambda, double delta) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0.0)
        throw new IllegalArgumentException ("lambda <= 0");

      this.alpha  = alpha;
      this.lambda = lambda;
      this.delta  = delta;
      supportA = delta;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha, lambda, delta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : alpha = " + alpha + ", lambda = " + lambda + ", delta = " + delta;
   }

}
