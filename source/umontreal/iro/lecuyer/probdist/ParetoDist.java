

/*
 * Class:        ParetoDist
 * Description:  Pareto distribution
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
 * Extends the class {@link ContinuousDistribution} for a distribution
 * from the <EM>Pareto</EM> family, with
 * shape parameter 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN> and location parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density for this type of Pareto distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#945;&#946;</I><SUP><I>&#945;</I></SUP>/<I>x</I><SUP><I>&#945;</I>+1</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;<I>&#946;</I>,
 * </DIV><P></P>
 * and 0 otherwise.  The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1 - (<I>&#946;</I>/<I>x</I>)<SUP><I>&#945;</I></SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;<I>&#946;</I>,
 * </DIV><P></P>
 * and the inverse distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>&#946;</I>(1 - <I>u</I>)<SUP>-1/<I>&#945;</I></SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I> &lt; 1.
 * </DIV><P></P>
 * 
 */
public class ParetoDist extends ContinuousDistribution {
   private double alpha;
   private double beta;



   /**
    * Constructs a <TT>ParetoDist</TT> object with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>
    *         <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN>.
    * 
    */
   public ParetoDist (double alpha) {
      setParams (alpha, 1.0);
   }


   /**
    * Constructs a <TT>ParetoDist</TT> object with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>
    *         <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>.
    * 
    */
   public ParetoDist (double alpha, double beta) {
      setParams (alpha, beta);
   }


   public double density (double x) {
      return density (alpha, beta, x);
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

   public double getMean() {
      return ParetoDist.getMean (alpha, beta);
   }

   public double getVariance() {
      return ParetoDist.getVariance (alpha, beta);
   }

   public double getStandardDeviation() {
      return ParetoDist.getStandardDeviation (alpha, beta);
   }

   /**
    * Computes the density function.
    * 
    */
   public static double density (double alpha, double beta, double x) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");

      return x < beta ? 0 : alpha*Math.pow (beta/x, alpha)/x;
   }


   /**
    * Computes the distribution function.
    * 
    */
   public static double cdf (double alpha, double beta, double x) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");
      if (x <= beta)
         return 0.0;
      return 1.0 - Math.pow (beta/x, alpha);
   }


   /**
    * Computes the complementary distribution function.
    * 
    */
   public static double barF (double alpha, double beta, double x) {
      if (alpha <= 0)
        throw new IllegalArgumentException ("c <= 0");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");
      if (x <= beta)
         return 1.0;
      return Math.pow (beta/x, alpha);
   }


   /**
    * Computes the inverse of the distribution function.
    * 
    */
   public static double inverseF (double alpha, double beta, double u) {
      if (alpha <= 0)
        throw new IllegalArgumentException ("c <= 0");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");

      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in [0,1]");

      if (u <= 0.0)
         return beta;

      double t;
      t = -Math.log1p (-u);
      if ((u >= 1.0) || t/Math.log(10) >= alpha * Num.DBL_MAX_10_EXP)
         return Double.POSITIVE_INFINITY;

      return beta / Math.pow (1 - u, 1.0/alpha);
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#946;</I>)</SPAN> of the Pareto distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double [] parameters = new double[2];
      parameters[1] = Double.POSITIVE_INFINITY;
      for (int i = 0; i < n; i++) {
         if (x[i] < parameters[1])
            parameters[1] = x[i];
      }

      double sum = 0.0;
      for (int i = 0; i < n; i++) {
         if (x[i] > 0.0)
            sum += Math.log (x[i] / parameters[1]);
         else
            sum -= 709.0;
      }
      parameters[0] = n / sum;
      return parameters;
   }


   /**
    * Creates a new instance of a Pareto distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>
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
   public static ParetoDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new ParetoDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;&#946;</I>/(<I>&#945;</I> - 1)</SPAN>
    *    of the Pareto distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the mean of the Pareto distribution
    * 
    */
   public static double getMean (double alpha, double beta) {
      if (alpha <= 1.0)
         throw new IllegalArgumentException("alpha <= 1");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");

      return ((alpha * beta) / (alpha - 1.0));
   }


   /**
    * Computes and returns the variance
    * of the Pareto distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the variance of the Pareto distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#945;&#946;</I><SUP>2</SUP>/[(<I>&#945;</I> -2)(<I>&#945;</I> - 1)]</SPAN>
    * 
    */
   public static double getVariance (double alpha, double beta) {
      if (alpha <= 2)
         throw new IllegalArgumentException("alpha <= 2");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");

      return ((alpha * beta * beta) / ((alpha - 2.0) * (alpha - 1.0) * (alpha - 1.0)));
   }


   /**
    * Computes and returns the standard deviation of the Pareto
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * @return the standard deviation of the Pareto distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double beta) {
      return Math.sqrt (ParetoDist.getVariance (alpha, beta));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> for this object.
    * 
    */
   public void setParams (double alpha, double beta) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");

      this.alpha = alpha;
      this.beta = beta;
      supportA = beta;
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
