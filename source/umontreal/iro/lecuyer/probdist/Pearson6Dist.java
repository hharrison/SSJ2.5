

/*
 * Class:        Pearson6Dist
 * Description:  Pearson type VI distribution
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
import optimization.*;


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>Pearson type VI</EM> distribution with shape parameters
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB> &gt; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB> &gt; 0</SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>x</I>/<I>&#946;</I>)<SUP><I>&#945;</I><SUB>1</SUB>-1</SUP>/(<I>&#946;B</I>(<I>&#945;</I><SUB>1</SUB>, <I>&#945;</I><SUB>2</SUB>)[1 + <I>x</I>/<I>&#946;</I>]<SUP><I>&#945;</I><SUB>1</SUB>+<I>&#945;</I><SUB>2</SUB></SUP>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> otherwise,
 * where <SPAN CLASS="MATH"><I>B</I></SPAN> is the beta function.
 * The distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>F</I><SUB>B</SUB>(<I>x</I>/(<I>x</I> + <I>&#946;</I>))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>F</I>(<I>x</I>) = 0</SPAN> otherwise, where <SPAN CLASS="MATH"><I>F</I><SUB>B</SUB>(<I>x</I>)</SPAN> is the distribution function
 * of a beta distribution with shape
 * parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>.
 * 
 */
public class Pearson6Dist extends ContinuousDistribution {
   protected double alpha1;
   protected double alpha2;
   protected double beta;
   protected double logBeta; // Ln (Beta (alpha1, alpha2))

   private static class Optim implements Uncmin_methods {
      private int n;
      private double[] x;

      public Optim (double[] x, int n) {
         this.n = n;
         this.x = new double[n];
         System.arraycopy (x, 0, this.x, 0, n);
      }

      public double f_to_minimize (double[] param) {

         if ((param[1] <= 0.0) || (param[2] <= 0.0) || (param[3] <= 0.0))
            return 1e200;

         double sumLogY = 0.0;
         double sumLog1_Y = 0.0;
         for (int i = 0; i < n; i++)
         {
            if (x[i] > 0.0)
               sumLogY += Math.log (x[i] / param[3]);
            else
               sumLogY -= 709.0;
            sumLog1_Y += Math.log1p (x[i] / param[3]);
         }

         return (n * (Math.log (param[3]) + Num.lnBeta (param[1], param[2])) -
         (param[1] - 1.0) * sumLogY + (param[1] + param[2]) * sumLog1_Y);
      }

      public void gradient (double[] x, double[] g)
      {
      }

      public void hessian (double[] x, double[][] h)
      {
      }
   }


   /**
    * Constructs a <TT>Pearson6Dist</TT> object with parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> =
    *    <TT>alpha1</TT>, <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> = <TT>alpha2</TT> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>.
    * 
    */
   public Pearson6Dist (double alpha1, double alpha2, double beta) {
      setParam (alpha1, alpha2, beta);
   }


   public double density (double x) {
      if (x <= 0.0)
         return 0.0;
      return Math.exp ((alpha1 - 1.0) * Math.log (x / beta) - (logBeta +
            (alpha1 + alpha2) * Math.log1p (x / beta))) / beta;
   }

   public double cdf (double x) {
      return cdf (alpha1, alpha2, beta, x);
   }

   public double barF (double x) {
      return barF (alpha1, alpha2, beta, x);
   }

   public double inverseF (double u) {
      return inverseF (alpha1, alpha2, beta, u);
   }

   public double getMean () {
      return getMean (alpha1, alpha2, beta);
   }

   public double getVariance () {
      return getVariance (alpha1, alpha2, beta);
   }

   public double getStandardDeviation () {
      return getStandardDeviation (alpha1, alpha2, beta);
   }

   /**
    * Computes the density function of a Pearson VI distribution with shape
    * parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>
    *    and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double density (double alpha1, double alpha2,
                                 double beta, double x) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      if (x <= 0.0)
         return 0.0;

      return Math.exp ((alpha1 - 1.0) * Math.log (x / beta) -
         (Num.lnBeta (alpha1, alpha2) + (alpha1 + alpha2) * Math.log1p (x / beta))) / beta;
   }


   /**
    * Computes the distribution function of a Pearson VI distribution with
    * shape parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>
    *    and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double cdf (double alpha1, double alpha2,
                             double beta, double x) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      if (x <= 0.0)
         return 0.0;

      return BetaDist.cdf (alpha1, alpha2, 15, x / (x + beta));
   }


   /**
    * Computes the complementary distribution function of a Pearson VI distribution
    *    with shape parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double barF (double alpha1, double alpha2,
                              double beta, double x) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      if (x <= 0.0)
         return 1.0;

      return 1.0 - BetaDist.cdf (alpha1, alpha2, 15, x / (x + beta));
   }


   /**
    * Computes the inverse distribution function of a Pearson VI distribution
    *    with shape parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double inverseF (double alpha1, double alpha2,
                                  double beta, double u) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");

      double y = BetaDist.inverseF (alpha1, alpha2, 15, u);

      return ((y * beta) / (1.0 - y));
   }


   /**
    * Estimates the parameters 
    * <SPAN CLASS="MATH">(<I>&#945;</I><SUB>1</SUB>, <I>&#945;</I><SUB>2</SUB>, <I>&#946;</I>)</SPAN> of the Pearson VI distribution
    *     using the maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *    <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimates are returned in a three-element
    *     array, in regular order: [
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB>, <I>&#945;</I><SUB>2</SUB></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    *    @return returns the parameters [
    * <SPAN CLASS="MATH">hat(&alpha;_1), hat(&alpha;_2), hat(&beta;)</SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");

      double[] parameters = new double[3];
      double[] xpls = new double[4];
      double[] param = new double[4];
      double[] fpls = new double[4];
      double[] gpls = new double[4];
      int[] itrcmd = new int[2];
      double[][] h = new double[4][4];
      double[] udiag = new double[4];

      Optim system = new Optim (x, n);

      double mean = 0.0;
      double mean2 = 0.0;
      double mean3 = 0.0;
      for (int i = 0; i < n; i++)
      {
         mean += x[i];
         mean2 += x[i] * x[i];
         mean3 += x[i] * x[i] * x[i];
      }
      mean /= (double) n;
      mean2 /= (double) n;
      mean3 /= (double) n;

      double r1 = mean2 / (mean * mean);
      double r2 = mean2 * mean / mean3;

      param[1] = - (2.0 * (-1.0 + r1 * r2)) / (-2.0 + r1 + r1 * r2);
      if(param[1] <= 0) param[1] = 1;
      param[2] = (- 3.0 - r2 + 4.0 * r1 * r2) / (- 1.0 - r2 + 2.0 * r1 * r2);
      if(param[2] <= 0) param[2] = 1;
      param[3] = (param[2] - 1.0) * mean / param[1];
      if(param[3] <= 0) param[3] = 1;

      Uncmin_f77.optif0_f77 (3, param, system, xpls, fpls, gpls, itrcmd, h, udiag);

      for (int i = 0; i < 3; i++)
         parameters[i] = xpls[i+1];

      return parameters;
   }


   /**
    * Creates a new instance of a Pearson VI distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>,
    *    <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, estimated using the maximum likelihood method based on
    *    the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations to use to evaluate parameters
    * 
    *    @param n the number of observations to use to evaluate parameters
    * 
    * 
    */
   public static Pearson6Dist getInstanceFromMLE (double[] x, int n) {
      double parameters[] = getMLE (x, n);
      return new Pearson6Dist (parameters[0], parameters[1], parameters[2]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (<I>&#946;&#945;</I><SUB>1</SUB>)/(<I>&#945;</I><SUB>2</SUB> - 1)</SPAN> of a
    *    Pearson VI distribution with shape parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and
    *    scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double getMean (double alpha1, double alpha2,
                                 double beta) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 1.0)
         throw new IllegalArgumentException("alpha2 <= 1");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");

      return ((beta * alpha1) / (alpha2 - 1.0));
   }


   /**
    * Computes and returns the variance
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = [<I>&#946;</I><SUP>2</SUP><I>&#945;</I><SUB>1</SUB>(<I>&#945;</I><SUB>1</SUB> + <I>&#945;</I><SUB>2</SUB> -1)]/[(<I>&#945;</I><SUB>2</SUB> -1)<SUP>2</SUP>(<I>&#945;</I><SUB>2</SUB> - 2)]</SPAN> of a Pearson VI distribution with shape
    *    parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double getVariance (double alpha1, double alpha2,
                                     double beta) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 2");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");

      return (((beta * beta) * alpha1 * (alpha1 + alpha2 - 1.0)) /
((alpha2 - 1.0) * (alpha2 - 1.0) * (alpha2 - 2.0)));
   }


   /**
    * Computes and returns the standard deviation of a Pearson VI
    * distribution with shape
    *    parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public static double getStandardDeviation (double alpha1, double alpha2,
                                              double beta) {
      return Math.sqrt (getVariance (alpha1, alpha2, beta));
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> parameter of this object.
    * 
    */
   public double getAlpha1() {
      return alpha1;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> parameter of this object.
    * 
    */
   public double getAlpha2() {
      return alpha2;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#946;</I></SPAN> parameter of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public void setParam (double alpha1, double alpha2, double beta) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      supportA = 0.0;
      this.alpha1 = alpha1;
      this.alpha2 = alpha2;
      this.beta = beta;
      logBeta = Num.lnBeta (alpha1, alpha2);
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {alpha1, alpha2, beta};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : alpha1 = " + alpha1 + ", alpha2 = " + alpha2 + ", beta = " + beta;
   }

}
