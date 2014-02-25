

/*
 * Class:        HyperbolicSecantDist
 * Description:  hyperbolic secant distribution
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

import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>hyperbolic secant</EM> distribution with location
 * parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and scale parameter 
 * <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 1/(2<I>&#963;</I>) sech(<I>&#960;</I>/2(<I>x</I> - <I>&#956;</I>)/<I>&#963;</I>)
 * </DIV><P></P>
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 2/<I>&#960;tan</I><SUP>-1</SUP>[<I>exp</I>(<I>&#960;</I>/2(<I>x</I> - <I>&#956;</I>)/<I>&#963;</I>)]
 * </DIV><P></P>
 * 
 * <P>
 * The non-static versions of the methods <TT>cdf</TT>, <TT>barF</TT>,
 * and <TT>inverseF</TT> call the static version of the same name.
 * 
 */
public class HyperbolicSecantDist extends ContinuousDistribution {
   protected double mu;
   protected double sigma;
   private static final double ZLIMB = 500.0;
   private static final double ZLIMS = 50.0;

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

         if (p[2] <= 0.0)
            return 1e200;

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
    * Constructs a hyperbolic secant distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public HyperbolicSecantDist (double mu, double sigma) {
      setParams (mu, sigma);
   }


   public double density (double x) {
      return HyperbolicSecantDist.density (mu, sigma, x);
   }

   public double cdf (double x) {
      return HyperbolicSecantDist.cdf (mu, sigma, x);
   }

   public double barF (double x) {
      return HyperbolicSecantDist.barF (mu, sigma, x);
   }

   public double inverseF (double u) {
      return HyperbolicSecantDist.inverseF (mu, sigma, u);
   }

   public double getMean() {
      return HyperbolicSecantDist.getMean (mu, sigma);
   }

   public double getVariance() {
      return HyperbolicSecantDist.getVariance (mu, sigma);
   }

   public double getStandardDeviation() {
      return HyperbolicSecantDist.getStandardDeviation (mu, sigma);
   }

   /**
    * Computes the density function
    *   for a hyperbolic secant distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public static double density (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      double y = (x - mu) / sigma;
      if (Math.abs(y) >= ZLIMB)
         return 0.0;
      else
         return (1.0 / (Math.cosh (Math.PI * y / 2.0) * 2.0 * sigma));
   }


   /**
    * Computes the distribution function of the hyperbolic secant distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public static double cdf (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      double y = (x - mu) / sigma;
      if (y >= ZLIMS)
         return 1.0;
      else if (y <= -ZLIMB)
      	 return 0.0;
      else
         return (2.0 * Math.atan (Math.exp (Math.PI * y / 2.0))) / Math.PI;
   }


   /**
    * Computes the complementary distribution function of the
    *    hyperbolic secant distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public static double barF (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");

      double y = (x - mu) / sigma;
      if (y >= ZLIMB)
         return 0.0;
      else if (y <= -ZLIMS)
      	 return 1.0;
      else
         return 2.0 / Math.PI * Math.atan (Math.exp (-Math.PI * y / 2.0));
   }


   /**
    * Computes the inverse of the hyperbolic secant distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public static double inverseF (double mu, double sigma, double u) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in [0,1]");

      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;
      else if (u <= 0.0)
         return Double.NEGATIVE_INFINITY;
      else
         return (mu + (2.0 * sigma / Math.PI * Math.log (Math.tan (Math.PI / 2.0 * u))));
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#956;</I>, <I>&#963;</I>)</SPAN> of the hyperbolic secant distribution
    *   using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(&mu;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&sigma;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      double sum;

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

      sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += x[i];
      param[1] = sum / (double) n;

      sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += (x[i] - param[1]) * (x[i] - param[1]);
      param[2] = Math.sqrt (sum / (double) n);

      Uncmin_f77.optif0_f77 (2, param, system, xpls, fpls, gpls, itrcmd, a, udiag);

      for (int i = 0; i < 2; i++)
         parameters[i] = xpls[i+1];

      return parameters;
   }


   /**
    * Creates a new instance of a hyperbolic secant distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> estimated using the maximum likelihood method based on
    *    the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static HyperbolicSecantDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new HyperbolicSecantDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I></SPAN> of the
    *    hyperbolic secant distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @return the mean of the hyperbolic secant distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I></SPAN>
    * 
    */
   public static double getMean (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      return mu;
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#963;</I><SUP>2</SUP></SPAN>
    *    of the hyperbolic secant distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @return the variance of the hyperbolic secant distribution
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#963;</I><SUP>2</SUP></SPAN>
    * 
    */
   public static double getVariance (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");

      return (sigma * sigma);
   }


   /**
    * Computes and returns the standard deviation
    *    of the hyperbolic secant distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @return the standard deviation of the hyperbolic secant distribution
    * 
    */
   public static double getStandardDeviation (double mu, double sigma) {
      return Math.sqrt (HyperbolicSecantDist.getVariance (mu, sigma));
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
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");

      this.mu = mu;
      this.sigma = sigma;
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
