

/*
 * Class:        NegativeMultinomialDist
 * Description:  negative multinomial distribution 
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
import umontreal.iro.lecuyer.util.RootFinder;
import umontreal.iro.lecuyer.functions.MathFunction;



/**
 * Implements the class {@link DiscreteDistributionIntMulti} for the
 * <EM>negative multinomial</EM> distribution with parameters <SPAN CLASS="MATH"><I>n</I> &gt; 0</SPAN> and
 * (
 * <SPAN CLASS="MATH"><I>p</I><SUB>1</SUB>,&#8230;, <I>p</I><SUB>d</SUB></SPAN>)  such that all <SPAN CLASS="MATH">0 &lt; <I>p</I><SUB>i</SUB> &lt; 1</SPAN> and  
 * <SPAN CLASS="MATH">&sum;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB> &lt; 1</SPAN>.
 * The probability mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>P</I>[<I>X</I> = (<I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>d</SUB>)] = (<I>&#915;</I>(<I>n</I>+&sum;<SUB>i=1</SUB><SUP>d</SUP><I>x</I><SUB>i</SUB>)/<I>&#915;</I>(<I>n</I>))<I>p</I><SUB>0</SUB><SUP>n</SUP>&prod;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB><SUP>x<SUB>i</SUB></SUP>/<I>x</I><SUB>i</SUB>!
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>p</I><SUB>0</SUB> = 1 - &sum;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB></SPAN>.
 * 
 */
public class NegativeMultinomialDist extends DiscreteDistributionIntMulti  {
   protected double n;
   protected double p[];

   private static class Function implements MathFunction {
      protected double Fl[];
      protected int ups[];
      protected int k;
      protected int M;
      protected int sumUps;

      public Function (int k, int m, int ups[], double Fl[]) {
         this.k = k;
         this.M = m;

         this.Fl = new double[Fl.length];
         System.arraycopy (Fl, 0, this.Fl, 0, Fl.length);
         this.ups = new int[ups.length];
         System.arraycopy (ups, 0, this.ups, 0, ups.length);

         sumUps = 0;
         for (int i = 0; i < ups.length; i++)
            sumUps += ups[i];
      }

      public double evaluate (double gamma) {
         double sum = 0.0;
         for (int l = 0; l < M; l++)
            sum += (Fl[l] / (gamma + (double) l));
         return (sum - Math.log1p (sumUps / (k * gamma)));
      }
   }




   /**
    * Creates a <TT>NegativeMultinomialDist</TT> object with parameters <SPAN CLASS="MATH"><I>n</I></SPAN>
    *     and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) such that 
    * <SPAN CLASS="MATH">&sum;<SUB>i=1</SUB><SUP>d</SUP><I>p</I><SUB>i</SUB> &lt; 1</SPAN>,
    *    as described above. We have <SPAN CLASS="MATH"><I>p</I><SUB>i</SUB> =</SPAN> <TT>p[i-1]</TT>.
    * 
    */
   public NegativeMultinomialDist (double n, double p[])  {
      setParams (n, p);
   }


   public double prob (int x[]) {
      return prob_ (n, p, x);
   }
/*
   public double cdf (int x[]) {
      throw new UnsupportedOperationException ("cdf not implemented");
   }
*/
   public double[] getMean() {
      return getMean_ (n, p);
   }

   public double[][] getCovariance() {
      return getCovariance_ (n, p);
   }

   public double[][] getCorrelation() {
      return getCorrelation_ (n, p);
   }

   private static void verifParam (double n, double p[]) {
      double sumPi = 0.0;

      if (n <= 0.0)
         throw new IllegalArgumentException("n <= 0");

      for (int i = 0; i < p.length;i++) {
         if ((p[i] < 0) || (p[i] >= 1))
            throw new IllegalArgumentException("p is not a probability vector");

         sumPi += p[i];
      }
      if (sumPi >= 1.0)
         throw new IllegalArgumentException("p is not a probability vector");
   }

   private static double prob_ (double n, double p[], int x[]) {
      double p0 = 0.0;
      double sumPi= 0.0;
      double sumXi= 0.0;
      double sumLnXiFact = 0.0;
      double sumXiLnPi = 0.0;

      if (x.length != p.length)
         throw new IllegalArgumentException ("x and p must have the same size");

      for (int i = 0; i < p.length;i++)
      {
         sumPi += p[i];
         sumXi += x[i];
         sumLnXiFact += Num.lnFactorial (x[i]);
         sumXiLnPi += x[i] * Math.log (p[i]);
      }
      p0 = 1.0 - sumPi;

      return Math.exp (Num.lnGamma (n + sumXi) - (Num.lnGamma (n) +
           sumLnXiFact) + n * Math.log (p0) + sumXiLnPi);
   }

   /**
    * Computes the probability mass function
    *    of the negative multinomial distribution with parameters
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>), evaluated at 
    * <SPAN CLASS="MATH"><B>x</B></SPAN>.
    * 
    */
   public static double prob (double n, double p[], int x[]) {
      verifParam (n, p);
      return prob_ (n, p, x);
   }


   private static double cdf_ (double n, double p[], int x[]) {
      throw new UnsupportedOperationException ("cdf not implemented");
   }

   /**
    * Computes the cumulative probability function <SPAN CLASS="MATH"><I>F</I></SPAN> of the
    *    negative multinomial distribution with parameters <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>k</SUB></SPAN>), evaluated at 
    * <SPAN CLASS="MATH"><B>x</B></SPAN>.
    * 
    */
   public static double cdf (double n, double p[], int x[]) {
      verifParam (n, p);

      return cdf_ (n, p, x);
   }


   private static double[] getMean_ (double n, double p[]) {
      double p0 = 0.0;
      double sumPi= 0.0;
      double mean[] = new double[p.length];

      for (int i = 0; i < p.length;i++)
         sumPi += p[i];
      p0 = 1.0 - sumPi;

      for (int i = 0; i < p.length; i++)
         mean[i] = n * p[i] / p0;

      return mean;
   }

   /**
    * Computes the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>np</I><SUB>i</SUB>/<I>p</I><SUB>0</SUB></SPAN> of the negative multinomial distribution
    *    with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[] getMean (double n, double p[]) {
      verifParam (n, p);

      return getMean_ (n, p);
   }


   private static double[][] getCovariance_ (double n, double p[]) {
      double p0 = 0.0;
      double sumPi= 0.0;
      double cov[][] = new double[p.length][p.length];

      for (int i = 0; i < p.length;i++)
         sumPi += p[i];
      p0 = 1.0 - sumPi;

      for (int i = 0; i < p.length; i++)
      {
         for (int j = 0; j < p.length; j++)
            cov[i][j] = n * p[i] * p[j] / (p0 * p0);

         cov[i][i] = n * p[i] * (p[i] + p0) / (p0 * p0);
      }

      return cov;
   }

   /**
    * Computes the covariance matrix of the negative multinomial distribution
    *    with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[][] getCovariance (double n, double p[]) {
      verifParam (n, p);

      return getCovariance_ (n, p);
   }


   private static double[][] getCorrelation_ (double n, double[] p) {
      double corr[][] = new double[p.length][p.length];
      double sumPi= 0.0;
      double p0;

      for (int i = 0; i < p.length;i++)
         sumPi += p[i];
      p0 = 1.0 - sumPi;

      for (int i = 0; i < p.length; i++) {
         for (int j = 0; j < p.length; j++)
            corr[i][j] = Math.sqrt(p[i] * p[j] /((p0 + p[i]) * (p0 + p[j])));
         corr[i][i] = 1.0;
      }
      return corr;
   }

   /**
    * Computes the correlation matrix of the negative multinomial distribution
    *    with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[][] getCorrelation (double n, double[] p) {
      verifParam (n, p);
      return getCorrelation_ (n, p);
   }


   /**
    * Estimates  and returns the parameters [<SPAN CLASS="MATH">hat(n)</SPAN>, <SPAN CLASS="MATH">hat(p)<SUB>1</SUB></SPAN>, ...,
    *   <SPAN CLASS="MATH">hat(p)<SUB>d</SUB></SPAN>]
    *    of the negative multinomial distribution using the maximum likelihood method.
    *    It uses the <SPAN CLASS="MATH"><I>m</I></SPAN> observations of <SPAN CLASS="MATH"><I>d</I></SPAN> components in table
    *    <TT>x[<SPAN CLASS="MATH"><I>i</I></SPAN>][<SPAN CLASS="MATH"><I>j</I></SPAN>]</TT>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN> and 
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;, <I>d</I> - 1</SPAN>.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param m the number of observations used to evaluate parameters
    * 
    *    @param d the dimension of each vector
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(n)</SPAN>, <SPAN CLASS="MATH">hat(p)<SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH">hat(p)<SUB>d</SUB></SPAN>]
    * 
    */
   public static double[] getMLE (int x[][], int m, int d) {
      double parameters[] = new double[d + 1];
      int ups[] = new int[m];
      double mean[] = new double[d];

      int i, j, l;
      int M;
      int prop;

      // Initialization
      for (i = 0; i < d; i++)
         mean[i] = 0;

      // Ups_j = Sum_k x_ji
      // mean_i = Sum_m x_ji / m
      for (j = 0; j < m; j++) {
         ups[j] = 0;
         for (i = 0; i < d; i++) {
            ups[j] += x[j][i];
            mean[i] += x[j][i];
         }
      }
      for (i = 0; i < d; i++)
         mean[i] /= m;

/*
      double var = 0.0;
      if (d > 1) {
         // Calcule la covariance 0,1
         for (j = 0; j < m; j++)
            var += (x[j][0] - mean[0])*(x[j][1] - mean[1]);
         var /= m;
      } else {
         // Calcule la variance 0
         for (j = 0; j < m; j++)
            var += (x[j][0] - mean[0])*(x[j][0] - mean[0]);
         var /= m;
      }
*/

      // M = Max(Ups_j)
      M = ups[0];
      for (j = 1; j < m; j++)
         if (ups[j] > M)
            M = ups[j];

      if (M >= Integer.MAX_VALUE)
         throw new IllegalArgumentException("n/p_i too large");

      double Fl[] = new double[M];
      for (l = 0; l < M; l++) {
         prop = 0;
         for (j = 0; j < m; j++)
            if (ups[j] > l)
               prop++;

         Fl[l] = (double) prop / (double) m;
      }

/*
      // Estime la valeur initiale de n pour brentDekker: pourrait
      // accélérer brentDekker (gam0/1000, gam0*1000, f, 1e-5).
      // Reste à bien tester.
      if (d > 1) {
         double gam0 = mean[0] * mean[1] / var;
         System.out.println ("gam0 = " + gam0);
      } else {
         double t = var/mean[0] - 1.0;
         double gam0 = mean[0] / t;
         System.out.println ("gam0 = " + gam0);
      }
*/
      Function f = new Function (m, (int)M, ups, Fl);
      parameters[0] = RootFinder.brentDekker (1e-9, 1e9, f, 1e-5);

      double lambda[] = new double[d];
      double sumLambda = 0.0;
      for (i = 0; i < d; i++) {
         lambda[i] = mean[i] / parameters[0];
         sumLambda += lambda[i];
      }

      for (i = 0; i < d; i++) {
         parameters[i + 1] = lambda[i] / (1.0 + sumLambda);
         if (parameters[i + 1] > 1.0)
            throw new IllegalArgumentException("p_i > 1");
      }

      return parameters;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public double getGamma() {
      return n;
   }


   /**
    * Returns the parameters (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) of this object.
    * 
    */
   public double[] getP() {
      return p;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and (<SPAN CLASS="MATH"><I>p</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>p</I><SUB>d</SUB></SPAN>) of this object.
    * 
    */
   public void setParams (double n, double p[]) {
      double sumPi = 0.0;

      if (n <= 0.0)
         throw new IllegalArgumentException("n <= 0");

      this.n = n;
      this.dimension = p.length;
      this.p = new double[dimension];
      for (int i = 0; i < dimension; i++) {
         if ((p[i] < 0) || (p[i] >= 1))
            throw new IllegalArgumentException("p is not a probability vector");

         sumPi += p[i];
         this.p[i] = p[i];
      }

      if (sumPi >= 1.0)
         throw new IllegalArgumentException("p is not a probability vector");
   }

}
