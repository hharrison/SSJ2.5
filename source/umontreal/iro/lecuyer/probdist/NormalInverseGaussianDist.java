

/*
 * Class:        NormalInverseGaussianDist
 * Description:  normal inverse gaussian distribution
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

import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.util.Num;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution with location parameter
 * <SPAN CLASS="MATH"><I>&#956;</I></SPAN>,  scale parameter 
 * <SPAN CLASS="MATH"><I>&#948;</I> &gt; 0</SPAN>, tail heavyness 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>, and
 *  asymmetry parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> such that 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;| <I>&#946;</I>| &lt; <I>&#945;</I></SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#945;&#948;e</I><SUP><I>&#948;&#947;</I>+<I>&#946;</I>(x-<I>&#956;</I>)</SUP><I>K</I><SUB>1</SUB>(<I>&#945;</I>(&delta;^2 + (x - &mu;)^2)<SUP>1/2</SUP>)/<I>&#960;</I>(&delta;^2 + (x - &mu;)^2)<SUP>1/2</SUP>,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>K</I><SUB>1</SUB></SPAN> is the modified Bessel function of the second kind of order 1,
 * and 
 * <SPAN CLASS="MATH"><I>&#947;</I> = (&alpha;^2 - &beta;^2)<SUP>1/2</SUP></SPAN>.
 * 
 * <P>
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = &int;<SUB>-&#8734;</SUB><SUP>x</SUP><I>dtf</I> (<I>t</I>),
 * </DIV><P></P>
 * 
 */
public class NormalInverseGaussianDist extends ContinuousDistribution {
   protected double alpha;
   protected double beta;
   protected double gamma;
   protected double delta;
   protected double mu;



   /**
    * Constructor for a <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution  with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT>,
    *  <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT>.
    * 
    */
   public NormalInverseGaussianDist (double alpha, double beta, double mu,
                                     double delta) {
      setParams (alpha, beta, mu, delta);
   }


   public double density (double x) {
      return density (alpha, beta, mu, delta, x);
   }

   public double cdf (double x) {
      return cdf (alpha, beta, mu, delta, x);
   }

   public double barF (double x) {
      return barF (alpha, beta, mu, delta, x);
   }

   public double getMean() {
      return getMean (alpha, beta, mu, delta);
   }

   public double getVariance() {
      return getVariance (alpha, beta, mu, delta);
   }

   public double getStandardDeviation() {
      return getStandardDeviation (alpha, beta, mu, delta);
   }

   /**
    * Computes the density function
    *      for the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN>
    *     and  <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double density (double alpha, double beta, double mu,
                                 double delta, double x) {
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (Math.abs(beta) >= alpha)
         throw new IllegalArgumentException ("|beta| >= alpha");

      double gamma = Math.sqrt(alpha*alpha - beta*beta);
      double z = (x - mu)/delta;
      double w;
      if (Math.abs(z) <= 1.0e10)
         w = Math.sqrt (1.0 + z*z);
      else
         w = Math.abs(z);
      double y = alpha*delta*w;
      double v = delta*(gamma + beta*z);
      double R = Num.expBesselK1(v, y);
      return alpha * R / (Math.PI*w);
   }


   /**
    * NOT IMPLEMENTED.
    *    Computes the distribution function
    *    of the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>,
    *   <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double cdf (double alpha, double beta, double mu,
                             double delta, double x) {
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (Math.abs(beta) >= alpha)
         throw new IllegalArgumentException ("|beta| >= alpha");

      double gamma = Math.sqrt(alpha*alpha - beta*beta);
      double z = (x - mu)/delta;
      if (z > 0.0 && (gamma + (beta - alpha)*z >= XBIG))
         return 1.0;
      if (z < 0.0 && (gamma + (beta + alpha)*z <= -XBIGM))
         return 0.0;
 //     double w = Math.sqrt (1.0 + z*z);

      throw new UnsupportedOperationException
         ("NormalInverseGaussianDist:   cdf NOT IMPLEMENTED");
   }


   /**
    * NOT IMPLEMENTED.
    *  Computes the complementary distribution function of the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution
    *  with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * 
    */
   public static double barF (double alpha, double beta, double mu,
                              double delta, double x) {
      return 1.0 - cdf (alpha, beta, mu, delta, x);
   }


   /**
    * NOT IMPLEMENTED. Computes the inverse of the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    */
   public static double inverseF (double alpha, double beta, double mu,
                                  double delta, double u) {
      throw new UnsupportedOperationException(" Inversion NOT IMPLEMENTED");
   }


   /**
    * NOT IMPLEMENTED.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @return returns the parameters
    *       [
    * <SPAN CLASS="MATH">hat(&alpha;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&beta;)</SPAN>, <SPAN CLASS="MATH">hat(&mu;)</SPAN>, 
    * <SPAN CLASS="MATH">hat(&delta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
/*
      double[] parameters = new double[4];
      double sum = 0;
      for (int i = 0; i < n; i++) {
         sum += x[i];
      }
      */
      throw new UnsupportedOperationException("getMLE is not implemented");

  //    return parameters;
   }


   /**
    * NOT IMPLEMENTED.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static NormalInverseGaussianDist getInstanceFromMLE (double[] x,
                                                               int n) {
      double parameters[] = getMLE (x, n);
      return new NormalInverseGaussianDist (parameters[0], parameters[1],
                                            parameters[2], parameters[3]);
   }


   /**
    * Returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I> + <I>&#948;&#946;</I>/<I>&#947;</I></SPAN> of the
    *   <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the mean of the normal inverse gaussian distribution
    *      
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I> + <I>&#948;&#946;</I>/<I>&#947;</I></SPAN>
    * 
    */
   public static double getMean (double alpha, double beta, double mu,
                                 double delta) {
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (Math.abs(beta) >= alpha)
         throw new IllegalArgumentException ("|beta| >= alpha");

      double gamma = Math.sqrt(alpha*alpha - beta*beta);
      return mu + delta*beta/gamma;
   }


   /**
    * Computes and returns the variance 
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#948;&#945;</I><SUP>2</SUP>/<I>&#947;</I><SUP>3</SUP></SPAN> of the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> distribution with parameters
    *  <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>,  <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the variance of the normal inverse gaussian distribution
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = <I>&#948;&#945;</I><SUP>2</SUP>/<I>&#947;</I><SUP>3</SUP></SPAN>
    * 
    */
   public static double getVariance (double alpha, double beta, double mu,
                                     double delta) {
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (Math.abs(beta) >= alpha)
         throw new IllegalArgumentException ("|beta| >= alpha");

      double gamma = Math.sqrt(alpha*alpha - beta*beta);
      return delta*alpha*alpha / (gamma*gamma*gamma);
   }


   /**
    * Computes and returns the standard deviation of the <SPAN  CLASS="textit">normal inverse gaussian</SPAN>
    *  distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * @return the standard deviation of the normal inverse gaussian distribution
    * 
    */
   public static double getStandardDeviation (double alpha, double beta,
                                              double mu, double delta) {
      return Math.sqrt (getVariance (alpha, beta, mu, delta));
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
    * 
    */
   public double getDelta() {
      return delta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
    * 
    */
   public void setParams (double alpha, double beta, double mu,
                          double delta) {
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (Math.abs(beta) >= alpha)
         throw new IllegalArgumentException ("|beta| >= alpha");

      gamma = Math.sqrt(alpha*alpha - beta*beta);

      this.mu = mu;
      this.delta = delta;
      this.beta = beta;
      this.alpha = alpha;
   }


   /**
    * Returns a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha, beta, mu, delta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + ": alpha = " + alpha + ", beta = " + beta +
                  ", mu = " + mu + ", delta = " + delta;
   }

}
