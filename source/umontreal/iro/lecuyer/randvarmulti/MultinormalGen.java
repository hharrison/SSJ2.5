

/*
 * Class:        MultinormalGen
 * Description:  multivariate normal random variable generator
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

package umontreal.iro.lecuyer.randvarmulti;

import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.randvar.NormalGen;
import umontreal.iro.lecuyer.rng.RandomStream;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.CholeskyDecomposition;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;


/**
 * Extends {@link RandomMultivariateGen} for a
 * <SPAN  CLASS="textit">multivariate normal</SPAN> (or <EM>multinormal</EM>) distribution.
 * The <SPAN CLASS="MATH"><I>d</I></SPAN>-dimensional multivariate normal distribution
 * with mean vector 
 * <SPAN CLASS="MATH"><I><B>&mu;</B></I>&#8712;<B>R</B><SUP>d</SUP></SPAN> and (symmetric positive-definite)
 * covariance matrix 
 * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>, denoted 
 * <SPAN CLASS="MATH"><I>N</I>(<I><B>&mu;</B></I>, <I><B>&Sigma;</B></I>)</SPAN>, has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<B>X</B>) = exp(- (<B>X</B> - <I><B>&mu;</B></I>)<SUP>t</SUP><I><B>&Sigma;</B></I><SUP>-1</SUP>(<B>X</B> - <I><B>&mu;</B></I>)/2)/((2&pi;)^d &nbsp;)<SUP>1/2</SUP>,
 * </DIV><P></P>
 * for all 
 * <SPAN CLASS="MATH"><B>X</B>&#8712;<B>R</B><SUP>d</SUP></SPAN>, and  
 * <SPAN CLASS="MATH"><B>X</B><SUP>t</SUP></SPAN> is the transpose vector of 
 * <SPAN CLASS="MATH"><B>X</B></SPAN>.
 * If 
 * <SPAN CLASS="MATH"><B>Z</B>&#8764;<I>N</I>( 0,<B>I</B>)</SPAN> where 
 * <SPAN CLASS="MATH"><B>I</B></SPAN> is the
 * identity matrix, 
 * <SPAN CLASS="MATH"><B>Z</B></SPAN> is said to have the <EM>standard multinormal</EM>
 *  distribution.
 * 
 * <P>
 * For the special case <SPAN CLASS="MATH"><I>d</I> = 2</SPAN>, if the random vector 
 * <SPAN CLASS="MATH"><B>X</B> = (<I>X</I><SUB>1</SUB>, <I>X</I><SUB>2</SUB>)<SUP>t</SUP></SPAN>
 * has a bivariate normal distribution, then it has mean
 * 
 * <SPAN CLASS="MATH"><I><B>&mu;</B></I> = (<I>&#956;</I><SUB>1</SUB>, <I>&#956;</I><SUB>2</SUB>)<SUP>t</SUP></SPAN>, and covariance matrix
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I><B>&Sigma;</B></I> = [<IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="MultinormalGenimg1.png"
 *  ALT="$\displaystyle \begin{array}{cc}
 * \sigma_1^2 &amp; \rho\sigma_1\sigma_2 \\
 * \rho\sigma_1\sigma_2 &amp;\sigma_2^2
 * \end{array}$">]
 * </DIV><P></P>
 * if and only if 
 * <SPAN CLASS="MATH">Var[<I>X</I><SUB>1</SUB>] = <I>&#963;</I><SUB>1</SUB><SUP>2</SUP></SPAN>, 
 * <SPAN CLASS="MATH">Var[<I>X</I><SUB>2</SUB>] = <I>&#963;</I><SUB>2</SUB><SUP>2</SUP></SPAN>, and the
 * linear correlation between <SPAN CLASS="MATH"><I>X</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>X</I><SUB>2</SUB></SPAN> is <SPAN CLASS="MATH"><I>&#961;</I></SPAN>, where 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> &gt; 0</SPAN>,
 *  
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB> &gt; 0</SPAN>, and 
 * <SPAN CLASS="MATH">-1&nbsp;&lt;=&nbsp;<I>&#961;</I>&nbsp;&lt;=&nbsp;1</SPAN>.
 * 
 */
public class MultinormalGen extends RandomMultivariateGen {
   protected double[] mu;
   protected DoubleMatrix2D sigma;
   protected DoubleMatrix2D sqrtSigma;
   protected double[] temp;
   protected static final double MYINF = 37.54;


   private void initMN (NormalGen gen1, double[] mu, int d) {
      if (gen1 == null)
         throw new NullPointerException ("gen1 is null");

      NormalDist dist = (NormalDist) gen1.getDistribution();
      if (dist.getMu() != 0.0)
         throw new IllegalArgumentException ("mu != 0");
      if (dist.getSigma() != 1.0)
         throw new IllegalArgumentException ("sigma != 1");
      this.gen1 = gen1;
      dist = null;

      if (mu == null) {    // d is the dimension
         dimension = d;
         this.mu = new double[d];
      } else {      // d is unused
         dimension = mu.length;
         this.mu = (double[])mu.clone();
      }
      temp = new double[dimension];
     }

   /**
    * Constructs a generator with the standard multinormal distribution
    *   (with 
    * <SPAN CLASS="MATH"><I><B>&mu;</B></I> = 0</SPAN> and 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I> = <B>I</B></SPAN>) in <SPAN CLASS="MATH"><I>d</I></SPAN> dimensions.
    *   Each vector 
    * <SPAN CLASS="MATH"><B>Z</B></SPAN> will be generated via <SPAN CLASS="MATH"><I>d</I></SPAN> successive calls to
    *   <TT>gen1</TT>, which must be a <SPAN  CLASS="textit">standard normal</SPAN> generator.
    * 
    * @param gen1 the one-dimensional generator
    * 
    *    @param d the dimension of the generated vectors
    * 
    *    @exception IllegalArgumentException if the one-dimensional normal
    *     generator uses a normal distribution with <SPAN CLASS="MATH"><I>&#956;</I></SPAN> not equal to 0, or
    *     <SPAN CLASS="MATH"><I>&#963;</I></SPAN> not equal to 1.
    * 
    *    @exception IllegalArgumentException if <TT>d</TT>
    *     is negative.
    * 
    *    @exception NullPointerException if <TT>gen1</TT> is <TT>null</TT>.
    * 
    * 
    */
   public MultinormalGen (NormalGen gen1, int d) {
      initMN (gen1, null, d);
      sigma = new DenseDoubleMatrix2D (d, d);
      sqrtSigma = new DenseDoubleMatrix2D (d, d);
      for (int i = 0; i < d; i++) {
         sigma.setQuick (i, i, 1.0);
         sqrtSigma.setQuick (i, i, 1.0);
      }
   }


   /**
    * Constructs a multinormal generator with mean vector
    *  <TT>mu</TT> and covariance matrix <TT>sigma</TT>.
    *  The mean vector must have the same length as the dimensions
    *  of the covariance matrix, which must be symmetric and positive-definite.
    *  If any of the above conditions is violated, an exception is thrown.
    *  The vector 
    * <SPAN CLASS="MATH"><B>Z</B></SPAN> is generated by calling <SPAN CLASS="MATH"><I>d</I></SPAN> times the generator <TT>gen1</TT>,
    *  which must be <SPAN  CLASS="textit">standard normal</SPAN>.
    * 
    * @param gen1 the one-dimensional generator
    * 
    *    @param mu the mean vector.
    * 
    *    @param sigma the covariance matrix.
    * 
    *    @exception NullPointerException if any argument is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the length of the mean
    *     vector is incompatible with the dimensions of the covariance matrix.
    * 
    * 
    */
   protected MultinormalGen (NormalGen gen1, double[] mu,
                             DoubleMatrix2D sigma) {
      initMN (gen1, mu, -1);
      this.sigma = sigma.copy();
   }


   /**
    * Equivalent to
    *  {@link #MultinormalGen((NormalGen, double[], DoubleMatrix2D)) MultinormalGen} <TT>(gen1, mu, new DenseDoubleMatrix2D (sigma))</TT>.
    * 
    */
   protected MultinormalGen (NormalGen gen1, double[] mu, double[][] sigma)  {
      initMN (gen1, mu, -1);
      this.sigma = new DenseDoubleMatrix2D (sigma);
   }

   /**
    * Returns the mean vector used by this generator.
    * 
    * @return the current mean vector.
    * 
    */
   public double[] getMu() {
      return mu;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>i</I></SPAN>-th component of the mean vector
    *  for this generator.
    * 
    * @param i the index of the required component.
    * 
    *    @return the value of <SPAN CLASS="MATH"><I>&#956;</I><SUB>i</SUB></SPAN>.
    *    @exception ArrayIndexOutOfBoundsException if
    *     <TT>i</TT> is negative or greater than or equal to {@link #getDimension(()) getDimension}.
    * 
    * 
    */
   public double getMu (int i) {
      return mu[i];
   }


   /**
    * Sets the mean vector to <TT>mu</TT>.
    * 
    * @param mu the new mean vector.
    * 
    *    @exception NullPointerException if <TT>mu</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the length of <TT>mu</TT>
    *     does not correspond to {@link #getDimension(()) getDimension}.
    * 
    * 
    */
   public void setMu (double[] mu) {
      if (mu.length != this.mu.length)
         throw new IllegalArgumentException
            ("Incompatible length of mean vector");
      this.mu = mu;
   }


   /**
    * Sets the <SPAN CLASS="MATH"><I>i</I></SPAN>-th component of the mean vector to <TT>mui</TT>.
    * 
    * @param i the index of the modified component.
    * 
    *    @param mui the new value of <SPAN CLASS="MATH"><I>&#956;</I><SUB>i</SUB></SPAN>.
    * 
    *    @exception ArrayIndexOutOfBoundsException if <TT>i</TT>
    *     is negative or greater than or equal to {@link #getDimension(()) getDimension}.
    * 
    * 
    */
   public void setMu (int i, double mui) {
      mu[i] = mui;
   }


   /**
    * Returns the covariance matrix 
    * <SPAN CLASS="MATH"><I><B>&Sigma;</B></I></SPAN>
    *  used by this generator.
    * 
    * @return the used covariance matrix.
    * 
    */
   public DoubleMatrix2D getSigma() {
      return sigma.copy();
   }


   /**
    * Generates a point from this multinormal distribution.
    * 
    * @param p the array to be filled with the generated point
    * 
    * 
    */
   public void nextPoint (double[] p) {
      int n = dimension;
      for (int i = 0; i < n; i++) {
         temp[i] = gen1.nextDouble();
         if (temp[i] == Double.NEGATIVE_INFINITY)
            temp[i] = -MYINF;
         if (temp[i] == Double.POSITIVE_INFINITY)
            temp[i] = MYINF;
      }
      for (int i = 0; i < n; i++) {
         p[i] = 0;
         for (int c = 0; c < n; c++)
            p[i] += sqrtSigma.getQuick (i, c)*temp[c];
         p[i] += mu[i];
      }
   }

}
