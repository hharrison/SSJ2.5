

/*
 * Class:        RayleighDist
 * Description:  Rayleigh distribution
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
 * This class extends the class {@link ContinuousDistribution} for
 * the <EM>Rayleigh</EM> distribution with
 *  location parameter <SPAN CLASS="MATH"><I>a</I></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>x</I>-<I>a</I>)/<I>&#946;</I><SUP>2</SUP>&nbsp;<I>e</I><SUP>-(x-a)<SUP>2</SUP>/(2<I>&#946;</I><SUP>2</SUP>)</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;<I>a</I>,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> for <SPAN CLASS="MATH"><I>x</I> &lt; <I>a</I></SPAN>.
 * The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1 - <I>e</I><SUP>-(x-a)<SUP>2</SUP>/(2<I>&#946;</I><SUP>2</SUP>)</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;<I>a</I>,
 * </DIV><P></P>
 * and the inverse distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>x</I> = <I>a</I> + <I>&#946;</I>(-2ln(1-u))<SUP>1/2</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;1.
 * </DIV><P></P>
 * 
 */
public class RayleighDist extends ContinuousDistribution {
   private double a;
   private double beta;





   /**
    * Constructs a <TT>RayleighDist</TT> object with parameters
    *     <SPAN CLASS="MATH"><I>a</I> = 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>.
    * 
    */
   public RayleighDist (double beta) {
      setParams (0.0, beta);
   }


   /**
    * Constructs a <TT>RayleighDist</TT> object with parameters 
    *      <SPAN CLASS="MATH"><I>a</I> =</SPAN> <TT>a</TT>, and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>.
    * 
    */
   public RayleighDist (double a, double beta) {
      setParams (a, beta);
   }


   public double density (double x) {
      return density (a, beta, x);
   }

   public double cdf (double x) {
      return cdf (a, beta, x);
   }

   public double barF (double x) {
      return barF (a, beta, x);
   }

   public double inverseF (double u) {
      return inverseF (a, beta, u);
   }

   public double getMean() {
      return RayleighDist.getMean (a, beta);
   }

   public double getVariance() {
      return RayleighDist.getVariance (beta);
   }

   public double getStandardDeviation() {
      return RayleighDist.getStandardDeviation (beta);
   }

   /**
    * Computes the density function.
    * 
    * @param a the location parameter
    * 
    *    @param beta the scale parameter
    * 
    *    @param x the value at which the density is evaluated
    * 
    *    @return the density function
    * 
    */
   public static double density (double a, double beta, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (x <= a)
         return 0.0;
      final double Z = (x - a)/beta;
      return Z/beta * Math.exp(-Z*Z/2.0);
   }


   /**
    * Same as <TT>density (0, beta, x)</TT>.
    * 
    * @param beta the scale parameter
    * 
    *    @param x the value at which the density is evaluated
    * 
    *    @return returns the density function
    * 
    */
   public static double density (double beta, double x) {
      return density (0.0, beta, x);
   }


   /**
    * Computes the distribution function.
    *  
    * @param a the location parameter
    * 
    *    @param beta the scale parameter
    * 
    *    @param x the value at which the distribution is evaluated
    * 
    *    @return returns the distribution function
    * 
    */
   public static double cdf (double a, double beta, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (x <= a)
         return 0.0;
      final double Z = (x - a)/beta;
      if (Z >= 10.0)
         return 1.0;
      return -Math.expm1(-Z*Z/2.0);
   }


   /**
    * Same as <TT>cdf (0, beta, x)</TT>.
    * 
    * @param beta the scale parameter
    * 
    *    @param x the value at which the distribution is evaluated
    * 
    *    @return returns the distribution function
    * 
    */
   public static double cdf (double beta, double x) {
      return cdf (0.0, beta, x);
   }


   /**
    * Computes  the complementary distribution function.
    *  
    * @param a the location parameter
    * 
    *    @param beta the scale parameter
    * 
    *    @param x the value at which the complementary distribution is evaluated
    * 
    *    @return returns the complementary distribution function
    * 
    */
   public static double barF (double a, double beta, double x) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (x <= a)
         return 1.0;
      double z = (x - a)/beta;
      if (z >= 44.0)
         return 0.0;
      return Math.exp(-z*z/2.0);
   }


   /**
    * Same as <TT>barF (0, beta, x)</TT>.
    * 
    * @param beta the scale parameter
    * 
    *    @param x the value at which the complementary distribution is evaluated
    * 
    *    @return returns the complementary distribution function
    * 
    */
   public static double barF (double beta, double x) {
      return barF (0.0, beta, x);
   }


   /**
    * Computes the inverse of the distribution function.
    *  
    * @param a the location parameter
    * 
    *    @param beta the scale parameter
    * 
    *    @param u the value at which the inverse distribution is evaluated
    * 
    *    @return returns the inverse of the distribution function
    * 
    */
   public static double inverseF (double a, double beta, double u) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (u < 0.0 || u > 1.0)
          throw new IllegalArgumentException ("u not in [0, 1]");
      if (u <= 0.0)
         return a;
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;

      return a + beta * Math.sqrt(-2.0 * Math.log1p(-u));
   }


   /**
    * Same as <TT>inverseF (0, beta, u)</TT>.
    * 
    * @param beta the scale parameter
    * 
    *    @param u the value at which the inverse distribution is evaluated
    * 
    *    @return returns the inverse of the distribution function
    * 
    */
   public static double inverseF (double beta, double u) {
      return inverseF (0.0, beta, u);
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of the Rayleigh distribution
    *    using the maximum likelihood method, assuming that <SPAN CLASS="MATH"><I>a</I></SPAN> is known,
    *    from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    *    The estimate is returned in a one-element array: [<SPAN CLASS="MATH">hat(&beta;)</SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @param a the location parameter
    * 
    *    @return returns the parameter [
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n, double a) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double somme = 0;
      for (int i = 0 ; i < n ; ++i) somme += (x[i]-a)*(x[i]-a);

      double [] parametres = new double [1];
      parametres[0] = Math.sqrt(somme/(2.0*n));
      return parametres;
   }


   /**
    * Creates a new instance of a Rayleigh distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN> and
    *    <SPAN CLASS="MATH">hat(&beta;)</SPAN>. This last is estimated using the maximum likelihood method 
    *    based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @param a the location parameter
    * 
    * 
    */
   public static RayleighDist getInstanceFromMLE (double[] x, int n,
                                                  double a) {
      double parameters[] = getMLE (x, n, a);
      return new RayleighDist (parameters[0], parameters[1]);
   }


   /**
    * Returns the mean 
    * <SPAN CLASS="MATH"><I>a</I> + <I>&#946;</I>(&pi;/2)<SUP>1/2</SUP></SPAN> of the
    *    Rayleigh distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @param a the location parameter
    * 
    *    @param beta the scale parameter
    * 
    *    @return the mean of the Rayleigh distribution
    * 
    */
   public static double getMean (double a, double beta) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      return (a + beta * Math.sqrt(Math.PI/2.0));
   }


   /**
    * Returns the variance
    *    of the Rayleigh distribution with parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @param beta the scale parameter
    * 
    *    @return the variance of the Rayleigh distribution
    * 
    */
   public static double getVariance (double beta) {
      if (beta == 0.0)
        throw new IllegalArgumentException ("beta = 0");
      return (2.0 - 0.5*Math.PI) * beta * beta;
   }


   /**
    * Returns the standard deviation 
    * <SPAN CLASS="MATH"><I>&#946;</I>(2 - &pi;/2)<SUP>1/2</SUP></SPAN> of
    *   the Rayleigh distribution with parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @param beta the scale parameter
    * 
    *    @return the standard deviation of the Rayleigh distribution
    * 
    */
   public static double getStandardDeviation (double beta) {
      return Math.sqrt (RayleighDist.getVariance (beta));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>a</I></SPAN>.
    *   
    * @return the location parameter <SPAN CLASS="MATH"><I>a</I></SPAN>
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    *   
    * @return the scale parameter <SPAN CLASS="MATH"><I>beta</I></SPAN>
    * 
    */
   public double getSigma() {
      return beta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> for this object.
    *   
    * @param a the location parameter
    * 
    *    @param beta the scale parameter
    * 
    * 
    */
   public void setParams (double a, double beta) {
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");
      this.a  = a;
      this.beta  = beta;
      supportA = a;
   }


   /**
    * Return an array containing the parameters of the current distribution
    *    in the order: [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * @return [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>]
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {a, beta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : a = " + a + ", beta = " + beta;
   }

}
