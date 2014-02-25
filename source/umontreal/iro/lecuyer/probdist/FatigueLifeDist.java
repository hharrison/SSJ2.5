

/*
 * Class:        FatigueLifeDist
 * Description:  fatigue life distribution
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
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for 
 * the <EM>fatigue life</EM> distribution with location
 * parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and shape
 * parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = [(((<I>x</I> - <I>&#956;</I>)/<I>&#946;</I>)<SUP>1/2</SUP> + (<I>&#946;</I>/(<I>x</I> - <I>&#956;</I>))<SUP>1/2</SUP>)/(2<I>&#947;</I>(<I>x</I> - <I>&#956;</I>))]<I>&#966;</I>((((<I>x</I> - <I>&#956;</I>)/<I>&#946;</I>)<SUP>1/2</SUP> - (<I>&#946;</I>/(<I>x</I> - <I>&#956;</I>))<SUP>1/2</SUP>)/<I>&#947;</I>),&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; <I>&#956;</I>,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#966;</I></SPAN> is the probability density of the standard normal distribution.
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>&#934;</I>((((<I>x</I> - <I>&#956;</I>)/<I>&#946;</I>)<SUP>1/2</SUP> - (<I>&#946;</I>/(<I>x</I> - <I>&#956;</I>))<SUP>1/2</SUP>)/<I>&#947;</I>),&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; <I>&#956;</I>,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#934;</I></SPAN> is the standard normal distribution function.
 * Restrictions: <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>, 
 * <SPAN CLASS="MATH"><I>&#947;</I> &gt; 0</SPAN>.
 * 
 * <P>
 * The non-static versions of the methods <TT>cdf</TT>, <TT>barF</TT>, 
 * and <TT>inverseF</TT> call the static version of the same name.
 * 
 */
public class FatigueLifeDist extends ContinuousDistribution {
   protected double mu;
   protected double beta;
   protected double gamma;

   private static class Optim implements Uncmin_methods
   {
      private int n;
      private double[] xi;
      private double mu;

      public Optim (double[] x, int n, double min)
      {
         this.n = n;
         this.mu = min;
         this.xi = new double[n];
         System.arraycopy (x, 0, this.xi, 0, n);
      }

      public double f_to_minimize (double[] p)
      {
         double sum = 0.0;

         if ((p[1] <= 0.0) || (p[2] <= 0.0))
            return 1e200;
      
         for (int i = 0; i < n; i++)
            sum -= Math.log (density (mu, p[1], p[2], xi[i]));

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
    * Constructs a fatigue life distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    */
   public FatigueLifeDist (double mu, double beta, double gamma) {
      setParams (mu, beta, gamma);
   }


   public double density (double x) {
      return FatigueLifeDist.density (mu, beta, gamma, x);
   }

   public double cdf (double x) {
      return FatigueLifeDist.cdf (mu, beta, gamma, x);
   }

   public double barF (double x) {
      return FatigueLifeDist.barF (mu, beta, gamma, x);
   }

   public double inverseF (double u) {
      return FatigueLifeDist.inverseF (mu, beta, gamma, u);
   }

   public double getMean() {
      return FatigueLifeDist.getMean (mu, beta, gamma);      
   }

   public double getVariance() {
      return FatigueLifeDist.getVariance (mu, beta, gamma);      
   }

   public double getStandardDeviation() {
      return FatigueLifeDist.getStandardDeviation (mu, beta, gamma);      
   }

   /**
    * Computes the density for the
    *    fatigue life distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    */
   public static double density (double mu, double beta, double gamma,
                                 double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      if (x <= mu)
         return 0.0;
      double y;
      y = (Math.sqrt ((x - mu) / beta) - Math.sqrt (beta / (x - mu))) / gamma;

      return (((Math.sqrt ((x - mu) / beta) + Math.sqrt (beta / (x - mu))) /
              (2 * gamma * (x - mu))) * NormalDist.density (0.0, 1.0, y));
   }


   /**
    * Computes the fatigue life distribution
    *    function with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    */
   public static double cdf (double mu, double beta, double gamma, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      if (x <= mu)
         return 0.0;

      return NormalDist.cdf01 ((Math.sqrt ((x - mu) / beta) - Math.sqrt (beta / (x - mu))) / gamma);
   }

   
   /**
    * Computes the complementary distribution function of the
    *    fatigue life distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    */
   public static double barF (double mu, double beta, double gamma,
                              double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      if (x <= mu)
         return 1.0;

      return NormalDist.barF01 ((Math.sqrt ((x - mu) / beta) - Math.sqrt (beta / (x - mu))) / gamma);
   }

   
   /**
    * Computes the inverse of the fatigue life distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    */
   public static double inverseF (double mu, double beta, double gamma,
                                  double u) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      if (u > 1.0 || u < 0.0)
          throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= 0.0)    // if u == 0, in fact
          return mu;
      if (u >= 1.0)    // if u == 1, in fact
          return Double.POSITIVE_INFINITY;

      double w = gamma * NormalDist.inverseF01 (u);
      double sqrtZ = 0.5 * (w + Math.sqrt (w * w + 4.0));

      return (mu + sqrtZ * sqrtZ * beta);
   }


   /**
    * Estimates the parameters (<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#947;</I></SPAN>) of the fatigue life
    *    distribution using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a three-element
    *    array, in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#947;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @param mu the location parameter
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&gamma;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n, double mu) {
      double sum = 0.0;
      
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double[] parameters = new double[3];
      double[] xpls = new double[3];
      double[] param = new double[3];
      double[] fpls = new double[3];
      double[] gpls = new double[3];
      int[] itrcmd = new int[2];
      double[][] h = new double[3][3];
      double[] udiag = new double[3];

      Optim system = new Optim (x, n, mu);

      double mean = 0.0;
      for (int i = 0; i < n; i++)
         mean += x[i];
      mean /= (double) n;

      double var = 0.0;
      for (int i = 0; i < n; i++)
         var += (x[i] - mean) * (x[i] - mean);
      var /= (double) n;

      double loc2 = (mean - mu) * (mean - mu);
      double a = 0.25 * (var - 5 * loc2);
      double b = (var - loc2);
      double c = var;

      double delta = b * b - 4.0 * a * c;

      double gamma2 = (- b - Math.sqrt (delta)) / (2.0 * a);
      param[2] = Math.sqrt (gamma2);
      param[1] = (mean - mu) / (1.0 + gamma2 / 2.0);

      Uncmin_f77.optif0_f77 (2, param, system, xpls, fpls, gpls, itrcmd, h, udiag);

      for (int i = 1; i < 3; i++)
         parameters[i] = xpls[i];
      parameters[0] = mu;
      return parameters;
   }


   /**
    * Computes and returns the mean
    *    
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I> + <I>&#946;</I>(1 + <I>&#947;</I><SUP>2</SUP>/2)</SPAN>
    *  of the fatigue life distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * @return the mean of the fatigue life distribution
    * 
    */
   public static double getMean (double mu, double beta, double gamma) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");

      return (mu + beta * (1 + 0.5 * gamma * gamma));
   }


   /**
    * Computes and returns the variance 
    * 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#946;</I><SUP>2</SUP><I>&#947;</I><SUP>2</SUP>(1 + 5<I>&#947;</I><SUP>2</SUP>/4)</SPAN> of the fatigue life distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * @return the variance of the fatigue life distribution
    * 
    */
   public static double getVariance (double mu, double beta, double gamma) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");

      return (beta * beta * gamma * gamma * (1.0 + 5.0/4.0 * gamma * gamma));
   }


   /**
    * Computes and returns the standard deviation
    *    of the fatigue life distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    * @return the standard deviation of the fatigue life distribution
    * 
    */
   public static double getStandardDeviation (double mu, double beta,
                                              double gamma) {
      return Math.sqrt (FatigueLifeDist.getVariance (mu, beta, gamma));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN> of this object.
    * 
    */
   public double getGamma() {
      return gamma;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN> of this object.
    * 
    */
   public void setParams (double mu, double beta, double gamma) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      
      this.mu = mu;
      this.beta = beta;
      this.gamma = gamma;
      supportA = mu;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#947;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {mu, beta, gamma};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : mu = " + mu + ", beta = " + beta + ", gamma = " + gamma;
   }

}
