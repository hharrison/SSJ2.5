

/*
 * Class:        PowerDist
 * Description:  power distribution
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
 * the <EM>power</EM> distribution with shape parameter
 * <SPAN CLASS="MATH"><I>c</I> &gt; 0</SPAN>, over the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>, where <SPAN CLASS="MATH"><I>a</I> &lt; <I>b</I></SPAN>.
 * It has density 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>c</I>(<I>x</I> - <I>a</I>)<SUP>c-1</SUP>/(<I>b</I> - <I>a</I>)<SUP>c</SUP>
 * </DIV><P></P>
 * for <SPAN CLASS="MATH"><I>a</I> &lt; <I>x</I> &lt; <I>b</I></SPAN>, and 0 elsewhere.  It has distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = (<I>x</I> - <I>a</I>)<SUP>c</SUP>/(<I>b</I> - <I>a</I>)<SUP>c</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>,
 * </DIV><P></P>
 * with <SPAN CLASS="MATH"><I>F</I>(<I>x</I>) = 0</SPAN> for <SPAN CLASS="MATH"><I>x</I>&nbsp;&lt;=&nbsp;<I>a</I></SPAN> and  <SPAN CLASS="MATH"><I>F</I>(<I>x</I>) = 1</SPAN> for <SPAN CLASS="MATH"><I>x</I>&nbsp;&gt;=&nbsp;<I>b</I></SPAN>.
 * 
 */
public class PowerDist extends ContinuousDistribution {
   private double a;
   private double b;
   private double c;




   /**
    * Constructs a <TT>PowerDist</TT> object with parameters 
    *      <SPAN CLASS="MATH"><I>a</I> =</SPAN> <TT>a</TT>, <SPAN CLASS="MATH"><I>b</I> =</SPAN> <TT>b</TT> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>.
    * 
    */
   public PowerDist (double a, double b, double c) {
      setParams (a, b, c);
   }


   /**
    * Constructs a <TT>PowerDist</TT> object with parameters 
    *      <SPAN CLASS="MATH"><I>a</I> = 0</SPAN>, <SPAN CLASS="MATH"><I>b</I> =</SPAN> <TT>b</TT> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>.
    * 
    */
   public PowerDist (double b, double c) {
      setParams (0.0, b, c);
   }


   /**
    * Constructs a <TT>PowerDist</TT> object with parameters 
    *      <SPAN CLASS="MATH"><I>a</I> = 0</SPAN>, <SPAN CLASS="MATH"><I>b</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>.
    * 
    */
   public PowerDist (double c) {
      setParams (0.0, 1.0, c);
   }


   public double density (double x) {
      return density (a, b, c, x);
   }

   public double cdf (double x) {
      return cdf (a, b, c, x);
   }

   public double barF (double x) {
      return barF (a, b, c, x);
   }

   public double inverseF (double u) {
      return inverseF (a, b, c, u);
   }

   public double getMean() {
      return PowerDist.getMean (a, b, c);
   }

   public double getVariance() {
      return PowerDist.getVariance (a, b, c);
   }

   public double getStandardDeviation() {
      return PowerDist.getStandardDeviation (a, b, c);
   }

   /**
    * Computes the density function.
    * 
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    *    @param x the value at which the density is evaluated
    * 
    *    @return returns the density function
    * 
    */
   public static double density (double a, double b, double c, double x) {
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (x <= a)
         return 0.0;
      if (x >= b)
         return 0.0;
      double z = (x-a)/(b-a);
      return c*Math.pow(z, c-1.0) / (b-a);
   }


   /**
    * Computes the distribution function.
    *  
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    *    @param x the value at which the distribution is evaluated
    * 
    *    @return returns the distribution function
    * 
    */
   public static double cdf (double a, double b, double c, double x) {
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (x <= a)
         return 0.0;
      if (x >= b)
         return 1.0;
      return Math.pow((x-a)/(b-a), c);
   }


   /**
    * Computes  the complementary distribution function.
    *  
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    *    @param x the value at which the complementary distribution is evaluated
    * 
    *    @return returns the complementary distribution function
    * 
    */
   public static double barF (double a, double b, double c, double x) {
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (x <= a)
         return 1.0;
      if (x >= b)
         return 0.0;
      return 1.0 - Math.pow((x-a)/(b-a),c);
   }


   /**
    * Computes  the inverse of the distribution function.
    *  
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    *    @param u the value at which the inverse distribution is evaluated
    * 
    *    @return returns the inverse of the distribution function
    * 
    */
   public static double inverseF (double a, double b, double c, double u) {
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      if (u < 0.0 || u > 1.0)
          throw new IllegalArgumentException ("u not in [0, 1]");
      if (u == 0.0)
         return a;
      if (u == 1.0)
         return b;

      return a + (b-a) * Math.pow(u,1.0/c);
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>c</I></SPAN> of the power distribution from the <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>, using the maximum
    *    likelihood method and assuming that <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN> are known. 
    *    The estimate is returned in a one-element array: [<SPAN CLASS="MATH"><I>c</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @return returns the shape parameter [<SPAN CLASS="MATH">hat(c)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n, double a, double b) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double d = b - a;
      double somme = 0;
      for (int i = 0 ; i < n ; ++i) somme += Math.log((x[i] - a)/d);

      double [] parametres = new double [1];
      parametres[0] = -1.0 / (somme/n);
      return parametres;
   }


   /**
    * Creates a new instance of a power distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN>,
    *    with <SPAN CLASS="MATH"><I>c</I></SPAN> estimated using the maximum likelihood method based on the 
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    * 
    */
   public static PowerDist getInstanceFromMLE (double[] x, int n,
                                               double a, double b) {
      double parameters[] = getMLE (x, n, a, b);
      return new PowerDist (a, b, parameters[0]);
   }


   /**
    * Returns the mean 
    * <SPAN CLASS="MATH"><I>a</I> + (<I>b</I> - <I>a</I>)<I>c</I>/(<I>c</I> + 1)</SPAN> of the power distribution
    * with parameters  <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN>.
    * 
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    *    @return returns the mean
    * 
    */
   public static double getMean (double a, double b, double c) {
      return a + (b-a) * c / (c+1.0);
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">(<I>b</I> - <I>a</I>)<SUP>2</SUP><I>c</I>/[(<I>c</I> + 1)<SUP>2</SUP>(<I>c</I> + 2)]</SPAN>
    *    of the power distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN>.
    * 
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    *    @return returns the variance
    * 
    */
   public static double getVariance (double a, double b, double c) {
      return (b-a)*(b-a)*c / ((c+1.0)*(c+1.0)*(c+2.0));
   }


   /**
    * Computes and returns the standard deviation
    *    of the power distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN>.
    * 
    * @return the standard deviation of the power distribution
    * 
    */
   public static double getStandardDeviation (double a, double b, double c) {
      return Math.sqrt (PowerDist.getVariance (a, b, c));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>a</I></SPAN>.
    *   
    * @return the left limit of interval <SPAN CLASS="MATH"><I>a</I></SPAN>
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>b</I></SPAN>.
    *   
    * @return the right limit of interval <SPAN CLASS="MATH"><I>b</I></SPAN>
    * 
    */
   public double getB() {
      return b;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>c</I></SPAN>.
    *   
    * @return the shape parameter <SPAN CLASS="MATH"><I>c</I></SPAN>
    * 
    */
   public double getC() {
      return c;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN> for this object.
    *   
    * @param a left limit of interval
    * 
    *    @param b right limit of interval
    * 
    *    @param c shape parameter
    * 
    * 
    */
   public void setParams (double a, double b, double c) {
      this.a  = a;
      this.b  = b;
      this.c  = c;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>c</I></SPAN>].
    * 
    * @return [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I>,</SPAN>c]
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {a, b, c};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : a = " + a + " : b = " + b + " : c = " + c;
   }

}
