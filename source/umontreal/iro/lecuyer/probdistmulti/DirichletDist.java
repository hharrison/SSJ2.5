

/*
 * Class:        DirichletDist
 * Description:  Dirichlet distribution
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
import optimization.*;


/**
 * Implements the abstract class {@link ContinuousDistributionMulti} for the
 * <EM>Dirichlet</EM> distribution with parameters
 * (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>), 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>i</SUB> &gt; 0</SPAN>.
 * The probability density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>d</SUB>) = <I>&#915;</I>(<I>&#945;</I><SUB>0</SUB>)&prod;<SUB>i=1</SUB><SUP>d</SUP><I>x</I><SUB>i</SUB><SUP><I>&#945;</I><SUB>i</SUB>-1</SUP>/(&prod;<SUB>i=1</SUB><SUP>d</SUP><I>&#915;</I>(<I>&#945;</I><SUB>i</SUB>))
 * </DIV><P></P>
 * where  <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB>&nbsp;&gt;=&nbsp; 0</SPAN>, 
 * <SPAN CLASS="MATH">&sum;<SUB>i=1</SUB><SUP>d</SUP><I>x</I><SUB>i</SUB> = 1</SPAN>, 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>0</SUB> = &sum;<SUB>i=1</SUB><SUP>d</SUP><I>&#945;</I><SUB>i</SUB></SPAN>,
 *    and <SPAN CLASS="MATH"><I>&#915;</I></SPAN> is the Gamma function.
 * 
 */
public class DirichletDist extends ContinuousDistributionMulti  {
   private static final double LOGMIN = -709.1;    // Log(MIN_DOUBLE/2)
   protected double[] alpha;

   private static class Optim implements Uncmin_methods
   {
      double[] logP;
      int n;
      int k;

      public Optim (double[] logP, int n) {
         this.n = n;
         this.k = logP.length;
         this.logP = new double[k];
         System.arraycopy (logP, 0, this.logP, 0, k);
      }

      public double f_to_minimize (double[] alpha) {
         double sumAlpha = 0.0;
         double sumLnGammaAlpha = 0.0;
         double sumAlphaLnP = 0.0;

         for (int i = 1; i < alpha.length; i++) {
            if (alpha[i] <= 0.0)
               return 1.0e200;

            sumAlpha += alpha[i];
            sumLnGammaAlpha += Num.lnGamma (alpha[i]);
            sumAlphaLnP += ((alpha[i] - 1.0) * logP[i - 1]);
         }

         return (- n * (Num.lnGamma (sumAlpha) - sumLnGammaAlpha + sumAlphaLnP));
      }

      public void gradient (double[] alpha, double[] g)
      {
      }

      public void hessian (double[] alpha, double[][] h)
      {
      }
   }



   public DirichletDist (double[] alpha)  {
      setParams (alpha);
   }


   public double density (double[] x) {
      return density_ (alpha, x);
   }

   public double[] getMean() {
      return getMean_ (alpha);
   }

   public double[][] getCovariance() {
      return getCovariance_ (alpha);
   }

   public double[][] getCorrelation () {
      return getCorrelation_ (alpha);
   }

   private static void verifParam (double[] alpha) {

      for (int i = 0; i < alpha.length;i++)
      {
         if (alpha[i] <= 0)
            throw new IllegalArgumentException("alpha[" + i + "] <= 0");
      }
   }

   private static double density_ (double[] alpha, double[] x) {
      double alpha0 = 0.0;
      double sumLnGamma = 0.0;
      double sumAlphaLnXi = 0.0;

      if (alpha.length != x.length)
         throw new IllegalArgumentException ("alpha and x must have the same dimension");

      for (int i = 0; i < alpha.length; i++) {
         alpha0 += alpha[i];
         sumLnGamma += Num.lnGamma (alpha[i]);
         if (x[i] <= 0.0 || x[i] >= 1.0)
            return 0.0;
         sumAlphaLnXi += (alpha[i] - 1.0) * Math.log (x[i]);
      }

      return Math.exp (Num.lnGamma (alpha0) - sumLnGamma + sumAlphaLnXi);
   }

   /**
    * Computes the density of the Dirichlet distribution
    *    with parameters (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double density (double[] alpha, double[] x) {
      verifParam (alpha);
      return density_ (alpha, x);
   }


   private static double[][] getCovariance_ (double[] alpha) {
      double[][] cov = new double[alpha.length][alpha.length];
      double alpha0 = 0.0;

      for (int i =0; i < alpha.length; i++)
         alpha0 += alpha[i];

      for (int i = 0; i < alpha.length; i++) {
         for (int j = 0; j < alpha.length; j++)
            cov[i][j] = - (alpha[i] * alpha[j]) / (alpha0 * alpha0 * (alpha0 + 1.0));

         cov[i][i] = (alpha[i] / alpha0) * (1.0 - alpha[i] / alpha0) / (alpha0 + 1.0);
      }

      return cov;
   }

   /**
    * Computes the covariance matrix of the Dirichlet distribution
    *    with parameters (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[][] getCovariance (double[] alpha) {
      verifParam (alpha);

      return getCovariance_ (alpha);
   }


   private static double[][] getCorrelation_ (double[] alpha) {
      double corr[][] = new double[alpha.length][alpha.length];
      double alpha0 = 0.0;

      for (int i =0; i < alpha.length; i++)
         alpha0 += alpha[i];

      for (int i = 0; i < alpha.length; i++) {
         for (int j = 0; j < alpha.length; j++)
            corr[i][j] = - Math.sqrt ((alpha[i] * alpha[j]) /
                                      ((alpha0 - alpha[i]) * (alpha0 - alpha[j])));
         corr[i][i] = 1.0;
      }
      return corr;
   }

   /**
    * Computes the correlation matrix of the Dirichlet distribution
    *    with parameters (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>).
    * 
    */
   public static double[][] getCorrelation (double[] alpha) {
      verifParam (alpha);
      return getCorrelation_ (alpha);
   }


   /**
    * Estimates the parameters [
    * <SPAN CLASS="MATH">hat(&alpha;_1),&#8230;, hat(&alpha;_d)</SPAN>]
    *    of the Dirichlet distribution using the maximum likelihood method. It uses the
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> observations of <SPAN CLASS="MATH"><I>d</I></SPAN> components in table <SPAN CLASS="MATH"><I>x</I>[<I>i</I>][<I>j</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>
    *    and 
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;, <I>d</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @param d the dimension of each vector
    * 
    *    @return returns the parameter [
    * <SPAN CLASS="MATH">hat(&alpha;_1),&#8230;, hat(&alpha;_d)</SPAN>]
    * 
    */
   public static double[] getMLE (double[][] x, int n, int d) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (d <= 0)
         throw new IllegalArgumentException ("d <= 0");

      double[] logP = new double[d];
      double mean[] = new double[d];
      double var[] = new double[d];
      int i;
      int j;
      for (i = 0; i < d; i++) {
         logP[i] = 0.0;
         mean[i] = 0.0;
      }

      for (i = 0; i < n; i++) {
         for (j = 0; j < d; j++) {
            if (x[i][j] > 0.)
               logP[j] += Math.log (x[i][j]);
            else
               logP[j] += LOGMIN;
            mean[j] += x[i][j];
         }
      }

      for (i = 0; i < d; i++) {
         logP[i] /= (double) n;
         mean[i] /= (double) n;
      }

      double sum = 0.0;
      for (j = 0; j < d; j++) {
         sum = 0.0;
         for (i = 0; i < n; i++)
            sum += (x[i][j] - mean[j]) * (x[i][j] - mean[j]);
         var[j] = sum / (double) n;
      }

      double alpha0 = (mean[0] * (1.0 - mean[0])) / var[0] - 1.0;
      Optim system = new Optim (logP, n);

      double[] parameters = new double[d];
      double[] xpls = new double[d + 1];
      double[] alpha = new double[d + 1];
      double[] fpls = new double[d + 1];
      double[] gpls = new double[d + 1];
      int[] itrcmd = new int[2];
      double[][] a = new double[d + 1][d + 1];
      double[] udiag = new double[d + 1];

      for (i = 1; i <= d; i++)
         alpha[i] = mean[i - 1] * alpha0;

      Uncmin_f77.optif0_f77 (d, alpha, system, xpls, fpls, gpls, itrcmd, a, udiag);

      for (i = 0; i < d; i++)
         parameters[i] = xpls[i+1];

      return parameters;
   }


   private static double[] getMean_ (double[] alpha) {
      double alpha0 = 0.0;
      double[] mean = new double[alpha.length];

      for (int i = 0; i < alpha.length;i++)
         alpha0 += alpha[i];

      for (int i = 0; i < alpha.length; i++)
         mean[i] = alpha[i] / alpha0;

      return mean;
   }


   /**
    * Computes the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;</I><SUB>i</SUB>/<I>&#945;</I><SUB>0</SUB></SPAN> of the Dirichlet distribution
    *    with parameters (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>), where 
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>0</SUB> = &sum;<SUB>i=1</SUB><SUP>d</SUP><I>&#945;</I><SUB>i</SUB></SPAN>.
    * 
    */
   public static double[] getMean (double[] alpha) {
      verifParam (alpha);
      return getMean_ (alpha);
   }


   /**
    * Returns the parameters (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>) of this object.
    * 
    */
   public double[] getAlpha() {
      return alpha;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>i</I></SPAN>th component of the alpha vector.
    * 
    */
   public double getAlpha (int i) {
      return alpha[i];
   }


   /**
    * Sets the parameters (<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, ..., <SPAN CLASS="MATH"><I>&#945;</I><SUB>d</SUB></SPAN>) of this object.
    * 
    */
   public void setParams (double[] alpha) {
      this.dimension = alpha.length;
      this.alpha = new double[dimension];
      for (int i = 0; i < dimension; i++) {
         if (alpha[i] <= 0)
            throw new IllegalArgumentException("alpha[" + i + "] <= 0");
         this.alpha[i] = alpha[i];
      }
   }

}
