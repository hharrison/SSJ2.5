

/*
 * Class:        MathFunctionUtil
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Éric Buist
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

package umontreal.iro.lecuyer.functions;

import umontreal.iro.lecuyer.util.Misc;


/**
 * Provides utility methods for computing
 * derivatives and integrals of functions.
 * 
 */
public class MathFunctionUtil {


   /**
    * Step length in <SPAN CLASS="MATH"><I>x</I></SPAN> to compute derivatives. Default: <SPAN CLASS="MATH">10<SUP>-6</SUP></SPAN>.
    * 
    */
   public static double H = 1e-6;

   private MathFunctionUtil() {}

   // For Gauss-Lobatto: nodes Cg and weights Wg
   private static final double[] Cg = { 0, 0.17267316464601142812, 0.5,
                                           0.82732683535398857188, 1 };
   private static final double[] Wg = { 0.05, 0.27222222222222222222,
                  0.35555555555555555555, 0.27222222222222222222, 0.05 };


   private static double[] fixBounds (MathFunction func, double a,
                                      double b, int numIntervals) {
   // For functions which are 0 on parts of [a, b], shorten the interval
   // [a, b] to the non-zero part of f(x). Returns the shortened interval.

       final double h = (b - a)/numIntervals;
       double x = b;
       while ((0 == func.evaluate (x)) && (x > a))
           x -= h;
       if (x < b)
           b = x + h;

       x = a;
       while ((0 == func.evaluate (x)) && (x < b))
           x += h;
       if (x > a)
          a = x - h;
       double[] D = {a, b};
       return D;
   }

   /**
    * Default number of intervals for Simpson's integral.
    * 
    */
   public static int NUMINTERVALS = 1024;


   /**
    * Returns the first derivative of the function <TT>func</TT>
    *  evaluated at <TT>x</TT>. If the given function implements
    *  {@link MathFunctionWithFirstDerivative}, this method calls
    * {@link MathFunctionWithFirstDerivative#derivative MathFunctionWithFirstDerivative.derivative}&nbsp;<TT>(double)</TT>.
    *  Otherwise, if the function implements {@link MathFunctionWithDerivative},
    *  this method calls
    * {@link MathFunctionWithDerivative#derivative MathFunctionWithDerivative.derivative}&nbsp;<TT>(double, int)</TT>.
    * If the function does not implement any of these two interfaces, the method
    *  uses {@link #finiteCenteredDifferenceDerivative finiteCenteredDifferenceDerivative}&nbsp;<TT>(MathFunction,
    *  double, double)</TT> to obtain an estimate of the derivative.
    * 
    * @param func the function to derivate.
    * 
    *    @param x the evaluation point.
    * 
    *    @return the first derivative.
    * 
    */
   public static double derivative (MathFunction func, double x) {
      if (func instanceof MathFunctionWithFirstDerivative)
         return ((MathFunctionWithFirstDerivative)func).derivative (x);
      else if (func instanceof MathFunctionWithDerivative)
         return ((MathFunctionWithDerivative)func).derivative (x, 1);
      else
         return finiteCenteredDifferenceDerivative (func, x, H);
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>n</I></SPAN>th derivative of function
    *  <TT>func</TT> evaluated at <TT>x</TT>.
    *  If <SPAN CLASS="MATH"><I>n</I> = 0</SPAN>, this returns <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    *  If <SPAN CLASS="MATH"><I>n</I> = 1</SPAN>, this calls
    *  {@link #derivative derivative}&nbsp;<TT>(MathFunction, double)</TT>
    *  and returns the resulting first derivative.
    *  Otherwise,
    *  if the function implements
    *  {@link MathFunctionWithDerivative},
    *  this method calls
    * {@link MathFunctionWithDerivative#derivative MathFunctionWithDerivative.derivative}&nbsp;<TT>(double, int)</TT>.
    *  If the function does not implement this
    *  interface,
    *  the method uses
    *  {@link #finiteCenteredDifferenceDerivative finiteCenteredDifferenceDerivative}&nbsp;<TT>(MathFunction, double, int, double)</TT>
    *  if <SPAN CLASS="MATH"><I>n</I></SPAN> is even, or
    *  {@link #finiteDifferenceDerivative finiteDifferenceDerivative}&nbsp;<TT>(MathFunction, double, int, double)</TT>
    *  if <SPAN CLASS="MATH"><I>n</I></SPAN> is odd,
    *  to obtain a numerical approximation of the derivative.
    * 
    * @param func the function to derivate.
    * 
    *    @param x the evaluation point.
    * 
    *    @param n the order of the derivative.
    * 
    *    @return the <SPAN CLASS="MATH"><I>n</I></SPAN>th derivative.
    * 
    */
   public static double derivative (MathFunction func, double x, int n) {
      if (n == 0)
         return func.evaluate (x);
      else if (n == 1)
         return derivative (func, x);
      else if (func instanceof MathFunctionWithDerivative)
         return ((MathFunctionWithDerivative)func).derivative (x, n);
      else if (n % 2 == 0)
         return finiteCenteredDifferenceDerivative (func, x, n, H);
      else
         return finiteDifferenceDerivative (func, x, n, H);
   }


   /**
    * Computes and returns an estimate
    *  of the <SPAN CLASS="MATH"><I>n</I></SPAN>th derivative of the
    *  function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    *  This method estimates
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="MathFunctionUtilimg1.png"
    *  ALT="$\displaystyle {\frac{{d^nf(x)}}{{dx^n}}}$">,
    * </DIV><P></P>
    * the <SPAN CLASS="MATH"><I>n</I></SPAN>th derivative of <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>
    *  evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    *  This method first computes
    *  
    * <SPAN CLASS="MATH"><I>f</I><SUB>i</SUB> = <I>f</I> (<I>x</I> + <I>i&#949;</I>)</SPAN>, for 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I></SPAN>, with
    *  
    * <SPAN CLASS="MATH"><I>&#949;</I> = <I>h</I><SUP>1/n</SUP></SPAN>.
    *  The estimate is then given by
    *  
    * <SPAN CLASS="MATH"><I>&#916;</I><SUP>n</SUP><I>f</I><SUB>0</SUB>/<I>h</I></SPAN>, where
    *  
    * <SPAN CLASS="MATH"><I>&#916;</I><SUP>n</SUP><I>f</I><SUB>i</SUB> = <I>&#916;</I><SUP>n-1</SUP><I>f</I><SUB>i+1</SUB> - <I>&#916;</I><SUP>n-1</SUP><I>f</I><SUB>i</SUB></SPAN>, and
    *  
    * <SPAN CLASS="MATH"><I>&#916;f</I><SUB>i</SUB> = <I>f</I><SUB>i+1</SUB> - <I>f</I><SUB>i</SUB></SPAN>.
    * 
    * @param func the function to derivate.
    * 
    *    @param x the evaluation point.
    * 
    *    @param n the order of the derivative.
    * 
    *    @param h the error.
    * 
    *    @return the estimate of the derivative.
    * 
    */
   public static double finiteDifferenceDerivative (
                 MathFunction func, double x, int n, double h) {
      if (n < 0)
         throw new IllegalArgumentException
         ("n must not be negative");
      if (n == 0)
         return func.evaluate (x);
      final double err = Math.pow (h, 1.0 / n);
      final double[] values = new double[n+1];
      for (int i = 0; i < values.length; i++)
         values[i] = func.evaluate (x + i*err);
      for (int j = 0; j < n; j++) {
         for (int i = 0; i < n - j; i++)
            values[i] = values[i + 1] - values[i];
      }
      return values[0] / h;
   }


   /**
    * Returns 
    * <SPAN CLASS="MATH">(<I>f</I> (<I>x</I> + <I>h</I>) - <I>f</I> (<I>x</I> - <I>h</I>))/(2<I>h</I>)</SPAN>,
    *  an estimate of the first derivative of <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>
    *  using centered differences.
    * 
    * @param func the function to derivate.
    * 
    *    @param x the evaluation point.
    * 
    *    @param h the error.
    * 
    *    @return the estimate of the first derivative.
    * 
    */
   public static double finiteCenteredDifferenceDerivative (
                 MathFunction func, double x, double h) {
      final double fplus = func.evaluate (x + h);
      final double fminus = func.evaluate (x - h);
      return (fplus - fminus) / (2*h);
   }


   /**
    * Computes and returns an estimate of the <SPAN CLASS="MATH"><I>n</I></SPAN>th derivative of the
    *  function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> using finite centered differences.
    *  If <SPAN CLASS="MATH"><I>n</I></SPAN> is even, this method returns
    * {@link #finiteDifferenceDerivative(MathFunction, double, int, double) finiteDifferenceDerivative}&nbsp;<TT>(func, x - <SPAN CLASS="MATH"><I>&#949;</I></SPAN>*n/2, n, h)</TT>, with 
    * <SPAN CLASS="MATH"><I>h</I> = <I>&#949;</I><SUP>n</SUP></SPAN>.
    * 
    * @param func the function to derivate.
    * 
    *    @param x the evaluation point.
    * 
    *    @param n the order of the derivative.
    * 
    *    @param h the error.
    * 
    *    @return the estimate of the derivative.
    * 
    */
   public static double finiteCenteredDifferenceDerivative (
                 MathFunction func, double x, int n, double h) {
      if (n < 0)
         throw new IllegalArgumentException
         ("n must not be negative");
      if (n == 0)
         return func.evaluate (x);
      if (n % 2 == 1)
         throw new IllegalArgumentException ("n must be even");
      final double err = Math.pow (h, 1.0 / n);
      return finiteDifferenceDerivative (func, x - n*err / 2, n, h);
   }


   /**
    * Removes any point <TT>(NaN, y)</TT> or <TT>(x, NaN)</TT> from
    *  <TT>x</TT> and <TT>y</TT>, and returns a 2D array containing the filtered
    *  points. This method filters each pair (<TT>x[i]</TT>, <TT>y[i]</TT>)
    *  containing at least one NaN element. It constructs a 2D array containing
    *  the two filtered arrays, whose size is smaller than or equal to
    *  <TT>x.length</TT>.
    * 
    * @param x the <SPAN CLASS="MATH"><I>X</I></SPAN> coordinates.
    * 
    *    @param y the <SPAN CLASS="MATH"><I>Y</I></SPAN> coordinates.
    * 
    *    @return the filtered <SPAN CLASS="MATH"><I>X</I></SPAN> and <SPAN CLASS="MATH"><I>Y</I></SPAN> arrays.
    * 
    */
   public static double[][] removeNaNs (double[] x, double[] y) {
      if (x.length != y.length)
         throw new IllegalArgumentException();
      int numNaNs = 0;
      for (int i = 0; i < x.length; i++)
         if (Double.isNaN (x[i]) || Double.isNaN (y[i]))
            ++numNaNs;
      if (numNaNs == 0)
         return new double[][] { x, y };
      final double[] nx = new double[x.length - numNaNs];
      final double[] ny = new double[y.length - numNaNs];
      int j = 0;
      for (int i = 0; i < x.length; i++)
         if (!Double.isNaN (x[i]) && !Double.isNaN (y[i])) {
            nx[j] = x[i];
            ny[j++] = y[i];
         }
      return new double[][] { nx, ny };
   }


   /**
    * Returns the integral of the function <TT>func</TT> over <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    *  If the given function implements {@link MathFunctionWithIntegral},
    *  this returns
    *  {@link umontreal.iro.lecuyer.functions.MathFunctionWithIntegral#integral MathFunctionWithIntegral.integral}&nbsp;<TT>(double, double)</TT>.
    *  Otherwise, this calls
    *  {@link #simpsonIntegral simpsonIntegral}&nbsp;<TT>(MathFunction, double, double, int)</TT>
    *  with {@link #NUMINTERVALS NUMINTERVALS} intervals.
    * 
    * @param func the function to integrate.
    * 
    *    @param a the lower bound.
    * 
    *    @param b the upper bound.
    * 
    *    @return the value of the integral.
    * 
    */
   public static double integral (MathFunction func, double a, double b)  {
      if (func instanceof MathFunctionWithIntegral)
         return ((MathFunctionWithIntegral)func).integral (a, b);
      else
         return simpsonIntegral (func, a, b, NUMINTERVALS);
   }


   /**
    * Computes and returns an approximation of the integral of <TT>func</TT> over
    *  <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>, using the Simpson's <SPAN CLASS="MATH">1/3</SPAN> method with <TT>numIntervals</TT>
    *  intervals. This method estimates
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * &int;<SUB>a</SUB><SUP>b</SUP><I>f</I> (<I>x</I>)<I>dx</I>,
    * </DIV><P></P>
    * where <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> is the function defined by <TT>func</TT> evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>,
    *  by dividing <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> in <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>numIntervals</TT> intervals of length
    *  
    * <SPAN CLASS="MATH"><I>h</I> = (<I>b</I> - <I>a</I>)/<I>n</I></SPAN>. The integral is estimated by
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="MathFunctionUtilimg2.png"
    *  ALT="$\displaystyle {\frac{{h}}{{3}}}$">(<I>f</I> (<I>a</I>) + 4<I>f</I> (<I>a</I> + <I>h</I>) + 2<I>f</I> (<I>a</I> + 2<I>h</I>) + 4<I>f</I> (<I>a</I> + 3<I>h</I>) + <SUP> ... </SUP> + <I>f</I> (<I>b</I>))
    * </DIV><P></P>
    * This method assumes that 
    * <SPAN CLASS="MATH"><I>a</I>&nbsp;&lt;=&nbsp;<I>b</I> &lt; &#8734;</SPAN>, and <SPAN CLASS="MATH"><I>n</I></SPAN> is even.
    * 
    * @param func the function being integrated.
    * 
    * @param a the left bound
    * 
    * @param b the right bound.
    * 
    * @param numIntervals the number of intervals.
    * 
    * @return the approximate value of the integral.
    * 
    */
   public static double simpsonIntegral (MathFunction func, double a,
                                         double b, int numIntervals)  {
      if (numIntervals % 2 != 0)
         throw new IllegalArgumentException
         ("numIntervals must be an even number");
      if (Double.isInfinite (a) || Double.isInfinite (b) ||
         Double.isNaN (a) || Double.isNaN (b))
         throw new IllegalArgumentException
             ("a and b must not be infinite or NaN");
      if (b < a)
         throw new IllegalArgumentException ("b < a");
      if (a == b)
         return 0;
      double[] D = fixBounds (func, a, b, numIntervals);
      a = D[0];
      b = D[1];
      final double h = (b - a) / numIntervals;
      final double h2 = 2*h;
      final int m = numIntervals / 2;
      double sum = 0;
      for (int i = 0; i < m - 1; i++) {
         final double x = a + h + h2*i;
         sum += 4*func.evaluate (x) + 2*func.evaluate (x + h);
      }
      sum += func.evaluate (a) + func.evaluate (b) + 4*func.evaluate (b - h);
      return sum * h / 3;
   }


   /**
    * Computes and returns a numerical approximation of the integral of
    *   <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> over <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>, using Gauss-Lobatto adaptive quadrature with
    *    5 nodes, with tolerance <TT>tol</TT>. This method estimates
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * &int;<SUB>a</SUB><SUP>b</SUP><I>f</I> (<I>x</I>)<I>dx</I>,
    * </DIV><P></P>
    * where <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> is the function defined by <TT>func</TT>.
    * Whenever the estimated error is larger than <TT>tol</TT>, the interval
    * <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> will be halved in two smaller intervals, and  the method will
    *  recursively call itself on the two smaller intervals until the estimated
    *  error is smaller than <TT>tol</TT>.
    * 
    * @param func the function being integrated.
    * 
    * @param a the left bound
    * 
    * @param b the right bound.
    * 
    * @param tol error.
    * 
    * @return the approximate value of the integral.
    * 
    */
   public static double gaussLobatto (MathFunction func, double a, double b,
                                      double tol) {
      if (b < a)
         throw new IllegalArgumentException ("b < a");
      if (Double.isInfinite (a) || Double.isInfinite (b) ||
          Double.isNaN (a) || Double.isNaN (b))
         throw new IllegalArgumentException ("a or b is infinite or NaN");
      if (a == b)
         return 0;
      double r0 = simpleGaussLob (func, a, b);
      final double h = (b - a)/2;
      double r1 = simpleGaussLob (func, a, a + h) +
                  simpleGaussLob (func, a + h, b);
      if (Math.abs(r0 - r1) <= tol)
         return r1;
      return gaussLobatto (func, a, a + h, tol) +
             gaussLobatto (func, a + h, b, tol);
   }


   private static double simpleGaussLob (MathFunction func, double a, double b) {
      // Gauss-Lobatto integral over [a, b] with 5 nodes
      if (a == b)
         return 0;
      final double h = b - a;
      double sum = 0;
      for (int i = 0; i < 5; i++) {
         sum += Wg[i] * func.evaluate(a + h*Cg[i]);
      }
      return h*sum;
   }


   /**
    * Similar to method
    * {@link #gaussLobatto gaussLobatto}<TT>(MathFunction, double, double, double)</TT>, but
    * also returns in <TT>T[0]</TT> the subintervals of integration, and in
    * <TT>T[1]</TT>, the partial values of the integral over the corresponding
    *  subintervals. Thus <TT>T[0][0]</TT> <SPAN CLASS="MATH">= <I>x</I><SUB>0</SUB> = <I>a</I></SPAN> and <TT>T[0][n]</TT>
    *   <SPAN CLASS="MATH">= <I>x</I><SUB>n</SUB> = <I>b</I></SPAN>; <TT>T[1][i]</TT> contains the value of the integral over
    *   the subinterval 
    * <SPAN CLASS="MATH">[<I>x</I><SUB>i-1</SUB>, <I>x</I><SUB>i</SUB>]</SPAN>; we also have <TT>T[1][0]</TT> <SPAN CLASS="MATH">= 0</SPAN>.
    *  The sum over all <TT>T[1][i]</TT>, for 
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>n</I></SPAN> gives the
    *  value of the integral over <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>, which is the value returned by this
    *  method. <SPAN  CLASS="textit">WARNING:</SPAN> The user <SPAN  CLASS="textit">must reserve</SPAN>  the 2 elements
    *  of the first dimension (<TT>T[0]</TT> and <TT>T[1]</TT>)
    *  before calling this method.
    *  
    * @param func function being integrated
    * 
    * @param a left bound of interval
    * 
    * @param b right bound of interval
    * 
    * @param tol error
    * 
    * @param T <SPAN CLASS="MATH">(<I>x</I>, <I>y</I>)</SPAN> = (values of partial intervals,partial values of integral)
    * 
    * @return value of the integral
    * 
    */
   public static double gaussLobatto (MathFunction func, double a, double b,
                                      double tol, double[][] T)  {
      if (b < a)
         throw new IllegalArgumentException ("b < a");
      if (a == b) {
         T[0] = new double [1];
         T[1] = new double [1];
         T[0][0] = a;
         T[1][0] = 0;
         return 0;
      }

      int n = 1;         // initial capacity
      T[0] = new double [n];
      T[1] = new double [n];
      T[0][0] = a;
      T[1][0] = 0;
      int[] s = {0};    // actual number of intervals
      double res = innerGaussLob (func, a, b, tol, T, s);
      n = s[0] + 1;
      double[] temp = new double[n];
      System.arraycopy (T[0], 0, temp, 0, n);
      T[0] = temp;
      temp = new double[n];
      System.arraycopy (T[1], 0, temp, 0, n);
      T[1] = temp;
      return res;
   }


   private static double innerGaussLob (MathFunction func, double a, double b,
                                        double tol, double[][] T, int[] s) {
      double r0 = simpleGaussLob (func, a, b);
      final double h = (b - a) / 2;
      double r1 = simpleGaussLob (func, a, a + h) +
                  simpleGaussLob (func, a + h, b);
      if (Math.abs(r0 - r1) <= tol) {
         ++s[0];
         int len = s[0];
         if (len >= T[0].length) {
            double[] temp = new double[2 * len];
            System.arraycopy (T[0], 0, temp, 0, len);
            T[0] = temp;
            temp = new double[2 * len];
            System.arraycopy (T[1], 0, temp, 0, len);
            T[1] = temp;
         }
         T[0][len] = b;
         T[1][len] = r1;
         return r1;
      }

      return innerGaussLob (func, a, a + h, tol, T, s) +
             innerGaussLob (func, a + h, b, tol, T, s);
   }

}
