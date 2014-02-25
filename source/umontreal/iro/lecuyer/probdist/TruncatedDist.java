

/*
 * Class:        TruncatedDist
 * Description:  an arbitrary continuous distribution truncated
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
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
import umontreal.iro.lecuyer.functions.MathFunctionUtil;
import umontreal.iro.lecuyer.functions.MathFunction;


/**
 * This container class takes an arbitrary continuous distribution and truncates
 * it to an interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>, where <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN> can be finite or infinite.
 * If the original density and distribution function are <SPAN CLASS="MATH"><I>f</I><SUB>0</SUB></SPAN> and <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB></SPAN>,
 * the new ones are <SPAN CLASS="MATH"><I>f</I></SPAN> and <SPAN CLASS="MATH"><I>F</I></SPAN>, defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>f</I><SUB>0</SUB>(<I>x</I>)/(<I>F</I><SUB>0</SUB>(<I>b</I>) - <I>F</I><SUB>0</SUB>(<I>a</I>))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> elsewhere, and
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = (<I>F</I><SUB>0</SUB>(<I>x</I>) - <I>F</I><SUB>0</SUB>(<I>a</I>))/(<I>F</I><SUB>0</SUB>(<I>b</I>) - <I>F</I><SUB>0</SUB>(<I>a</I>))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>.
 * </DIV><P></P>
 * The inverse distribution function of the truncated distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>F</I><SUB>0</SUB><SUP>-1</SUP>(<I>F</I><SUB>0</SUB>(<I>a</I>) + (<I>F</I><SUB>0</SUB>(<I>b</I>) - <I>F</I><SUB>0</SUB>(<I>a</I>))<I>u</I>)
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB><SUP>-1</SUP></SPAN> is the inverse distribution function of the
 * original distribution.
 * 
 */
public class TruncatedDist extends ContinuousDistribution {
   public static int NUMINTERVALS = 500;

   private ContinuousDistribution dist0;  // The original (non-truncated) dist.
   private double fa;                    // F(a)
   private double fb;                    // F(b)
   private double barfb;                 // bar F(b)
   private double fbfa;                  // F(b) - F(a)
   private double a;
   private double b;
   private double approxMean;
   private double approxVariance;
   private double approxStandardDeviation;


   /**
    * Constructs a new distribution by truncating distribution <TT>dist</TT>
    *   to the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>. Restrictions: <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN> must be finite.
    * 
    */
   public TruncatedDist (ContinuousDistribution dist, double a, double b) {
      setParams (dist, a, b);
   }


   public double density (double x) {
      if ((x < a) || (x > b))
         return 0;
      return dist0.density (x) / fbfa;
   }

   public double cdf (double x) {
      if (x <= a)
         return 0;
      else if (x >= b)
         return 1;
      else
         return (dist0.cdf (x) - fa) / fbfa;
   }

   public double barF (double x) {
      if (x <= a)
         return 1;
      else if (x >= b)
         return 0;
      else
         return (dist0.barF (x) - barfb) / fbfa;
   }

   public double inverseF (double u) {
      if (u == 0)
         return a;
      if (u == 1)
         return b;
      return dist0.inverseF (fa + fbfa * u);
   }

   /**
    * Returns an approximation of the mean computed with the
    *   Simpson <SPAN CLASS="MATH">1/3</SPAN> numerical integration rule.
    * 
    * @exception UnsupportedOperationException the mean of the truncated distribution is unknown
    * 
    * 
    */
   public double getMean() {
      if (Double.isNaN (approxMean))
         throw new UnsupportedOperationException("Undefined mean");
      return approxMean;
   }


   /**
    * Returns an approximation of the variance computed with the
    *   Simpson <SPAN CLASS="MATH">1/3</SPAN> numerical integration rule.
    * 
    * @exception UnsupportedOperationException the mean of the truncated distribution is unknown
    * 
    * 
    */
   public double getVariance() {
      if (Double.isNaN (approxVariance))
         throw new UnsupportedOperationException("Unknown variance");
      return approxVariance;
   }


   /**
    * Returns the square root of the approximate variance.
    * 
    * @exception UnsupportedOperationException the mean of the truncated distribution is unknown
    * 
    * 
    */
   public double getStandardDeviation() {
      if (Double.isNaN (approxStandardDeviation))
         throw new UnsupportedOperationException("Unknown standard deviation");
      return approxStandardDeviation;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * 
    */
   public double getB() {
      return b;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB>(<I>a</I>)</SPAN>.
    * 
    */
   public double getFa() {
      return fa;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB>(<I>b</I>)</SPAN>.
    * 
    */
   public double getFb() {
      return fb;
   }


   /**
    * Returns the value of 
    * <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB>(<I>b</I>) - <I>F</I><SUB>0</SUB>(<I>a</I>)</SPAN>,
    *   the area under the truncated density function.
    * 
    */
   public double getArea() {
      return fbfa;
   }



   /**
    * Sets the parameters <TT>dist</TT>, <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN> for this object. See the
    *   constructor for details.
    * 
    */
   public void setParams (ContinuousDistribution dist, double a, double b) {
      if (a >= b)
         throw new IllegalArgumentException ("a must be smaller than b.");
      this.dist0 = dist;
      if (a <= dist.getXinf())
         a = dist.getXinf();
      if (b >= dist.getXsup())
         b = dist.getXsup();
      supportA = this.a = a;
      supportB = this.b = b;
      fa = dist.cdf (a);
      fb = dist.cdf (b);
      fbfa = fb - fa;
      barfb = dist.barF (b);

      if (((a <= dist.getXinf()) && (b >= dist.getXsup())) ||
       ((a == Double.NEGATIVE_INFINITY) && (b == Double.POSITIVE_INFINITY))) {
         approxMean = dist.getMean();
         approxVariance = dist.getVariance();
         approxStandardDeviation = dist.getStandardDeviation();

      } else {
         // The mean is the integral of xf*(x) over [a, b].
         MomentFunction func1 = new MomentFunction (dist, 1);
         approxMean = MathFunctionUtil.simpsonIntegral (func1, a, b, NUMINTERVALS) / fbfa;

         // Estimate the integral of (x-E[X])^2 f*(x) over [a, b]
         MomentFunction func2 = new MomentFunction (dist, 2, approxMean);
         approxVariance = MathFunctionUtil.simpsonIntegral (func2, a, b, NUMINTERVALS) / fbfa;

         approxStandardDeviation = Math.sqrt (approxVariance);
      }
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in order: [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB>(<I>a</I>)</SPAN>, <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB>(<I>b</I>)</SPAN>, 
    * <SPAN CLASS="MATH"><I>F</I><SUB>0</SUB>(<I>b</I>) - <I>F</I><SUB>0</SUB>(<I>a</I>)</SPAN>].
    * 
    */
   public double[] getParams () {
      double[] retour = {a, b, fa, fb, fbfa};
      return retour;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString () {
      return getClass().getSimpleName() + " : a = " + a + ", b = " + b + ", F(a) = " + fa + ", F(b) = " + fb + ", F(b)-F(a) = " + fbfa;
   }


   private static class MomentFunction implements MathFunction {
      private ContinuousDistribution dist;
      private int moment;
      private double offset;

      public MomentFunction (ContinuousDistribution dist, int moment) {
         this.dist = dist;
         this.moment = moment;
         offset = 0;
      }

      public MomentFunction (ContinuousDistribution dist, int moment, double offset) {
         this (dist, moment);
         this.offset = offset;
      }

      public double evaluate (double x) {
         double res = dist.density (x);
         final double offsetX = x - offset;
         for (int i = 0; i < moment; i++)
            res *= offsetX;
         return res;
      }
   }
}
