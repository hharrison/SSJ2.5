

/*
 * Class:        NakagamiDist
 * Description:  Nakagami distribution
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

package  umontreal.iro.lecuyer.probdist;

import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.functions.MathFunction;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>Nakagami</EM> distribution with location parameter <SPAN CLASS="MATH"><I>a</I></SPAN>,
 * scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN> and shape parameter <SPAN CLASS="MATH"><I>c</I> &gt; 0</SPAN>.
 * The density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 2<I>&#955;</I><SUP>c</SUP>/<I>&#915;</I>(<I>c</I>) &nbsp;(<I>x</I> - <I>a</I>)<SUP>2c-1</SUP> &nbsp;<I>e</I><SUP>-<I>&#955;</I>(x-a)<SUP>2</SUP></SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; <I>a</I>,
 * </DIV><P></P>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I>&nbsp;&lt;=&nbsp;<I>a</I>,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#915;</I></SPAN> is the gamma function.
 * 
 */
public class NakagamiDist extends ContinuousDistribution {
   protected double a;              // Location parameter
   protected double lambda;         // Scale parameter
   protected double c;              // Shape parameter
   private double factor;
   private double ratio;            // Gamma(c + 1/2)/Gamma(c)



   /**
    * Constructs a <TT>NakagamiDist</TT> object with parameters <SPAN CLASS="MATH"><I>a</I> =</SPAN>
    *    <TT>a</TT>,  <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>.
    * 
    */
   public NakagamiDist (double a, double lambda, double c) {
      setParams (a, lambda, c);
   }


   public double density (double x) {
      if (x <= a) return 0.0;
      return 2.0 * Math.exp( factor
                             + Math.log(x-a)*(2.0*c-1.0)
                             - lambda*(x-a)*(x-a) );
   }

   public double cdf (double x) {
      return cdf (a, lambda, c, x);
   }

   public double barF (double x) {
      return barF (a, lambda, c, x);
   }

   public double inverseF (double u) {
      return inverseF (a, lambda, c, u);
   }

   public double getMean() {
      return a + ratio/Math.sqrt(lambda);
   }

   public double getVariance() {
      return (c - ratio*ratio)/lambda;
   }

   public double getStandardDeviation() {
      return Math.sqrt(getVariance ());
   }

   /**
    * Computes the density function of the <EM>Nakagami</EM> distribution.
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @param x the value at which the density is evaluated
    * 
    *    @return returns the density function
    * 
    */
   public static double density (double a, double lambda, double c,
                                 double x) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (x <= a)
         return 0.0;

      return 2.0 * Math.exp( Math.log(lambda)*c - Num.lnGamma(c)
                             +  Math.log(x-a)*(2.0*c-1.0)
                             - lambda*(x-a)*(x-a) );
   }


   /**
    * Computes the distribution function.
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @param x the value at which the distribution is evaluated
    * 
    *    @return returns the cdf function
    * 
    */
   public static double cdf (double a, double lambda, double c, double x) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (x <= a)
         return 0.0;

      return GammaDist.cdf(c, 12, lambda*(x-a)*(x-a));
   }


   /**
    * Computes the complementary distribution function.
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @param x the value at which the complementary distribution is evaluated
    * 
    *    @return returns the complementary distribution function
    * 
    */
   public static double barF (double a, double lambda, double c, double x) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (x <= a)
         return 1.0;
      return GammaDist.barF(c, 12, lambda*(x-a)*(x-a));
   }


   /**
    * Computes the inverse of the distribution function.
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @param u the value at which the inverse distribution is evaluated
    * 
    *    @return returns the inverse distribution function
    * 
    */
   public static double inverseF (double a, double lambda, double c,
                                  double u) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (u > 1.0 || u < 0.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= 0.0) return a;
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;
      double res = GammaDist.inverseF(c, 12, u);
      return a + Math.sqrt(res/lambda);
   }


   /**
    * .
    * 
    * Computes and returns the mean
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>E</I>[<I>X</I>] = <I>a</I> + <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="NakagamiDistimg1.png"
    *  ALT="$\displaystyle {\frac{{1}}{{\sqrt{\lambda}}}}$"> &nbsp;<IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="NakagamiDistimg2.png"
    *  ALT="$\displaystyle {\frac{{\Gamma(c+1/2)}}{{\Gamma(c)}}}$">.
    * </DIV><P></P>
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @return returns the mean
    * 
    */
   public static double getMean (double a, double lambda, double c) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      return a + Num.gammaRatioHalf(c) / Math.sqrt(lambda);
   }


   /**
    * .
    * 
    * Computes and returns the variance
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * Var[<I>X</I>] = <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="NakagamiDistimg3.png"
    *  ALT="$\displaystyle {\frac{{1}}{{\lambda}}}$">[<I>c</I> - ([tex2html_wrap_indisplay259])<SUP>2</SUP>].
    * </DIV><P></P>
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @return returns the variance
    * 
    */
   public static double getVariance (double a, double lambda, double c) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      double rat = Num.gammaRatioHalf(c);
      return (c - rat*rat) / lambda;
   }


   /**
    * Computes the standard deviation of the Nakagami distribution with
    *    parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN>.
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @return returns the standard deviation
    * 
    */
   public static double getStandardDeviation (double a, double lambda,
                                              double c)  {
      return Math.sqrt (NakagamiDist.getVariance (a, lambda, c));
   }


   /**
    * Returns the location parameter <SPAN CLASS="MATH"><I>a</I></SPAN> of this object.
    *   
    * @return returns the location parameter
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the scale parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    *   
    * @return returns the scale parameter
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Returns the shape parameter <SPAN CLASS="MATH"><I>c</I></SPAN> of this object.
    *   
    * @return returns the shape parameter
    * 
    */
   public double getC() {
      return c;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN> of this object.
    * 
    * @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    * 
    */
    public void setParams (double a, double lambda, double c) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      this.a = a;
      this.lambda = lambda;
      this.c = c;
      factor = (Math.log(lambda)*c - Num.lnGamma(c));
      ratio = Num.gammaRatioHalf(c);
    } 


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>, <SPAN CLASS="MATH"><I>c</I></SPAN>].
    * 
    * @return returns the parameters [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>, <SPAN CLASS="MATH"><I>c</I></SPAN>]
    * 
    */
   public double[] getParams () {
      double[] retour = {a, lambda, c};
      return retour;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    * @return returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString () {
      return getClass().getSimpleName() + " : a = " + a + ", lambda = " + lambda
                                  + ", c = " + c;
   }

}
