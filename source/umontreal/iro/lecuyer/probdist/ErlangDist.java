

/*
 * Class:        ErlangDist
 * Description:  Erlang distribution
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

/**
 * Extends the class {@link GammaDist} for the special case
 * of the <EM>Erlang</EM> distribution with
 * shape parameter <SPAN CLASS="MATH"><I>k</I> &gt; 0</SPAN> and scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * This distribution is a special case of the gamma distribution
 * for which the shape parameter <SPAN CLASS="MATH"><I>k</I> = <I>&#945;</I></SPAN> is an integer.
 * 
 */
public class ErlangDist extends GammaDist {


   /**
    * Constructs a <TT>ErlangDist</TT> object with parameters
    *   <SPAN CLASS="MATH"><I>k</I></SPAN> = <TT>k</TT> and <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN>.
    * 
    */
   public ErlangDist (int k) {
      super (k);
   }


   /**
    * Constructs a <TT>ErlangDist</TT> object with parameters
    *   <SPAN CLASS="MATH"><I>k</I></SPAN> = <TT>k</TT>  and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>.
    * 
    */
   public ErlangDist (int k, double lambda) {
      super (k, lambda);
   }


   /**
    * Computes the density function.
    * 
    */
   public static double density (int k, double lambda, double x) {
      return density ((double)k, lambda, x);
   }


   /**
    * Computes the distribution function using
    *    the gamma distribution function.
    * 
    */
   public static double cdf (int k, double lambda, int d, double x) {
      return cdf ((double)k, d, lambda*x);
   }


   /**
    * Computes the complementary distribution function.
    * 
    */
   public static double barF (int k, double lambda, int d, double x) {
      return barF ((double)k, d, lambda*x);
   }


   /**
    * Returns the inverse distribution function.
    * 
    */
   public static double inverseF (int k, double lambda, int d, double u) {
      return inverseF ((double)k, lambda, d, u);
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>k</I>, <I>&#955;</I>)</SPAN> of the Erlang distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>k</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(k)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&lambda;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      double parameters[] = GammaDist.getMLE (x, n);
      parameters[0] = Math.round (parameters[0]);
      return parameters;
   }


   /**
    * Creates a new instance of an Erlang distribution with parameters <SPAN CLASS="MATH"><I>k</I></SPAN> and
    *    <SPAN CLASS="MATH"><I>&#955;</I></SPAN> estimated
    *    using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>,
    *    
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static ErlangDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new ErlangDist ((int) parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean, 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>k</I>/<I>&#955;</I></SPAN>,
    *    of the Erlang distribution with parameters <SPAN CLASS="MATH"><I>k</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the mean of the Erlang distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>k</I>/<I>&#955;</I></SPAN>
    * 
    */
   public static double getMean (int k, double lambda) {
      if (k <= 0)
         throw new IllegalArgumentException ("k <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      return (k / lambda);
   }


   /**
    * Computes and returns the variance, 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>k</I>/<I>&#955;</I><SUP>2</SUP></SPAN>,
    *    of the Erlang distribution with parameters <SPAN CLASS="MATH"><I>k</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the variance of the Erlang distribution 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>k</I>/<I>&#955;</I><SUP>2</SUP></SPAN>
    * 
    */
   public static double getVariance (int k, double lambda) {
      if (k <= 0)
         throw new IllegalArgumentException ("k <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return (k / (lambda * lambda));
   }


   /**
    * Computes and returns the standard deviation of the Erlang
    *    distribution with parameters <SPAN CLASS="MATH"><I>k</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the standard deviation of the Erlang distribution
    * 
    */
   public static double getStandardDeviation (int k, double lambda) {
      if (k <= 0)
         throw new IllegalArgumentException ("k <= 0");
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return (Math.sqrt (k) / lambda);
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>k</I></SPAN> for this object.
    * 
    */
   public int getK() {
      return (int) getAlpha();
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>k</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of the distribution for this
    *   object.  Non-static methods are computed with a rough target of
    *   <TT>d</TT> decimal digits of precision.
    * 
    */
   public void setParams (int k, double lambda, int d) {
      setParams ((double)k, lambda, d);
   }


   /**
    * Return a table containing parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>k</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      return super.getParams();
   }


   public String toString () {
      return getClass().getSimpleName() + " : k = " + (int)super.getAlpha() + ", lambda = " + super.getLambda();
   }

}
