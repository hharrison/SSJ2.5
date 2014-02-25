

/*
 * Class:        ChiSquareDistQuick
 * Description:  chi-square distribution
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


/**
 * Provides a variant of {@link ChiSquareDist} with
 * faster but less accurate methods.
 * The non-static version of 
 * <TT>inverseF</TT> calls the static version.
 * This method is not very accurate for small <SPAN CLASS="MATH"><I>n</I></SPAN> but becomes
 * better as <SPAN CLASS="MATH"><I>n</I></SPAN> increases.
 * The other methods are the same as in {@link ChiSquareDist}.
 * 
 */
public class ChiSquareDistQuick extends ChiSquareDist {



   /**
    * Constructs a chi-square distribution with <TT>n</TT> degrees of freedom.
    * 
    */
   public ChiSquareDistQuick (int n) {
      super (n);
   }


   public double inverseF (double u) {
      return inverseF (n, u);
   }

   /**
    * Computes a quick-and-dirty approximation of <SPAN CLASS="MATH"><I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN>, 
    *   where <SPAN CLASS="MATH"><I>F</I></SPAN> is the <EM>chi-square</EM> distribution with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom.
    *   Uses the approximation given in  Figure L.24 of  
    *   Bratley, Fox and Schrage (1987) over most of the range.
    *   For <SPAN CLASS="MATH"><I>u</I> &lt; 0.02</SPAN> or <SPAN CLASS="MATH"><I>u</I> &gt; 0.98</SPAN>, it uses the approximation given in 
    *    Goldstein for <SPAN CLASS="MATH"><I>n</I>&nbsp;&gt;=&nbsp;10</SPAN>, and returns 
    *   <TT>2.0 *</TT>
    *   {@link GammaDist#inverseF(double,int,double) GammaDist.inverseF}&nbsp;<TT>(n/2, 6, u)</TT>
    *    for <SPAN CLASS="MATH"><I>n</I> &lt; 10</SPAN> in order to avoid
    *   the loss of precision of the above approximations. 
    *    When <SPAN CLASS="MATH"><I>n</I>&nbsp;&gt;=&nbsp;10</SPAN> or 
    * <SPAN CLASS="MATH">0.02 &lt; <I>u</I> &lt; 0.98</SPAN>,
    *   it is between 20 to 30 times faster than the same method in
    *   {@link ChiSquareDist} for <SPAN CLASS="MATH"><I>n</I></SPAN> between <SPAN CLASS="MATH">10</SPAN> and <SPAN CLASS="MATH">1000</SPAN> and even faster
    *   for larger <SPAN CLASS="MATH"><I>n</I></SPAN>. 
    * 
    * <P>
    * Note that the number <SPAN CLASS="MATH"><I>d</I></SPAN> of decimal digits of precision
    *   generally increases with <SPAN CLASS="MATH"><I>n</I></SPAN>. For <SPAN CLASS="MATH"><I>n</I> = 3</SPAN>, we only have <SPAN CLASS="MATH"><I>d</I> = 3</SPAN> over
    *   most of the range. For <SPAN CLASS="MATH"><I>n</I> = 10</SPAN>, <SPAN CLASS="MATH"><I>d</I> = 5</SPAN> except far in the tails where <SPAN CLASS="MATH"><I>d</I> = 3</SPAN>.
    *   For <SPAN CLASS="MATH"><I>n</I> = 100</SPAN>, one has more than <SPAN CLASS="MATH"><I>d</I> = 7</SPAN> over most of the range and for
    *   <SPAN CLASS="MATH"><I>n</I> = 1000</SPAN>, at least <SPAN CLASS="MATH"><I>d</I> = 8</SPAN>.
    *   The cases <SPAN CLASS="MATH"><I>n</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>n</I> = 2</SPAN> are exceptions, with precision of about <SPAN CLASS="MATH"><I>d</I> = 10</SPAN>.
    * 
    */
   public static double inverseF (int n, double u) {
      /*
       * Returns an approximation of the inverse of Chi square cdf
       * with n degrees of freedom.
       * As in Figure L.24 of P.Bratley, B.L.Fox, and L.E.Schrage.
       *         A Guide to Simulation Springer-Verlag,
       *         New York, second edition, 1987.
       */

      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u is not in [0,1]");
      if (u <= 0.0)
         return 0.0;
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;

      final double SQP5 = 0.70710678118654752440;
      final double DWARF = 0.1e-15;
      final double ULOW = 0.02;
      double Z, arg, v, ch, sqdf;

      if (n == 1) {
          Z = NormalDist.inverseF01 ((1.0 + u)/2.0);
          return Z*Z;

      } else if (n == 2) {
         arg = 1.0 - u;
         if (arg < DWARF)
            arg = DWARF;
         return -Math.log (arg)*2.0;

     } else if ((u > ULOW) && (u < 1.0 - ULOW)) {
        Z = NormalDist.inverseF01 (u);
        sqdf = Math.sqrt ((double)n);
        v = Z * Z;

        ch = -(((3753.0 * v + 4353.0) * v - 289517.0) * v -
           289717.0) * Z * SQP5 / 9185400;

        ch = ch / sqdf + (((12.0 * v - 243.0) * v - 923.0)
           * v + 1472.0) / 25515.0;

        ch = ch / sqdf + ((9.0 * v + 256.0) * v - 433.0)
           * Z * SQP5 / 4860;

        ch = ch / sqdf - ((6.0 * v + 14.0) * v - 32.0) / 405.0;
        ch = ch / sqdf + (v - 7.0) * Z * SQP5 / 9;
        ch = ch / sqdf + 2.0 * (v - 1.0) / 3.0;
        ch = ch / sqdf + Z / SQP5;
        return n * (ch / sqdf + 1.0);

     } else if (n >= 10) {
        Z = NormalDist.inverseF01 (u);
        v = Z * Z;
        double temp;
        temp = 1.0 / 3.0 + (-v + 3.0) / (162.0 * n) -
           (3.0 * v * v + 40.0 * v + 45.0) / (5832.0 * n * n) +
           (301.0 * v * v * v - 1519.0 * v * v - 32769.0 * v -
           79349.0) / (7873200.0 * n * n * n);
        temp *= Z * Math.sqrt (2.0 / n);

        ch = 1.0 - 2.0 / (9.0 * n) + (4.0 * v * v + 16.0 * v -
           28.0) / (1215.0 * n * n) + (8.0 * v * v * v + 720.0 * v * v +
           3216.0 * v + 2904.0) / (229635.0 * n * n * n) + temp;

        return n * ch * ch * ch;

    } else {
    // Note: this implementation is quite slow.
    // Since it is restricted to the tails, we could perhaps replace
    // this with some other approximation (series expansion ?)
        return 2.0*GammaDist.inverseF (n/2.0, 6, u);
    }
}

}
