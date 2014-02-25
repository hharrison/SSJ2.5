

/*
 * Class:        Pearson5Dist
 * Description:  Pearson type V distribution
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

@Deprecated
/**
 * <SPAN  CLASS="textbf">THIS CLASS HAS BEEN RENAMED {@link InverseGammaDist}</SPAN>.
 * 
 * <P>
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>Pearson type V</EM> distribution with shape parameter
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>&#946;</I><SUP><I>&#945;</I></SUP>exp<SUP>-<I>&#946;</I>/x</SUP>)/(<I>x</I><SUP><I>&#945;</I>+1</SUP><I>&#915;</I>(<I>&#945;</I>))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> otherwise,
 * where <SPAN CLASS="MATH"><I>&#915;</I></SPAN> is the gamma function.
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1 - <I>F</I><SUB>G</SUB>(1/<I>x</I>),&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>F</I>(<I>x</I>) = 0</SPAN> otherwise, where <SPAN CLASS="MATH"><I>F</I><SUB>G</SUB>(<I>x</I>)</SPAN> is the distribution function
 * of a gamma
 * distribution with shape parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
 * 
 */
public class Pearson5Dist extends ContinuousDistribution {
   protected double alpha;
   protected double beta;
   protected double logam;   // Ln (Gamma(alpha))



   /**
    * <SPAN  CLASS="textbf">THIS CLASS HAS BEEN RENAMED {@link InverseGammaDist}</SPAN>.
    *    Constructs a <TT>Pearson5Dist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>.
    * 
    */
   public Pearson5Dist (double alpha, double beta) {
      setParam (alpha, beta);
   }


   public double density (double x) {
      if (x <= 0.0)
         return 0.0;
      return Math.exp (alpha * Math.log (beta/x) - (beta / x) - logam) / x;
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

   public double getMean () {
      return getMean (alpha, beta);
   }

   public double getVariance () {
      return getVariance (alpha, beta);
   }

   public double getStandardDeviation () {
      return getStandardDeviation (alpha, beta);
   }

   /**
    * Computes the density function of a Pearson V distribution with shape
    * parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
    *    and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double density (double alpha, double beta, double x) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      if (x <= 0.0)
         return 0.0;

      return Math.exp (alpha * Math.log (beta/x) - (beta / x) - Num.lnGamma (alpha)) / x;
   }


   /**
    * Computes the density function of a Pearson V distribution with shape
    * parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
    *    and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double cdf (double alpha, double beta, double x) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      if (x <= 0.0)
         return 0.0;

      return GammaDist.barF (alpha, beta, 15, 1.0 / x);
   }


   /**
    * Computes the complementary distribution function of a Pearson V distribution
    *    with shape parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double barF (double alpha, double beta, double x) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      if (x <= 0.0)
         return 1.0;

      return GammaDist.cdf (alpha, beta, 15, 1.0 / x);
   }


   /**
    * Computes the inverse distribution function of a Pearson V distribution
    *    with shape parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double inverseF (double alpha, double beta, double u) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");

      return 1.0 / GammaDist.inverseF (alpha, beta, 15, 1 - u);
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#946;</I>)</SPAN> of the Pearson V distribution
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
    * <SPAN CLASS="MATH">hat(&alpha;), hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      double[] y = new double[n];

      for (int i = 0; i < n; i++) {
	 if(x[i] > 0)
	     y[i] = 1.0 / x[i];
	 else
	     y[i] = 1.0E100;
      }

      return GammaDist.getMLE (y, n);
   }


   /**
    * Creates a new instance of a Pearson V distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>
    *    and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static Pearson5Dist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new Pearson5Dist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#946;</I>/(<I>&#945;</I> - 1)</SPAN> of a Pearson V
    *    distribution with shape parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double getMean (double alpha, double beta) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");

      return (beta / (alpha - 1.0));
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#946;</I><SUP>2</SUP>/((<I>&#945;</I> -1)<SUP>2</SUP>(<I>&#945;</I> - 2)</SPAN>
    *    of a Pearson V distribution with shape parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale
    * parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double getVariance (double alpha, double beta) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");

      return ((beta * beta) / ((alpha - 1.0) * (alpha - 1.0) * (alpha - 2.0)));
   }


   /**
    * Computes and returns the standard deviation of a Pearson V distribution with
    *    shape parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double getStandardDeviation (double alpha, double beta) {
      return Math.sqrt (getVariance (alpha, beta));
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#945;</I></SPAN> parameter of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#946;</I></SPAN> parameter of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public void setParam (double alpha, double beta) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      supportA = 0.0;
      this.alpha = alpha;
      this.beta = beta;
      logam = Num.lnGamma (alpha);
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
