

/*
 * Class:        FisherFDist
 * Description:  Fisher F-distribution
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

import umontreal.iro.lecuyer.probdist.BetaDist;
import umontreal.iro.lecuyer.util.*;

/**
 * Extends the class {@link ContinuousDistribution} for
 * the <SPAN  CLASS="textit">Fisher F</SPAN> distribution with <SPAN CLASS="MATH"><I>n</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>n</I><SUB>2</SUB></SPAN>
 * degrees of freedom, where <SPAN CLASS="MATH"><I>n</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>n</I><SUB>2</SUB></SPAN> are positive integers.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#915;</I>((<I>n</I><SUB>1</SUB> + <I>n</I><SUB>2</SUB>)/2)<I>n</I><SUB>1</SUB><SUP>n<SUB>1</SUB>/2</SUP><I>n</I><SUB>2</SUB><SUP>n<SUB>2</SUB>/2</SUP>/[<I>&#915;</I>(<I>n</I><SUB>1</SUB>/2)<I>&#915;</I>(<I>n</I><SUB>2</SUB>/2)]<I>x</I><SUP>(n<SUB>1</SUB>-2)/2</SUP>/(<I>n</I><SUB>2</SUB> + <I>n</I><SUB>1</SUB><I>x</I>)<SUP>(n<SUB>1</SUB>+n<SUB>2</SUB>)/2</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined in
 * {@link GammaDist}.
 * 
 */
public class FisherFDist extends ContinuousDistribution {
   protected int n1;
   protected int n2;
   protected double C1;
   private static final int DECPREC = 15;    // decimal precision



   /**
    * Constructs a Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with <TT>n1</TT> and <TT>n2</TT> degrees of freedom.
    * 
    */
   public FisherFDist (int n1, int n2) {
      setParams (n1, n2);
   }


   public double density (double x) {
      if (x <= 0.0)
         return 0.0;
      return Math.exp (C1 + 0.5 * (n1 - 2) * Math.log (x) -
           (0.5 * (n1 + n2) * Math.log (n2 + n1 * x)));
   }

   public double cdf (double x) {
      return FisherFDist.cdf (n1, n2, x);
   }

   public double barF (double x) {
      return FisherFDist.barF (n1, n2, x);
   }

   public double inverseF (double u) {
      return FisherFDist.inverseF (n1, n2, u);
   }

   public double getMean() {
      return FisherFDist.getMean (n1, n2);
   }

   public double getVariance() {
      return FisherFDist.getVariance (n1, n2);
   }

   public double getStandardDeviation() {
      return FisherFDist.getStandardDeviation (n1, n2);
   }

   /**
    * Computes the density function for a Fisher
    * <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with <TT>n1</TT> and <TT>n2</TT> degrees of freedom,
    *  evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    * 
    */
   public static double density (int n1, int n2, double x) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 0)
         throw new IllegalArgumentException ("n2 <= 0");
      if (x <= 0.0)
         return 0.0;

      return Math.exp (((n1/2.0) * Math.log (n1) + (n2/2.0) * Math.log(n2) +
          ((n1 - 2) / 2.0) * Math.log (x)) -
          (Num.lnBeta (n1/2.0, n2/2.0) + 
          ((n1 + n2) / 2.0) * Math.log (n2 + n1 * x)));
   }


   @Deprecated
   public static double cdf (int n1, int n2, int d, double x) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 0)
         throw new IllegalArgumentException ("n2 <= 0");
      if (x <= 0.0)
         return 0.0;
      return BetaDist.cdf (n1/2.0, n2/2.0, d, (n1*x)/(n1*x + n2));
   }


   /**
    * Computes the distribution function of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with 
    * parameters <TT>n1</TT> and <TT>n2</TT>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    * 
    */
   public static double cdf (int n1, int n2, double x) {
       return cdf (n1, n2, DECPREC, x);
   }


   @Deprecated
   public static double barF (int n1, int n2, int d, double x) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 0)
         throw new IllegalArgumentException ("n2 <= 0");
      if (x <= 0.0)
         return 1.0;
      return BetaDist.barF (n1/2.0, n2/2.0, d, (n1 * x) / (n1 * x + n2));
   }


   /**
    * Computes the complementary distribution function of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution
    * with parameters <TT>n1</TT> and <TT>n2</TT>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    * 
    */
   public static double barF (int n1, int n2, double x) {
       return barF (n1, n2, DECPREC, x);      
   }


   @Deprecated
   public static double inverseF (int n1, int n2, int d, double u) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 0)
         throw new IllegalArgumentException ("n2 <= 0");
      if (u > 1.0 || u < 0.0)
         throw new IllegalArgumentException ("u < 0 or u > 1");
      if (u <= 0.0)
         return 0.0;
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;

      double z = BetaDist.inverseF (n1 / 2.0, n2 / 2.0, d, u);
      return ((n2 * z) / (n1 * (1 - z)));
   }


   /**
    * Computes the inverse of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with parameters <TT>n1</TT>
    *  and <TT>n2</TT>, evaluated at <SPAN CLASS="MATH"><I>u</I></SPAN>.
    * 
    */
   public static double inverseF (int n1, int n2, double u) {
       return inverseF (n1, n2, DECPREC, u);      
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>n</I><SUB>2</SUB>/(<I>n</I><SUB>2</SUB> - 2)</SPAN> of the
    * Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with parameters <TT>n1</TT> and <TT>n2</TT> <SPAN CLASS="MATH">= <I>n</I><SUB>2</SUB></SPAN>.
    * 
    * @return the mean of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution
    * 
    */
   public static double getMean (int n1, int n2) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 2)
         throw new IllegalArgumentException ("n2 <= 2");

      return (n2 / (n2 - 2.0));
   }


   /**
    * Computes and returns the variance
    * of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with parameters <TT>n1</TT> <SPAN CLASS="MATH">= <I>n</I><SUB>1</SUB></SPAN>
    *  and <TT>n2</TT> <SPAN CLASS="MATH">= <I>n</I><SUB>2</SUB></SPAN>.
    * 
    * @return the variance of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = (2<I>n</I>2<SUP>2</SUP>(<I>n</I>2 + <I>n</I>1 - 2))/(<I>n</I>1(<I>n</I>2 - 2)<SUP>2</SUP>(<I>n</I>2 - 4))</SPAN>
    * 
    */
   public static double getVariance (int n1, int n2) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 4)
         throw new IllegalArgumentException ("n2 <= 4");

      return ((2.0 * n2 * n2 * (n2 + n1 - 2)) / (n1 * (n2 - 2.0) * (n2 - 2.0) * (n2 - 4.0)));
   }


   /**
    * Computes and returns the standard deviation
    *    of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution with parameters <TT>n1</TT> and <TT>n2</TT>.
    * 
    * @return the standard deviation of the Fisher <SPAN CLASS="MATH"><I>F</I></SPAN> distribution
    * 
    * 
    */
   public static double getStandardDeviation (int n1, int n2) {
      return Math.sqrt (FisherFDist.getVariance (n1, n2));
   }


   /**
    * Returns the parameter <TT>n1</TT> of this object.
    * 
    */
   @Deprecated
   public int getN() {
      return n1;
   }


   @Deprecated
   public int getM() {
      return n2;
   }


   /**
    * Returns the parameter <TT>n1</TT> of this object.
    * 
    */
   public int getN1() {
      return n1;
   }


   /**
    * Returns the parameter <TT>n2</TT> of this object.
    * 
    */
   public int getN2() {
      return n2;
   }


   /**
    * Sets the parameters <TT>n1</TT> and <TT>n2</TT> of this object.
    * 
    */
   public void setParams (int n1, int n2) {
      if (n1 <= 0)
         throw new IllegalArgumentException ("n1 <= 0");
      if (n2 <= 0)
         throw new IllegalArgumentException ("n2 <= 0");

      this.n1 = n1;
      this.n2 = n2;
      supportA = 0;
      C1 = (n1 / 2.0) * Math.log (n1) + (n2 / 2.0) * Math.log (n2) -
           Num.lnBeta (n1 / 2.0, n2 / 2.0);
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<TT>n1</TT>, <TT>n2</TT>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {n1, n2};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : n1 = " + n1 + ", n2 = " + n2;
   }

}
