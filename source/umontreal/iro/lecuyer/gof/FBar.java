

/*
 * Class:        FBar
 * Description:  Complementary empirical distributions
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

import umontreal.iro.lecuyer.probdist.*;


/**
 * This class is similar to {@link FDist}, except that it provides static methods
 * to compute or approximate the complementary distribution function of <SPAN CLASS="MATH"><I>X</I></SPAN>,
 * which we define as 
 * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>]</SPAN>, instead of 
 * <SPAN CLASS="MATH"><I>F</I>(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&lt;=&nbsp;<I>x</I>]</SPAN>.
 * Note that with our definition of <SPAN CLASS="MATH">bar(F)</SPAN>, one has
 * 
 * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = 1 - <I>F</I>(<I>x</I>)</SPAN> for continuous distributions and
 * 
 * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = 1 - <I>F</I>(<I>x</I> - 1)</SPAN> for discrete distributions over the integers.
 * 
 */
public class FBar {
   private FBar() {}

   private static final double EPSILONSCAN = 1.0E-7;

   private static double scanGlaz (int n, double d, int m) {
      int j, jmoy;
      double temp;
      double jr, jm1r, nr = n;
      int signe;
      double q = 1.0 - d;
      double q4, q3, q2, q1;
      double bin, binMoy;

      jmoy = (int)((n + 1)*d);              // max term of the Binomial
      if (jmoy < m - 1)
         jmoy = m - 1;

      /*---------------------------------------------------------
       * Compute q1: formula (2.5) in Glaz (1989)
       * Compute q2: formula (A.6) in Berman and Eagleson (1985)
       * Compute q3, q4 : Theorem (3.2) in Glaz (1989)
       *---------------------------------------------------------*/

      // compute the probability of term j = jmoy
      q1 = 0.0;
      for (j = 1; j <= jmoy; j++) {
         jr = j;
         q1 += Math.log (nr - jr + 1.0) - Math.log (jr);
      }
      q1 += jmoy*Math.log (d) + (nr - jmoy)*Math.log (q);
      binMoy = Math.exp (q1);
      q1 = binMoy;
      jm1r = jmoy - m + 1;
      if (((jmoy - m + 1) & 1) != 0)
         signe = -1;
      else
         signe = 1;
      q2 = signe*binMoy;
      q3 = signe*binMoy*(2.0 - jm1r*jm1r + jm1r);
      q4 = signe*binMoy*(jm1r + 1.0)*(jm1r + 2.0)*(6.0 + jm1r*jm1r -
         5.0*jm1r);

      // compute the probability of terms j > jmoy
      if (((jmoy - m + 1) & 1) != 0)
         signe = -1;
      else
         signe = 1;

      jm1r = jmoy - m + 1;
      bin = binMoy;
      for (j = jmoy + 1; j <= n; j++) {
         jr = j;
         jm1r += 1.0;
         signe = -signe;
         bin = (bin*(nr - jr + 1.0)*d)/(jr*q);
         if (bin < EPSILONSCAN)
            break;
         q1 += bin;
         q2 += signe*bin;
         q3 += signe*bin*(2.0 - jm1r*jm1r + jm1r);
         q4 += signe*bin*(jm1r + 1.0)*(jm1r + 2.0)*(6.0 + jm1r*jm1r -
            5.0*jm1r);
      }

      q1 = 1.0 - q1;
      q3 /= 2.0;
      q4 /= 12.0;
      if (m == 3) {
        // Problem with this formula; I do not get the same results as Glaz
         q4 = ((nr*(nr - 1.0)*d*d*Math.pow (q, nr - 2.0))/8.0
            + nr*d*2.0*Math.pow (1.0 - 2.0*d, nr - 1.0))
            - 4.0*Math.pow (1.0 - 2.0*d, nr);
         if (d < 1.0/3.0) {
            q4 += nr*d*2.0*Math.pow (1.0 - 3.0*d, nr - 1.0)
                  + 4.0*Math.pow (1.0 - 3.0*d, nr);
         }
      }
      // compute probability: Glaz, equations (3.2) and (3.3)
      q3 = q1 - q2 - q3;
      q4 = q3 - q4;
      //when the approximation is bad, avoid overflow
      temp = Math.log (q3) + (nr - m - 2.0)*Math.log (q4/q3);
      if (temp >= 0.0)
         return 0.0;
      if (temp < -30.0)
         return 1.0;
      q4 = Math.exp (temp);
      return 1.0 - q4;
     }

     //-----------------------------------------------------------------

     private static double scanWNeff (int n, double d, int m) {
      double q = 1.0 - d;
      double temp;
      double bin;
      double sum;
      int j;

      /*--------------------------------------
       * Anderson-Titterington: equation (4)
       *--------------------------------------*/

      // compute the probability of term j = m
      sum = 0.0;
      for (j = 1; j <= m; j++)
         sum += Math.log ((double)(n - j + 1)) - Math.log ((double)j);

      sum += m*Math.log (d) + (n - m)*Math.log (q);
      bin = Math.exp (sum);
      temp = (m/d - n - 1.0)*bin;
      sum = bin;

      // compute the probability of terms j > m
      for (j = m + 1; j <= n; j++) {
         bin *= (n - j + 1)*d/(j*q);
         if (bin < EPSILONSCAN)
            break;
         sum += bin;
      }
      sum = 2.0*sum + temp;
      return sum;
     }

     //----------------------------------------------------------------

     private static double scanAsympt (int n, double d, int m) {
      double kappa;
      double temp;
      double theta;
      double sum;

      /*--------------------------------------------------------------
       * Anderson-Titterington: asymptotic formula after equation (4)
       *--------------------------------------------------------------*/

      theta = Math.sqrt (d/(1.0 - d));
      temp = Math.sqrt ((double)n);
      kappa = m/(d*temp) - temp;
      temp = theta*kappa;
      temp = temp*temp/2.0;
      sum = 2.0*(1.0 - NormalDist.cdf01 (theta*kappa)) +
         (kappa*theta*Math.exp (-temp))/(d*Math.sqrt (2.0*Math.PI));
      return sum;
   }

   /**
    * Return 
    * <SPAN CLASS="MATH"><I>P</I>[<I>S</I><SUB>N</SUB>(<I>d</I> )&nbsp;&gt;=&nbsp;<I>m</I>]</SPAN>, where <SPAN CLASS="MATH"><I>S</I><SUB>N</SUB>(<I>d</I> )</SPAN> is the scan
    *  statistic.  It is defined as
    *   
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>S</I><SUB>N</SUB>(<I>d</I> )= <I>sup</I><SUB>0&nbsp;&lt;=&nbsp;y&nbsp;&lt;=&nbsp;1-d</SUB><I>&#951;</I>[<I>y</I>,&nbsp;<I>y</I> + <I>d</I>],
    * </DIV><P></P>
    * where <SPAN CLASS="MATH"><I>d</I></SPAN> is a constant in <SPAN CLASS="MATH">(0, 1)</SPAN>,
    *   
    * <SPAN CLASS="MATH"><I>&#951;</I>[<I>y</I>,&nbsp;<I>y</I> + <I>d</I>]</SPAN> is the number of observations falling inside
    *   the interval <SPAN CLASS="MATH">[<I>y</I>, <I>y</I> + <I>d</I>]</SPAN>, from a sample of <SPAN CLASS="MATH"><I>N</I></SPAN> i.i.d. <SPAN CLASS="MATH"><I>U</I>(0, 1)</SPAN>
    *   random variables.
    * The approximation returned by this function is generally good when
    *   it is close to 0, but is not very reliable when it exceeds, say, 0.4.
    *   
    * Restrictions: <SPAN CLASS="MATH"><I>N</I>&nbsp;&gt;=&nbsp;2</SPAN> and <SPAN CLASS="MATH"><I>d</I>&nbsp;&lt;=&nbsp;1/2</SPAN>.
    * <BR>
    * @param n sample size (<SPAN CLASS="MATH">&nbsp;&gt;=&nbsp;2</SPAN>)
    * 
    *    @param d length of the test interval (<SPAN CLASS="MATH">&#8712;(0, 1)</SPAN>)
    * 
    *    @param m scan statistic
    * 
    *    @return the complementary distribution function of the statistic evaluated at <TT>m</TT>
    */
   public static double scan (int n, double d, int m) {
      double mu;
      double prob;

      if (n < 2)
        throw new IllegalArgumentException ("Calling scan with n < 2");
      if (d <= 0.0 || d >= 1.0)
         throw new IllegalArgumentException ("Calling scan with "+
                    "d outside (0,1)");

      if (m > n)
         return 0.0;
      if (m <= 1)
         return 1.0;
      if (m <= 2) {
         if ((n - 1)*d >= 1.0)
            return 1.0;
         return 1.0 - Math.pow (1.0 - (n - 1)*d, (double)n);
      }
      if (d >= 0.5 && m <= (n + 1)/2.0)
         return 1.0;
      if (d > 0.5)
        return -1.0;              // Error
      // util_Assert (d <= 0.5, "Calling fbar_Scan with d > 1/2");

      mu = n*d;                    // mean of a binomial
      if (m <= mu + d)
         return 1.0;
      if (mu <= 10.0)
         return scanGlaz (n, d, m);
      prob = scanAsympt (n, d, m);
      if ((d >= 0.3 && n >= 50.0) || (n*d*d >= 250.0 && d < 0.3)) {
         if (prob <= 0.4)
            return prob;
      }
      prob = scanWNeff (n, d, m);
      if (prob <= 0.4)
         return prob;
      prob = scanGlaz (n, d, m);
      if (prob > 0.4 && prob <= 1.0)
         return prob;
      return 1.0;
   }
}
