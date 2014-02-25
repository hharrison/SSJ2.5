

/*
 * Class:        LaplaceDist
 * Description:  Laplace distribution
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

/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>Laplace</EM> distribution.
 * It has location parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>e</I><SUP>-| x-<I>&#956;</I>|/<I>&#946;</I></SUP>/(2<I>&#946;</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">(1/2)<I>e</I><SUP>(x-<I>&#956;</I>)/<I>&#946;</I></SUP></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>x</I>&nbsp;&lt;=&nbsp;<I>&#956;</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1 - (1/2)<I>e</I><SUP>(<I>&#956;</I>-x)/<I>&#946;</I></SUP></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; otherwise, </TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * and its inverse is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I><SUP>-1</SUP>(<I>u</I>) =</TD>
 * <TD ALIGN="LEFT"><I>&#946;</I>log(2<I>u</I>) + <I>&#956;</I></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if 0&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;1/2,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I><SUP>-1</SUP>(<I>u</I>) =</TD>
 * <TD ALIGN="LEFT"><I>&#956;</I> - <I>&#946;</I>log(2(1 - <I>u</I>))</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; otherwise. </TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * 
 */
public class LaplaceDist extends ContinuousDistribution {
   private double mu;
   private double beta;



   /**
    * Constructs a <TT>LaplaceDist</TT> object with default
    *  parameters <SPAN CLASS="MATH"><I>&#956;</I> = 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN>.
    * 
    */
   public LaplaceDist() {
      mu = 0;
      beta = 1;
   }


   /**
    * Constructs a <TT>LaplaceDist</TT> object with parameters
    *  <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>.
    * 
    */
   public LaplaceDist (double mu, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      this.mu = mu;
      this.beta = beta;
   }


   public double density (double x) {
      return density (mu, beta, x);
   }

   public double cdf (double x) {
      return cdf (mu, beta, x);
   }

   public double barF (double x) {
      return barF (mu, beta, x);
   }

   public double inverseF (double u) {
      return inverseF (mu, beta, u);
   }

   public double getMean() {
      return LaplaceDist.getMean (mu, beta);
   }

   public double getVariance() {
      return LaplaceDist.getVariance (mu, beta);
   }

   public double getStandardDeviation() {
      return LaplaceDist.getStandardDeviation (mu, beta);
   }

   /**
    * Computes the Laplace density function.
    * 
    */
   public static double density (double mu, double beta, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      return Math.exp (-Math.abs (x - mu)/beta)/(2.0*beta);
   }


   /**
    * Computes the Laplace distribution function.
    * 
    */
   public static double cdf (double mu, double beta, double x) {
      if (x <= mu)
         return Math.exp ((x - mu)/beta)/2.0;
      else
         return 1.0 - Math.exp ((mu - x)/beta)/2.0;
   }


   /**
    * Computes the Laplace complementary distribution function.
    * 
    */
   public static double barF (double mu, double beta, double x) {
      if (x <= mu)
         return 1.0 - Math.exp ((x - mu)/beta)/2.0;
      else
         return Math.exp ((mu - x)/beta)/2.0;
   }

  //====================================================
  // code taken and adapted from unuran
  // file /distribution/c_laplca_gen.c
  //====================================================

   /**
    * Computes the inverse Laplace distribution function.
    * 
    */
   public static double inverseF (double mu, double beta, double u) {
     // transform to random variate
     if (u < 0.0 || u > 1.0)
        throw new IllegalArgumentException ("u should be in [0,1]");
     if (u <= 0.0)
        return Double.NEGATIVE_INFINITY;
     if (u >= 1.0)
        return Double.POSITIVE_INFINITY;

     double x = (u>0.5) ? -Math.log(2.-2*u) : Math.log(2*u);
     return mu + beta*x;
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#956;</I>, <I>&#946;</I>)</SPAN> of the Laplace distribution
    *   using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(&mu;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[];
      parameters = new double[2];

      parameters[0] = EmpiricalDist.getMedian (x, n);

      double sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += Math.abs (x[i] - parameters[0]);
      parameters[1] = sum / (double) n;

      return parameters;
   }


   /**
    * Creates a new instance of a Laplace distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>
    *    and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> estimated using the maximum likelihood method based on the
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static LaplaceDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new LaplaceDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I></SPAN> of the Laplace
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the mean of the Laplace distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I></SPAN>
    * 
    */
   public static double getMean (double mu, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      return mu;
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = 2<I>&#946;</I><SUP>2</SUP></SPAN>
    *    of the Laplace distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the variance of the Laplace distribution 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = 2<I>&#946;</I><SUP>2</SUP></SPAN>
    * 
    */
   public static double getVariance (double mu, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      return (2.0 * beta * beta);
   }


   /**
    * Computes and returns the standard deviation of the Laplace
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the standard deviation of the Laplace distribution
    * 
    */
   public static double getStandardDeviation (double mu, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      return (Num.RAC2 * beta);
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {mu, beta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : mu = " + mu+ ", beta = " + beta;
   }

}
