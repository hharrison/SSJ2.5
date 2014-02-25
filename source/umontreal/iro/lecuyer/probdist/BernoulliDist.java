

/*
 * Class:        BernoulliDist
 * Description:  Bernoulli distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        August 2010

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
 * Extends the class {@link DiscreteDistributionInt} for the <SPAN  CLASS="textit">Bernoulli</SPAN>
 * distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>, where 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>p</I>&nbsp;&lt;=&nbsp;1</SPAN>.
 * Its mass function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1 - <I>p</I>,</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>x</I> = 0;</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT"><I>p</I>,</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>x</I> = 1;</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">0,</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; otherwise. </TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * Its distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">0,</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>x</I> &lt; 0;</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1 - <I>p</I>,</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if 0&nbsp;&lt;=&nbsp;<I>x</I> &lt; 1;</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1,</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>x</I>&nbsp;&gt;=&nbsp;1.</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * 
 */
public class BernoulliDist extends DiscreteDistributionInt {
   private double p;
   private double q;



   /**
    * Creates a Bernoulli distribution object.
    * 
    */
   public BernoulliDist (double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in [0,1]");
      this.p = p;
      q = 1 - p;
      supportA = 0;
      supportB = 1;
   }

   public double prob (int x) {
      if (1 == x) return p;
      if (0 == x) return q;
      return 0.0;
   }

   public double cdf (int x) {
      if (x < 0) return 0.0;
      if (x < 1) return q;
      return 1.0;
   }

   public double barF (int x) {
      if (x > 1) return 0.0;
      if (x > 0) return p;
      return 1.0;
   }

   public int inverseFInt (double u) {
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u > q) return 1;
      return 0;
   }

   public double getMean() {
      return BernoulliDist.getMean (p);
   }

   public double getVariance() {
      return BernoulliDist.getVariance (p);
   }

   public double getStandardDeviation() {
      return BernoulliDist.getStandardDeviation (p);
   }



   /**
    * Returns the Bernoulli probability <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>
    * (see eq.).
    * 
    */
   public static double prob (double p, int x) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in [0,1]");
      if (1 == x) return p;
      if (0 == x) return 1.0 - p;
      return 0.0;
   }


   /**
    * Returns the Bernoulli distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>
    *  with parameter <SPAN CLASS="MATH"><I>p</I></SPAN> (see eq.).
    * 
    */
   public static double cdf (double p, int x) {
      if (p < 0.0 | p > 1.0)
         throw new IllegalArgumentException ("p not in [0,1]");
      if (x < 0) return 0.0;
      if (x < 1) return 1.0 - p;
      return 1.0;
   }


   /**
    * Returns the complementary Bernoulli distribution
    *   function 
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>]</SPAN> with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    */
   public static double barF (double p, int x) {
      if (p < 0.0 | p > 1.0)
         throw new IllegalArgumentException ("p not in [0,1]");
      if (x > 1) return 0.0;
      if (x > 0) return p;
      return 1.0;
   }


   /**
    * Returns the inverse of the Bernoulli distribution function
    *  with parameter <SPAN CLASS="MATH"><I>p</I></SPAN> at <SPAN CLASS="MATH"><I>u</I></SPAN>.
    * 
    */
   public static int inverseF (double p, double u)  {
      if (p < 0.0 | p > 1.0)
         throw new IllegalArgumentException ("p not in [0,1]");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u > 1.0 - p) return 1;
      return 0;
    }


   /**
    * Estimates the parameters <SPAN CLASS="MATH"><I>p</I></SPAN> of the Bernoulli distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>m</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>. The estimate is returned in a one-element
    *     array: [<SPAN CLASS="MATH"><I>p</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param m the number of observations used to evaluate parameters
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH">hat(p)</SPAN>]
    * 
    */
   public static double[] getMLE (int[] x, int m) {
      if (m < 2)
         throw new UnsupportedOperationException(" m < 2");

      // compute the empirical mean
      double sum = 0.0;
      for (int i = 0; i < m; i++)
         sum += x[i];
      sum /= (double) m;

      double param[] = new double[1];
      param[0] = sum;
      return param;
   }


   /**
    * Creates a new instance of a Bernoulli distribution with parameter
    *    <SPAN CLASS="MATH"><I>p</I></SPAN> estimated using the maximum likelihood method, from
    *    the <SPAN CLASS="MATH"><I>m</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>,  
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to estimate the parameters
    * 
    *    @param m the number of observations to use to estimate the parameters
    * 
    * 
    */
   public static BernoulliDist getInstanceFromMLE (int[] x, int m) {
      double param[] = new double[1];
      param = getMLE (x, m);
      return new BernoulliDist (param[0]);
   }


   /**
    * Returns the mean <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>p</I></SPAN> of the Bernoulli distribution with
    *    parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the mean of the Bernoulli distribution <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>np</I></SPAN>
    * 
    */
   public static double getMean (double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in [0, 1]");

      return (p);
   }


   /**
    * Computes the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>p</I>(1 - <I>p</I>)</SPAN> of the Bernoulli
    *    distribution with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the variance of the Bernoulli distribution
    * 
    */
   public static double getVariance (double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in [0, 1]");
      return (p * (1.0 - p));
   }


   /**
    * Computes the standard deviation of the Bernoulli distribution
    *    with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the standard deviation of the Bernoulli distribution
    * 
    */
   public static double getStandardDeviation (double p) {
      return Math.sqrt (BernoulliDist.getVariance (p));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   public double getP() {
      return p;
   }


   /**
    * Returns an array that contains the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of the current
    *    distribution: [<SPAN CLASS="MATH"><I>p</I></SPAN>].
    * 
    */
   public double[] getParams () {
      double[] retour = {p};
      return retour;
   }


   /**
    * Resets the parameter to this new value.
    * 
    * 
    */
   public void setParams (double p) {
      this.p = p;
      q = 1 - p;
   }


   public String toString () {
      return getClass().getSimpleName() + " : p = " + p;
   }

}
