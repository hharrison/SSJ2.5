

/*
 * Class:        FoldedNormalDist
 * Description:  folded normal distribution
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
import umontreal.iro.lecuyer.functions.MathFunction;
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>folded normal</EM> distribution with
 * parameters <SPAN CLASS="MATH"><I>&#956;</I>&nbsp;&gt;=&nbsp; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * The density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#966;</I>((<I>x</I> - <I>&#956;</I>)/<I>&#963;</I>) + <I>&#966;</I>((- <I>x</I> - <I>&#956;</I>)/<I>&#963;</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;0,
 * </DIV><P></P>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 0,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &lt; 0,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#966;</I></SPAN> denotes the density function 
 * of a standard normal distribution.
 * 
 */
public class FoldedNormalDist extends ContinuousDistribution {
   protected double mu;
   protected double sigma;
   private static final double RACPI = 1.7724538509055160273; // Sqrt[PI]

   private static class FunctionInverse implements MathFunction {
        private double u, mu, sigma;

        public FunctionInverse (double mu, double sigma, double u) {
            this.u = u;
            this.mu = mu;
            this.sigma = sigma;
        }

        public double evaluate (double x) {
            return u - cdf(mu, sigma, x);
        }
    }



   /**
    * Constructs a <TT>FoldedNormalDist</TT> object with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>.
    * 
    */
   public FoldedNormalDist (double mu, double sigma) {
      setParams (mu, sigma);
   }


   public double density (double x) {
      return density (mu, sigma, x);
   }

   public double cdf (double x) {
      return cdf (mu, sigma, x);
   }

   public double barF (double x) {
      return barF (mu, sigma, x);
   }

   public double inverseF (double u) {
      return inverseF (mu, sigma, u);
   }

   public double getMean() {
      return FoldedNormalDist.getMean (mu, sigma);
   }

   public double getVariance() {
      return FoldedNormalDist.getVariance (mu, sigma);
   }

   public double getStandardDeviation() {
      return FoldedNormalDist.getStandardDeviation (mu, sigma);
   }

   /**
    * Computes the density function of the <EM>folded normal</EM> 
    * distribution.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param x the value at which the density is evaluated
    * 
    *    @return returns the density function
    * 
    */
   public static double density (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      if (x < 0.0) return 0.0;
      return NormalDist.density(mu,sigma,x) + NormalDist.density(mu,sigma,-x);
   }


   /**
    * Computes the distribution function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param x the value at which the distribution is evaluated
    * 
    *    @return returns the cdf function
    * 
    */
   public static double cdf (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      if (x <= 0.0) return 0.0;
      return NormalDist.cdf01((x-mu)/sigma) - NormalDist.cdf01((-x-mu)/sigma);
   }


   /**
    * Computes the complementary distribution function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param x the value at which the complementary distribution is evaluated
    * 
    *    @return returns the complementary distribution function
    * 
    */
   public static double barF (double mu, double sigma, double x) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      if (x <= 0.0) return 1.0;
      return NormalDist.barF01((x-mu)/sigma) - NormalDist.barF01((-x-mu)/sigma);
   }


   /**
    * Computes the inverse of the distribution function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @param u the value at which the inverse distribution is evaluated
    * 
    *    @return returns the inverse distribution function
    * 
    */
   public static double inverseF (double mu, double sigma, double u) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      if (u > 1.0 || u < 0.0)
         throw new IllegalArgumentException ("u not in [0,1]");
      if (u <= 0.0) return 0.0;
      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;

      MathFunction f = new FunctionInverse (mu, sigma, u);
      return RootFinder.brentDekker (0.0, mu + 10.0*sigma, f, 1.0e-14);
   }


   /**
    * .
    * 
    * Computes and returns the mean
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>E</I>[<I>X</I>] = <I>&#963;</I>()<SUP>1/2</SUP>2<I>&#960;</I> &nbsp;<I>e</I><SUP>-<I>&#956;</I><SUP>2</SUP>/(2<I>&#963;</I><SUP>2</SUP>)</SUP> + <I>&#956;</I>&nbsp;erf(<IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="FoldedNormalDistimg1.png"
    *  ALT="$\displaystyle {\frac{\mu}{{\sigma\sqrt 2}}}$">),
    * </DIV><P></P>
    * where erf<SPAN CLASS="MATH">(<I>z</I>)</SPAN> is the error function.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return returns the mean
    * 
    */
   public static double getMean (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");

      return sigma * Num.RAC2 / RACPI * Math.exp(-mu*mu/(2.0*sigma*sigma))
             + mu * Num.erf(mu/(sigma*Num.RAC2));
   }


   /**
    * .
    * 
    * Computes and returns the variance
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * Var[<I>X</I>] = <I>&#956;</I><SUP>2</SUP> + <I>&#963;</I><SUP>2</SUP> - <I>E</I>[<I>X</I>]<SUP>2</SUP>.
    * </DIV><P></P>
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return returns the variance
    * 
    */
   public static double getVariance (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      double mean = sigma * Num.RAC2 / RACPI * Math.exp(-mu*mu/(2.0*sigma*sigma))
                    + mu * Num.erf(mu/(sigma*Num.RAC2));
      return mu*mu + sigma*sigma - mean*mean;
   }


   /**
    * Computes the standard deviation of the folded normal distribution
    * with  parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return returns the standard deviation
    * 
    */
   public static double getStandardDeviation (double mu, double sigma)  {
      return Math.sqrt (FoldedNormalDist.getVariance (mu, sigma));
   }


   /**
    * NOT IMPLEMENTED.  Les formules pour le MLE sont donn&#233;es dans.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters [<SPAN CLASS="MATH">hat(&mu;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&sigma;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      throw new UnsupportedOperationException("getMLE is not implemented ");
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    *   
    * @return returns the parameter mu
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    *   
    * @return returns the parameter sigma
    * 
    */
   public double getSigma() {
      return sigma;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> for this object.
    * 
    * @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    * 
    */
   public void setParams (double mu, double sigma)  {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      this.mu = mu;
      this.sigma = sigma;
    } 


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>].
    * 
    * @return returns the parameters [<SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I></SPAN>]
    * 
    */
   public double[] getParams () {
      double[] retour = {mu, sigma};
      return retour;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    * @return returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString () {
      return getClass().getSimpleName() + " : mu = " + mu + ", sigma = " + sigma;
   }

}
