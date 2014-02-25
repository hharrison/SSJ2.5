

/*
 * Class:        GeometricDist
 * Description:  geometric distribution
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

import  umontreal.iro.lecuyer.util.Num;

/**
 * Extends the class {@link DiscreteDistributionInt} for
 * the <EM>geometric</EM> distribution with parameter
 * <SPAN CLASS="MATH"><I>p</I></SPAN>, where <SPAN CLASS="MATH">0 &lt; <I>p</I> &lt; 1</SPAN>.
 * Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = <I>p</I>&nbsp;(1 - <I>p</I>)<SUP>x</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,&#8230;
 * </DIV><P></P>
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1 - (1 - <I>p</I>)<SUP>x+1</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,&#8230;
 * </DIV><P></P>
 * and its inverse is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = floor(ln(1 - <I>u</I>)/ln(1 - <I>p</I>)),&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I> &lt; 1.
 * </DIV><P></P>
 * 
 */
public class GeometricDist extends DiscreteDistributionInt {

   private double p;
   private double vp;


   /**
    * Constructs a geometric distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    */
   public GeometricDist (double p) {
      setP(p);
   }


   public double prob (int x) {
      return prob (p, x);
   }

   public double cdf (int x) {
      return cdf (p, x);
   }

   public double barF (int x) {
      return barF (p, x);
   }

   public int inverseFInt (double u) {
        if (u > 1.0 || u < 0.0)
            throw new IllegalArgumentException ("u not in [0,1]");

        if (p >= 1.0)
            return 0;
        if (u <= p)
            return 0;
        if (u >= 1.0 || p <= 0.0)
            return Integer.MAX_VALUE;

        return (int)Math.floor (Math.log1p(-u)/vp);
   }

   public double getMean() {
      return GeometricDist.getMean (p);
   }

   public double getVariance() {
      return GeometricDist.getVariance (p);
   }

   public double getStandardDeviation() {
      return GeometricDist.getStandardDeviation (p);
   }



   /**
    * Computes the geometric probability <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double prob (double p, int x) {
      if (p < 0 || p > 1)
         throw new IllegalArgumentException ("p not in range (0,1)");
      if (p <= 0)
         return 0;
      if (p >= 1)
         return 0;
      if (x < 0)
         return 0;
      return p*Math.pow (1 - p, x);
   }


   /**
    * Computes the distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double cdf (double p, int x) {
      if (p < 0.0 || p > 1.0)
        throw new IllegalArgumentException ("p not in [0,1]");
      if (x < 0)
         return 0.0;
      if (p >= 1.0)                  // In fact, p == 1
         return 1.0;
      if (p <= 0.0)                  // In fact, p == 0
         return 0.0;
      return 1.0 - Math.pow (1.0 - p, (double)x + 1.0);
   }


   /**
    * Computes the complementary distribution function.
    * <SPAN  CLASS="textit">WARNING:</SPAN> The complementary distribution function is defined as
    * 
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>]</SPAN>.
    * 
    */
   public static double barF (double p, int x) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in [0,1]");
      if (x < 0)
         return 1.0;
      if (p >= 1.0)                  // In fact, p == 1
         return 0.0;
      if (p <= 0.0)                  // In fact, p == 0
         return 1.0;

      return Math.pow (1.0 - p, x);
   }


   /**
    * Computes the inverse of the geometric
    *  distribution.
    * 
    */
   public static int inverseF (double p, double u) {
        if (p > 1.0 || p < 0.0)
            throw new IllegalArgumentException ( "p not in [0,1]");
        if (u > 1.0 || u < 0.0)
            throw new IllegalArgumentException ("u not in [0,1]");
        if (p >= 1.0)
            return 0;
        if (u <= p)
           return 0;
        if (u >= 1.0 || p <= 0.0)
            return Integer.MAX_VALUE;

         double v = Math.log1p (-p);
         return (int)Math.floor (Math.log1p (-u)/v);
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of the geometric distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimate is returned in element 0
    *    of the returned array.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH">hat(p)</SPAN>]
    * 
    */
   public static double[] getMLE (int[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[];
      parameters = new double[1];
      double sum = 0.0;
      for (int i = 0; i < n; i++) {
         sum += x[i];
      }

      parameters[0] = 1.0 / (((double) sum / (double) n) + 1.0);

      return parameters;
   }


   /**
    * Creates a new instance of a geometric distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>
    *    estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static GeometricDist getInstanceFromMLE (int[] x, int n) {

      double parameters[] = getMLE (x, n);

      return new GeometricDist (parameters[0]);
   }


   /**
    * Computes and returns the mean  
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (1 - <I>p</I>)/<I>p</I></SPAN> of the
    *    geometric distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the mean of the geometric distribution 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (1 - <I>p</I>)/<I>p</I></SPAN>
    * 
    */
   public static double getMean (double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range (0,1)");

      return (1 - p) / p;
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = (1 - <I>p</I>)/<I>p</I><SUP>2</SUP></SPAN>
    *    of the geometric distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the variance of the Geometric distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = (1 - <I>p</I>)/<I>p</I><SUP>2</SUP></SPAN>
    * 
    */
   public static double getVariance (double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range (0,1)");

      return ((1 - p) / (p * p));
   }


   /**
    * Computes and returns the standard deviation of the geometric
    *    distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the standard deviation of the geometric distribution
    * 
    */
   public static double getStandardDeviation (double p) {
      return Math.sqrt (GeometricDist.getVariance (p));
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>p</I></SPAN> associated with this object.
    * 
    */
   public double getP() {
      return p;
   }


   /**
    * Resets the value of <SPAN CLASS="MATH"><I>p</I></SPAN> associated with this object.
    * 
    */
   public void setP (double p) {
      if (p < 0 || p > 1)
         throw new IllegalArgumentException ("p not in range (0,1)");
      vp = Math.log1p (-p);
      this.p = p;
      supportA = 0;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {p};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : p = " + p;
   }

}
