

/*
 * Class:        MultiNormalDist
 * Description:  multinormal distribution
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

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;



/**
 * Implements the abstract class {@link ContinuousDistributionMulti} for the
 * <EM>multinormal</EM> distribution with mean vector <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> and covariance
 * matrix 
 * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>.
 * The probability density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<B>x</B> = <I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>d</SUB>) = exp(- (<B>x</B> - <I><B>&mu;</B></I>)<SUP>T</SUP><I><B>&Sigma;</B></I><SUP>-1</SUP>(<B>x</B> - <I><B>&mu;</B></I>)/2)/((2&pi;)^d det())<SUP>1/2</SUP>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><IMG
 *  ALIGN="BOTTOM" BORDER="0" SRC="MultiNormalDistimg1.png"
 *  ALT="$ \boldx$"> = (<I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>d</SUB>)</SPAN>.
 * 
 */
public class MultiNormalDist extends ContinuousDistributionMulti  {
   protected int dim;
   protected double[] mu;
   protected DoubleMatrix2D sigma;
   protected DoubleMatrix2D invSigma;

   protected static Algebra algebra = new Algebra();



   public MultiNormalDist (double[] mu, double[][] sigma)  {
      setParams (mu, sigma);
   }


   public double density (double[] x) {
      double sum = 0.0;

      if (invSigma == null)
         invSigma = algebra.inverse(sigma);

      double[] temp = new double[mu.length];
      for (int i = 0; i < mu.length; i++)
      {
         sum = 0.0;
         for (int j = 0; j < mu.length; j++)
            sum += ((x[j] - mu[j]) * invSigma.getQuick (j, i));
         temp[i] = sum;
      }

      sum = 0.0;
      for (int i = 0; i < mu.length; i++)
         sum += temp[i] * (x[i] - mu[i]);

      return (Math.exp(-0.5 * sum) / Math.sqrt (Math.pow (2 * Math.PI, mu.length) * algebra.det (sigma)));
   }

   public double[] getMean() {
      return mu;
   }

   public double[][] getCovariance() {
      return sigma.toArray();
   }

   public double[][] getCorrelation () {
      return getCorrelation_ (mu, sigma.toArray());
   }

   /**
    * Computes the density of the multinormal distribution
    *    with parameters <SPAN CLASS="MATH"><I><B>&mu;</B></I> =</SPAN> <TT>mu</TT>  and 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I> =</SPAN> <TT>sigma</TT>,
    *   evaluated at <TT>x</TT>.
    * 
    */
   public static double density (double[] mu, double[][] sigma, double[] x) {
      double sum = 0.0;
      DoubleMatrix2D sig;
      DoubleMatrix2D inv;

      if (sigma.length != sigma[0].length)
         throw new IllegalArgumentException ("sigma must be a square matrix");
      if (mu.length != sigma.length)
         throw new IllegalArgumentException ("mu and sigma must have the same dimension");

      sig = new DenseDoubleMatrix2D (sigma);
      inv = algebra.inverse (sig);

      double[] temp = new double[mu.length];
      for (int i = 0; i < mu.length; i++)
      {
         sum = 0.0;
         for (int j = 0; j < mu.length; j++)
            sum += ((x[j] - mu[j]) * inv.getQuick (j, i));
         temp[i] = sum;
      }

      sum = 0.0;
      for (int i = 0; i < mu.length; i++)
         sum += temp[i] * (x[i] - mu[i]);

      return (Math.exp(-0.5 * sum) / Math.sqrt (Math.pow (2 * Math.PI, mu.length) * algebra.det (sig)));
   }


   /**
    * Returns the dimension <SPAN CLASS="MATH"><I>d</I></SPAN> of the distribution.
    * 
    */
   public int getDimension() {
      return dim;
   }


   /**
    * Returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I><B>X</B></I>] = <I><B>&mu;</B></I></SPAN> of the multinormal distribution
    *    with parameters <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> and 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>.
    * 
    */
   public static double[] getMean (double[] mu, double[][] sigma) {
      if (sigma.length != sigma[0].length)
         throw new IllegalArgumentException ("sigma must be a square matrix");
      if (mu.length != sigma.length)
         throw new IllegalArgumentException ("mu and sigma must have the same dimension");

      return mu;
   }


   /**
    * Computes the covariance matrix of the multinormal distribution
    *    with parameters <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> and 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>.
    * 
    */
   public static double[][] getCovariance (double[] mu, double[][] sigma) {
      if (sigma.length != sigma[0].length)
         throw new IllegalArgumentException ("sigma must be a square matrix");
      if (mu.length != sigma.length)
         throw new IllegalArgumentException ("mu and sigma must have the same dimension");

      return sigma;
   }


   private static double[][] getCorrelation_ (double[] mu, double[][] sigma) {
      double corr[][] = new double[mu.length][mu.length];

      for (int i = 0; i < mu.length; i++) {
         for (int j = 0; j < mu.length; j++)
            corr[i][j] = - sigma[i][j] / Math.sqrt (sigma[i][i] * sigma[j][j]);
         corr[i][i] = 1.0;
      }
      return corr;
   }

   /**
    * Computes the correlation matrix of the multinormal distribution
    *    with parameters <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> and 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>).
    * 
    */
   public static double[][] getCorrelation (double[] mu, double[][] sigma) {
      if (sigma.length != sigma[0].length)
         throw new IllegalArgumentException ("sigma must be a square matrix");
      if (mu.length != sigma.length)
         throw new IllegalArgumentException ("mu and sigma must have the same dimension");

      return getCorrelation_ (mu, sigma);
   }


   /**
    * Estimates the parameters <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> of the multinormal distribution using
    *    the maximum likelihood method. It uses the <SPAN CLASS="MATH"><I>n</I></SPAN> observations of <SPAN CLASS="MATH"><I>d</I></SPAN>
    *    components in table <SPAN CLASS="MATH"><I>x</I>[<I>i</I>][<I>j</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN> and
    *    
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;, <I>d</I> - 1</SPAN>.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @param d the dimension of each observation
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH"><I><B>&mu;</B></I><SUB>1</SUB></SPAN>,...,<SPAN CLASS="MATH"><I><B>&mu;</B></I><SUB>d</SUB></SPAN>]
    * 
    */
   public static double[] getMLEMu (double[][] x, int n, int d) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (d <= 0)
         throw new IllegalArgumentException ("d <= 0");

      double[] parameters = new double[d];
      for (int i = 0; i < parameters.length; i++)
         parameters[i] = 0.0;

      for (int i = 0; i < n; i++)
         for (int j = 0; j < d; j++)
            parameters[j] += x[i][j];

      for (int i = 0; i < parameters.length; i++)
         parameters[i] = parameters[i] / (double) n;

      return parameters;
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN> of the multinormal distribution using
    *    the maximum likelihood method. It uses the <SPAN CLASS="MATH"><I>n</I></SPAN> observations of <SPAN CLASS="MATH"><I>d</I></SPAN>
    *    components in table <SPAN CLASS="MATH"><I>x</I>[<I>i</I>][<I>j</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN> and
    *    
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;, <I>d</I> - 1</SPAN>.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @param d the dimension of each observation
    * 
    *    @return returns the covariance matrix 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>
    * 
    */
   public static double[][] getMLESigma (double[][] x, int n, int d) {
      double sum = 0.0;

      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (d <= 0)
         throw new IllegalArgumentException ("d <= 0");

      double[] mean = getMLEMu (x, n, d);
      double[][] parameters = new double[d][d];
      for (int i = 0; i < parameters.length; i++)
         for (int j = 0; j < parameters.length; j++)
            parameters[i][j] = 0.0;

      for (int i = 0; i < parameters.length; i++)
      {
         for (int j = 0; j < parameters.length; j++)
         {
            sum = 0.0;
            for (int t = 0; t < n; t++)
               sum += (x[t][i] - mean[i]) * (x[t][j] - mean[j]);
            parameters[i][j] = sum / (double) n;
         }
      }

      return parameters;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> of this object.
    * 
    */
   public double[] getMu() {
      return mu;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>i</I></SPAN>-th component of the parameter <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> of this object.
    * 
    */
   public double getMu (int i) {
      return mu[i];
   }


   /**
    * Returns the parameter 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN> of this object.
    * 
    */
   public double[][] getSigma() {
      return sigma.toArray();
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I><B>&mu;</B></I></SPAN> and 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN> of this object.
    * 
    */
   public void setParams (double[] mu, double[][] sigma) {
      if (sigma.length != sigma[0].length)
         throw new IllegalArgumentException ("sigma must be a square matrix");
      if (mu.length != sigma.length)
         throw new IllegalArgumentException ("mu and sigma must have the same dimension");

      this.mu = new double[mu.length];
      this.dimension = mu.length;
      System.arraycopy(mu, 0, this.mu, 0, mu.length);
      this.sigma = new DenseDoubleMatrix2D (sigma);

      invSigma = null;
   }

}
