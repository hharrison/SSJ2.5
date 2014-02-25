

/*
 * Class:        DiscreteDistribution
 * Description:  discrete distributions over a set of real numbers
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

import java.util.Formatter;
import java.util.Locale;


/**
 * This class implements discrete distributions over a <SPAN  CLASS="textit">finite set of real numbers</SPAN>
 * (also over <SPAN  CLASS="textit">integers</SPAN> as a particular case).
 * We assume that the random variable <SPAN CLASS="MATH"><I>X</I></SPAN> of interest can take one of the
 * <SPAN CLASS="MATH"><I>n</I></SPAN> values 
 * <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB> &lt; <SUP> ... </SUP> &lt; <I>x</I><SUB>n-1</SUB></SPAN>, which  <SPAN  CLASS="textit">must be sorted</SPAN> by
 * increasing order.
 * <SPAN CLASS="MATH"><I>X</I></SPAN> can take the value <SPAN CLASS="MATH"><I>x</I><SUB>k</SUB></SPAN> with probability 
 * <SPAN CLASS="MATH"><I>p</I><SUB>k</SUB> = <I>P</I>[<I>X</I> = <I>x</I><SUB>k</SUB>]</SPAN>.
 * In addition to the methods specified in the interface
 * {@link umontreal.iro.lecuyer.probdist.Distribution Distribution},
 * a method that returns the probability <SPAN CLASS="MATH"><I>p</I><SUB>k</SUB></SPAN> is supplied.
 * 
 */
public class DiscreteDistribution implements Distribution {
  /*
     For better precision in the tails, we keep the cumulative probabilities
     (F) in cdf[x] for x <= xmed (i.e. cdf[x] is the sum off all the probabi-
     lities pr[i] for i <= x),
     and the complementary cumulative probabilities (1 - F) in cdf[x] for
     x > xmed (i.e. cdf[x] is the sum off all the probabilities pr[i]
     for i >= x).
  */

   protected double cdf[] = null;    // cumulative probabilities
   protected double pr[] = null;     // probability terms or mass distribution
   protected int xmin = 0;           // pr[x] = 0 for x < xmin
   protected int xmax = 0;           // pr[x] = 0 for x > xmax
   protected int xmed = 0;           // cdf[x] = F(x) for x <= xmed, and
                                     // cdf[x] = bar_F(x) for x > xmed
   protected int nVal;               // number of different values
   protected double sortedVal[];
   protected double supportA = Double.NEGATIVE_INFINITY;
   protected double supportB = Double.POSITIVE_INFINITY;



   protected DiscreteDistribution () {}
   // Default constructor called by subclasses such as 'EmpiricalDist'


   /**
    * Constructs a discrete distribution over the <SPAN CLASS="MATH"><I>n</I></SPAN> values
    *  contained in array <TT>values</TT>, with probabilities given in array <TT>prob</TT>.
    *  Both arrays must have at least <SPAN CLASS="MATH"><I>n</I></SPAN> elements, the probabilities must
    *  sum to 1, and the values are assumed to be sorted by increasing order.
    * 
    */
   public DiscreteDistribution (double[] values, double[] prob, int n) {
      init(n, values, prob);
   }


   /**
    * Similar to
    * {@link #DiscreteDistribution(double[], double[], int) DiscreteDistribution}<TT>(double[], double[], int)</TT>.
    * 
    */
   public DiscreteDistribution (int[] values, double[] prob, int n) {
      double[] A = new double[n];
      for(int i=0; i<n; i++)
         A[i] = values[i];
      init(n, A, prob);
   }


   /**
    * Constructs a discrete distribution whose parameters are given
    *    in a single ordered array: <TT>params[0]</TT> contains <SPAN CLASS="MATH"><I>n</I></SPAN>, the number of
    *    values to consider. Then the next <SPAN CLASS="MATH"><I>n</I></SPAN> values of <TT>params</TT> are the
    *    values, and the last <SPAN CLASS="MATH"><I>n</I></SPAN> values of <TT>params</TT>
    *    are the probabilities values.
    * 
    * 
    */
   @Deprecated
   public DiscreteDistribution (double[] params) {
      if (params.length != 1+params[0]*2)
         throw new IllegalArgumentException("Wrong parameter size");

      int n =  (int)params[0];
      double[] val = new double[n];
      double[] prob = new double[n];

      //int indice = 1;
      System.arraycopy (params, 1, val, 0, n);
      System.arraycopy (params, n+1, prob, 0, n);
      init(n, val, prob);
    }


   private void init(int n, double[] val, double[] prob) {
      int no = val.length;
      int np = prob.length;
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (no < n || np < n)
         throw new IllegalArgumentException
         ("Size of arrays 'values' or 'prob' less than 'n'");

      nVal = n;
      pr = prob;

      // cdf
      sortedVal = new double[nVal];
      System.arraycopy (val, 0, sortedVal, 0, nVal);

      supportA = sortedVal[0];
      supportB = sortedVal[nVal - 1];
      xmin = 0;
      xmax = nVal - 1;

      /* Compute the cumulative probabilities until F >= 0.5, and keep them in
         the lower part of cdf */
      cdf = new double[nVal];
      cdf[0] = pr[0];
      int i = 0;
      while (i < xmax && cdf[i] < 0.5) {
         i++;
         cdf[i] = pr[i] + cdf[i - 1];
      }
      // This is the boundary between F and barF in the CDF
      xmed = i;

      /* Compute the cumulative probabilities of the complementary
         distribution and keep them in the upper part of cdf. */
      cdf[nVal - 1] = pr[nVal - 1];
      i = nVal - 2;
      while (i > xmed) {
         cdf[i] = pr[i] + cdf[i + 1];
         i--;
      }
}

   /**
    * @param x value at which the distribution function is evaluated
    * 
    *    @return the distribution function evaluated at <TT>x</TT>
    * 
    */
   public double cdf (double x) {
      if (x < sortedVal[0])
         return 0.0;
      if (x >= sortedVal[nVal-1])
         return 1.0;
      if ((xmax == xmed) || (x < sortedVal[xmed+1])) {
         for (int i = 0; i <= xmed; i++)
            if (x >= sortedVal[i] && x < sortedVal[i+1])
               return cdf[i];
      } else {
         for (int i = xmed + 1; i < nVal-1; i++)
            if (x >= sortedVal[i] && x < sortedVal[i+1])
               return 1.0 - cdf[i+1];
      }
      throw new IllegalStateException();
   }


   /**
    * @param x value at which the complementary distribution function is evaluated
    * 
    *    @return the complementary distribution function evaluated at <TT>x</TT>
    * 
    */
   public double barF (double x) {
      if (x <= sortedVal[0])
         return 1.0;
      if (x > sortedVal[nVal-1])
         return 0.0;
      if ((xmax == xmed) || (x <= sortedVal[xmed+1])) {
         for (int i = 0; i <= xmed; i++)
            if (x > sortedVal[i] && x <= sortedVal[i+1])
               return 1.0 - cdf[i];
      } else {
         for (int i = xmed + 1; i < nVal-1; i++)
            if (x > sortedVal[i] && x <= sortedVal[i+1])
               return cdf[i + 1];
      }
      throw new IllegalStateException();
   }


   public double inverseF (double u) {
      int i, j, k;

      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= 0.0)
         return supportA;
      if (u >= 1.0)
         return supportB;

      // Remember: the upper part of cdf contains the complementary distribu-
      // tion for xmed < s <= xmax, and the lower part of cdf the
      // distribution for xmin <= s <= xmed

      if (u <= cdf[xmed - xmin]) {
         // In the lower part of cdf
         if (u <= cdf[0])
            return sortedVal[xmin];
         i = 0;
         j = xmed - xmin;
         while (i < j) {
            k = (i + j) / 2;
            if (u > cdf[k])
               i = k + 1;
            else
               j = k;
         }
      }
      else {
         // In the upper part of cdf
         u = 1 - u;
         if (u < cdf[xmax - xmin])
            return sortedVal[xmax];

         i = xmed - xmin + 1;
         j = xmax - xmin;
         while (i < j) {
            k = (i + j) / 2;
            if (u < cdf[k])
               i = k + 1;
            else
               j = k;
         }
         i--;
      }

      return sortedVal[i + xmin];
   }


   /**
    * Computes the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = &sum;<SUB>i</SUB><I>p</I><SUB>i</SUB><I>x</I><SUB>i</SUB></SPAN> of the distribution.
    * 
    */
   public double getMean() {
      double mean = 0.0;
      for (int i = 0; i < nVal; i++)
         mean += sortedVal[i] * pr[i];
      return mean;
   }


   /**
    * Computes the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = &sum;<SUB>i</SUB><I>p</I><SUB>i</SUB>(<I>x</I><SUB>i</SUB> - <I>E</I>[<I>X</I>])<SUP>2</SUP></SPAN>
    *    of the distribution.
    * 
    */
   public double getVariance() {
      double mean = getMean();
      double variance = 0.0;
      for (int i = 0; i < nVal; i++)
         variance += (sortedVal[i] - mean) * (sortedVal[i] - mean) * pr[i];
      return (variance);
   }


   /**
    * Computes the standard deviation of the distribution.
    * 
    */
   public double getStandardDeviation() {
      return Math.sqrt (getVariance());
   }


   /**
    * Returns a table containing the parameters of the current distribution.
    *    This table is built in regular order, according to constructor
    *    <TT>DiscreteDistribution(double[] params)</TT> order.
    * 
    */
   public double[] getParams() {
      double[] retour = new double[1+nVal*2];
      double sum = 0;
      retour[0] = nVal;
      System.arraycopy (sortedVal, 0, retour, 1, nVal);
      for(int i = 0; i<nVal-1; i++) {
         retour[nVal+1+i] = cdf[i] - sum;
         sum = cdf[i];
      }
      retour[2*nVal] = 1.0 - sum;

      return retour;
   }


   /**
    * Returns the number of possible values <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN>.
    * 
    */
   public int getN() {
      return nVal;
   }


   /**
    * Returns <SPAN CLASS="MATH"><I>p</I><SUB>i</SUB></SPAN>, the probability of
    *   the <SPAN CLASS="MATH"><I>i</I></SPAN>-th value, for <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>i</I> &lt; <I>n</I></SPAN>.
    * 
    * @param i value number, 
    * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>i</I> &lt; <I>n</I></SPAN>
    * 
    *    @return the probability of value <TT>i</TT>
    * 
    */
   public double prob (int i) {
      return pr[i];
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>i</I></SPAN>-th value <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN>, for <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>i</I> &lt; <I>n</I></SPAN>.
    * 
    */
   public double getValue (int i) {
      return sortedVal[i];
   }


   /**
    * Returns the lower limit <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB></SPAN> of the support of the distribution.
    * 
    * @return <SPAN CLASS="MATH"><I>x</I></SPAN> lower limit of support
    * 
    */
   public double getXinf() {
      return supportA;
   }


   /**
    * Returns the upper limit <SPAN CLASS="MATH"><I>x</I><SUB>n-1</SUB></SPAN> of the support of the distribution.
    * 
    * @return <SPAN CLASS="MATH"><I>x</I></SPAN> upper limit of support
    * 
    */
   public double getXsup() {
      return supportB;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString() {
      StringBuilder sb = new StringBuilder ();
      Formatter formatter = new Formatter (sb, Locale.US);
      formatter.format ("%s%n", getClass ().getSimpleName ());
      formatter.format ("%s :      %s%n", "value", "cdf");
      for (int i = 0; i < nVal - 1; i++)
         formatter.format ("%f : %f%n", sortedVal[i], cdf[i]);
      formatter.format ("%f : %f%n", sortedVal[nVal-1], 1.0);
      return sb.toString ();
   }

}
