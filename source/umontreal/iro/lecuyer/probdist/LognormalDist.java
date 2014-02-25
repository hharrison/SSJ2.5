

/*
 * Class:        LognormalDist
 * Description:  lognormal distribution
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

import umontreal.iro.lecuyer.util.*;

/**
 * Extends the class {@link ContinuousDistribution} for the
 * <EM>lognormal</EM> distribution. It has scale
 * parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and shape parameter 
 * <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * The density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = ((2&pi;)<SUP>1/2</SUP><I>&#963;x</I>)<SUP>-1</SUP><I>e</I><SUP>-(ln(x)-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#963;</I><SUP>2</SUP>)</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * and 0 elsewhere.  The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>&#934;</I>((ln(<I>x</I>)-<I>&#956;</I>)/<I>&#963;</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#934;</I></SPAN> is the standard normal distribution function.
 * Its inverse is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>e</I><SUP><I>&#956;</I>+<I>&#963;&#934;</I><SUP>-1</SUP>(u)</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I> &lt; 1.
 * </DIV><P></P>
 * If <SPAN CLASS="MATH">ln(<I>Y</I>)</SPAN> has a <EM>normal</EM> distribution, then <SPAN CLASS="MATH"><I>Y</I></SPAN> has a
 * <EM>lognormal</EM> distribution with the same parameters.
 * 
 * <P>
 * This class relies on the methods
 *   {@link NormalDist#cdf01 NormalDist.cdf01} and
 *  {@link NormalDist#inverseF01 NormalDist.inverseF01}
 * of {@link NormalDist} to approximate <SPAN CLASS="MATH"><I>&#934;</I></SPAN> and <SPAN CLASS="MATH"><I>&#934;</I><SUP>-1</SUP></SPAN>.
 * 
 */
public class LognormalDist extends ContinuousDistribution {
   private double mu;
   private double sigma; 


   /**
    * Constructs a <TT>LognormalDist</TT> object with default
    *    parameters <SPAN CLASS="MATH"><I>&#956;</I> = 0</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#963;</I> = 1</SPAN>.
    * 
    */
   public LognormalDist() {
      setParams (0.0, 1.0);
   }


   /**
    * Constructs a <TT>LognormalDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> = <TT>sigma</TT>.
    * 
    */
   public LognormalDist (double mu, double sigma) {
      setParams (mu, sigma);
   }


   public double density (double x) {
      return density (mu, sigma, x);
   }

   public double cdf (double x) {
      return cdf (mu, sigma, x);
   }

   public double barF (double x) {
      return barF (mu, sigma, x);
   }

   public double inverseF (double u) {
      return inverseF (mu, sigma, u);
   }

   public double getMean() {
      return LognormalDist.getMean (mu, sigma);
   }

   public double getVariance() {
      return LognormalDist.getVariance (mu, sigma);
   }

   public double getStandardDeviation() {
      return LognormalDist.getStandardDeviation (mu, sigma);
   }

   /**
    * Computes the lognormal density function
    *   <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    */
   public static double density (double mu, double sigma, double x) {
      if (sigma <= 0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (x <= 0)
         return 0;
      double diff = Math.log (x) - mu;
      return Math.exp (-diff*diff/(2*sigma*sigma))/
              (Math.sqrt (2*Math.PI)*sigma*x);
   }


   /**
    * Computes the lognormal distribution function, using
    *  {@link NormalDist#cdf01 cdf01}.
    * 
    */
   public static double cdf (double mu, double sigma, double x) {
      if (sigma <= 0.0)
        throw new IllegalArgumentException ("sigma  <= 0");
      if (x <= 0.0)
         return 0.0;
      return NormalDist.cdf01 ((Math.log (x) - mu)/sigma);
   }


   /**
    * Computes the lognormal complementary distribution function 
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>)</SPAN>,
    *   using {@link NormalDist#barF01 NormalDist.barF01}.
    * 
    */
   public static double barF (double mu, double sigma, double x) {
      if (sigma <= 0.0)
        throw new IllegalArgumentException ("sigma  <= 0");
      if (x <= 0.0)
         return 1.0;
      return NormalDist.barF01 ((Math.log (x) - mu)/sigma);
   }


   /**
    * Computes the inverse of the lognormal distribution function,
    *   using {@link NormalDist#inverseF01 NormalDist.inverseF01}.
    * 
    */
   public static double inverseF (double mu, double sigma, double u) {
        double t, v;

        if (sigma <= 0.0)
            throw new IllegalArgumentException ("sigma  <= 0");

        if (u > 1.0 || u < 0.0)
            throw new IllegalArgumentException ("u not in [0,1]");

        if (Num.DBL_EPSILON >= 1.0 - u)
            return Double.POSITIVE_INFINITY;

        if (u <= 0.0)
            return 0.0;

        t = NormalDist.inverseF01 (u);
        v = mu + sigma * t;

        if ((t >= XBIG) || (v >= Num.DBL_MAX_EXP * Num.LN2))
            return Double.POSITIVE_INFINITY;
        if ((t <= -XBIG) || (v <= -Num.DBL_MAX_EXP*Num.LN2))
            return 0.0;

        return Math.exp (v);
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#956;</I>, <I>&#963;</I>)</SPAN> of the lognormal distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(&mu;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&sigma;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      final double LN_EPS = Num.LN_DBL_MIN - Num.LN2;
      double parameters[];
      parameters = new double[2];
      double sum = 0.0;
      for (int i = 0; i < n; i++) {
         if (x[i] > 0.0)
            sum += Math.log (x[i]);
         else
            sum += LN_EPS;       // log(DBL_MIN / 2)
      }
      parameters[0] = sum / n;

      double temp;
      sum = 0.0;
      for (int i = 0; i < n; i++) {
         if (x[i] > 0.0)
            temp = Math.log (x[i]) - parameters[0];
         else
            temp = LN_EPS - parameters[0];
         sum += temp * temp;
      }
      parameters[1] = Math.sqrt (sum / n);

      return parameters;
   }


   /**
    * Creates a new instance of a lognormal distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>
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
   public static LognormalDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new LognormalDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>e</I><SUP><I>&#956;</I>+<I>&#963;</I><SUP>2</SUP>/2</SUP></SPAN>
    *    of the lognormal distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @return the mean of the lognormal distribution
    * 
    */
   public static double getMean (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");

      return (Math.exp(mu + (sigma * sigma) / 2.0));
   }


   /**
    * Computes and returns the variance
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>e</I><SUP>2<I>&#956;</I>+<I>&#963;</I><SUP>2</SUP></SUP>(<I>e</I><SUP><I>&#963;</I><SUP>2</SUP></SUP> - 1)</SPAN>
    *    of the lognormal distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @return the variance of the lognormal distribution
    * 
    */
   public static double getVariance (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");

      return (Math.exp(2.0 * mu + sigma * sigma) * (Math.exp(sigma * sigma) - 1.0));
   }


   /**
    * Computes and returns the standard deviation
    *    of the lognormal distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @return the standard deviation of the lognormal distribution
    * 
    */
   public static double getStandardDeviation (double mu, double sigma) {
      return Math.sqrt (LognormalDist.getVariance (mu, sigma));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    * 
    */
   public double getSigma() {
      return sigma;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    * 
    */
   public void setParams (double mu, double sigma) {
      if (sigma <= 0)
         throw new IllegalArgumentException ("sigma <= 0");
      this.mu = mu;
      this.sigma = sigma;
      supportA = 0.0;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {mu, sigma};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : mu = " + mu + ", sigma = " + sigma;
   }

}
