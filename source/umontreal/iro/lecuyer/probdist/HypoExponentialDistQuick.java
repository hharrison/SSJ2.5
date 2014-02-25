
/*
 * Class:        HypoExponentialDistQuick
 * Description:  Hypo-exponential distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        January 2011

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

import java.util.Formatter;
import java.util.Locale;
import  umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.functions.MathFunction;

/**
 * This class is a subclass of {@link HypoExponentialDist}
 * and also implements  the <SPAN  CLASS="textit">hypoexponential</SPAN> distribution. It uses
 * different algorithms to compute the probabilities.
 * The formula for the complementary distribution
 * is mathematically equivalent to (see and)
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:convolution-hypo"></A>
 * bar(F)(<I>x</I>) = <IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="HypoExponentialDistQuickimg1.png"
 *  ALT="$\displaystyle \mathbb {P}$">[<I>X</I><SUB>1</SUB> + <SUP> ... </SUP> + <I>X</I><SUB>k</SUB> &gt; <I>x</I>] = &sum;<SUB>i=1</SUB><SUP>k</SUP><I>e</I><SUP>-<I>&#955;</I><SUB>i</SUB>x</SUP>&prod;<SUB><IMG
 *  ALIGN="BOTTOM" BORDER="0" SRC="HypoExponentialDistQuickimg2.png"
 *  ALT="$\scriptstyle \substack$">j=1<tex2html_row_mark>j <IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="HypoExponentialDistQuickimg3.png"
 *  ALT="$\scriptstyle \not=$">i</SUB><SUP>k</SUP><IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="HypoExponentialDistQuickimg4.png"
 *  ALT="$\displaystyle {\frac{{\lambda_j}}{{\lambda_
 * j - \lambda_i}}}$">.
 * </DIV><P></P>
 * 
 * <P>
 * The expression is much faster to compute than the
 * matrix exponential formula, but it becomes numerically
 * unstable when <SPAN CLASS="MATH"><I>k</I></SPAN> gets large and/or the differences between the <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB></SPAN>
 * are too small, because it is an alternating sum with relatively large terms
 * of similar size. When the <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB></SPAN> are close, many of the
 * factors 
 * <SPAN CLASS="MATH"><I>&#955;</I><SUB>j</SUB> - <I>&#955;</I><SUB>i</SUB></SPAN> in are small,
 * and the effect of this is amplified when <SPAN CLASS="MATH"><I>k</I></SPAN> is large. This gives rise to
 * large terms of opposite sign in the sum and the formula becomes unstable
 * due to subtractive cancellation.
 * For example, with the computations done in standard 64-bit floating-point
 * arithmetic, if the <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB></SPAN> are regularly spaced with differences
 * of 
 * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i+1</SUB> - <I>&#955;</I><SUB>i</SUB> = 0.1</SPAN> for all <SPAN CLASS="MATH"><I>i</I></SPAN>, the formula breaks down already for 
 * <SPAN CLASS="MATH"><I>k</I>&nbsp;=&nbsp;&nbsp;15</SPAN>, while if
 * the differences 
 * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i+1</SUB> - <I>&#955;</I><SUB>i</SUB> = 3</SPAN>, it gives a few decimal
 * digits of precision for <SPAN CLASS="MATH"><I>k</I></SPAN> up to 
 * <SPAN CLASS="MATH">&nbsp;=&nbsp;&nbsp;300</SPAN>.
 * 
 * <P>
 * The formula for the density is mathematically equivalent to
 * the much faster formula
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = &sum;<SUB>i=1</SUB><SUP>k</SUP><I>&#955;</I><SUB>i</SUB><I>e</I><SUP>-<I>&#955;</I><SUB>i</SUB>x</SUP>&prod;<SUB>[tex2html_wrap_indisplay213]j=1<tex2html_row_mark>j [tex2html_wrap_indisplay214]i</SUB><SUP>k</SUP>[tex2html_wrap_indisplay215],
 * </DIV><P></P>
 * which is also  numerically
 * unstable when <SPAN CLASS="MATH"><I>k</I></SPAN> gets large and/or the differences between the <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB></SPAN>
 * are too small.
 * 
 */
public class HypoExponentialDistQuick extends HypoExponentialDist {
   private double[] m_H;

   private static double[] computeH (double[] lambda) {
      int k = lambda.length;
      double[] H = new double[k];
      double tem;
      int j;
      for (int i = 0; i < k; i++) {
         tem = 1.0;
         for (j = 0; j < i; j++)
            tem *= lambda[j] / (lambda[j] - lambda[i]);
         for (j = i + 1; j < k; j++)
            tem *= lambda[j] / (lambda[j] - lambda[i]);
         H[i] = tem;
      }
      return H;
   }


   private static double m_density(double[] lambda, double[] H, double x) {
      if (x < 0)
         return 0;
      int k = lambda.length;
      double tem;
      int j;
      double prob = 0;            // probability

      for (j = 0; j < k; j++) {
         prob += lambda[j] * H[j] * Math.exp (-lambda[j] * x);
      }
/*
      for (j = 0; j < k; j++) {
         if (x*lambda[j] <= 705.) {
            prob += lambda[j] * H[j] * Math.exp (-lambda[j] * x);
         } else {
            tem = Math.log (Math.abs (H[j])) - lambda[j] * x;
            prob += Math.signum (H[j]) * lambda[j] * Math.exp (tem);
         }
      }
*/
      return prob;
   }


   private static double m_barF(double[] lambda, double[] H, double x) {
      if (x <= 0)
         return 1;
      int k = lambda.length;
      double tem;
      int j;
      double prob = 0;            // probability

      for (j = 0; j < k; j++) {
         prob += H[j] * Math.exp (-lambda[j] * x);
      }
/*
      for (j = 0; j < k; j++) {
         if (x*lambda[j] <= 705.) {
            prob += H[j] * Math.exp (-lambda[j] * x);
         } else {
            tem = Math.log (Math.abs (H[j])) - lambda[j] * x;
            prob += Math.signum (H[j]) * Math.exp (tem);
         }
      }
*/
      return prob;
	}


   private static class myFunc implements MathFunction {
      // For inverseF
      private double[] m_lam;
      private double m_u;

      public myFunc (double[] lam, double u) {
         m_lam = lam;
         m_u = u;
      }

      public double evaluate (double x) {
         return m_u - HypoExponentialDistQuick.cdf(m_lam, x);
      }
   }



   /**
    * Constructs a <TT>HypoExponentialDistQuick</TT> object,
    * with rates 
    * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB> =</SPAN> <TT>lambda[<SPAN CLASS="MATH"><I>i</I> - 1</SPAN>]</TT>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>k</I></SPAN>.
    *   
    * @param lambda rates of the hypoexponential distribution
    * 
    */
   public HypoExponentialDistQuick (double[] lambda) {
      super (lambda);
      m_H = computeH (lambda);
   }

  /*  These public methods are necessary so that methods cdf,
   *  barF and inverseF used are those of the present
   *  class and not those of the mother class.
   */
   public double density (double x) {
      return m_density (m_lambda, m_H, x);
   }

   public double cdf (double x) {
      return 1.0 - m_barF (m_lambda, m_H, x);
   }

   public double barF (double x) {
      return m_barF (m_lambda, m_H, x);
   }

   public double inverseF (double u) {
      return m_inverseF (m_lambda, m_H, u);
   }

   /**
    * Computes the density function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>, with 
    * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB> =</SPAN>
    * <TT>lambda[<SPAN CLASS="MATH"><I>i</I> - 1</SPAN>]</TT>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>k</I></SPAN>.
    * 
    * @param lambda rates of the hypoexponential distribution
    * 
    *    @param x value at which the density is evaluated
    * 
    *    @return density at <SPAN CLASS="MATH"><I>x</I></SPAN>
    * 
    */
   public static double density (double[] lambda, double x) {
      testLambda (lambda);
      double[] H = computeH (lambda);
      return m_density(lambda, H, x);
   }


   /**
    * Computes the distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>,
    * with 
    * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB> =</SPAN> <TT>lambda[<SPAN CLASS="MATH"><I>i</I> - 1</SPAN>]</TT>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>k</I></SPAN>.
    *  
    * @param lambda rates of the hypoexponential distribution
    * 
    *    @param x value at which the distribution is evaluated
    * 
    *    @return value of distribution at <SPAN CLASS="MATH"><I>x</I></SPAN>
    * 
    */
   public static double cdf (double[] lambda, double x) {
      return 1.0 - barF(lambda, x);
   }


   /**
    * Computes the complementary distribution <SPAN CLASS="MATH">bar(F)(<I>x</I>)</SPAN>,
    * with 
    * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB> =</SPAN> <TT>lambda[<SPAN CLASS="MATH"><I>i</I> - 1</SPAN>]</TT>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>k</I></SPAN>.
    *  
    * @param lambda rates of the hypoexponential distribution
    * 
    *    @param x value at which the complementary distribution is evaluated
    * 
    *    @return value of complementary distribution at <SPAN CLASS="MATH"><I>x</I></SPAN>
    * 
    */
   public static double barF (double[] lambda, double x) {
      testLambda (lambda);
		double[] H = computeH (lambda);
      return m_barF(lambda, H, x);
   }


   /**
    * Computes the inverse distribution function <SPAN CLASS="MATH"><I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN>,
    * with 
    * <SPAN CLASS="MATH"><I>&#955;</I><SUB>i</SUB> =</SPAN> <TT>lambda[<SPAN CLASS="MATH"><I>i</I> - 1</SPAN>]</TT>, 
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>k</I></SPAN>.
    * It uses a root-finding method and is very slow.
    * 
    * @param lambda rates of the hypoexponential distribution
    * 
    *    @param u value at which the inverse distribution is evaluated
    * 
    *    @return inverse distribution at <SPAN CLASS="MATH"><I>u</I></SPAN>
    * 
    */
   public static double inverseF (double[] lambda, double u) {
      testLambda (lambda);
		double[] H = computeH (lambda);
      return m_inverseF(lambda, H, u);
   }

   private static double m_inverseF(double[] lambda, double[] H, double u) {
      if (u < 0.0 || u > 1.0)
          throw new IllegalArgumentException ("u not in [0,1]");
      if (u >= 1.0)
          return Double.POSITIVE_INFINITY;
      if (u <= 0.0)
          return 0.0;

      final double EPS = 1.0e-12;
      myFunc fonc = new myFunc (lambda, u);
      double x1 = getMean (lambda);
      double v = 1.0 - m_barF(lambda, H, x1);
      if (u <= v)
         return RootFinder.brentDekker (0, x1, fonc, EPS);

      // u > v
      double x2 = 4.0*x1 + 1.0;
      v = 1.0 - m_barF(lambda, H, x2);
      while (v < u) {
         x1 = x2;
         x2 = 4.0*x2;
         v = 1.0 - m_barF(lambda, H, x2);
      }
      return RootFinder.brentDekker (x1, x2, fonc, EPS);
   }


   public void setLambda (double[] lambda) {
      super.setLambda (lambda);
      m_H = computeH (lambda);
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb, Locale.US);
      formatter.format(getClass().getSimpleName() + " : lambda = {" +
           PrintfFormat.NEWLINE);
      int k = m_lambda.length;
      for(int i = 0; i < k; i++) {
         formatter.format("   %f%n", m_lambda[i]);
      }
      formatter.format("}%n");
      return sb.toString();
   }
}
