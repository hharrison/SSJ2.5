

/*
 * Class:        LogisticDist
 * Description:  logistic distribution
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
 * Extends the class {@link ContinuousDistribution} for the
 * <EM>logistic</EM> distribution.
 * It has location parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
 * and scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * The density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>&#955;e</I><SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP>)/((1 + <I>e</I><SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP>)<SUP>2</SUP>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * and the distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1/[1 + <I>e</I><SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP>]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * For <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>&#945;</I> = 0</SPAN>, one can write
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = (1+tanh(<I>x</I>/2))/2.
 * </DIV><P></P>
 * The inverse distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = ln(<I>u</I>/(1 - <I>u</I>))/<I>&#955;</I> + <I>&#945;</I>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I> &lt; 1.
 * </DIV><P></P>
 * 
 */
public class LogisticDist extends ContinuousDistribution {
   private double alpha;
   private double lambda;

   private static class Optim implements Lmder_fcn
   {
      protected double[] xi;
      protected int n;

      public Optim (double[] x, int n) {
         this.n = n;
         this.xi = new double[n];
         System.arraycopy (x, 0, this.xi, 0, n);
      }

      public void fcn (int m, int n, double[] x, double[] fvec, double[][] fjac, int iflag[])
      {
         if (x[2] <= 0.0) {
             final double BIG = 1.0e100;
             fvec[1] = BIG;
             fvec[2] = BIG;
             fjac[1][1] = BIG;
             fjac[1][2] = 0.0;
             fjac[2][1] = 0.0;
             fjac[2][2] = BIG;
             return;
         }

         double sum;
         double prod;

         if (iflag[1] == 1)
         {
            sum = 0.0;
            for (int i = 0; i < n; i++)
               sum += (1.0 / (1.0 + Math.exp (x[2] * (xi[i] - x[1]))));
            fvec[1] = sum - n / 2.0;

            sum = 0.0;
            for (int i = 0; i < n; i++)
            {
               prod = x[2] * (xi[i] - x[1]);
               sum -= prod * Math.tanh(prod/2.0);
            }
            fvec[2] = sum - n;
         }
         else if (iflag[1] == 2)
         {
            sum = 0.0;
            for (int i = 0; i < n; i++)
            {
               prod = Math.exp (x[2] * (xi[i] - x[1]));
               sum -= x[2] * prod / ((1 + prod) * (1 + prod));
            }
            fjac[1][1] = sum;

            sum = 0.0;
            for (int i = 0; i < n; i++)
            {
               prod = Math.exp (x[2] * (xi[i] - x[1]));
               sum -= (xi[i] - x[1])  * prod / ((1 + prod) * (1 + prod));
            }
            fjac[1][2] = sum;

            sum = 0.0;
            for (int i = 0; i < n; i++)
            {
               prod = Math.exp (x[2] * (xi[i] - x[1]));
               sum -= (x[2] * ((-1.0 + prod) * (1.0 + prod) - (2.0 * (x[2] * (xi[i] - x[1])) * prod))) / ((1.0 + prod) * (1.0 + prod));
            }
            fjac[2][1] = sum;

            sum = 0.0;
            for (int i = 0; i < n; i++)
            {
               prod = Math.exp (x[2] * (xi[i] - x[1]));
               sum -= ((x[1] - xi[1])  * ((-1.0 + prod) * (1.0 + prod) - (2.0 * (x[2] * (xi[i] - x[1])) * prod))) / ((1.0 + prod) * (1.0 + prod));
            }
            fjac[2][2] = sum;
         }
      }
   }



   /**
    * Constructs a <TT>LogisticDist</TT> object with default parameters
    *    
    * <SPAN CLASS="MATH"><I>&#945;</I> = 0</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN>.
    * 
    */
   public LogisticDist() {
      setParams (0.0, 1.0);
   }


   /**
    * Constructs a <TT>LogisticDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>.
    * 
    */
   public LogisticDist (double alpha, double lambda) {
      setParams (alpha, lambda);
   }


   public double density (double x) {
      return density (alpha, lambda, x);
   }

   public double cdf (double x) {
      return cdf (alpha, lambda, x);
   }

   public double barF (double x) {
      return barF (alpha, lambda, x);
   }

   public double inverseF (double u) {
      return inverseF (alpha, lambda, u);
   }

   public double getMean() {
      return LogisticDist.getMean (alpha, lambda);
   }

   public double getVariance() {
      return LogisticDist.getVariance (alpha, lambda);
   }

   public double getStandardDeviation() {
      return LogisticDist.getStandardDeviation (alpha, lambda);
   }

   /**
    * Computes the density function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    */
   public static double density (double alpha, double lambda, double x) {
      if (lambda <= 0)
        throw new IllegalArgumentException ("lambda <= 0");
      double z = lambda * (x - alpha);
      if (z >= -100.0) {
         double v = Math.exp(-z);
         return lambda * v / ((1.0 + v)*(1.0 + v));
      }
      return lambda * Math.exp(z);
   }


   /**
    * Computes the distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double cdf (double alpha, double lambda, double x) {
      if (lambda <= 0)
        throw new IllegalArgumentException ("lambda <= 0");
      double z = lambda * (x - alpha);
      if (z >= -100.0)
         return 1.0 / (1.0 + Math.exp(-z));
      return Math.exp(z);
   }


   /**
    * Computes  the complementary distribution function <SPAN CLASS="MATH">1 - <I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double barF (double alpha, double lambda, double x) {
      if (lambda <= 0)
        throw new IllegalArgumentException ("lambda <= 0");
      double z = lambda * (x - alpha);
      if (z <= 100.0)
         return 1.0 / (1.0 + Math.exp(z));
      return Math.exp( -z);
   }


   /**
    * Computes the inverse distribution function <SPAN CLASS="MATH"><I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN>.
    * 
    */
   public static double inverseF (double alpha, double lambda, double u) {
        if (lambda <= 0)
           throw new IllegalArgumentException ("lambda <= 0");
        if (u < 0.0 || u > 1.0)
           throw new IllegalArgumentException ("u not in [0, 1]");
        if (u >= 1.0)
            return Double.POSITIVE_INFINITY;
        if (u <= 0.0)
            return Double.NEGATIVE_INFINITY;

        return Math.log (u/(1.0 - u))/lambda + alpha;
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#955;</I>)</SPAN> of the logistic distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameter [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&lambda;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += x[i];

      double[] param = new double[3];
      param[1] = sum / (double) n;

      sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += ((x[i] - param[1]) * (x[i] - param[1]));

      param[2] = Math.sqrt (Math.PI * Math.PI * n / (3.0 * sum));

      double[] fvec = new double [3];
      double[][] fjac = new double[3][3];
      int[] iflag = new int[2];
      int[] info = new int[2];
      int[] ipvt = new int[3];
      Optim system = new Optim (x, n);

      Minpack_f77.lmder1_f77 (system, 2, 2, param, fvec, fjac, 1e-5, info, ipvt);

      double parameters[] = new double[2];
      parameters[0] = param[1];
      parameters[1] = param[2];

      return parameters;
   }


   /**
    * Creates a new instance of a logistic distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
    *    and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>
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
   public static LogisticDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new LogisticDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;</I></SPAN> of the logistic distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the mean of the logistic distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;</I></SPAN>
    * 
    */
   public static double getMean (double alpha, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return alpha;
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#960;</I><SUP>2</SUP>/(3<I>&#955;</I><SUP>2</SUP>)</SPAN> of the logistic distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the variance of the logistic distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = 1/3<I>&#960;</I><SUP>2</SUP>*(1/<I>&#955;</I><SUP>2</SUP>)</SPAN>
    * 
    */
   public static double getVariance (double alpha, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return ((Math.PI * Math.PI / 3) * (1 / (lambda * lambda)));
   }


   /**
    * Computes and returns the standard deviation of the logistic distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the standard deviation of the logistic distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return (Math.sqrt(1.0 / 3.0) * Math.PI / lambda);
   }


   /**
    * Return the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   public void setParams (double alpha, double lambda) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      this.alpha  = alpha;
      this.lambda = lambda;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha, lambda};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : alpha = " + alpha + ", lambda = " + lambda;
   }

}
