

/*
 * Class:        InverseGaussianDist
 * Description:  inverse Gaussian distribution
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

import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.functions.MathFunction;
 

/**
 * Extends the class {@link ContinuousDistribution} for 
 * the <EM>inverse Gaussian</EM> distribution with location parameter
 * <SPAN CLASS="MATH"><I>&#956;</I> &gt; 0</SPAN> and scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (&lambda;/ (2&pi;x^3))<SUP>1/2</SUP><I>e</I><SUP>-<I>&#955;</I>(x-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#956;</I><SUP>2</SUP>x)</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>&#934;</I>((&lambda;/x)<SUP>1/2</SUP>(<I>x</I>/<I>&#956;</I> -1)) + <I>e</I><SUP>2<I>&#955;</I>/<I>&#956;</I></SUP><I>&#934;</I>(- (&lambda;/x)<SUP>1/2</SUP>(<I>x</I>/<I>&#956;</I> + 1)),
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#934;</I></SPAN> is the standard normal distribution function.
 * 
 * <P>
 * The non-static versions of the methods <TT>cdf</TT>, <TT>barF</TT>, 
 * and <TT>inverseF</TT> call the static version of the same name.
 * 
 */
public class InverseGaussianDist extends ContinuousDistribution {
   protected double mu;
   protected double lambda;

   private static class Function implements MathFunction {
      protected double mu;
      protected double lambda;
      protected double u;

      public Function (double mu, double lambda, double u) {
         this.mu = mu;
         this.lambda = lambda;
         this.u = u;
      }

      public double evaluate (double x) {
         return u - cdf(mu, lambda, x);
      }
   }



   /**
    * Constructs the <EM>inverse Gaussian</EM> distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    */
   public InverseGaussianDist (double mu, double lambda) {
      setParams (mu, lambda);
   }


   public double density (double x) {
      return density (mu, lambda, x);
   }

   public double cdf (double x) {
      return cdf (mu, lambda, x);
   }

   public double barF (double x) {
      return barF (mu, lambda, x);
   }

   public double inverseF (double u) {
      return inverseF (mu, lambda, u);
   }

   public double getMean() {
      return getMean (mu, lambda);
   }

   public double getVariance() {
      return getVariance (mu, lambda);
   }

   public double getStandardDeviation() {
      return getStandardDeviation (mu, lambda);
   }

   /**
    * Computes the density function for the
    *      inverse gaussian distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>,
    *      evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double density (double mu, double lambda, double x) {
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (x <= 0.0)
         return 0.0;

      double sqrtX = Math.sqrt (x);

      return (Math.sqrt (lambda / (2 * Math.PI)) / (sqrtX * sqrtX * sqrtX) *
              Math.exp (-lambda * (x - 2 * mu + (mu * mu / x)) / (2 * mu * mu)));
   }


   /**
    * Computes the distribution function
    *    of the inverse gaussian distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and
    *    <SPAN CLASS="MATH"><I>&#955;</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double cdf (double mu, double lambda, double x) {
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (x <= 0.0)
         return 0.0;
      double temp = Math.sqrt (lambda / x);
      double z = temp * (x / mu - 1.0);
      double w = temp * (x / mu + 1.0);

      // C'est bien un + dans    exp (2 * lambda / mu)
      return (NormalDist.cdf01 (z) +
              Math.exp (2 * lambda / mu) * NormalDist.cdf01 (-w));
   }

   
   /**
    * Computes the complementary distribution function of the inverse gaussian distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double barF (double mu, double lambda, double x) {
      return 1.0 - cdf (mu, lambda, x);
   }

   
   /**
    * Computes the inverse of the inverse gaussian distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    */
   public static double inverseF (double mu, double lambda, double u) {
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u must be in [0,1]");
      if (u == 1.0)
         return Double.POSITIVE_INFINITY;
      if (u == 0.0)
         return 0.0;

      Function f = new Function (mu, lambda, u);

      // Find interval containing root = x*
      double sig = getStandardDeviation(mu, lambda);
      double x0 = 0.0;
      double x = mu;
      double v = cdf(mu, lambda, x);
      while (v < u) {
         x0 = x;
         x += 3.0*sig;
         v = cdf(mu, lambda, x);
      }

      return RootFinder.brentDekker (x0, x, f, 1e-12);
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#956;</I>, <I>&#955;</I>)</SPAN> of the inverse gaussian distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(&mu;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&lambda;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[];
      parameters = new double[2];
      double sum = 0;
      for (int i = 0; i < n; i++) {
         sum += x[i];   
      }
      parameters[0] = sum / (double) n;

      sum = 0;
      for (int i = 0; i < n; i++) {
         sum += ((1.0 / (double) x[i]) - (1.0 / parameters[0]));
      }
      parameters[1] = (double) n / (double) sum;

      return parameters;
   }


   /**
    * Creates a new instance of an inverse gaussian distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> estimated using the maximum likelihood method based on
    *    the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static InverseGaussianDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new InverseGaussianDist (parameters[0], parameters[1]);
   }


   /**
    * Returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I></SPAN> of the
    *    inverse gaussian distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the mean of the inverse gaussian distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I></SPAN>
    * 
    */
   public static double getMean (double mu, double lambda) {
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return mu;      
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#956;</I><SUP>3</SUP>/<I>&#955;</I></SPAN> of
    *    the inverse gaussian distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the variance of the inverse gaussian distribution
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#956;</I><SUP>3</SUP>/<I>&#955;</I></SPAN>
    * 
    */
   public static double getVariance (double mu, double lambda) {
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return (mu * mu * mu / lambda);
   }


   /**
    * Computes and returns the standard deviation
    *    of the inverse gaussian distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the standard deviation of the inverse gaussian distribution
    * 
    */
   public static double getStandardDeviation (double mu, double lambda) {
      return Math.sqrt (getVariance (mu, lambda));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   public void setParams (double mu, double lambda) {
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      this.mu = mu;
      this.lambda = lambda;
      supportA = 0.0;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {mu, lambda};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : mu = " + mu + ", lambda = " + lambda;
   }

}
