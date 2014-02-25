

/*
 * Class:        FDist
 * Description:  Empirical distributions
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

package umontreal.iro.lecuyer.gof;


/**
 * This class provides methods to compute (or approximate)
 * the distribution functions of special types of
 * goodness-of-fit test statistics.
 * 
 */
public class FDist {
   private FDist() {}


   /**
    * Similar to
    * {@link umontreal.iro.lecuyer.probdist.KolmogorovSmirnovPlusDist KolmogorovSmirnovPlusDist}
    *  but for the case where the distribution function <SPAN CLASS="MATH"><I>F</I></SPAN> has a jump of size
    *  <SPAN CLASS="MATH"><I>a</I></SPAN> at a given point <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB></SPAN>, is zero at the left of <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB></SPAN>,
    *   and is continuous at the right of <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB></SPAN>.
    * Restriction: <SPAN CLASS="MATH">0 &lt; <I>a</I> &lt; 1</SPAN>.
    *   
    * @param N sample size
    * 
    *    @param a size of the jump
    * 
    *    @param x positive or negative Kolmogorov-Smirnov statistic
    * 
    *    @return the distribution function of the statistic evaluated at <TT>x</TT>
    * 
    */
   public static double kolmogorovSmirnovPlusJumpOne (int N, double a,
                                                      double x) {
      final double EPSILONLR = 1.E-15;
      final double EPSILON = 1.0E-290;
      final double NXAPARAM = 6.5;   // frontier: alternating series
      double LogCom;
      double q, p1, q1;
      double Sum = 0.0;
      double term;
      double Njreal;
      double jreal;
      int Sign;
      int j;
      int jmax;

      if (N < 1)
        throw new IllegalArgumentException (
                             "Calling kolmogorovSmirnovPlusJumpOne "+
                             "with N < 1");
      if (a >= 1.0 || a <= 0.0)
        throw new IllegalArgumentException (
                             "Calling kolmogorovSmirnovPlusJumpOne "+
                             "with a outside (0, 1)");
      if (x <= 0.0)
         return 0.0;
      if (x + a >= 1.0)
         return 1.0;
      LogCom = Math.log ((double)N);

      //--------------------------------------------------------------------
      // the alternating series is stable and fast for N*(x + a) very small
      //--------------------------------------------------------------------
      if (N*(x + a) < NXAPARAM && a + x < 0.5) {
         jmax = (int)(N*(x + a));
         for (j = 1; j <= jmax; j++) {
            jreal = j;
            Njreal = N - j;
            q = jreal/N - x;
            if ((q < 0.0 && ((j & 1) != 0)) ||
               ((q > 1.0) && (((N - j - 1) & 1) != 0)))
               Sign = -1;
            else
               Sign = 1;

            // we must avoid log (0.0)
            q1 = Math.abs (q);
            p1 = Math.abs (1.0 - q);
            if (q1 > EPSILON && p1 > EPSILON) {
               term = LogCom + jreal*Math.log (q1) +
                     (Njreal - 1.0)*Math.log (p1);
               Sum += Sign*Math.exp (term);
            }
            LogCom += Math.log (Njreal/(jreal + 1.0));
         }
         // add the term j = 0
         Sum += Math.exp ((N - 1)*Math.log (1.0 + x));
         return Sum*x;
      }

      //---------------------------------------------
      // For N (x + a) >= NxaParam or (a + x) > 0.5,
      // use the non-alternating series.
      //---------------------------------------------

      // EpsilonLR because the distribution has a jump
      jmax = (int)(N*(1.0 - a - x - EPSILONLR));
      for (j = 1; j <= jmax; j++) {
         jreal = j;
         Njreal = N - jreal;
         q = jreal/N + x;
         if (1.0 - q > EPSILON) {
            term = LogCom + (jreal - 1.0)*Math.log (q) + Njreal*Math.log (1.0 - q);
            Sum += Math.exp (term);
         }
         LogCom += Math.log (Njreal/(jreal + 1.0));
      }
      Sum *= x;

      // add the term j = 0
      if (1.0 - x > EPSILON)
         Sum += Math.exp (N*Math.log (1.0 - x));
      return 1.0 - Sum;
   }


   /**
    * Returns <SPAN CLASS="MATH"><I>F</I>(<I>m</I>)</SPAN>, the distribution function of the scan statistic
    *   with parameters <SPAN CLASS="MATH"><I>N</I></SPAN> and <SPAN CLASS="MATH"><I>d</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>m</I></SPAN>.
    *   For a description of this statistic and its distribution, see
    *   {@link FBar#scan(int,double,int) scan},
    *   which computes its complementary distribution
    *   
    * <SPAN CLASS="MATH">bar(F)(<I>m</I>) = 1 - <I>F</I>(<I>m</I> - 1)</SPAN>.
    *  
    * @param N sample size (<SPAN CLASS="MATH">&nbsp;&gt;=&nbsp;2</SPAN>)
    * 
    *    @param d length of the test interval (<SPAN CLASS="MATH">&#8712;(0, 1)</SPAN>)
    * 
    *    @param m scan statistic
    * 
    *    @return the distribution function of the statistic evaluated at <TT>m</TT>
    */
   public static double scan (int N, double d, int m) {
      return 1.0 - FBar.scan (N, d, m);
   }
}
