

/*
 * Class:        ChiDist
 * Description:  chi distribution
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
import umontreal.iro.lecuyer.util.Num;
import umontreal.iro.lecuyer.functions.MathFunction;


/**
 * Extends the class {@link ContinuousDistribution} for the <EM>chi</EM>
 *   distribution with shape parameter
 *  <SPAN CLASS="MATH"><I>v</I> &gt; 0</SPAN>,  where the number of degrees of freedom
 *  <SPAN CLASS="MATH"><I>v</I></SPAN> is a positive integer.
 * The density function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>e</I><SUP>-x<SUP>2</SUP>/2</SUP><I>x</I><SUP>v-1</SUP>/(2<SUP>(v/2)-1</SUP><I>&#915;</I>(<I>v</I>/2)) for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined in
 * {@link GammaDist}.
 * The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = 1/<I>&#915;</I>(<I>v</I>/2)&int;<SUB>0</SUB><SUP>x<SUP>2</SUP>/2</SUP><I>t</I><SUP>v/2-1</SUP><I>e</I><SUP>-t</SUP>&nbsp;<I>dt</I>.
 * </DIV><P></P>
 * It is equivalent to the gamma distribution function with parameters
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I> = <I>v</I>/2</SPAN> and <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I><SUP>2</SUP>/2</SPAN>.
 * 
 */
public class ChiDist extends ContinuousDistribution {
   private int nu;
   private double C1;

   private static class Function implements MathFunction {
      protected int n;
      protected double sum;

      public Function (double s, int n)
      {
         this.n = n;
         this.sum = s;
      }

      public double evaluate (double k)
      {
         if (k < 1.0) return 1.0e200;
         return (sum + n * (Num.lnGamma (k / 2.0) - 0.5*(Num.LN2) - Num.lnGamma ((k + 1.0) / 2.0)));
      }
   }



   /**
    * Constructs a <TT>ChiDist</TT> object.
    * 
    */
   public ChiDist (int nu) {
      setNu (nu);
   }


   public double density (double x) {
       if (x <= 0.0)
         return 0.0;
      return Math.exp ((nu - 1)*Math.log (x) - x*x/2.0 - C1);
   }

   public double cdf (double x) {
      return cdf (nu, x);
   }

   public double barF (double x) {
      return barF (nu, x);
   }

   public double inverseF (double u) {
      return inverseF (nu, u);
   }

   public double getMean() {
      return ChiDist.getMean (nu);
   }

   public double getVariance() {
      return ChiDist.getVariance (nu);
   }

   public double getStandardDeviation() {
      return ChiDist.getStandardDeviation (nu);
   }

   /**
    * Computes the density function.
    * 
    */
   public static double density (int nu, double x) {
      if (nu <= 0)
         throw new IllegalArgumentException ("nu <= 0");
      if (x <= 0.0)
         return 0.0;
      return Math.exp ((nu - 1)*Math.log (x) - x*x/2.0
                         - (nu/2.0 - 1.0)*Num.LN2 - Num.lnGamma (nu/2.0));
   }


   /**
    * Computes the  distribution function by using the
    *   gamma distribution function.
    * 
    */
   public static double cdf (int nu, double x) {
      if (x <= 0.0)
         return 0.0;
      return GammaDist.cdf (nu/2.0, 15, x*x/2.0);
   }


   /**
    * Computes the complementary distribution.
    * 
    */
   public static double barF (int nu, double x) {
      if (x <= 0.0)
         return 1.0;
      return GammaDist.barF (nu/2.0, 15, x*x/2.0);
   }


   /**
    * Returns the inverse distribution function computed
    *   using the gamma inversion.
    * 
    */
   public static double inverseF (int nu, double u) {
       double res =  GammaDist.inverseF (nu/2.0, 15, u);
       return Math.sqrt (2*res);
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN> of the chi distribution
    *    using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimate is returned in element 0
    *    of the returned array.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH">hat(&nu;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      double[] parameters = new double[1];

      double mean = 0.0;
      for (int i = 0; i < n; i++)
         mean += x[i];
      mean /= (double) n;

      double var = 0.0;
      for (int i = 0; i < n; i++)
         var += ((x[i] - mean) * (x[i] - mean));
      var /= (double) n;

      double k = Math.round (var + mean * mean) - 5.0;
      if (k < 1.0)
         k = 1.0;

      double sum = 0.0;
      for (int i = 0; i < n; i++) {
         if (x[i] > 0.0)
            sum += Math.log (x[i]);
         else
            sum -= 709.0;
      }

      Function f = new Function (sum, n);
      while (f.evaluate(k) > 0.0)
         k++;
      parameters[0] = k;

      return parameters;
   }


   /**
    * Creates a new instance of a chi distribution with parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN> estimated using
    *    the maximum likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>,
    *    
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static ChiDist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new ChiDist ((int) parameters[0]);
   }


   /**
    * Computes and returns the mean
    * of the chi distribution with parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN>.
    * 
    * @return the mean of the chi distribution
    *     
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (2)<SUP>1/2</SUP><I>&#915;</I>((<I>&#957;</I> +1)/2)/<I>&#915;</I>(<I>&#957;</I>/2)</SPAN>
    * 
    */
   public static double getMean (int nu) {
      if (nu <= 0)
         throw new IllegalArgumentException ("nu <= 0");
      return  Num.RAC2 * Num.gammaRatioHalf(nu / 2.0);
   }


   /**
    * Computes and returns the variance
    * of the chi distribution with parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN>.
    * 
    * @return the variance of the chi distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = 2[<I>&#915;</I>(<I>&#957;</I>/2)<I>&#915;</I>(1 + <I>&#957;</I>/2) - <I>&#915;</I><SUP>2</SUP>(1/2(<I>&#957;</I> +1))]/<I>&#915;</I>(<I>&#957;</I>/2)</SPAN>
    * 
    */
   public static double getVariance (int nu) {
      if (nu <= 0)
         throw new IllegalArgumentException ("nu <= 0");
      double mean = ChiDist.getMean(nu);
      return (nu - (mean * mean));
   }


   /**
    * Computes and returns the standard deviation of the chi distribution
    *    with parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN>.
    * 
    * @return the standard deviation of the chi distribution
    * 
    */
   public static double getStandardDeviation (int nu) {
      return Math.sqrt (ChiDist.getVariance (nu));
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#957;</I></SPAN> for this object.
    * 
    */
   public int getNu() {
      return nu;
   }



   /**
    * Sets the value of <SPAN CLASS="MATH"><I>&#957;</I></SPAN> for this object.
    * 
    */
   public void setNu (int nu) {
      if (nu <= 0)
         throw new IllegalArgumentException ("nu <= 0");
      this.nu = nu;
      supportA = 0.0;
      C1 = (nu/2.0 - 1.0)*Num.LN2 + Num.lnGamma (nu/2.0);
   }


   /**
    * Return a table containing parameters of the current distribution.
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {nu};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : nu = " + nu;
   }

}
