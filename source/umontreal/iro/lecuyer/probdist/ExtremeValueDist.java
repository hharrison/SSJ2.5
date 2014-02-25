

/*
 * Class:        ExtremeValueDist
 * Description:  Gumbel distribution
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
import umontreal.iro.lecuyer.util.RootFinder;
import umontreal.iro.lecuyer.functions.MathFunction;

@Deprecated
/**
 * <SPAN  CLASS="textbf">This class has been replaced by {@link GumbelDist}</SPAN>.
 * 
 * <P>
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>extreme value</EM> (or <SPAN  CLASS="textit">Gumbel</SPAN>) distribution, with location parameter
 * <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * It has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#955;e</I><SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP><I>e</I><SUP>-e<SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP></SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>e</I><SUP>-e<SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP></SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;,
 * </DIV><P></P>
 * and inverse distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = - ln(- ln(<I>u</I>))/<I>&#955;</I> + <I>&#945;</I>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;1.
 * </DIV><P></P>
 * 
 */
public class ExtremeValueDist extends ContinuousDistribution {
   private double alpha;
   private double lambda;

   private static class Function implements MathFunction {
      protected int n;
      protected double mean;
      protected double[] x;

      public Function (double[] x, int n, double mean) {
         this.n = n;
         this.mean = mean;
         this.x = new double[n];

         System.arraycopy(x, 0, this.x, 0, n);
      }

      public double evaluate (double lambda) {
         if (lambda <= 0.0) return 1.0e200;
         double exp = 0.0;
         double sumXiExp = 0.0;
         double sumExp = 0.0;

         for (int i = 0; i < n; i++)
         {
            exp = Math.exp (-x[i] * lambda);
            sumExp += exp;
            sumXiExp += x[i] * exp;
         }

         return ((mean - 1.0 / lambda) * sumExp - sumXiExp);
      }
   }



   /**
    * <SPAN  CLASS="textbf">THIS CLASS HAS BEEN REPLACED BY {@link GumbelDist}</SPAN>.
    *    Constructs a <TT>ExtremeValueDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = 0 and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = 1.
    * 
    */
   public ExtremeValueDist() {
      setParams (0.0, 1.0);
   }


   /**
    * <SPAN  CLASS="textbf">THIS CLASS HAS BEEN REPLACED BY {@link GumbelDist}</SPAN>.
    *    Constructs a <TT>ExtremeValueDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>.
    * 
    */
   public ExtremeValueDist (double alpha, double lambda) {
      setParams (alpha, lambda);
   }


   public double density (double x) {
      return density (alpha, lambda, x);
   }

   public double cdf (double x) {
      return cdf (alpha, lambda, x);
   }

   public double barF (double x) {
      return barF (alpha, lambda, x);
   }

   public double inverseF (double u) {
      return inverseF (alpha, lambda, u);
   }

   public double getMean() {
      return ExtremeValueDist.getMean (alpha, lambda);
   }

   public double getVariance() {
      return ExtremeValueDist.getVariance (alpha, lambda);
   }

   public double getStandardDeviation() {
      return ExtremeValueDist.getStandardDeviation (alpha, lambda);
   }

   /**
    * Computes the density function.
    * 
    */
   public static double density (double alpha, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      final double z = lambda*(x - alpha);
      if (z <= -10.0)
         return 0.0;
      double t = Math.exp (-z);
      return lambda * t * Math.exp (-t);
   }


   /**
    * <SPAN  CLASS="textbf">THIS CLASS HAS BEEN REPLACED BY {@link GumbelDist}</SPAN>.
    *  Computes  the distribution function.
    * 
    */
   public static double cdf (double alpha, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      final double z = lambda*(x - alpha);
      if (z <= -10.0)
         return 0.0;
      if (z >= XBIG)
         return 1.0;
      return Math.exp (-Math.exp (-z));
   }


   /**
    * Computes  the complementary distribution function.
    * 
    */
   public static double barF (double alpha, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      final double z = lambda*(x - alpha);
      if (z <= -10.0)
         return 1.0;
      if (z >= XBIGM)
         return 0.0;
      return -Math.expm1 (-Math.exp (-z));
   }


   /**
    * Computes the inverse distribution function.
    * 
    */
   public static double inverseF (double alpha, double lambda, double u) {
       if (u < 0.0 || u > 1.0)
          throw new IllegalArgumentException ("u not in [0, 1]");
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
       if (u >= 1.0)
           return Double.POSITIVE_INFINITY;
       if (u <= 0.0)
           return Double.NEGATIVE_INFINITY;

       return -Math.log (-Math.log (u))/lambda+alpha;
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#955;</I>)</SPAN> of the extreme value distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&lambda;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[] = new double[2];

      double sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += x[i];
      double mean = sum / (double) n;

      sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += (x[i] - mean) * (x[i] - mean);
      double variance = sum / ((double) n - 1.0);

      double lambda0 = Math.PI / Math.sqrt (6 * variance);

      Function f = new Function (x, n, mean);

      double a;
      if ((a = lambda0 - 10.0) < 0)
         a = 1e-15;
      parameters[1] = RootFinder.brentDekker (a, lambda0 + 10.0, f, 1e-7);

      double sumExp = 0.0;
      for (int i = 0; i < n; i++)
         sumExp += Math.exp (- x[i] * parameters[1]);
      parameters[0] = - Math.log (sumExp / (double) n) / parameters[1];

      return parameters;
   }


   /**
    * Same as {@link #getMLE getMLE}.
    * 
    */
   @Deprecated
   public static double[] getMaximumLikelihoodEstimate (double[] x, int n) {
      return getMLE(x, n);
   }


   /**
    * Creates a new instance of an extreme value distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and
    *    <SPAN CLASS="MATH"><I>&#955;</I></SPAN> estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static ExtremeValueDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new ExtremeValueDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean,  
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;</I> + <I>&#947;</I>/<I>&#955;</I></SPAN>,
    *    of the extreme value distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>,
    *   where 
    * <SPAN CLASS="MATH"><I>&#947;</I> = 0.5772156649</SPAN> is the Euler-Mascheroni constant.
    * 
    * @return the mean of the Extreme Value distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;</I> + <I>&#947;</I>/<I>&#955;</I></SPAN>
    * 
    */
   public static double getMean (double alpha, double lambda) {
     if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return (alpha + Num.EULER / lambda);
   }


   /**
    * Computes and returns the variance, 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#960;</I><SUP>2</SUP>/(6<I>&#955;</I><SUP>2</SUP>)</SPAN>,
    *    of the extreme value distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the variance of the extreme value distribution 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = 1/6<I>&#960;</I><SUP>2</SUP>1/<I>&#955;</I><SUP>2</SUP></SPAN>
    * 
    */
   public static double getVariance (double alpha, double lambda) {
     if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return ((1.0 / 6.0 * Math.PI * Math.PI) * (1.0 / (lambda * lambda)));
   }


   /**
    * Computes and returns the standard deviation
    *    of the extreme value distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the standard deviation of the extreme value distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double lambda) {
     if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");

      return (Math.sqrt(1.0 / 6.0) * Math.PI / lambda);
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   public double getLambda() {
      return lambda;
   }



   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   public void setParams (double alpha, double lambda) {
     if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      this.alpha  = alpha;
      this.lambda = lambda;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha, lambda};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : alpha = " + alpha + ", lambda = " + lambda;
   }

}
