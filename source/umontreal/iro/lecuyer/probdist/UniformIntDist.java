

/*
 * Class:        UniformIntDist
 * Description:  discrete uniform distribution
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
 * Extends the class {@link DiscreteDistributionInt} for
 * the <EM>discrete uniform</EM> distribution over the range <SPAN CLASS="MATH">[<I>i</I>, <I>j</I>]</SPAN>.
 * Its mass function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = 1/(<I>j</I> - <I>i</I> + 1)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> = <I>i</I>, <I>i</I> + 1,&#8230;, <I>j</I>
 * </DIV><P></P>
 * and 0 elsewhere.  The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = (floor(<I>x</I>) - <I>i</I> + 1)/(<I>j</I> - <I>i</I> + 1)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>i</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>j</I>
 * </DIV><P></P>
 * and its inverse is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>i</I> + (<I>j</I> - <I>i</I> + 1)<I>u</I>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;1.
 * </DIV><P></P>
 * 
 */
public class UniformIntDist extends DiscreteDistributionInt {
   protected int i;
   protected int j;


   /**
    * Constructs a discrete uniform distribution over the interval <SPAN CLASS="MATH">[<I>i</I>, <I>j</I>]</SPAN>.
    * 
    */
   public UniformIntDist (int i, int j) {
      setParams (i, j);
   }


   public double prob (int x) {
      return prob (i, j, x);
   }

   public double cdf (int x) {
      return cdf (i, j, x);
   }

   public double barF (int x) {
      return barF (i, j, x);
   }

   public int inverseFInt (double u) {
      return inverseF (i, j, u);
   }

   public double getMean() {
      return getMean (i, j);
   }

   public double getVariance() {
      return getVariance (i, j);
   }

   public double getStandardDeviation() {
      return getStandardDeviation (i, j);
   }

   /**
    * Computes the discrete uniform probability <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double prob (int i, int j, int x) {
      if (j < i)
         throw new IllegalArgumentException ("j < i");
      if (x < i || x > j)
         return 0.0;

      return (1.0 / (j - i + 1.0));
   }


   /**
    * Computes the discrete uniform distribution function
    * defined in.
    * 
    */
   public static double cdf (int i, int j, int x) {
      if (j < i)
         throw new IllegalArgumentException ("j < i");
      if (x < i)
         return 0.0;
      if (x >= j)
         return 1.0;

      return ((x - i + 1) / (j - i + 1.0));
   }


   /**
    * Computes the discrete uniform complementary distribution function
    *   
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>)</SPAN>.
    *   <SPAN  CLASS="textit">WARNING:</SPAN> The complementary distribution function is defined as
    *     
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>]</SPAN>.
    * 
    */
   public static double barF (int i, int j, int x) {
      if (j < i)
        throw new IllegalArgumentException ("j < i");
      if (x <= i)
         return 1.0;
      if (x > j)
         return 0.0;

      return ((j - x + 1.0) / (j - i + 1.0));
   }


   /**
    * Computes the inverse of the discrete uniform distribution function.
    * 
    */
   public static int inverseF (int i, int j, double u) {
      if (j < i)
        throw new IllegalArgumentException ("j < i");

       if (u > 1.0 || u < 0.0)
           throw new IllegalArgumentException ("u not in [0, 1]");

       if (u <= 0.0)
           return i;
       if (u >= 1.0)
           return j;

       return i + (int) (u * (j - i + 1.0));
   }


   /**
    * Estimates the parameters <SPAN CLASS="MATH">(<I>i</I>, <I>j</I>)</SPAN> of the uniform distribution
    *    over integers using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>k</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>k</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a two-element
    *     array, in regular order: [<SPAN CLASS="MATH"><I>i</I></SPAN>, <SPAN CLASS="MATH"><I>j</I></SPAN>].
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&inodot;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&jnodot;)</SPAN>]
    * 
    */
   public static double[] getMLE (int[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double parameters[] = new double[2];
      parameters[0] = (double) Integer.MAX_VALUE;
      parameters[1] = (double) Integer.MIN_VALUE;
      for (int i = 0; i < n; i++) {
         if ((double) x[i] < parameters[0])
            parameters[0] = (double) x[i];
         if ((double) x[i] > parameters[1])
            parameters[1] = (double) x[i];
      }
      return parameters;
   }


   /**
    * Creates a new instance of a discrete uniform distribution over integers with parameters
    *    <SPAN CLASS="MATH"><I>i</I></SPAN> and <SPAN CLASS="MATH"><I>j</I></SPAN> estimated using the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>k</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>k</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static UniformIntDist getInstanceFromMLE (int[] x, int n) {

      double parameters[] = getMLE (x, n);

      return new UniformIntDist ((int) parameters[0], (int) parameters[1]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (<I>i</I> + <I>j</I>)/2</SPAN>
    *    of the discrete uniform distribution.
    * 
    * @return the mean of the discrete uniform distribution
    * 
    */
   public static double getMean (int i, int j) {
      if (j < i)
        throw new IllegalArgumentException ("j < i");

      return ((i + j) / 2.0);
   }


   /**
    * Computes and returns the variance
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = [(<I>j</I> - <I>i</I> + 1)<SUP>2</SUP> -1]/12</SPAN>
    *    of the discrete uniform distribution.
    * 
    * @return the variance of the discrete uniform distribution
    * 
    */
   public static double getVariance (int i, int j) {
      if (j < i)
        throw new IllegalArgumentException ("j < i");

      return (((j - i + 1.0) * (j - i + 1.0) - 1.0) / 12.0);
   }


   /**
    * Computes and returns the standard deviation
    *    of the discrete uniform distribution.
    * 
    * @return the standard deviation of the discrete uniform distribution
    * 
    */
   public static double getStandardDeviation (int i, int j) {
      return Math.sqrt (UniformIntDist.getVariance (i, j));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>i</I></SPAN>.
    * 
    */
   public int getI() {
      return i;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>j</I></SPAN>.
    * 
    */
   public int getJ() {
      return j;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>i</I></SPAN> and <SPAN CLASS="MATH"><I>j</I></SPAN> for this object.
    * 
    */
   public void setParams (int i, int j) {
      if (j < i)
        throw new IllegalArgumentException ("j < i");

      supportA = this.i = i;
      supportB = this.j = j;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>i</I></SPAN>, <SPAN CLASS="MATH"><I>j</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {i, j};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : i = " + i + ", j = " + j;
   }

}
