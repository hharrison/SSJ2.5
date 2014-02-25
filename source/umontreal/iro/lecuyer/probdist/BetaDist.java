

/*
 * Class:        BetaDist
 * Description:  beta distribution
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
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>beta</EM> distribution with shape parameters
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>, over the interval <SPAN CLASS="MATH">(<I>a</I>, <I>b</I>)</SPAN>, where <SPAN CLASS="MATH"><I>a</I> &lt; <I>b</I></SPAN>.
 * It has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>x</I> - <I>a</I>)<SUP><I>&#945;</I>-1</SUP>(<I>b</I> - <I>x</I>)<SUP><I>&#946;</I>-1</SUP>/[<I>B</I>(<I>&#945;</I>, <I>&#946;</I>)(<I>b</I> - <I>a</I>)<SUP><I>&#945;</I>+<I>&#946;</I>-1</SUP>]
 * </DIV><P></P>
 * for <SPAN CLASS="MATH"><I>a</I> &lt; <I>x</I> &lt; <I>b</I></SPAN>, and 0 elsewhere.  It has distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>I</I><SUB><I>&#945;</I>, <I>&#946;</I></SUB>(<I>x</I>) = &int;<SUB>a</SUB><SUP>x</SUP>(<I>&#958;</I> - <I>a</I>)<SUP><I>&#945;</I>-1</SUP>(<I>b</I> - <I>&#958;</I>)<SUP><I>&#946;</I>-1</SUP>/[<I>B</I>(<I>&#945;</I>, <I>&#946;</I>)(<I>b</I> - <I>a</I>)<SUP><I>&#945;</I>+<I>&#946;</I>-1</SUP>]<I>d&#958;</I>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>a</I> &lt; <I>x</I> &lt; <I>b</I>,
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>B</I>(<I>&#945;</I>, <I>&#946;</I>)</SPAN> is the <EM>beta</EM> function defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:betadef"></A>
 * <I>B</I>(<I>&#945;</I>, <I>&#946;</I>) = <I>&#915;</I>(<I>&#945;</I>)<I>&#915;</I>(<I>&#946;</I>)/<I>&#915;</I>(<I>&#945;</I> + <I>&#946;</I>),
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined in
 * {@link GammaDist}.
 * 
 */
public class BetaDist extends ContinuousDistribution {
   private static final double RENORM = 1.0e300;
   protected double alpha;         // First parameter
   protected double beta;          // Second parameter
   protected double a, b;          // Interval x in [a, b]
   protected double bminusa;
   protected double logFactor;
   protected double Beta;          // Function Beta(alpha, beta)
   protected double logBeta;       // Ln(Beta(alpha, beta))

   private static class Optim implements Lmder_fcn
   {
      private double a;
      private double b;

      public Optim (double a, double b)
      {
         this.a = a;
         this.b = b;
      }

      public void fcn (int m, int n, double[] x, double[] fvec, double[][] fjac, int iflag[])
      {
         if (x[1] <= 0.0 || x[2] <= 0.0) {
             final double BIG = 1.0e100;
             fvec[1] = BIG;
             fvec[2] = BIG;
             fjac[1][1] = BIG;
             fjac[1][2] = 0.0;
             fjac[2][1] = 0.0;
             fjac[2][2] = BIG;
             return;
         }

         double trig;
         if (iflag[1] == 1)
         {
            trig = Num.digamma (x[1] + x[2]);
            fvec[1] = Num.digamma(x[1]) - trig - a;
            fvec[2] = Num.digamma(x[2]) - trig - b;
         }
         else if (iflag[1] == 2)
         {
            trig = Num.trigamma (x[1] + x[2]);

            fjac[1][1] = Num.trigamma (x[1]) - trig;
            fjac[1][2] = - trig;
            fjac[2][1] = - trig;
            fjac[2][2] = Num.trigamma (x[2]) - trig;
         }
      }
   }


   /**
    * Constructs a <TT>BetaDist</TT> object with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>
    *      <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT> and default domain <SPAN CLASS="MATH">(0, 1)</SPAN>.
    * 
    */
   public BetaDist (double alpha, double beta) {
      setParams (alpha, beta, 0.0, 1.0, decPrec);
   }


   /**
    * Constructs a <TT>BetaDist</TT> object with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>
    *      <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, and domain <SPAN CLASS="MATH">(</SPAN><TT>a</TT><SPAN CLASS="MATH">,</SPAN>&nbsp;<TT>b</TT><SPAN CLASS="MATH">)</SPAN>.
    * 
    */
   public BetaDist (double alpha, double beta, double a, double b) {
      setParams (alpha, beta, a, b, decPrec);
   }


   /**
    * Constructs a <TT>BetaDist</TT> object with parameters 
    * <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>
    *    <TT>alpha</TT> and 
    * <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, and approximations of roughly <TT>d</TT>
    *    decimal digits of precision when computing the distribution,  complementary
    *    distribution, and inverse functions. The default domain <SPAN CLASS="MATH">(0, 1)</SPAN> is used.
    * 
    */
   public BetaDist (double alpha, double beta, int d) {
      setParams (alpha, beta, 0.0, 1.0, d);
   }


   /**
    * Constructs a <TT>BetaDist</TT> object with parameters 
    * <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>
    *    and 
    * <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, and approximations of roughly <TT>d</TT>
    *    decimal digits of precision when computing distribution, complementary
    *    distribution, and inverse functions.  The domain <SPAN CLASS="MATH">(</SPAN><TT>a</TT><SPAN CLASS="MATH">,</SPAN>&nbsp;<TT>b</TT><SPAN CLASS="MATH">)</SPAN>
    *    is used.
    * 
    */
   public BetaDist (double alpha, double beta, double a, double b, int d) {
      setParams (alpha, beta, a, b, d);
   }


   public double density (double x) {
      if (x <= a || x >= b)
         return 0;
      double temp = (alpha - 1) * Math.log(x - a) + (beta - 1) * Math.log(b - x);
//      return factor*Math.pow (x - a, alpha - 1)*Math.pow (b - x, beta - 1);
      return Math.exp(logFactor + temp);
   }

   public double cdf (double x) {
      return cdf (alpha, beta, decPrec, (x - a)/bminusa);
   }

   public double inverseF (double u) {
      return a + (b - a)*inverseF (alpha, beta,  decPrec, u);
   }

   public double getMean() {
      return BetaDist.getMean (alpha, beta, a, b);
   }

   public double getVariance() {
      return BetaDist.getVariance (alpha, beta, a, b);
   }

   public double getStandardDeviation() {
      return BetaDist.getStandardDeviation (alpha, beta, a, b);
   }

   /**
    * Same as
    *  {@link #density(double,double,double,double,double) density}&nbsp;<TT>(alpha, beta, 0, 1, x)</TT>.
    * 
    */
   public static double density (double alpha, double beta, double x) {
      return density (alpha, beta, 0.0, 1.0, x);
   }


   /**
    * Computes the density function of the <EM>beta</EM> distribution.
    * 
    */
   public static double density (double alpha, double beta,
                                 double a, double b, double x) {
      if (a >= b)
         throw new IllegalArgumentException ("a >= b");
      if (x <= a || x >= b)
         return 0;

      double z = -Num.lnBeta (alpha, beta) - (alpha + beta - 1)* Math.log(b-a) +
		      (alpha-1)*Math.log(x-a) + (beta-1)*Math.log(b-x);
      return Math.exp(z);
   }


/*
 *    Submittal of an algorithm for publication in one of the  ACM
 *    Transactions implies that unrestricted use of the algorithm within a
 *    computer is permissible. General permission to copy and distribute
 *    the algorithm without fee is granted provided that the copies are not
 *    made or distributed for direct commercial  advantage. The ACM
 *    copyright notice and the title of the publication and its date appear,
 *    and  notice is given that copying is by permission of the Association
 *    for Computing Machinery. To copy otherwise, or to republish, requires
 *    a fee and/or specific permission.
 */

/*
 * Part of the algorithm for the cdf below is taken from
 *     W. Gautschi, Algorithm 222: Incomplete Beta Function Ratios,
 *     Communications of the ACM, Vol. 7, 3, pp 143-144, 1964.
 */
   private static double isubx_alphabeta_small (double alpha, double beta,
                                                double x, int d)
   /*
    * Evaluates beta (alpha, beta, d, x) when 0 < alpha <= 1 and
    * 0 < beta <= 2 to a
    * precision of d = -log10 (2 epsilon) decimal digits. Uses a series
    * expansion in powers of x.
    */
   {
      int k = 0;
      double s, u, v;
      double epsilon;
      if (alpha <= 0.0 || alpha > 1.0)
        throw new IllegalArgumentException ("alpha not in (0, 1] ");
      if (beta <= 0.0 || beta > 2.0)
        throw new IllegalArgumentException ("beta not in (0, 2] ");

      epsilon = EPSARRAY[d];
      u = Math.pow (x, alpha);
      s = u/alpha;
      do {
         u = (k + 1 - beta)*x*u/(k + 1);
         v = u/(k + 1 + alpha);
         s += v;
         k++;
      } while ((Math.abs (v)/s) > epsilon);

      v = Num.lnGamma (alpha + beta) - Num.lnGamma (alpha) - Num.lnGamma (beta);
      return s*Math.exp (v);
   }

   private static void forward (double alpha, double beta, double x,
                                double I0, double I1, int nmax, double I[])
   /*
    * Given I0 = beta (alpha, beta, x) and I1 = beta (alpha, beta + 1, x),
    * generates beta (alpha, beta + n, x) for n = 0, 1, 2, ..., nmax, and
    * stores the result in I.
    */
   {
      int n;

      I[0] = I0;
      if (nmax > 0)
         I[1] = I1;
      for (n = 1; n < nmax; n++)
         I[n + 1] = (1 + (n - 1 + alpha + beta)*(1. - x)/(n + beta))*I[n]
            - (n - 1 + alpha + beta)*(1. - x)*I[n - 1]/(n + beta);
   }

   private static void backward (double alpha, double beta, double x,
                                 double I0, int d, int nmax, double I[])
   /*
    * Given I0 = beta (alpha, beta, x), generates beta (alpha + n, beta, x)
    * for n = 0, 1, 2,..., nmax to d significant digits, using a variant of
    * J.C.P. Miller's backward recurrence algorithm. Stores the result in I.
    *  Reference ???
    */
   {
      int n, nu, m, ntab;
      boolean again;
      double[] Itemp, Iapprox, Rr;
      double epsilon, r;

      I[0] = I0;
      if (nmax == 0)
         return;

      epsilon = EPSARRAY[d];
      nu = 2*nmax + 5;
      ntab = 64;
      while (ntab <= nu)
         ntab *= 2;

      Rr = new double[ntab];
      Iapprox = new double[ntab];
      Itemp = new double[ntab];

      for (n = 1; n <= nmax; n++)
         Iapprox[n] = 0.0;
      for (n = 0; n <= nmax; n++)
         Itemp[n] = I[n];

      do {
         n = nu;
         r = 0.0;
         do {
            r = (n - 1 + alpha + beta)*x/
                (n + alpha + (n - 1 + alpha + beta)*x - (n + alpha)*r);
            if (n <= nmax)
               Rr[n - 1] = r;
            n--;
         } while (n >= 1);

         for (n = 0; n < nmax; n++)
            Itemp[n + 1] = Rr[n]*Itemp[n];

         again = false;
         for (n = 1; n <= nmax; n++) {
            if (Math.abs ((Itemp[n] - Iapprox[n])/Itemp[n]) > epsilon) {
               again = true;
               for (m = 1; m <= nmax; m++)
                  Iapprox[m] = Itemp[m];
               nu += 5;
               if (ntab <= nu) {
                  ntab *= 2;
                  double[] nT = new double[ntab];
                  System.arraycopy (Rr, 0, nT, 0, Rr.length);
                  Rr = nT;
                  nT = new double[ntab];
                  System.arraycopy (Iapprox, 0, nT, 0, Iapprox.length);
                  Iapprox = nT;
                  nT = new double[ntab];
                  System.arraycopy (Itemp, 0, nT, 0, Itemp.length);
                  Itemp = nT;
               }
               break;
            }
         }
      } while (again);

      for (n = 0; n <= nmax; n++)
         I[n] = Itemp[n];
   }

   private static void isubx_beta_fixed (double alpha, double beta, double x,
                                         int d, int nmax, double I[])
   /*
    * Generates beta (alpha + n, beta, x), 0 < alpha <= 1, for n = 0, 1, 2,...,
    * nmax to d significant digits, using procedure backward. First reduces
    * beta modulo 1 to beta0, where 0 < beta0 <= 1.
    */
   {
      int m, mmax;
      double s, beta0, Ibeta0, Ibeta1 = 0.0;
      double[] Ibeta;

      if (alpha <= 0.0 || alpha > 1.0)
        throw new IllegalArgumentException ("alpha not in (0, 1] ");
      m = (int)beta;                         // integer part of beta
      s = beta - m;                     // fractionnal part of beta
      if (s > 0) {
         beta0 = s;
         mmax = m;
      }
      else {
         beta0 = s + 1;
         mmax = m - 1;
      }
      Ibeta0 = RENORM * isubx_alphabeta_small (alpha, beta0, x, d);
      if (mmax > 0)
         Ibeta1 = RENORM * isubx_alphabeta_small (alpha, beta0 + 1.0, x, d);

      Ibeta = new double[mmax + 1];
      forward (alpha, beta0, x, Ibeta0, Ibeta1, mmax, Ibeta);
      backward (alpha, beta, x, Ibeta[mmax], d, nmax, I);
      for (m = 0; m <= nmax; m++)
         I[m] /= RENORM;
   }

   private static void isubx_alpha_fixed (double alpha, double beta,
           double x, int d, int nmax, double I[])
   /*
    * Generates beta (alpha, beta + n, x), 0 < beta <= 1, for n = 0, 1, 2,...,
    * nmax to d significant digits, using procedure forward.
    */
   {
      int m, mmax;
      double s, alpha0, I0, Ibeta0, I1, Ibeta1;
      double[] Ialpha;

      if (beta <= 0.0 || beta > 1.0)
        throw new IllegalArgumentException ("beta not in (0, 1] ");

      m = (int)alpha;         // integer part of alpha
      s = alpha - m;          // fractionnal part of alpha
      if (s > 0) {
         alpha0 = s;
         mmax = m;
      }
      else {
         alpha0 = s + 1;
         mmax = m - 1;
      }
      I0 = RENORM * isubx_alphabeta_small (alpha0, beta, x, d);
      I1 = RENORM * isubx_alphabeta_small (alpha0, beta + 1.0, x, d);

      Ialpha = new double[mmax + 1];
      backward (alpha0, beta, x, I0, d, mmax, Ialpha);
      Ibeta0 = Ialpha[mmax];
      backward (alpha0, beta + 1.0, x, I1, d, mmax, Ialpha);
      Ibeta1 = Ialpha[mmax];
      forward (alpha, beta, x, Ibeta0, Ibeta1, nmax, I);
      for (m = 0; m <= nmax; m++)
         I[m] /= RENORM;
   }

   private static void beta_beta_fixed (double alpha, double beta,
           double x, int d, int nmax, double I[])
   {
      int n;
      if (alpha <= 0.0 || alpha > 1.0)
        throw new IllegalArgumentException ("alpha not in (0, 1]");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");
      if (nmax < 0)
        throw new IllegalArgumentException ("nmax < 0");
      if (x == 0.0 || x == 1.0) {
         for (n = 0; n <= nmax; n++)
            I[n] = x;
         return;
      }
      if (x <= 0.5)
         isubx_beta_fixed (alpha, beta, x, d, nmax, I);
      else {
         isubx_alpha_fixed (beta, alpha, 1.0 - x, d, nmax, I);
         for (n = 0; n <= nmax; n++)
            I[n] = 1.0 - I[n];
      }
  }

   private static void beta_alpha_fixed (double alpha, double beta,
           double x, int d, int nmax, double I[])
   {
      int n;
      if (beta <= 0.0 || beta > 1.0)
        throw new IllegalArgumentException ("beta not in (0, 1]");
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (nmax < 0)
        throw new IllegalArgumentException ("nmax < 0");
      if (x == 0.0 || x == 1.0) {
         for (n = 0; n <= nmax; n++)
            I[n] = x;
         return;
      }
      if (x <= 0.5)
         isubx_alpha_fixed (alpha, beta, x, d, nmax, I);
      else {
         isubx_beta_fixed (beta, alpha, 1.0 - x, d, nmax, I);
         for (n = 0; n <= nmax; n++)
            I[n] = 1.0 - I[n];
      }
   }

   private static double beta_g (double x, int d)
   /*
    * Used in the normal approximation of beta. This is the function
    * (1.0 - x*x + 2.0*x*log (x))/((1.0 - x)*(1.0 - x)).
    */
   {
      double epsilon;
      double y, sum, term, inc;
      int j;

      if (x > 1.3)
         return -beta_g (1.0/x, d);
      if (x < 1.0E-20)
         return 1.0;
      if (x < 0.7)
         return (1.0 - x*x + 2.0*x*Math.log (x))/((1.0 - x)*(1.0 - x));
      if (x == 1.0)
         return 0.0;

      // For x near 1, to avoid loss of precision, use a series expansion
      epsilon = EPSARRAY[d];
      y = 1.0 - x;
      sum = 0.0;
      term = 1.0;
      j = 2;
      do {
         term *= y;
         inc = term/(j*(j + 1));
         sum += inc;
         j++;
      } while (Math.abs (inc/sum) > epsilon);
      return 2.0*sum;
   }

   /**
    * Same as
    * {@link #cdf(double,double,double,double,int,double) cdf}&nbsp;<TT>(alpha, beta, 0, 1, d, x)</TT>.
    * 
    */
   public static double cdf (double alpha, double beta, int d, double x) {
   /*
    * The exact section of beta below is very slow for large parameters.
    * It is an old algorithm of Gautschi (1964). There is an algorithm
    * for beta that is recent and is supposed to be very fast.
    * ACM Trans. Math. Soft. 18, pp 360, (1992).  (R. Simard.)
    */

   /*
    * I[j] will contain either the values of cdf (alpha0 + j, beta, d, x),
    * where 0 < alpha0 <= 1, for j = 0, 1, 2, ..., n,  with
    * alpha = alpha0 + n;
    * or the values of cdf (alpha, beta0 + j, d, x), where 0 < beta0 <= 1,
    * for j = 0, 1, 2, ..., n, with beta = beta0 + n.
    */

      final double ALPHABETAMAX = 1000.0;
      final double ALPHABETALIM = 30.0;
      int n;
      boolean flag = false;
      double alpha0, beta0, u, temp, yd, gam, h1, h3, y;
      double[] I;

      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
        throw new IllegalArgumentException ("beta <= 0");
      if (d <= 0)
        throw new IllegalArgumentException ("d <= 0");
      if (x <= 0.0)
         return 0.0;
      if (x >= 1.0)
         return 1.0;

      if (Math.max (alpha, beta) <= ALPHABETAMAX) {
         if (alpha < beta) {
            n = (int)alpha;                   // integer part of alpha
            alpha0 = alpha - n;              // fractionnal part of alpha
            if (alpha0 <= 0.0) {         // alpha0 == 0 not allowed
               alpha0 = 1.0;
               n--;
            }
            I = new double[n + 1];
            beta_beta_fixed (alpha0, beta, x, d, n, I);
            u = I[n];
            /* There may be numerical errors far in
             * the tails giving very small
             *  negative values instead of 0. */
            if (u <= 0.0)
               return 0.0;
            else if (u <= 1.0)
               return u;
            else
               return 1.0;
         }
         else {
            n = (int)beta;                   // integer part of beta
            beta0 = beta - n;              // fractionnal part of beta
            if (beta0 <= 0.0) {         // beta0 == 0 not allowed
               beta0 = 1.0;
               n--;
            }
            I = new double[n + 1];
            beta_alpha_fixed (alpha, beta0, x, d, n, I);
            u = I[n];

            /* There may be numerical errors far in
             * the tails giving very small
             * negative values instead of 0. */
            if (u <= 0.0)
               return 0.0;
            else if (u <= 1.0)
               return u;
            else
               return 1.0;
         }
      }

      if ((alpha > ALPHABETAMAX && beta < ALPHABETALIM) ||
          (beta > ALPHABETAMAX && alpha < ALPHABETALIM)) {
         // Bol'shev approximation for large max (alpha, beta)
         // and small min (alpha, beta)
         if (x > 0.5)
            return 1.0 - cdf (beta, alpha, d, 1.0 - x);

         if (alpha < beta) {
            u = alpha;
            alpha = beta;
            beta = u;
            flag = false;
         }
         else
            flag = true;

         u = alpha + 0.5*beta - 0.5;
         if (!flag)
            temp = x/(2.0 - x);
         else
            temp = (1.0 - x)/(1.0 + x);
         yd = 2.0*u*temp;
         gam = (Math.exp (beta*Math.log (yd) - yd -
                  Num.lnGamma (beta))*(2.0*yd*yd - (beta -
                  1.0)*yd - (beta*beta - 1.0)))/(24.0*u*u);
         if (flag) {
            yd = GammaDist.barF (beta, d, yd);
            return yd - gam;
         } else {
            yd = GammaDist.cdf (beta, d, yd);
            return yd + gam;
         }
      }

      // Normal approximation of Peizer and Pratt.   Reference: \cite{tPEI68a}
      h1 = alpha + beta - 1.0;
      y = 1.0 - x;
      h3 = Math.sqrt ((1.0 + y*beta_g ((alpha - 0.5)/(h1*x), d)
            + x*beta_g ((beta - 0.5)/(h1*y), d))
         /((h1 + 1.0/6.0)*x*y))
         *((h1 + 1.0/3.0 + 0.02*(1.0/alpha + 1.0/beta + 1.0/(alpha + beta)))
         *x - alpha + 1.0/3.0 - 0.02/alpha - 0.01/(alpha + beta));

      return NormalDist.cdf01 (h3);
   }


   /**
    * Computes an approximation of the distribution function, with roughly
    *   <TT>d</TT> decimal digits of precision.
    * 
    */
   public static double cdf (double alpha, double beta,
                             double a, double b, int d, double x) {
      return cdf (alpha, beta, d, (x - a)/(b - a));
   }


   /**
    * Same as
    *  {@link #barF(double,double,double,double,int,double) barF}&nbsp;<TT>(alpha, beta, 0, 1, d, x)</TT>.
    * 
    */
   public static double barF (double alpha, double beta, int d, double x) {
      return 1.0 - cdf (alpha, beta, d, x);
   }


   /**
    * Computes the complementary distribution function.
    * 
    */
   public static double barF (double alpha, double beta,
                              double a, double b, int d, double x) {
      if (a >= b)
         throw new IllegalArgumentException ("a >= b");
      return 1.0 - cdf (alpha, beta, d, (x - a)/(b - a));
   }


   /**
    * Same as
    *  {@link #inverseF(double,double,double,double,int,double) inverseF}&nbsp;<TT>(alpha, beta, 0, 1, d, u)</TT>.
    * 
    */
   public static double inverseF (double alpha, double beta, int d, double u) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (d <= 0)
        throw new IllegalArgumentException ("d <= 0");
      if (u > 1.0 || u < 0.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= 0)
         return 0;
      if (u >= 1)
         return 1;

      /*
       * Code taken from
       * Cephes Math Library Release 2.8:  June, 2000
       * Copyright 1984, 1996, 2000 by Stephen L. Moshier
       */
      final double MACHEP = 1.11022302462515654042E-16;
      final double MAXLOG = 7.09782712893383996843E2;
      final double MINLOG = -7.08396418532264106224E2;
      final double MAXNUM = 1.7976931348623158E308;

      boolean ihalve = false;
      boolean newt = false;

      double p = 0, q = 0, y0 = 0, z = 0, y = 0, x = 0, x0, x1, lgm = 0,
             yp = 0, di = 0, dithresh = 0, yl, yh, xt = 0;
      int i, dir;
      boolean rflg, nflg;
      x0 = 0.0;
      yl = 0.0;
      x1 = 1.0;
      yh = 1.0;
      nflg = false;
      rflg = false;
      if (alpha <= 1.0 || beta <= 1.0) {
         dithresh = 1.0e-6;
         rflg = false;
         p = alpha;
         q = beta;
         y0 = u;
         x = p/(p+q);
         y = cdf (p, q, d, x);
         ihalve = true;
      }
      else
         dithresh = 1.0e-4;

mainloop:
      while (true) {
         if (ihalve) {
            ihalve = false;
            dir = 0;
            di = 0.5;
            for (i = 0; i<100; i++) {
               if (i != 0) {
                  x = x0  +  di*(x1 - x0);
                  if (x == 1.0)
                     x = 1.0 - MACHEP;
                  if (x == 0.0) {
                     di = 0.5;
                     x = x0  +  di*(x1 - x0);
                     if (x == 0.0) {
                        // System.err.println ("BetaDist.inverseF: underflow");
                        return 0;
                     }
                  }
                  y = cdf (p, q, d, x);
                  yp = (x1 - x0)/(x1 + x0);
                  if (Math.abs (yp) < dithresh) {
                     newt = true;
                     continue mainloop;
                  }
                  yp = (y-y0)/y0;
                  if (Math.abs (yp) < dithresh) {
                     newt = true;
                     continue mainloop;
                  }
               }
               if (y < y0) {
                  x0 = x;
                  yl = y;
                  if (dir < 0) {
                     dir = 0;
                     di = 0.5;
                  }
                  else if (dir > 3)
                     di = 1.0 - (1.0 - di)*(1.0 - di);
                  else if (dir > 1)
                     di = 0.5*di + 0.5;
                  else
                     di = (y0 - y)/(yh - yl);
                  dir += 1;
                  if (x0 > 0.75) {//            if (0 == y)
//               y = EPS;
                     if (rflg) {
                        rflg = false;
                        p = alpha;
                        q = beta;
                        y0 = u;
                     }
                     else {
                        rflg = true;
                        p = beta;
                        q = alpha;
                        y0 = 1.0 - u;
                     }
                     x = 1.0 - x;
                     y = cdf (p, q, d, x);
                     x0 = 0.0;
                     yl = 0.0;
                     x1 = 1.0;
                     yh = 1.0;
                     ihalve = true;
                     continue mainloop;
                  }
               }
               else {
                  x1 = x;
                  if (rflg && x1 < MACHEP) {
                     x = 0.0;
                     break mainloop;
                  }
                  yh = y;
                  if (dir > 0) {
                     dir = 0;
                     di = 0.5;
                  }
                  else if (dir < -3)
                     di = di*di;
                  else if (dir < -1)
                     di = 0.5*di;
                  else
                     di = (y - y0)/(yh - yl);
                  dir -= 1;
               }
            }
            // PLOSS error
            if (x0 >= 1.0) {
               x = 1.0 - MACHEP;
               break mainloop;
            }
            if (x <= 0.0) {
            // System.err.println ("BetaDist.inverseF: underflow");
               return 0 ;
            }
            newt = true;
         }
         if (newt) {
            newt = false;
            if (nflg)
               break mainloop;
            nflg = true;
            lgm = Num.lnGamma (p+q) - Num.lnGamma (p) - Num.lnGamma (q);

            for (i=0; i<8; i++) {
               /* Compute the function at this point. */
               if (i != 0)
                  y = cdf (p, q, d, x);
               if (y < yl) {
                  x = x0;
                  y = yl;
               }
               else if (y > yh) {
                  x = x1;
                  y = yh;
               }
               else if (y < y0) {
                  x0 = x;
                  yl = y;
               }
               else {
                  x1 = x;
                  yh = y;
               }
               if (x >= 1.0 || x <= 0.0)
                  break;
               /* Compute the derivative of the function at this point. */
               z = (p - 1.0)*Math.log (x) + (q - 1.0)*Math.log1p (-x) + lgm;
               if (z < MINLOG)
                  break mainloop;
               if (z > MAXLOG)
                  break;
               z = Math.exp (z);
               /* Compute the step to the next approximation of x. */
               z = (y - y0)/z;
               xt = x - z;
               if (xt <= x0) {
                  y = (x - x0) / (x1 - x0);
                  xt = x0 + 0.5*y*(x - x0);
                  if (xt <= 0.0)
                     break;
               }
               if (xt >= x1) {
                  y = (x1 - x) / (x1 - x0);
                  xt = x1 - 0.5*y*(x1 - x);
                  if (xt >= 1.0)
                     break;
               }
               x = xt;
               if (Math.abs (z/x) < 128.0*MACHEP)
                  break mainloop;
            }
            /* Did not converge.  */
            dithresh = 256.0*MACHEP;
            ihalve = true;
            continue mainloop;
         }

         yp = -NormalDist.inverseF01 (u);

         if (u > 0.5) {
            rflg = true;
            p = beta;
            q = alpha;
            y0 = 1.0 - u;
            yp = -yp;
         }
         else {
            rflg = false;
            p = alpha;
            q = beta;
            y0 = u;
         }

         lgm = (yp*yp - 3.0)/6.0;
         x = 2.0/(1.0/(2.0*p-1.0)  +  1.0/(2.0*q-1.0));
         z = yp*Math.sqrt (x + lgm)/x
           - (1.0/(2.0*q-1.0) - 1.0/(2.0*p-1.0) )
           * (lgm + 5.0/6.0 - 2.0/(3.0*x));
         z = 2.0*z;
         if (z < MINLOG) {
            x = 1.0;
            // System.err.println ("BetaDist.inverseF: underflow");
            return 0;
         }
         x = p/( p + q*Math.exp (z));
         y = cdf (p, q, d, x);
         yp = (y - y0)/y0;
         if (Math.abs (yp) < 0.2) {
            newt = true;
            continue mainloop;
         }
         ihalve = true;
      }

      // Done
      if (rflg) {
         if (x <= MACHEP)
            x = 1.0 - MACHEP;
         else
            x = 1.0 - x;
      }
      return x;
   }


   /**
    * Returns the inverse beta distribution function
    *   using the algorithm implemented in
    *    the <A NAME="tex2html1"
    *   HREF="http://www.moshier.net/">Cephes math library</A>.
    *    The method performs interval halving or Newton iterations to
    *    compute the inverse.  The precision depends on the
    *    accuracy of the {@link #cdf cdf} method.  The argument <TT>d</TT> gives
    *    a good idea of the precision attained.
    * 
    */
   public static double inverseF (double alpha, double beta,
                                  double a, double b, int d, double u) {
      if (a >= b)
        throw new IllegalArgumentException ("a >= b");
      return a + (b - a)*inverseF (alpha, beta, d, u);
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I>, <I>&#946;</I>)</SPAN> of the beta distribution over the
    *   interval <SPAN CLASS="MATH">[0, 1]</SPAN> using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      double sum;

      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      sum = 0.0;
      double a = 0.0;
      double b = 0.0;
      for (int i = 0; i < n; i++)
      {
         sum += x[i];
         if (x[i] > 0.0)
            a += Math.log (x[i]);
         else
            a -= 709.0;
         if (x[i] < 1.0)
            b += Math.log1p (-x[i]);
         else
            b -= 709.0;
      }
      double mean = sum / (double) n;

      sum = 0.0;
      for (int i = 0; i < n; i++)
         sum += (x[i] - mean) * (x[i] - mean);
      double var = sum / ((double) n - 1.0);

      Optim system = new Optim (a, b);

      double[] param = new double[3];
      param[1] = mean * ((mean * (1.0 - mean) / var) - 1.0);
      param[2] = (1.0 - mean) * ((mean * (1.0 - mean) / var) - 1.0);
      double[] fvec = new double [3];
      double[][] fjac = new double[3][3];
      int[] iflag = new int[2];
      int[] info = new int[2];
      int[] ipvt = new int[3];

      Minpack_f77.lmder1_f77 (system, 2, 2, param, fvec, fjac, 1e-5, info, ipvt);

      double parameters[] = new double[2];
      parameters[0] = param[1];
      parameters[1] = param[2];

      return parameters;
   }


   /**
    * Creates a new instance of a beta distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and
    *    <SPAN CLASS="MATH"><I>&#946;</I></SPAN> over the interval <SPAN CLASS="MATH">[0, 1]</SPAN> estimated using the maximum likelihood
    *     method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static BetaDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new BetaDist (parameters[0], parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#945;</I>/(<I>&#945;</I> + <I>&#946;</I>)</SPAN>
    *    of the beta distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, over the
    *    interval <SPAN CLASS="MATH">[0, 1]</SPAN>.
    * 
    * @return the mean of the Beta distribution
    * 
    */
   public static double getMean (double alpha, double beta) {
      return getMean (alpha, beta, 0.0, 1.0);
   }


   /**
    * Computes and returns the mean
    *    
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (<I>b&#945;</I> + <I>a&#946;</I>)/(<I>&#945;</I> + <I>&#946;</I>)</SPAN>
    *    of the beta distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> over the
    *     interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    * 
    * @return the mean of the Beta distribution
    * 
    */
   public static double getMean (double alpha, double beta, double a,
                                 double b)  {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      return (alpha*b + beta*a) / (alpha + beta);
   }


   /**
    * .
    * 
    * Computes and returns the variance
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="BetaDistimg1.png"
    *  ALT="$ {\frac{{\alpha\beta}}{{(\alpha + \beta)^2 (\alpha + \beta + 1)}}}$"></SPAN>
    *    of the beta distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, over the
    *     interval <SPAN CLASS="MATH">[0, 1]</SPAN>.
    * 
    * @return the variance of the beta distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#945;&#946;</I>/[(<I>&#945;</I> + <I>&#946;</I>)<SUP>2</SUP>(<I>&#945;</I> + <I>&#946;</I> + 1)]</SPAN>.
    * 
    */
   public static double getVariance (double alpha, double beta) {
      return getVariance (alpha, beta, 0.0, 1.0);
   }


   /**
    * .
    * 
    * Computes and returns the variance
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="BetaDistimg2.png"
    *  ALT="$ {\frac{{\alpha\beta(b-a)^2}}{{(\alpha + \beta)^2 (\alpha + \beta + 1)}}}$"></SPAN>
    *    of the beta distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, over the
    *     interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    * 
    * @return the variance of the beta distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#945;&#946;</I>/[(<I>&#945;</I> + <I>&#946;</I>)<SUP>2</SUP>(<I>&#945;</I> + <I>&#946;</I> + 1)]</SPAN>.
    * 
    */
   public static double getVariance (double alpha, double beta, double a,
                                     double b)  {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");

      return ((alpha * beta)*(b-a)*(b-a)) /
              ((alpha + beta) * (alpha + beta) * (alpha + beta + 1));
   }


   /**
    * Computes the standard deviation of the beta distribution with
    *    parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, over the interval <SPAN CLASS="MATH">[0, 1]</SPAN>.
    * 
    * @return the standard deviation of the Beta distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double beta)  {
      return Math.sqrt (BetaDist.getVariance (alpha, beta));
   }


   /**
    * Computes the standard deviation of the beta distribution with
    *    parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, over the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    * 
    * @return the standard deviation of the Beta distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double beta,
                                              double a, double b)  {
      return Math.sqrt (BetaDist.getVariance (alpha, beta, a, b));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>a</I></SPAN> of this object.
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>b</I></SPAN> of this object.
    * 
    */
   public double getB() {
      return b;
   }

    public void setParams (double alpha, double beta,
                           double a, double b, int d) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (a >= b)
         throw new IllegalArgumentException ("a >= b");
      this.alpha = alpha;
      this.beta = beta;
      this.decPrec = d;
      supportA = this.a = a;
      supportB = this.b = b;
      bminusa = b - a;
      double temp = Num.lnGamma (alpha);
      if (alpha == beta)
         temp *= 2.0;
      else
         temp += Num.lnGamma (beta);
      logBeta = temp - Num.lnGamma (alpha + beta);
      Beta = Math.exp(logBeta);
//      this.factor = 1.0 / (Beta * Math.pow (bminusa, alpha + beta - 1));
      this.logFactor = - logBeta - Math.log (bminusa) * (alpha + beta - 1);
    } 


   /**
    * Return a table containing parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha, beta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : alpha = " + alpha + ", beta = " + beta;
   }

}
