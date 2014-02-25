

/*
 * Class:        WatsonUDist
 * Description:  Watson U  distribution 
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

import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.functions.MathFunction;
 

/**
 * Extends the class {@link ContinuousDistribution} for the
 * <SPAN  CLASS="textit">Watson U</SPAN>  distribution (see).
 * Given a sample of <SPAN CLASS="MATH"><I>n</I></SPAN> independent uniforms <SPAN CLASS="MATH"><I>u</I><SUB>i</SUB></SPAN> over <SPAN CLASS="MATH">[0, 1]</SPAN>,
 * the <SPAN  CLASS="textit">Watson</SPAN> statistic <SPAN CLASS="MATH"><I>U</I><SUB>n</SUB><SUP>2</SUP></SPAN> is defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>W</I><SUB>n</SUB><SUP>2</SUP> = 1/12<I>n</I> + &sum;<SUB>j=1</SUB><SUP>n</SUP>{<I>u</I><SUB>(j)</SUB> - (<I>j</I>-0.5)/<I>n</I>}<SUP>2</SUP>,
 * <BR><I>U</I><SUB>n</SUB><SUP>2</SUP> = <I>W</I><SUB>n</SUB><SUP>2</SUP> - <I>n</I>(bar(u)<SUB>n</SUB> -1/2)<SUP>2</SUP>.
 * </DIV><P></P>
 * where the <SPAN CLASS="MATH"><I>u</I><SUB>(j)</SUB></SPAN> are the <SPAN CLASS="MATH"><I>u</I><SUB>i</SUB></SPAN> sorted in increasing order, and
 *   <SPAN CLASS="MATH">bar(u)<SUB>n</SUB></SPAN> is the average of the observations <SPAN CLASS="MATH"><I>u</I><SUB>i</SUB></SPAN>.
 *  The distribution function (the cumulative probabilities)
 *  is defined as 
 * <SPAN CLASS="MATH"><I>F</I><SUB>n</SUB>(<I>x</I>) = <I>P</I>[<I>U</I><SUB>n</SUB><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>]</SPAN>.
 * 
 */
public class WatsonUDist extends ContinuousDistribution {
   private static final double XSEPARE = 0.15;
   private static final double PI = Math.PI;
   private static final int JMAX = 10;
   protected int n;

   private static class Function implements MathFunction {
      protected int n;
      protected double u;

      public Function (int n, double u) {
         this.n = n;
         this.u = u;
      }

      public double evaluate (double x) {
         return u - cdf(n,x);
      }
   }

   private static double cdfn (int n, double x) {
      // The 1/n correction for the cdf, for x < XSEPARE
      double terme;
      double v = Math.exp (-0.125/x);
      double somme = 0;
      int j = 0;

      do {
         double a = (2*j + 1)*(2*j + 1);
         terme = Math.pow (v, (double)(2*j + 1)*(2*j + 1));
         double der = terme*(a - 4.0*x)/(8.0*x*x);
         somme += (5.0*x - 1.0/12.0) * der / 12.0;
         der = terme* (a*a - 24.0*a*x + 48.0*x*x)/ (64.0*x*x*x*x);
         somme += x*x*der/6.0;
         ++j;
      } while (!(terme <= Math.abs(somme) * Num.DBL_EPSILON || j > JMAX));
      if (j > JMAX)
         System.err.println (x + ": watsonU:  somme 1/n has not converged");

      v = -2.0*somme/(n*Math.sqrt (2.0*PI*x));
      return v;
   }


   /**
    * Constructs a <EM>Watson U</EM> distribution for a sample of size <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public WatsonUDist (int n) {
      setN (n);
   }


   public double density (double x) {
      return density (n, x);
   }

   public double cdf (double x) {
      return cdf (n, x);
   }

   public double barF (double x) {
      return barF (n, x);
   }

   public double inverseF (double u) {
      return inverseF (n, u);
   }

   public double getMean() {
      return WatsonUDist.getMean (n);
   }

   public double getVariance() {
      return WatsonUDist.getVariance (n);
   }

   public double getStandardDeviation() {
      return WatsonUDist.getStandardDeviation (n);
   }

   /**
    * Computes the density of the <EM>Watson U</EM> distribution
    *   with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double density (int n, double x) {
      if (n < 2)
         throw new IllegalArgumentException ("n < 2");

      if (x <= 1.0/(12.0*n) || x >= n/12.0 || x >= XBIG)
         return 0.0;

      final double EPS = 1.0 / 100.0;
      return (cdf(n, x + EPS) - cdf(n, x - EPS)) / (2.0 * EPS);

/*
// This is the asymptotic density for n -> infinity
      int j;
      double signe;
      double v;
      double terme;
      double somme;

      if (x > XSEPARE) {
         // this series converges rapidly for x > 0.15
         v = Math.exp (-(x*2.0*PI*PI));
         signe = 1.0;
         somme = 0.0;
         j = 1;
         do {
            terme = j*j*Math.pow (v, (double)j*j);
            somme += signe*terme;
            signe = -signe;
            ++j;
         } while (!(terme < Num.DBL_EPSILON || j > JMAX));
         if (j > JMAX)
            System.err.println ("watsonU:  sum1 has not converged");
         return 4.0*PI*PI*somme;
      }

      // this series converges rapidly for x <= 0.15
      v = Math.exp (-0.125/x);
      somme = v;
      double somme2 = v;
      j = 2;
      do {
         terme = Math.pow (v, (double)(2*j - 1)*(2*j - 1));
         somme += terme;
         somme2 += terme * (2*j - 1)*(2*j - 1);
         ++j;
      } while (!(terme <= somme2 * Num.DBL_EPSILON || j > JMAX));
      if (j > JMAX)
         System.err.println ("watsonU:  sum2 has not converged");

      final double RACINE = Math.sqrt (2.0*PI*x);
      return -somme/(x*RACINE) + 2*somme2 * 0.125/ ((x*x) * RACINE);
*/
   }


   /**
    * Computes the Watson <SPAN CLASS="MATH"><I>U</I></SPAN> distribution function, i.e. returns
    *   
    * <SPAN CLASS="MATH"><I>P</I>[<I>U</I><SUB>n</SUB><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>]</SPAN>, where <SPAN CLASS="MATH"><I>U</I><SUB>n</SUB><SUP>2</SUP></SPAN> is the Watson statistic  
    *   defined in. We use the asymptotic distribution for
    * 
    * <SPAN CLASS="MATH"><I>n</I>&nbsp;-&gt;&nbsp;&#8734;</SPAN>, plus a correction in <SPAN CLASS="MATH"><I>O</I>(1/<I>n</I>)</SPAN>, as given in.
    * 
    */
   public static double cdf (int n, double x) {
      if (n < 2)
         throw new IllegalArgumentException ("n < 2");

      if (x <= 1.0/(12.0*n))
         return 0.0;
      if (x > 3.95 || x >= n/12.0)
         return 1.0;

      if (2 == n) {
         if (x <= 1.0/24.0)
            return 0.0;
         if (x >= 1.0/6.0)
            return 1.0;
         return 2.0*Math.sqrt(2.0*x - 1.0/12.0);
      }

      if (x > XSEPARE)
         return 1.0 - barF (n, x);

      // this series converges rapidly for x <= 0.15
      double terme;
      double v = Math.exp (-0.125/x);
      double somme = v;
      int j = 2;

      do {
         terme = Math.pow (v, (double)(2*j - 1)*(2*j - 1));
         somme += terme;
         ++j;
      } while (!(terme <= somme * Num.DBL_EPSILON || j > JMAX));
      if (j > JMAX)
         System.err.println (x + ": watsonU:  sum2 has not converged");

      v = 2.0*somme/Math.sqrt (2.0*PI*x);
      v += cdfn(n, x);
      if (v >= 1.0)
         return 1.0;
      if (v <= 0.0)
         return 0.0;
       return v;
   }


   /**
    * Computes the complementary distribution function  
    * <SPAN CLASS="MATH">bar(F)<SUB>n</SUB>(<I>x</I>)</SPAN>,
    *   where <SPAN CLASS="MATH"><I>F</I><SUB>n</SUB></SPAN> is the <EM>Watson</EM> <SPAN CLASS="MATH"><I>U</I></SPAN> distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double barF (int n, double x) {
      if (n < 2)
         throw new IllegalArgumentException ("n < 2");

      if (x <= 1.0/(12.0*n))
         return 1.0;
      if (x >= XBIG || x >= n/12.0)
         return 0.0;

      if (2 == n)
         return 1.0 - 2.0*Math.sqrt(2.0*x - 1.0/12.0);

      if (x > XSEPARE) {
         // this series converges rapidly for x > 0.15
         double terme, ter;
         double v = Math.exp (-2.0*PI*PI*x);
         double signe = 1.0;
         double somme = 0.0;
         double son = 0.0;
         int j = 1;

         do {
            terme = Math.pow (v, (double)j*j);
            somme += signe*terme;
            double h = 2*j*PI*x;
            ter = (5.0*x - h*h - 1.0/12.0)*j*j;
            son += signe*terme*ter;
            signe = -signe;
            ++j;
         } while (!(terme < Num.DBL_EPSILON || j > JMAX));
         if (j > JMAX)
            System.err.println (x + ": watsonU:  sum1 has not converged");
         v = 2.0*somme + PI*PI*son/(3.0*n);
         if (v <= 0.0) 
            return 0.0;
         if (v >= 1.0)
            return 1.0;
         return v;
      }

      return (1.0 - cdf(n, x));
   }


   /**
    * Computes 
    * <SPAN CLASS="MATH"><I>x</I> = <I>F</I><SUB>n</SUB><SUP>-1</SUP>(<I>u</I>)</SPAN>, where <SPAN CLASS="MATH"><I>F</I><SUB>n</SUB></SPAN> is the 
    *   <EM>Watson</EM> <SPAN CLASS="MATH"><I>U</I></SPAN> distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double inverseF (int n, double u) {
      if (n < 2)
         throw new IllegalArgumentException ("n < 2");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u must be in [0,1]");
      if (u >= 1.0)
         return n/12.0;
      if (u <= 0.0)
         return 1.0/(12.0*n);

      if (2 == n)
         return 1.0/24.0 + u*u/8.0;

      Function f = new Function (n,u);
      return RootFinder.brentDekker (0.0, 2.0, f, 1e-7);
   }


   /**
    * Returns the mean of the <EM>Watson</EM> <SPAN CLASS="MATH"><I>U</I></SPAN> distribution with
    *    parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return Returns the mean
    * 
    */
   public static double getMean (int n) {
      return 1.0/12.0;
   }


   /**
    * Returns the variance of the <EM>Watson</EM> <SPAN CLASS="MATH"><I>U</I></SPAN> distribution with
    *    parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the variance
    * 
    */
   public static double getVariance (int n) {
      return (n - 1)/(360.0*n);
   }


   /**
    * Returns the standard deviation of the <EM>Watson</EM> <SPAN CLASS="MATH"><I>U</I></SPAN>
    *   distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the standard deviation
    * 
    */
   public static double getStandardDeviation (int n) {
      return Math.sqrt (WatsonUDist.getVariance (n));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public int getN() {
      return n;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public void setN (int n) {
      if (n < 2)
         throw new IllegalArgumentException ("n < 2");
      this.n = n;
      supportA = 1.0/(12.0*n);
      supportB = n/12.0;
   }


   /**
    * Return an array containing the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {n};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : n = " + n;
   }

}
