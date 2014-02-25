

/*
 * Class:        KolmogorovSmirnovDist
 * Description:  Kolmogorov-Smirnov distribution
 * Environment:  Java
 * Software:     SSJ
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
 *  <EM>Kolmogorov-Smirnov</EM> distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
 * Given an empirical distribution <SPAN CLASS="MATH"><I>F</I><SUB>n</SUB></SPAN> with <SPAN CLASS="MATH"><I>n</I></SPAN> independent observations and
 * a continuous distribution <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>, the two-sided  statistic is defined as
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>D</I><SUB>n</SUB> = sup<SUB>-&#8734;&nbsp;&lt;=&nbsp;x&nbsp;&lt;=&nbsp;&#8734;</SUB>| <I>F</I><SUB>n</SUB>(<I>x</I>) - <I>F</I>(<I>x</I>)|&nbsp; = {<I>D</I><SUB>n</SUB><SUP>+</SUP>, <I>D</I><SUB>n</SUB><SUP>-</SUP>},
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>D</I><SUB>n</SUB><SUP>+</SUP></SPAN> and <SPAN CLASS="MATH"><I>D</I><SUB>n</SUB><SUP>-</SUP></SPAN> are the + and <SPAN CLASS="MATH">-</SPAN> statistics as defined in
 * equations  and  on page <A HREF="#eq:DNp"><IMG  ALIGN="BOTTOM" BORDER="1" ALT="[*]"
 *  SRC="file:/u/buisteri/usr/share/lib/latex2html/icons/crossref.png"></A> of this guide.
 * This class implements a high precision version of the 
 *  distribution 
 * <SPAN CLASS="MATH"><I>P</I>[<I>D</I><SUB>n</SUB>&nbsp;&lt;=&nbsp;<I>x</I>]</SPAN>; it is a Java translation of the <SPAN CLASS="MATH"><I>C</I></SPAN> program
 * written in.  According to its authors, it should give
 *  13 decimal digits of precision. It is extremely slow
 *  for large values of <SPAN CLASS="MATH"><I>n</I></SPAN>.
 * 
 */
public class KolmogorovSmirnovDist extends ContinuousDistribution {
   protected int n;
   protected static final int NEXACT = 500;

   // For the Durbin matrix algorithm
   private static final double NORM = 1.0e140;
   private static final double INORM = 1.0e-140;
   private static final int LOGNORM = 140;



   //========================================================================

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
    * Constructs a  distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    *    Restriction: <SPAN CLASS="MATH"><I>n</I>&nbsp;&gt;=&nbsp;1</SPAN>.
    * 
    */
   public KolmogorovSmirnovDist (int n) {
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

   private static double dclem (int n, double x, double EPS) {
      return (cdf(n, x + EPS) - cdf(n, x - EPS)) / (2.0 * EPS);
   }

   protected static double densConnue (int n, double x) {
      if ((x >= 1.0) || (x <= 0.5 / n))
         return 0.0;
      if (n == 1)
         return 2.0;

      if (x <= 1.0 / n) {
         double w;
         double t = 2.0 * x * n - 1.0;
         if (n <= NEXACT) {
            w = 2.0 * n * n * Num.factoPow (n);
            w *= Math.pow (t, (double) (n - 1));
            return w;
         }
         w = Num.lnFactorial  (n) + (n-1) * Math.log (t/n);
         return 2*n*Math.exp (w);
      }

      if (x >= 1.0 - 1.0 / n)
         return 2.0 * n * Math.pow (1.0 - x, (double) (n - 1));

      return -1.0;
   }

   /**
    * Computes the density for the  distribution with
    *   parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double density (int n, double x) {
      double Res = densConnue(n,x);
      if (Res != -1.0)
         return Res;

      double EPS = 1.0 / 200.0;
      final double D1 = dclem(n, x, EPS);
      final double D2 = dclem(n, x, 2.0 * EPS);
      Res = D1 + (D1 - D2) / 3.0;
      if (Res <= 0.0)
         return 0.0;
      return Res;
   }


   /*=========================================================================

   The following implements the Durbin matrix algorithm and was programmed
   in C by G. Marsaglia, Wai Wan Tsang and Jingbo Wong in C.

   I have translated their program in Java. Only small modifications have
   been made in their program; the most important is to prevent the return
   of NAN or infinite values in some regions. (Richard Simard)

   =========================================================================*/

   /*
    The C program to compute Kolmogorov's distribution

                K(n,d) = Prob(D_n < d),         where

         D_n = max(x_1-0/n,x_2-1/n...,x_n-(n-1)/n,1/n-x_1,2/n-x_2,...,n/n-x_n)

       with  x_1<x_2,...<x_n  a purported set of n independent uniform [0,1)
       random variables sorted into increasing order.
       See G. Marsaglia, Wai Wan Tsang and Jingbo Wong,
          J.Stat.Software, 8, 18, pp 1--4, (2003).
   */


   private static void mMultiply (double[] A, double[] B, double[] C, int m){
      int i, j, k;
      double s;
      for (i = 0; i < m; i++)
         for (j = 0; j < m; j++) {
            s = 0.0;
            for (k = 0; k < m; k++)
               s += A[i * m + k] * B[k * m + j];
            C[i * m + j] = s;
         }
   }


   private static void renormalize (double[] V, int m, int[] p)
   {
      int i;
      for (i = 0; i < m * m; i++)
         V[i] *= INORM;
      p[0] += LOGNORM;
   }


   private static void mPower (double[] A, int eA, double[] V, int[] eV,
                                              int m, int n)
   {
      int i;
      if (n == 1) {
         for (i = 0; i < m * m; i++)
            V[i] = A[i];
         eV[0] = eA;
         return;
      }
      mPower (A, eA, V, eV, m, n / 2);

      double[] B = new double[m * m];
      int[] pB = new int[1];

      mMultiply (V, V, B, m);
      pB[0] = 2 * (eV[0]);
      if (B[(m / 2) * m + (m / 2)] > NORM)
         renormalize (B, m, pB);

      if (n % 2 == 0) {
         for (i = 0; i < m * m; i++)
            V[i] = B[i];
         eV[0] = pB[0];
      } else {
         mMultiply (A, B, V, m);
         eV[0] = eA + pB[0];
      }

      if (V[(m / 2) * m + (m / 2)] > NORM)
         renormalize (V, m, eV);
   }


   protected static double DurbinMatrix (int n, double d)
   {
      int k, m, i, j, g, eH;
      double h, s;
      double[] H;
      double[] Q;
      int[] pQ;

      //Omit next two lines if you require >7 digit accuracy in the right tail
      if (false) {
         s = d * d * n;
         if (s > 7.24 || (s > 3.76 && n > 99))
            return 1 - 2 * Math.exp (-(2.000071 + .331 / Math.sqrt (n) +
                    1.409 / n) * s);
      }
      k = (int) (n * d) + 1;
      m = 2 * k - 1;
      h = k - n * d;
      H = new double[m * m];
      Q = new double[m * m];
      pQ = new int[1];

      for (i = 0; i < m; i++)
         for (j = 0; j < m; j++)
            if (i - j + 1 < 0)
               H[i * m + j] = 0;
            else
               H[i * m + j] = 1;
      for (i = 0; i < m; i++) {
         H[i * m] -= Math.pow (h, (double)(i + 1));
         H[(m - 1) * m + i] -= Math.pow (h, (double)(m - i));
      }
      H[(m - 1) * m] += (2 * h - 1 > 0 ? Math.pow (2 * h - 1, (double) m) : 0);
      for (i = 0; i < m; i++)
         for (j = 0; j < m; j++)
            if (i - j + 1 > 0)
               for (g = 1; g <= i - j + 1; g++)
                  H[i * m + j] /= g;
      eH = 0;
      mPower (H, eH, Q, pQ, m, n);
      s = Q[(k - 1) * m + k - 1];

      for (i = 1; i <= n; i++) {
         s = s * (double) i / n;
         if (s < INORM) {
            s *= NORM;
            pQ[0] -= LOGNORM;
         }
      }
      s *= Math.pow (10., (double) pQ[0]);
      return s;
   }


   protected static double cdfConnu (int n, double x) {
      // For nx^2 > 18, barF(n, x) is smaller than 5e-16
      if ((n * x * x >= 18.0) || (x >= 1.0))
         return 1.0;

      if (x <= 0.5 / n)
         return 0.0;

      if (n == 1)
         return 2.0 * x - 1.0;

      if (x <= 1.0 / n) {
         double w;
         double t = 2.0 * x * n - 1.0;
         if (n <= NEXACT) {
            w = Num.factoPow (n);
            return w * Math.pow (t, (double) n);
         }
         w = Num.lnFactorial(n) + n * Math.log (t/n);
         return Math.exp (w);
      }

      if (x >= 1.0 - 1.0 / n) {
         return 1.0 - 2.0 * Math.pow (1.0 - x, (double) n);
      }

      return -1.0;
   }

   /**
    * Computes the  distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN> with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>
    * using Durbin's matrix formula. It is a translation of the
    * <SPAN CLASS="MATH"><I>C</I></SPAN> program in;
    * according to its authors, it returns 13 decimal digits
    * of precision. It is extremely slow for large <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double cdf (int n, double x) {
      double Res = cdfConnu(n,x);
      if (Res != -1.0)
         return Res;

      return DurbinMatrix (n, x);
   }


   protected static double barFConnu (int n, double x) {
      final double w = n * x * x;

      if ((w >= 370.0) || (x >= 1.0))
         return 0.0;
      if ((w <= 0.0274) || (x <= 0.5 / n))
         return 1.0;
      if (n == 1)
         return 2.0 - 2.0 * x;

      if (x <= 1.0 / n) {
         double v;
         final double t = 2.0 * x*n - 1.0;
         if (n <= NEXACT) {
            v = Num.factoPow (n);
            return 1.0 - v * Math.pow (t, (double) n);
         }
         v = Num.lnFactorial(n) + n * Math.log (t/n);
         return 1.0 - Math.exp (v);
      }

      if (x >= 1.0 - 1.0 / n) {
         return 2.0 * Math.pow (1.0 - x, (double) n);
      }

      return -1.0;
   }

   /**
    * Computes the complementary  distribution function  <SPAN CLASS="MATH">bar(F)(<I>x</I>)</SPAN>
    *   with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>. Simply returns <TT>1 - cdf(n,x)</TT>. It is not precise in
    *   the upper tail.
    * 
    */
   public static double barF (int n, double x) {
      double h = barFConnu(n, x);
      if (h >= 0.0)
         return h;

      h = 1.0 - cdf(n, x);
      if (h >= 0.0)
         return h;
      return 0.0;
   }


   protected static double inverseConnue (int n, double u) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u must be in [0,1]");
      if (u == 1.0)
         return 1.0;

      if (u == 0.0)
         return 0.5/n;

      if (n == 1)
         return (u + 1.0) / 2.0;

      final double NLNN = n*Math.log (n);
      final double LNU = Math.log(u) - Num.lnFactorial (n);
      if (LNU <= -NLNN){
         double t = 1.0/n*(LNU);
         return 0.5 * (Math.exp(t) + 1.0/n);
      }

      if (u >= 1.0 - 2.0 / Math.exp (NLNN))
         return 1.0 - Math.pow((1.0-u)/2.0, 1.0/n);

      return -1.0;
   }

   /**
    * Computes the inverse 
    * <SPAN CLASS="MATH"><I>x</I> = <I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN> of the
    *    distribution <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN> with parameter <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    */
   public static double inverseF (int n, double u) {
      double Res = inverseConnue(n,u);
      if (Res != -1.0)
         return Res;
      Function f = new Function (n,u);
      return RootFinder.brentDekker (0.5/n, 1.0, f, 1e-10);
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
      supportA = 0.5 / n;
      supportB = 1.0;
   }


   /**
    * Returns an array containing the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
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
