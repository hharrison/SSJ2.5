

/*
 * Class:        MultinomialDist
 * Description:  multinomial distribution
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

package umontreal.iro.lecuyer.probdistmulti;

import umontreal.iro.lecuyer.util.Num;


/**
 * Implements the abstract class {@link DiscreteDistributionIntMulti} for the
 * <EM>multinomial</EM> distribution with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and
 * (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
 * The probability mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>P</I>[<I>X</I> = (<I>x</I><SUB>1</SUB>,..., <I>x</I><SUB>d</SUB>)] = <I>n</I>!&prod;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB><SUP>x<SUB>i</SUB></SUP>/<I>x</I><SUB>i</SUB>!,
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH">&sum;<SUB>i=1</SUB><SUP>d</SUP><I>x</I><SUB>i</SUB> = <I>n</I></SPAN> and 
 * <SPAN CLASS="MATH">&sum;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB> = 1</SPAN>.
 * 
 */
public class MultinomialDist extends DiscreteDistributionIntMulti  {
   protected int n;
   protected double p[];




   /**
    * Creates a <TT>MultinomialDist</TT> object with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and
    *    (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) such that 
    * <SPAN CLASS="MATH">&sum;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB> = 1</SPAN>. We have
    *    <SPAN CLASS="MATH"><I>p</I><SUB>i</SUB> =</SPAN> <TT>p[i-1]</TT>.
    * 
    */
   public MultinomialDist (int n, double p[])  {
      setParams (n, p);
   }

   public double prob (int x[]) {
      return prob_ (n, p, x);
   }

   public double cdf (int x[]) {
      return cdf_ (n, p, x);
   }

   public double[] getMean() {
      return getMean_ (n, p);
   }

   public double[][] getCovariance() {
      return getCovariance_ (n, p);
   }

   public double[][] getCorrelation () {
      return getCorrelation_ (n, p);
   }

   private static void verifParam(int n, double p[]) {
      double sumPi = 0.0;

      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      for (int i = 0; i < p.length; i++)
      {
         if ((p[i] < 0) || (p[i] > 1))
            throw new IllegalArgumentException("p is not a probability vector");

         sumPi += p[i];
      }

      if (sumPi != 1.0)
         throw new IllegalArgumentException ("p is not a probability vector");
   }

   private static double prob_ (int n, double p[], int x[]) {
      double sumXFact = 0.0;
      int sumX = 0;
      double sumPX = 0.0;

      if (x.length != p.length)
         throw new IllegalArgumentException ("x and p must have the same dimension");

      for (int i = 0; i < p.length; i++)
      {
         sumX += x[i];
         sumXFact += Num.lnFactorial (x[i]);
         sumPX += (x[i] * Math.log (p[i]));
      }

      if (sumX != n)
         return 0.0;
      else
      {
         return Math.exp (Num.lnFactorial (n) - sumXFact + sumPX);
      }
   }


   /**
    * Computes the probability mass function
    *    of the multinomial distribution with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and
    *    (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double prob (int n, double p[], int x[]) {
      verifParam (n, p);
      return prob_ (n, p, x);
   }


   private static double cdf_ (int n, double p[], int x[]) {
      boolean end = false;
      double sum = 0.0;
      int j;

      if (x.length != p.length)
         throw new IllegalArgumentException ("x and p must have the same dimension");

      int is[] = new int[x.length];
      for (int i = 0; i < is.length; i++)
         is[i] = 0;

      sum = 0.0;
      while (! end)
      {
         sum += prob (n, p, is);
         is[0]++;

         if (is[0] > x[0])
         {
            is[0] = 0;
            j = 1;
            while (j < x.length && is[j] == x[j])
               is[j++] = 0;

            if (j == x.length)
               end = true;
            else
               is[j]++;
         }
      }

      return sum;
   }

   /**
    * Computes the function <SPAN CLASS="MATH"><I>F</I></SPAN> of the multinomial distribution with
    *    parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double cdf (int n, double p[], int x[]) {
      verifParam (n, p);
      return cdf_ (n, p, x);
   }


   private static double[] getMean_ (int n, double[] p) {
      double mean[] = new double[p.length];

      for (int i = 0; i < p.length; i++)
         mean[i] = n * p[i];

      return mean;
   }

   /**
    * Computes the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I><SUB>i</SUB>] = <I>np</I><SUB>i</SUB></SPAN> of the multinomial distribution
    *    with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[] getMean (int n, double[] p) {
      verifParam (n, p);

      return getMean_ (n, p);
   }


   private static double[][] getCovariance_ (int n, double[] p) {
      double cov[][] = new double[p.length][p.length];

      for (int i = 0; i < p.length; i++)
      {
         for (int j = 0; j < p.length; j++)
            cov[i][j] = -n * p[i] * p[j];

         cov[i][i] = n * p[i] * (1.0 - p[i]);
      }
      return cov;
   }

   /**
    * Computes the covariance matrix of the multinomial distribution
    *    with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[][] getCovariance (int n, double[] p) {
      verifParam (n, p);
      return getCovariance_ (n, p);
   }


   private static double[][] getCorrelation_ (int n, double[] p) {
      double corr[][] = new double[p.length][p.length];

      for (int i = 0; i < p.length; i++) {
         for (int j = 0; j < p.length; j++)
            corr[i][j] = -Math.sqrt(p[i] * p[j] / ((1.0 - p[i]) * (1.0 - p[j])));
         corr[i][i] = 1.0;
      }
      return corr;
   }

   /**
    * Computes the correlation matrix of the multinomial distribution
    *    with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[][] getCorrelation (int n, double[] p) {
      verifParam (n, p);
      return getCorrelation_ (n, p);
   }


   /**
    * Estimates and returns the parameters [<SPAN CLASS="MATH">hat(p_i)</SPAN>,...,<SPAN CLASS="MATH">hat(p_d)</SPAN>] of the
    *    multinomial distribution using the maximum likelihood method. It uses the <SPAN CLASS="MATH"><I>m</I></SPAN>
    *    observations of <SPAN CLASS="MATH"><I>d</I></SPAN> components in table <SPAN CLASS="MATH"><I>x</I>[<I>i</I>][<I>j</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>
    *    and 
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;, <I>d</I> - 1</SPAN>.
    *    
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param m the number of observations used to evaluate parameters
    * 
    *    @param d the dimension of each observation
    * 
    *    @param n the number of independant trials for each series
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(p_i)</SPAN>,...,<SPAN CLASS="MATH">hat(p_d)</SPAN>]
    * 
    */
   public static double[] getMLE (int x[][], int m, int d, int n) {
      double parameters[] = new double[d];
      double xBar[] = new double[d];
      double N = 0.0;

      if (m <= 0)
         throw new IllegalArgumentException ("m <= 0");
      if (d <= 0)
         throw new IllegalArgumentException ("d <= 0");

      for (int i = 0; i < d; i++)
         xBar[i] = 0;

      for (int v = 0; v < m; v++)
         for (int c = 0; c < d; c++)
            xBar[c] += x[v][c];

      for (int i = 0; i < d; i++)
      {
         xBar[i] = xBar[i] / (double) n;
         N += xBar[i];
      }
      if (N != (double) n)
         throw new IllegalArgumentException("n is not correct");

      for (int i = 0; i < d; i++)
         parameters[i] = xBar[i] / (double) n;

      return parameters;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public int getN() {
      return n;
   }


   /**
    * Returns the parameters (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) of this object.
    * 
    */
   public double[] getP() {
      return p;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) of this object.
    * 
    */
   public void setParams (int n, double p[]) {
      double sumP = 0.0;

      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (p.length < 2)
         throw new IllegalArgumentException ("p.length < 2");

      this.n = n;
      this.dimension = p.length;
      this.p = new double[dimension];
      for (int i = 0; i < dimension; i++)
      {
         if ((p[i] < 0) || (p[i] > 1))
            throw new IllegalArgumentException("p is not a probability vector");

         this.p[i] = p[i];
         sumP += p[i];
      }

      if (sumP != 1.0)
         throw new IllegalArgumentException ("p is not a probability vector");
   }

}
