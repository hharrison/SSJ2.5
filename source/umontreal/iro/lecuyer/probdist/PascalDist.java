

/*
 * Class:        PascalDist
 * Description:  Pascal distribution
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

import umontreal.iro.lecuyer.util.RootFinder;
import umontreal.iro.lecuyer.functions.MathFunction;


/**
 * The <SPAN  CLASS="textit">Pascal</SPAN> distribution is a special case of the
 * <SPAN  CLASS="textit">negative binomial</SPAN> distribution with
 * parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>, where <SPAN CLASS="MATH"><I>n</I></SPAN> is a positive
 * integer and 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>p</I>&nbsp;&lt;=&nbsp;1</SPAN>.
 * Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = nCr(<I>n</I> + <I>x</I> - 1, <I>x</I>)<I>p</I><SUP>n</SUP>(1 - <I>p</I>)<SUP>x</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,&#8230;
 * </DIV><P></P>
 * where nCr is defined in {@link BinomialDist}.
 * This <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN> can be interpreted as the probability of having <SPAN CLASS="MATH"><I>x</I></SPAN> failures
 * before the <SPAN CLASS="MATH"><I>n</I></SPAN>th success in a sequence of independent Bernoulli trials
 * with  probability of success <SPAN CLASS="MATH"><I>p</I></SPAN>.
 * For <SPAN CLASS="MATH"><I>n</I> = 1</SPAN>, this gives the <SPAN  CLASS="textit">geometric</SPAN> distribution.
 * 
 */
public class PascalDist extends NegativeBinomialDist {
   private static final double EPSI = 1.0E-10;

   private static class Function implements MathFunction {
      protected int m;
      protected int max;
      protected double mean;
      protected int[] Fj;

      public Function (int m, int max, double mean, int[] Fj) {
         this.m = m;
         this.max = max;
         this.mean = mean;
         this.Fj = new int[Fj.length];
         System.arraycopy(Fj, 0, this.Fj, 0, Fj.length);
      }

      public double evaluate (double p) {
         double sum = 0.0;
         double s = (p * mean) / (1.0 - p);

         for (int j = 0; j < max; j++)
            sum += Fj[j] / (s + (double) j);

         return sum + m * Math.log (p);
      }

      public double evaluateN (int n, double p) {
         double sum = 0.0;

         for (int j = 0; j < max; j++)
            sum += Fj[j] / (n + j);

         return sum + m * Math.log (p);
      }
   }



   /**
    * Creates an object that contains the probability
    *    terms and the distribution function for
    *    the Pascal distribution with parameter <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    */
   public PascalDist (int n, double p) {
      setParams (n, p);
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH">(<I>n</I>, <I>p</I>)</SPAN> of the Pascal distribution
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
      if (m <= 0)
         throw new IllegalArgumentException ("m <= 0");

      double sum = 0.0;
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < m; i++)
      {
         sum += x[i];
         if (x[i] > max)
            max = x[i];
      }
      double mean = (double) sum / (double) m;

      double var = 0.0;
      for (int i = 0; i < m; i++)
         var += (x[i] - mean) * (x[i] - mean);
      var /= (double) m;

      if (mean >= var)
           throw new UnsupportedOperationException("mean >= variance");

      int[] Fj = new int[max];
      for (int j = 0; j < max; j++) {
         int prop = 0;
         for (int i = 0; i < m; i++)
            if (x[i] > j)
               prop++;

         Fj[j] = prop;
      }

      double[] parameters = new double[2];
      Function f = new Function (m, max, mean, Fj);

      parameters[1] = RootFinder.brentDekker (EPSI, 1 - EPSI, f, 1e-5);
      if (parameters[1] >= 1.0)
         parameters[1] = 1.0 - 1e-15;

      parameters[0] = Math.round ((parameters[1] * mean) / (1.0 - parameters[1]));
      if (parameters[0] == 0)
          parameters[0] = 1;

      return parameters;
   }


   /**
    * Creates a new instance of a Pascal distribution with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and
    *    <SPAN CLASS="MATH"><I>p</I></SPAN> estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>m</I></SPAN>
    *    observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param m the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static PascalDist getInstanceFromMLE (int[] x, int m) {
      double parameters[] = getMLE (x, m);
      return new PascalDist ((int) parameters[0], parameters[1]);
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public int getN1() {
      return (int) (n + 0.5);
   }
   


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   public void setParams (int n, double p) {
      super.setParams ((double) n, p);
   }


   public String toString () {
      return getClass().getSimpleName() + " : n = " + getN1() + ", p = " + getP();
   }

}
