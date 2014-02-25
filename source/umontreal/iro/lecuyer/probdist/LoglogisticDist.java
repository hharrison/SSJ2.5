

/*
 * Class:        LoglogisticDist
 * Description:  log-logistic distribution
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
import umontreal.iro.lecuyer.util.Misc;
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for the
 * <EM>Log-Logistic</EM> distribution with shape parameter 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>
 * and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>&#945;</I>(<I>x</I>/<I>&#946;</I>)<SUP><I>&#945;</I>-1</SUP>)/(<I>&#946;</I>[1 + (<I>x</I>/<I>&#946;</I>)<SUP><I>&#945;</I></SUP>]<SUP>2</SUP>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0
 * </DIV><P></P>
 * and its distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1/(1 + (<I>x</I>/<I>&#946;</I>)<SUP>-<I>&#945;</I></SUP>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * The complementary distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * bar(F)(<I>x</I>) = 1/(1 + (<I>x</I>/<I>&#946;</I>)<SUP><I>&#945;</I></SUP>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * 
 */
public class LoglogisticDist extends ContinuousDistribution {
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
         if ((p[1] <= 0.0) || (p[2] <= 0.0))
            return 1e200;

         double sum = 0.0;
         for (int i = 0; i < n; i++) {
	    double tmp = density (p[1], p[2], xi[i]);
            if (tmp > 0.0)
	        sum -= Math.log (tmp);
            else
	        sum += 709.0;    // log (Double.MIN_VALUE)
	 }
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
    * Constructs a log-logistic distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public LoglogisticDist (double alpha, double beta) {
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

   public double inverseF (double u) {
      return inverseF (alpha, beta, u);
   }

   public double getMean() {
      return getMean (alpha, beta);
   }

   public double getVariance() {
      return getVariance (alpha, beta);
   }

   public double getStandardDeviation() {
      return getStandardDeviation (alpha, beta);
   }

   /**
    * Computes the density function
    *   for a log-logisitic distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
    *   and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double density (double alpha, double beta, double x) {
      double denominateur;

      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (x <= 0.0 || x >= Double.MAX_VALUE / 2.0)
         return 0.0;

      if (x <= beta) {
         double v = Math.pow (x / beta, alpha);
         denominateur = 1.0 + v;
         denominateur *= denominateur * beta;
         return alpha * v * beta / (x * denominateur);
      } else {
         double v = Math.pow (beta / x, alpha);
         denominateur = 1.0 + v;
         denominateur *= denominateur * beta;
         return alpha * v * beta / (x * denominateur);
      }
   }


   /**
    * Computes the distribution function of the
    *    log-logistic distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double cdf (double alpha, double beta, double x) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (x <= 0.0)
         return 0.0;
      if (x >= Double.MAX_VALUE / 2.0)
         return 1.0;
      double z = x/beta;
      if (z >= 1.0)
         return 1.0 / (1.0 + Math.pow (1.0/z, alpha));
      double v = Math.pow (z, alpha);
      return v/(v + 1.0);
   }


   /**
    * Computes the complementary distribution function
    *    of the log-logistic distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double barF (double alpha, double beta, double x) {
      double power;

      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (x <= 0.0)
         return 1.0;
      if (x >= Double.MAX_VALUE / 2.0)
         return 0.0;

      double z = x/beta;
      if (z <= 1.0)
         return 1.0 / (1.0 + Math.pow (z, alpha));
      double v = Math.pow (1.0/z, alpha);
      return v/(v + 1.0);
   }


   /**
    * Computes the inverse of the log-logistic distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double inverseF (double alpha, double beta, double u) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in (0, 1]");
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;
      if (u <= 0.0)
         return 0.0;

      if (u <= 0.5)
         return (beta * Math.pow (u / (1.0 - u), 1.0 / alpha));
      else
         return (beta / Math.pow ((1.0 - u)/ u, 1.0 / alpha));
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#946;</I>)</SPAN> of the log-logistic distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
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

      param[2] = EmpiricalDist.getMedian (x, n);

      if (param[2] < 0) throw new IllegalArgumentException ("median < 0");
      if (param[2] <= 0) param[2] = 1.0;

      int m = Math.round ((float) n / 4.0f);
      double q1 = Misc.quickSelect (x, n, m);

      if (q1 < 0) throw new IllegalArgumentException ("x[i] < 0");
      if (q1 > 0)
          param[1] = Math.log (3) / (Math.log(param[2]) - Math.log(q1));
      else
          param[1] = 1.0;

      Uncmin_f77.optif0_f77 (2, param, system, xpls, fpls, gpls, itrcmd, a, udiag);

      for (int i = 0; i < 2; i++)
         parameters[i] = xpls[i+1];

      return parameters;
   }


   /**
    * Creates a new instance of a log-logistic distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> estimated using the maximum likelihood method based on
    *    the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static LoglogisticDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new LoglogisticDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean
    * of the log-logistic distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the mean of the log-logistic distribution
    *    
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#946;&#952;</I>&nbsp;cosec(<I>&#952;</I>), where <I>&#952;</I> = <I>&#960;</I>/<I>&#945;</I></SPAN>
    * 
    */
   public static double getMean (double alpha, double beta) {
      double theta;

      if (alpha <= 1.0)
         throw new IllegalArgumentException ("alpha <= 1");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      theta = Math.PI / alpha;

      return (beta * theta / Math.sin (theta));
   }


   /**
    * Computes and returns the variance
    * of the log-logistic distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the variance of the log-logistic distribution
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#946;</I><SUP>2</SUP><I>&#952;</I>(2cosec(2<I>&#952;</I>) - <I>&#952;</I>[cosec(<I>&#952;</I>)]<SUP>2</SUP>), where <I>&#952;</I> = <I>&#960;</I>/<I>&#945;</I></SPAN>
    * 
    */
   public static double getVariance (double alpha, double beta) {
      double theta;

      if (alpha <= 2.0)
         throw new IllegalArgumentException ("alpha <= 2");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      theta = Math.PI / alpha;

      return (beta * beta * theta * ((2.0 / Math.sin (2.0 * theta)) -
                   (theta / (Math.sin (theta) * Math.sin (theta)))));
   }


   /**
    * Computes and returns the standard deviation of the log-logistic
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the standard deviation of the log-logistic distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double beta) {
      return Math.sqrt (getVariance (alpha, beta));
   }


   /**
    * Return the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public void setParams (double alpha, double beta) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      this.alpha  = alpha;
      this.beta = beta;
      supportA = 0.0;
   }


   /**
    * Return a table containing the parameters of the current distribution.
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
