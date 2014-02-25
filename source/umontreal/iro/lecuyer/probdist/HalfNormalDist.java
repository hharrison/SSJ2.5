

/*
 * Class:        HalfNormalDist
 * Description:  half-normal distribution
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

package  umontreal.iro.lecuyer.probdist;

import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.functions.MathFunction;
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for the <EM>half-normal</EM>
 * distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and 
 * <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = ((2/&pi;)<SUP>1/2</SUP>/<I>&#963;</I>)<I>e</I><SUP>-(x-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#963;</I><SUP>2</SUP>)</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; = <I>&#956;</I>,
 * </DIV><P></P>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &lt; <I>&#956;</I>,
 * </DIV><P></P>
 * 
 */
public class HalfNormalDist extends ContinuousDistribution {
   protected double mu;
   protected double sigma;
   protected double C1;



   /**
    * Constructs a <TT>HalfNormalDist</TT> object with parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>
    *      <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>.
    * 
    */
   public HalfNormalDist (double mu, double sigma) {
      setParams (mu, sigma);
   }


   public double density (double x) {
      final double z = (x-mu)/sigma;
      if (z < 0.0)
         return 0.0;
      return C1 * Math.exp(-z*z/2.0);
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
      return HalfNormalDist.getMean (mu, sigma);
   }

   public double getVariance() {
      return HalfNormalDist.getVariance (mu, sigma);
   }

   public double getStandardDeviation() {
      return HalfNormalDist.getStandardDeviation (mu, sigma);
   }

   /**
    * Computes the density function of the <EM>half-normal</EM> distribution.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param x the value at which the density is evaluated
    * 
    *    @return returns the density function
    * 
    */
   public static double density (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      final double Z = (x-mu)/sigma;
      if (Z < 0.0) return 0.0;
      return Math.sqrt(2.0/Math.PI) / sigma * Math.exp(-Z*Z/2.0);
   }


   /**
    * Computes the distribution function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param x the value at which the distribution is evaluated
    * 
    *    @return returns the cdf function
    * 
    */
   public static double cdf (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      final double Z = (x-mu)/sigma;
      if (Z <= 0.0) return 0.0;
      return Num.erf(Z/Num.RAC2);
   }


   /**
    * Computes the complementary distribution function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param x the value at which the complementary distribution is evaluated
    * 
    *    @return returns the complementary distribution function
    * 
    */
   public static double barF (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      final double Z = (x-mu)/sigma;
      if (Z <= 0.0) return 1.0;
      return Num.erfc(Z/Num.RAC2);
   }


   /**
    * Computes the inverse of the distribution function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param u the value at which the inverse distribution is evaluated
    * 
    *    @return returns the inverse distribution function
    * 
    */
   public static double inverseF (double mu, double sigma, double u) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (u > 1.0 || u < 0.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= 0.0) return mu;
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;

      final double Z = Num.RAC2 * Num.erfInv(u);
      return mu + sigma * Z;
   }


   /**
    * Estimates the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of the half-normal distribution
    *    using the maximum likelihood method from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double mu = Double.MAX_VALUE;
      for (int i = 0 ; i < n ; ++i)
         if (x[i] < mu)
            mu = x[i];

      double sigma = 0.0;
      for (int i = 0 ; i < n ; ++i)
         sigma += (x[i]-mu)*(x[i]-mu);

      double[] parametres = new double [2];
      parametres[0] = mu;
      parametres[1] = Math.sqrt(sigma/n);
      return parametres;
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of the half-normal distribution using the
    *    maximum likelihood method from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN> and the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT>. The estimate is
    *    returned in a one-element array: [<SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameter
    * 
    *    @param mu the parameter mu
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH"><I>&#963;</I></SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n, double mu) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double sigma = 0.0;
      for (int i = 0 ; i < n ; ++i)
         sigma += (x[i]-mu)*(x[i]-mu);

      double[] parametres = new double [1];
      parametres[0] = Math.sqrt(sigma/n);
      return parametres;
   }


   /**
    * Computes and returns the mean
    * 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I> + <I>&#963;</I>(2 / &pi;)<SUP>1/2</SUP>.</SPAN>
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return returns the mean
    * 
    */
   public static double getMean (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      return mu + sigma*Math.sqrt(2.0/Math.PI);
   }


   /**
    * Computes and returns the variance
    * 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = (1 - 2/<I>&#960;</I>)<I>&#963;</I><SUP>2</SUP>.</SPAN>
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return returns the variance
    * 
    */
   public static double getVariance (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      return (1.0 - 2.0/Math.PI)*sigma*sigma;
   }


   /**
    * Computes the standard deviation of the half-normal distribution with
    *    parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return returns the standard deviation
    * 
    */
   public static double getStandardDeviation (double mu, double sigma)  {
      return Math.sqrt (HalfNormalDist.getVariance (mu, sigma));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    *   
    * @return returns the parameter mu
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    *   
    * @return returns the parameter sigma
    * 
    */
   public double getSigma() {
      return sigma;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    * 
    */
   public void setParams (double mu, double sigma)  {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      this.mu = mu;
      this.sigma = sigma;
      C1 = Math.sqrt(2.0/Math.PI) / sigma;
    } 


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * @return returns the parameters [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>]
    * 
    */
   public double[] getParams () {
      double[] retour = {mu, sigma};
      return retour;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    * @return returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString () {
      return getClass().getSimpleName() + " : mu = " + mu + ", sigma = " + sigma;
   }

}
