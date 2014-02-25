

/*
 * Class:        CramerVonMisesDist
 * Description:  Cramér-von Mises distribution
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

import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.functions.MathFunction;
 

/**
 * Extends the class {@link ContinuousDistribution} for the
 * Cram&#233;r-von Mises distribution (see).
 * Given a sample of <SPAN CLASS="MATH"><I>n</I></SPAN> independent uniforms <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN> over <SPAN CLASS="MATH">[0, 1]</SPAN>,
 * the Cram&#233;r-von Mises statistic <SPAN CLASS="MATH"><I>W</I><SUB>n</SUB><SUP>2</SUP></SPAN> is defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>W</I><SUB>n</SUB><SUP>2</SUP> = 1/12<I>n</I> + &sum;<SUB>j=1</SUB><SUP>n</SUP>(<I>U</I><SUB>(j)</SUB> - (<I>j</I>-0.5)/<I>n</I>)<SUP>2</SUP>,
 * </DIV><P></P>
 * where the <SPAN CLASS="MATH"><I>U</I><SUB>(j)</SUB></SPAN> are the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN> sorted in increasing order.
 *   The  distribution function (the cumulative probabilities)
 *   is defined as 
 * <SPAN CLASS="MATH"><I>F</I><SUB>n</SUB>(<I>x</I>) = <I>P</I>[<I>W</I><SUB>n</SUB><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>]</SPAN>.
 * 
 */
public class CramerVonMisesDist extends ContinuousDistribution {
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



   /**
    * Constructs a <EM>Cram&#233;r-von Mises</EM> distribution for a sample of size <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public CramerVonMisesDist (int n) {
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
      return CramerVonMisesDist.getMean (n);
   }

   public double getVariance() {
      return CramerVonMisesDist.getVariance (n);
   }

   public double getStandardDeviation() {
      return CramerVonMisesDist.getStandardDeviation (n);
   }

   /**
    * Computes the density function
    *   for a <EM>Cram&#233;r-von Mises</EM> distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double density (int n, double x) {
      if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");

      if (x <= 1.0/(12.0*n) || x >= n/3.0)
         return 0.0;

      if (n == 1)
         return 1.0 / Math.sqrt (x - 1.0/12.0);

      if (x <= 0.002 || x > 3.95)
         return 0.0;

      throw new UnsupportedOperationException("density not implemented.");
   }


   /**
    * Computes the Cram&#233;r-von Mises distribution function with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    *   Returns an approximation of 
    * <SPAN CLASS="MATH"><I>P</I>[<I>W</I><SUB>n</SUB><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>]</SPAN>, where <SPAN CLASS="MATH"><I>W</I><SUB>n</SUB><SUP>2</SUP></SPAN> is the
    *   Cram&#233;r von Mises  statistic (see).
    *   The approximation is based on the distribution function of 
    * <SPAN CLASS="MATH"><I>W</I><SUP>2</SUP> = lim<SUB>n&nbsp;-&gt;&nbsp;&#8734;</SUB><I>W</I><SUB>n</SUB><SUP>2</SUP></SPAN>, which has the following series expansion derived
    *   by Anderson and Darling:
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<I>P</I>(<I>W</I><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>)&nbsp; = &nbsp;<IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="CramerVonMisesDistimg1.png"
    *  ALT="$\displaystyle {\frac{1}{{\pi\sqrt x}}}$">&sum;<SUB>j=0</SUB><SUP>&#8734;</SUP>(- 1)<SUP>j</SUP><IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="CramerVonMisesDistimg2.png"
    *  ALT="$\displaystyle \binom{-1/2}{j} $">(4j+1)<SUP>1/2</SUP> &nbsp; &nbsp;<I>exp</I>{ - <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="CramerVonMisesDistimg3.png"
    *  ALT="$\displaystyle {\frac{{(4j+1)^2}}{{16 x}}}$">}<I>K</I><SUB>1/4</SUB>([tex2html_wrap_indisplay244]),
    * </DIV><P></P>
    * where 
    * <SPAN CLASS="MATH"><I>K</I><SUB><I>&#957;</I></SUB></SPAN> is the  modified Bessel function of the 
    *   second kind.
    *   To correct for the deviation between 
    * <SPAN CLASS="MATH"><I>P</I>(<I>W</I><SUB>n</SUB><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>)</SPAN> and 
    * <SPAN CLASS="MATH"><I>P</I>(<I>W</I><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>)</SPAN>,
    *   we add a correction in <SPAN CLASS="MATH">1/<I>n</I></SPAN>, obtained empirically by simulation.
    *   For <SPAN CLASS="MATH"><I>n</I> = 10</SPAN>, 20, 40, the error is less than
    *   0.002, 0.001, and 0.0005, respectively, while for
    *   <SPAN CLASS="MATH"><I>n</I>&nbsp;&gt;=&nbsp;100</SPAN> it is less than 0.0005.
    *   For 
    * <SPAN CLASS="MATH"><I>n</I>&nbsp;-&gt;&nbsp;&#8734;</SPAN>, we estimate that the method returns
    *   at least 6 decimal digits of precision.
    *   For <SPAN CLASS="MATH"><I>n</I> = 1</SPAN>, the method uses the exact distribution:
    *   
    * <SPAN CLASS="MATH"><I>P</I>(<I>W</I><SUB>1</SUB><SUP>2</SUP>&nbsp;&lt;=&nbsp;<I>x</I>) = 2(x - 1/12)<SUP>1/2</SUP></SPAN> for 
    * <SPAN CLASS="MATH">1/12&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;1/3</SPAN>.
    * 
    */
   public static double cdf (int n, double x) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      if (n == 1) {
         if (x <= 1.0/12.0)
            return 0.0;
         if (x >= 1.0/3.0)
            return 1.0;
         return 2.0*Math.sqrt (x - 1.0/12.0);
      }

      if (x <= 1.0/(12.0*n))
         return 0.0;

      if (x <= (n + 3.0)/(12.0*n*n)) {
         double t = Num.lnFactorial(n) - Num.lnGamma (1.0 + 0.5*n) +
            0.5*n*Math.log (Math.PI*(x - 1.0/(12.0*n)));
         return Math.exp(t);
      }

      if (x <= 0.002)
         return 0.0;
      if (x > 3.95 || x >= n/3.0)
         return 1.0;

      final double EPSILON = Num.DBL_EPSILON;
      final int JMAX = 20;
      int j = 0;
      double Cor, Res, arg;
      double termX, termS, termJ;

      termX = 0.0625/x;            // 1 / (16x)
      Res = 0.0;

      final double A[] = {
         1.0,
         1.11803398875,
         1.125,
         1.12673477358,
         1.1274116945,
         1.12774323743,
         1.1279296875,
         1.12804477649,
         1.12812074678,
         1.12817350091
      };

      do {
         termJ = 4*j + 1;
         arg = termJ*termJ*termX;
         termS = A[j]*Math.exp (-arg)*Num.besselK025 (arg);
         Res += termS;
         ++j;
      } while (!(termS < EPSILON || j > JMAX));

      if (j > JMAX)
         System.err.println ("cramerVonMises: iterations have not converged");
      Res /= Math.PI*Math.sqrt (x);

      // Empirical correction in 1/n
      if (x < 0.0092)
         Cor = 0.0;
      else if (x < 0.03)
         Cor = -0.0121763 + x*(2.56672 - 132.571*x);
      else if (x < 0.06)
         Cor = 0.108688 + x*(-7.14677 + 58.0662*x);
      else if (x < 0.19)
         Cor = -0.0539444 + x*(-2.22024 + x*(25.0407 - 64.9233*x));
      else if (x < 0.5)
         Cor = -0.251455 + x*(2.46087 + x*(-8.92836 + x*(14.0988 -
                  x*(5.5204 + 4.61784*x))));
      else if (x <= 1.1)
         Cor = 0.0782122 + x*(-0.519924 + x*(1.75148 +
               x*(-2.72035 + x*(1.94487 - 0.524911*x))));
      else
         Cor = Math.exp (-0.244889 - 4.26506*x);

      Res += Cor/n;
      // This empirical correction is not very precise, so ...
      if (Res <= 1.0)
         return Res;
      else
         return 1.0;
   }


   /**
    * Computes the complementary distribution function 
    * <SPAN CLASS="MATH">bar(F)<SUB>n</SUB>(<I>x</I>)</SPAN>
    *   with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double barF (int n, double x) {
      return 1.0 - cdf(n,x);
   }


   /**
    * Computes 
    * <SPAN CLASS="MATH"><I>x</I> = <I>F</I><SUB>n</SUB><SUP>-1</SUP>(<I>u</I>)</SPAN>, where <SPAN CLASS="MATH"><I>F</I><SUB>n</SUB></SPAN> is the 
    *   <EM>Cram&#233;r-von Mises</EM> distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double inverseF (int n, double u) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u must be in [0,1]");

      if (u >= 1.0)
         return n/3.0;
      if (u <= 0.0)
         return 1.0/(12.0*n);

      if (n == 1)
         return 1.0/12.0 + 0.25*u*u;

      Function f = new Function (n,u);
      return RootFinder.brentDekker (0.0, 10.0, f, 1e-6);
   }


   /**
    * Returns the mean of the distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the mean
    * 
    */
   public static double getMean (int n) {
      return 1.0 / 6.0;
   }


   /**
    * Returns the variance of the distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return variance
    * 
    */
   public static double getVariance (int n) {
      return (4.0 * n - 3.0) / (180.0 * n ); 
   }


   /**
    * Returns the standard deviation of the distribution with
    *    parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @return the standard deviation
    * 
    */
   public static double getStandardDeviation (int n) {
     return Math.sqrt(getVariance(n));
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
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      this.n = n;
      supportA = 1.0/(12.0*n);
      supportB = n/3.0;
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
