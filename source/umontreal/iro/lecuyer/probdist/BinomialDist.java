

/*
 * Class:        BinomialDist
 * Description:  binomial distribution
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
 * Extends the class {@link DiscreteDistributionInt} for the
 * <SPAN  CLASS="textit">binomial</SPAN> distribution with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>, where
 * <SPAN CLASS="MATH"><I>n</I></SPAN> is a positive integer and 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>p</I>&nbsp;&lt;=&nbsp;1</SPAN>.
 * Its mass function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:fmass-binomial"></A>
 * <I>p</I>(<I>x</I>) = nCr(<I>n</I>, <I>x</I>)<I>p</I><SUP>x</SUP>(1 - <I>p</I>)<SUP>n-x</SUP> = <I>n</I>!/[<I>x</I>!(<I>n</I> - <I>x</I>)!] &nbsp;<I>p</I><SUP>x</SUP>(1 - <I>p</I>)<SUP>n-x</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,&#8230;<I>n</I>,
 * </DIV><P></P>
 * and its distribution function is
 *   
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = &sum;<SUB>j=0</SUB><SUP>x</SUP>nCr(<I>n</I>, <I>j</I>) &nbsp;<I>p</I><SUP>j</SUP>(1 - <I>p</I>)<SUP>n-j</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,&#8230;<I>n</I>,
 * </DIV><P></P>
 * where nCr<SPAN CLASS="MATH">(<I>n</I>, <I>x</I>)</SPAN> is the number of possible combinations of <SPAN CLASS="MATH"><I>x</I></SPAN> elements
 * chosen among a set of <SPAN CLASS="MATH"><I>n</I></SPAN> elements.
 * 
 */
public class BinomialDist extends DiscreteDistributionInt {
   private int n;
   private double p;
   private double q;
   private static final double EPS2 = 100.0*EPSILON;

   private static class Function implements MathFunction {
      protected int m;
      protected int R;
      protected double mean;
      protected int f[];

      public Function (int m, double mean, int R, int f[]) {
         this.m = m;
         this.mean = mean;
         this.R = R;
         this.f = new int[f.length];
         System.arraycopy (f, 0, this.f, 0, f.length);
      }


      public double evaluate (double x) {
         if (x < R)
            return 1e100;

         double sum = 0.0;
         for (int j = 0; j < R; j++)
            sum += f[j] / (x - (double) j);

         return (sum + m * Math.log1p (-mean / x));
      }
   }


   public static double MAXN = 100000;


   /**
    * Creates an object that contains the binomial terms, for 
    * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>n</I></SPAN>, and the corresponding
    *    cumulative function.
    *    These values are computed and stored in dynamic arrays, unless
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> exceeds <TT>MAXN</TT>.
    * 
    */
   public BinomialDist (int n, double p) {
      setBinomial (n, p);
   }

   public double prob (int x) {
      if (n == 0)
         return 1.0;

      if (x < 0 || x > n)
         return 0.0;

      if (p == 0.0) {
         if (x > 0)
            return 0.0;
         else
            return 1.0;
      }

      if (q == 0.0) {
         if (x < n)
            return 0.0;
         else
            return 1.0;
      }

      if (pdf == null)
         return prob (n, p, q, x);

      if (x > xmax || x < xmin)
         return prob (n, p, q, x);

      return pdf[x - xmin];
   }


   public double cdf (int x) {
      if (n == 0)
         return 1.0;

      if (x < 0)
         return 0.0;

      if (x >= n)
         return 1.0;

      if (p == 0.0)
         return 1.0;

      if (p == 1.0)
         return 0.0;

      if (cdf != null) {
         if (x >= xmax)
            return 1.0;
         if (x < xmin) {
            final int RMAX = 20;
            int i;
            double term = prob(x);
            double Sum = term;
            final double z = (1.0 - p) / p;
            i = x;
            while (i > 0 && i >= x - RMAX) {
               term *= z * i / (n - i + 1);
               i--;
               Sum += term;
            }
            return Sum;
         }
         if (x <= xmed)
            return cdf[x - xmin];
         else
            // We keep the complementary distribution in the upper part of cdf
            return 1.0 - cdf[x + 1 - xmin];
      } else
         return cdf (n, p, x);
   }


   public double barF (int x) {
      if (n == 0)
         return 1.0;

      if (x < 1)
         return 1.0;

      if (x > n)
         return 0.0;

      if (p == 0.0)
         return 0.0;

      if (p == 1.0)
         return 1.0;

      if (cdf != null) {
         if (x > xmax) {
            // Add IMAX dominant terms to get a few decimals in the tail
            final double q = 1.0 - p;
            double z, sum, term;
            int i;
            sum = term = prob(x);
            z = p / q;
            i = x;
            final int IMAX = 20;
            while (i < n && i < x + IMAX) {
               term = term * z * (n - i) / (i + 1);
               sum += term;
               i++;
            }
            return sum;
            // return fdist_Beta (x, n - x + 1, 10, p);
         }

         if (x <= xmin)
            return 1.0;
         if (x > xmed)
            // We keep the complementary distribution in the upper part of cdf
            return cdf[x - xmin];
         else
            return 1.0 - cdf[x - 1 - xmin];
      } else
         return 1.0 - cdf (n, p, x - 1);
   }


   public int inverseFInt (double u) {
      if ((cdf == null) || (u <= EPS2))
         return inverseF (n, p, u);
      else
         return super.inverseFInt (u);
   }

   public double getMean() {
      return BinomialDist.getMean (n, p);
   }

   public double getVariance() {
      return BinomialDist.getVariance (n, p);
   }

   public double getStandardDeviation() {
      return BinomialDist.getStandardDeviation (n, p);
   }



   /**
    * Computes and returns the binomial probability <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN> in eq..
    * 
    */
   public static double prob (int n, double p, int x) {
      return prob (n, p, 1.0 - p, x);
   }


   /**
    * A generalization of the previous method.
    *  Computes and returns the binomial term
    * 
    * <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = (<I>n</I>!/<I>x</I>!(<I>n</I>-<I>x</I>)!)<I>p</I><SUP>x</SUP><I>q</I><SUP>n-x</SUP></SPAN>,
    * where <SPAN CLASS="MATH"><I>p</I></SPAN> and <SPAN CLASS="MATH"><I>q</I></SPAN> are arbitrary real numbers
    *  (<SPAN CLASS="MATH"><I>q</I></SPAN> is not necessarily equal to <SPAN CLASS="MATH">1 - <I>p</I></SPAN>).
    *  In the case where 
    * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>p</I>&nbsp;&lt;=&nbsp;1</SPAN> and <SPAN CLASS="MATH"><I>q</I> = 1 - <I>p</I></SPAN>, the returned
    *  value is a probability term for the binomial distribution.
    * 
    */
   public static double prob (int n, double p, double q, int x) {
      final int SLIM = 50;          // To avoid overflow
      final double MAXEXP = (Num.DBL_MAX_EXP - 1)*Num.LN2;// To avoid overflow
      final double MINEXP = (Num.DBL_MIN_EXP - 1)*Num.LN2;// To avoid underflow
      int signe = 1;
      double Res;

      if (n < 0)
        throw new IllegalArgumentException ("n < 0");
      if (n == 0)
         return 1.0;
      if (x < 0 || x > n)
         return 0.0;

      // Combination (n, x) are symmetric between x and n-x
      if (x > n/2) {
         x = n - x;
         Res = p;
         p = q;
         q = Res;
      }

      if (p < 0.0) {
         p = -p;
         if ((x & 1) != 0)
            signe *= -1;             // odd x
      }
      if (q < 0.0) {
         q = -q;
         if (((n - x) & 1) != 0)
            signe *= -1;             // odd n - x
      }

      if (n <= SLIM) {
         Res = Math.pow (p, (double)x)*Num.combination (n, x)*Math.pow (q,
            (double)(n - x));
         return signe*Res;
      }
      else {
         /* This could be calculated with more precision as there is some
            cancellation because of subtraction of the large LnFactorial: the
            last few digits can be lost. But we need the function lgammal in
            long double precision. Another possibility would be to use an
            asymptotic expansion for the binomial coefficient. */

         Res = x*Math.log (p) + (n - x)*Math.log (q) + Num.lnFactorial (n)
            - Num.lnFactorial (n - x) - Num.lnFactorial (x);
         if (Res >= MAXEXP)
           throw new IllegalArgumentException ("term overflow");

         if (Res < MINEXP)
            return 0.0;

         return signe*Math.exp (Res);
      }
   }


   /**
    * Computes <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>, the distribution function of a
    *   binomial
    *   random variable with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double cdf (int n, double p, int x) {
      final int NLIM1 = 10000;
      final double VARLIM = 50.0;
//      final double EPS = DiscreteDistributionInt.EPSILON * EPS_EXTRA;
      double y, z, q = 1.0 - p;
      double sum, term, termmid;
      int i, mid;
      boolean flag = false;

      if (p < 0.0 | p > 1.0)
        throw new IllegalArgumentException ("p not in [0,1]");
      if (n < 0)
        throw new IllegalArgumentException ("n < 0");

      if (n == 0)
         return 1.0;
      if (x < 0)
         return 0.0;
      if (x >= n)
         return 1.0;
      if (p <= 0.0)
         return 1.0;
      if (p >= 1.0)
         return 0.0;                 // For any x < n

      if (n < NLIM1) {               // Exact Binomial
         /* Sum RMAX terms to get a few decimals in the lower tail */
         final int RMAX = 20;
         mid = (int)((n + 1)*p);
         if (mid > x)
            mid = x;
         sum = term = termmid = prob (n, p, 1.0 - p, mid);

         z = q/p;
            i = mid;
            while (term >= EPSILON || i >= mid - RMAX) {
            term *= z * i / (n - i + 1);
            sum += term;
            i--;
            if (i == 0) break;
         }

         z = p/q;
         term = termmid;
         for (i = mid; i < x; i++) {
            term *= z*(n - i)/(i + 1);
            if (term < EPSILON)
               break;
            sum += term;
         }
         if (sum >= 1.0) return 1.0;
         return sum;

      } else {
         if (p > 0.5 || ((p == 0.5) && (x > n/2))) {
            // use F (p, n, x) = 1 - F (q, n, n-x-1)
            p = q;
            q = 1.0 - p;
            flag = true;
            x = n - x - 1;
         }
         if (n*p*q > VARLIM) {   // Normal approximation
            /* Uses the Camp-Paulson approximation based on the F-distribution.
               Its maximum absolute error is smaller than 0.007 / sqrt (npq).
               Ref: W. Molenaar; Approximations to the Poisson, Binomial,....
               QA273.6 M64, p. 93 (1970) */
            term = Math.pow ((x+1)*q/((n-x)*p), 1.0/3.0);
            y = term*(9.0 - 1.0/(x + 1)) - 9.0 + 1.0/(n - x);
            z = 3.0*Math.sqrt (term*term/(x + 1) + 1.0/(n - x));
            y /= z;
            if (flag)
               return NormalDist.barF01 (y);
            else
               return NormalDist.cdf01 (y);
         }
         else {                    // Poisson approximation
            /* Uses a Bol'shev approximation based on the Poisson distribution.
               Error is O (1/n^4) as n -> infinity. Ref: W. Molenaar;
             Approximations to the Poisson, Binomial,... QA273.6 M64, p. 107,
               Table 6.2, Formule lambda_9 (1970). */
            y = (2.0*n - x)*p/(2.0 - p);
            double t = 2.0*n - x;
            z = (2.0*y*y - x*y - x*(double)x - 2.0*x)/(6*t*t);
            z = y/(1.0 - z);
            if (flag)
               return PoissonDist.barF(z, x - 1);
            else
               return PoissonDist.cdf (z, x);
         }
      }
   }


   /**
    * Returns 
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>]</SPAN>, the complementary
    *    distribution function.
    * 
    */
   public static double barF (int n, double p, int x) {
      return 1.0 - cdf (n, p, x - 1);
   }


   /**
    * Computes 
    * <SPAN CLASS="MATH"><I>x</I> = <I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN>, the inverse of the binomial distribution.
    * 
    */
   public static int inverseF (int n, double p, double u)  {
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= prob (n, p, 0))
         return 0;
      if ((u > 1.0 - prob (n, p, n)) || (u >= 1.0))
         return n;
      final int NLIM1 = 10000;
      int i, mid;
      final double q = 1.0 - p;
      double z, sum, term, termmid;

      if (n < NLIM1) {               // Exact Binomial
         i = (int)((n + 1)*p);
         if (i > n)
            i = n;
         term = prob (n, p, i);
         while ((term >= u) && (term > Double.MIN_NORMAL)) {
            i /= 2;
            term = prob (n, p, i);
         }

         z = q/p;
         if (term <= Double.MIN_NORMAL) {
            i *= 2;
            term = prob (n, p, i);
            while (term >= u && (term > Double.MIN_NORMAL)) {
               term *= z*i/(n - i + 1);
               i--;
            }
         }

         mid = i;
         term = prob (n, p, i);
         sum = termmid = term;

         z = q/p;
         for (i = mid; i > 0; i--) {
            term *= z*i/(n - i + 1);
            if (term < EPSILON)
               break;
            sum += term;
         }

         i = mid ;
         term = termmid;
         if (sum >= u) {
             while (sum >= u){
                 z = q/p;
                 sum -= term;
                 term *= z*i/(n - i + 1);
                 i--;
             }
             return i+1;
         }

        double prev = -1;
        while ((sum < u) && (sum > prev)) {
            z = p/q;
            term *=  z*(n - i)/(i + 1);
            prev = sum;
            sum += term;
            i++;
         }
         return i;

      } else{  // Normal or Poisson  approximation
          for(i = 0 ; i <= n ; i++){
              sum = cdf(n,p,i);
              if (sum >= u)
                  break;
          }
          return i;
      }
    }


   /**
    * Estimates the parameters <SPAN CLASS="MATH">(<I>n</I>, <I>p</I>)</SPAN> of the binomial distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>m</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>n</I></SPAN>, <SPAN CLASS="MATH"><I>p</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param m the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(n)</SPAN>, <SPAN CLASS="MATH">hat(p)</SPAN>]
    * 
    */
   public static double[] getMLE (int[] x, int m) {
      if (m <= 1)
         throw new UnsupportedOperationException(" m < 2");

      int i;
      int r = 0;
      double mean = 0.0;
      for (i = 0; i < m; i++) {
         mean += x[i];
         if (x[i] > r)
            r = x[i];
      }
      mean /= (double) m;

      double sum = 0.0;
      for (i = 0; i < m; i++)
         sum += (x[i] - mean)*(x[i] - mean);
      double var = sum / m;
      if (mean <= var)
         throw new UnsupportedOperationException("mean <= variance");

      int f[] = new int[r];
      for (int j = 0; j < r; j++) {
         f[j] = 0;
         for (i = 0; i < m; i++)
            if (x[i] > j) f[j]++;
      }

      double p = 1.0 - var/mean;
      double rup = (int) (5*mean/p);
      if (rup < 1) rup = 1;

      Function fct = new Function (m, mean, r, f);
      double parameters[] = new double[2];
      parameters[0] = (int) RootFinder.brentDekker (r-1, rup, fct, 1e-5);
      if (parameters[0] < r)
         parameters[0] = r;
      parameters[1] = mean / parameters[0];

      return parameters;
   }


   /**
    * Creates a new instance of a binomial distribution with both parameters
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> estimated using the maximum likelihood method, from
    *    the <SPAN CLASS="MATH"><I>m</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>,  
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to estimate the parameters
    * 
    *    @param m the number of observations to use to estimate the parameters
    * 
    * 
    */
   public static BinomialDist getInstanceFromMLE (int[] x, int m) {
      double parameters[] = new double[2];
      parameters = getMLE (x, m);
      return new BinomialDist ((int) parameters[0], parameters[1]);
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of the binomial distribution with
    *    given (fixed) parameter <SPAN CLASS="MATH"><I>n</I></SPAN>, by the maximum likelihood method,
    *    from the <SPAN CLASS="MATH"><I>m</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>.
    *    Returns the estimator in an array with a single element.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param m the number of observations used to evaluate parameters
    * 
    *    @param n the number of success
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH">hat(p)</SPAN>]
    * 
    */
   public static double[] getMLE (int[] x, int m, int n) {
      if (m <= 0)
         throw new IllegalArgumentException ("m <= 0");
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[] = new double[1];
      double mean = 0.0;
      for (int i = 0; i < m; i++)
         mean += x[i];
      mean /= (double) m;

      parameters[0] = mean / (double) n;
      return parameters;
   }


   /**
    * Creates a new instance of a binomial distribution with given (fixed) parameter <SPAN CLASS="MATH"><I>n</I></SPAN>, and
    *    with parameter <SPAN CLASS="MATH"><I>p</I></SPAN> estimated by the maximum likelihood method based on the
    *    <SPAN CLASS="MATH"><I>m</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param m the number of observations to use to evaluate parameters
    * 
    *    @param n the parameter n of the binomial
    * 
    * 
    */
   public static BinomialDist getInstanceFromMLE (int[] x, int m, int n) {
      double parameters[] = new double[1];
      parameters = getMLE (x, m, n);
      return new BinomialDist (n, parameters[0]);
   }


   /**
    * Computes the mean <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>np</I></SPAN> of the binomial distribution with
    *    parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the mean of the Binomial distribution <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>np</I></SPAN>
    * 
    */
   public static double getMean (int n, double p) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range (0, 1)");

      return (n * p);
   }


   /**
    * Computes the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>np</I>(1 - <I>p</I>)</SPAN> of the binomial
    *    distribution with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the variance of the binomial distribution 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>np</I>(1 - <I>p</I>)</SPAN>
    * 
    */
   public static double getVariance (int n, double p) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range (0, 1)");

      return (n * p * (1 - p));
   }


   /**
    * Computes the standard deviation of the Binomial distribution with
    *    parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the standard deviation of the binomial distribution
    * 
    */
   public static double getStandardDeviation (int n, double p) {
      return Math.sqrt (BinomialDist.getVariance (n, p));
   }

 
   private void setBinomial (int n, double p) {
     /*
      * Compute all probability terms of the binomial distribution; start near
      * the mean, and calculate probabilities on each side until they become
      * smaller than EPSILON, then stop there.
      * However, this is more general than the binomial probability distribu-
      * tion as this will compute the binomial terms when p + q != 1, and
      * even when p or q are negative. However in this case, the cumulative
      * terms will be meaningless.
      */

      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range (0, 1)");
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      supportA = 0;
      supportB = n;
      double q = 1.0 - p;
      final double EPS = DiscreteDistributionInt.EPSILON * EPS_EXTRA;

      this.n = n;
      this.p = p;
      this.q = q;

      int i, mid;
      int imin, imax;
      double z = 0.0;
      double[] P;     // Binomial "probability" terms
      double[] F;     // Binomial cumulative "probabilities"

      // For n > MAXN, we shall not use pre-computed arrays
      if (n > MAXN) {
         pdf = null;
         cdf = null;
         return;
      }

      P = new double[1 + n];
      F = new double[1 + n];

      // the maximum term in absolute value
      mid = (int)((n + 1)*Math.abs (p)/(Math.abs (p) + Math.abs (q)));
      if (mid > n)
         mid = n;
      P[mid] = prob (n, p, q, mid);

      if (p != 0.0 || p != -0.0)
         z = q/p;
      i = mid;

      while (i > 0 && Math.abs (P[i]) > EPS) {
         P[i - 1] = P[i]*z*i/(n - i + 1);
         i--;
      }
      imin = i;

      if (q != 0.0 || q != -0.0)
         z = p/q;
      i = mid;

      while (i < n && Math.abs (P[i]) > EPS) {
         P[i + 1] = P[i]*z*(n - i)/(i + 1);
         i++;
      }
      imax = i;

   /* Here, we assume that we are dealing with a probability distribution.
      Compute the cumulative probabilities for F and keep them in the
      lower part of CDF.*/
      F[imin] = P[imin];
      i = imin;
      while (i < n && F[i] < 0.5) {
         i++;
         F[i] = F[i - 1] + P[i];
      }

      // This is the boundary between F (i <= xmed) and 1 - F (i > xmed) in
      // the array CDF
      xmed = i;

      // Compute the cumulative probabilities of the complementary
      // distribution and keep them in the upper part of the array
      F[imax] = P[imax];
      i = imax - 1;
      while (i > xmed) {
         F[i] = P[i] + F[i + 1];
         i--;
      }

       // Reset imin because we lose too much precision for a few terms near
       //   imin when we stop adding terms < epsilon.
       i = imin;
       while (i < xmed && F[i] < DiscreteDistributionInt.EPSILON)
          i++;
       xmin = imin = i;

       /* Same thing with imax */
       i = imax;
       while (i > xmed && F[i] < DiscreteDistributionInt.EPSILON)
          i--;
       xmax = imax = i;

       pdf  = new double[imax + 1 - imin];
       cdf  = new double[imax + 1 - imin];
       System.arraycopy (P, imin, pdf, 0, imax+1-imin);
       System.arraycopy (F, imin, cdf, 0, imax+1-imin);

   }
   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public int getN() {
      return n;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   public double getP() {
      return p;
   }


   /**
    * Returns a table that contains the parameters <SPAN CLASS="MATH">(<I>n</I>, <I>p</I>)</SPAN> of the current distribution,
    *    in regular order: [<SPAN CLASS="MATH"><I>n</I></SPAN>, <SPAN CLASS="MATH"><I>p</I></SPAN>].
    * 
    */
   public double[] getParams () {
      double[] retour = {n, p};
      return retour;
   }


   /**
    * Resets the parameters to these new values and recomputes everything
    *    as in the constructor.  From the performance viewpoint, it is
    *    essentially the same as constructing a new {@link BinomialDist} object.
    * 
    * 
    */
   public void setParams (int n, double p) {
      setBinomial (n, p);
   }


   public String toString () {
      return getClass().getSimpleName() + " : n = " + n + ", p = " + p;
   }

}
