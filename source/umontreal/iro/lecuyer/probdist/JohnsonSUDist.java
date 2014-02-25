

/*
 * Class:        JohnsonSUDist
 * Description:  Johnson S_U distribution
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


/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN></EM> distribution.
 * It  has shape parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN> and 
 * <SPAN CLASS="MATH"><I>&#948;</I> &gt; 0</SPAN>, location parameter 
 * <SPAN CLASS="MATH"><I>&#958;</I></SPAN>, and scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * Denoting 
 * <SPAN CLASS="MATH"><I>y</I> = (<I>x</I> - <I>&#958;</I>)/<I>&#955;</I></SPAN>, the distribution has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#948;</I>/(<I>&#955;</I>(y^2 + 1)<SUP>1/2</SUP>(2&pi;)<SUP>1/2</SUP>exp(- (1/2)[<I>&#947;</I> + <I>&#948;</I>ln[<I>y</I> + (y^2 + 1)<SUP>1/2</SUP>]]<SUP>2</SUP>))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;,
 * </DIV><P></P>
 * and distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>&#934;</I>{<I>&#947;</I> + <I>&#948;</I>ln[<I>y</I> + (y^2 + 1)<SUP>1/2</SUP>]},&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#934;</I></SPAN> is the standard normal distribution function.
 * The inverse distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>&#958;</I> + <I>&#955;</I>(<I>e</I><SUP>t(u)</SUP> - <I>e</I><SUP>-t(u)</SUP>)/2,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I> &lt; 1,
 * </DIV><P></P>
 * where
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>t</I>(<I>u</I>) = [<I>&#934;</I><SUP>-1</SUP>(<I>u</I>) - <I>&#947;</I>]/<I>&#948;</I>.
 * </DIV><P></P>
 * 
 * <P>
 * This class relies on the methods  {@link NormalDist#cdf01 NormalDist.cdf01} and
 *    {@link NormalDist#inverseF01 NormalDist.inverseF01} of {@link NormalDist} to
 *   approximate <SPAN CLASS="MATH"><I>&#934;</I></SPAN> and <SPAN CLASS="MATH"><I>&#934;</I><SUP>-1</SUP></SPAN>.
 * 
 */
public class JohnsonSUDist extends ContinuousDistribution {
   private double gamma;
   private double delta;
   private double xi;
   private double lambda;

   private static double calcR (double a, double b, double x) {
      /* ** Soit y = (x - a)/b; cette fonction calcule r
              r = y + sqrt(y*y + 1)
            en utilisant un algorithme stable ****/

      double y = (x - a)/b;
      boolean negative;
      if (y < 0.0) {
         y = -y;
         negative = true;
      } else
         negative = false;

      double r;
      if (y < 1.0e10)
         r = y + Math.sqrt (1.0 + y*y);
      else
         r = 2.0 * y;
      if (negative)
         r = 1.0 / r;
      return r;
   }



   /**
    * Same as {@link #JohnsonSUDist(double,double,double,double) JohnsonSUDist}
    *     (gamma, delta, 0.0, 1.0).
    * 
    */
   public JohnsonSUDist (double gamma, double delta) {
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      this.gamma = gamma;
      this.delta = delta;
      this.xi = 0.0;
      this.lambda = 1.0;
   }


   /**
    * Constructs a <TT>JohnsonSUDist</TT> object
    *    with shape parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>,
    *    location parameter <SPAN CLASS="MATH"><I>&#958;</I></SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    */
   public JohnsonSUDist (double gamma, double delta,
                         double xi, double lambda) {
      setParams (gamma, delta, xi, lambda);
   }


   public double density (double x) {
      return density (gamma, delta, xi, lambda, x);
   }

   public double cdf (double x) {
      return cdf (gamma, delta, xi, lambda, x);
   }

   public double barF (double x) {
      return barF (gamma, delta, xi, lambda, x);
   }

   public double inverseF (double u){
      return inverseF (gamma, delta, xi, lambda, u);
   }

   public double getMean() {
      return JohnsonSUDist.getMean (gamma, delta, xi, lambda);
   }

   public double getVariance() {
      return JohnsonSUDist.getVariance (gamma, delta, xi, lambda);
   }

   public double getStandardDeviation() {
      return JohnsonSUDist.getStandardDeviation (gamma, delta, xi, lambda);
   }


   /**
    * Computes the density function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    */
   public static double density (double gamma, double delta,
                                 double xi, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      double r = calcR (xi, lambda, x);
      if (r <= 0.0)
         return 0.0;
      double t = gamma + delta*Math.log (r);
      double y = (x - xi)/lambda;
      return delta/(lambda*Math.sqrt (2.0*Math.PI)*Math.sqrt (y*y + 1.0))*
           Math.exp (-t*t/2.0);
   }


   /**
    * Computes the  distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double cdf (double gamma, double delta,
                             double xi, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      double r = calcR (xi, lambda, x);
      if (r > 0.0)
         return NormalDist.cdf01 (gamma + delta*Math.log (r));
      else
         return 0.0;
   }


   /**
    * Computes the complementary distribution function <SPAN CLASS="MATH">1 - <I>F</I>(<I>x</I>)</SPAN>.
    * 
    */
   public static double barF (double gamma, double delta,
                              double xi, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");

      double r = calcR (xi, lambda, x);
      if (r > 0.0)
         return NormalDist.barF01 (gamma + delta * Math.log (r));
      else
         return 1.0;
   }


   /**
    * Computes the inverse distribution function <SPAN CLASS="MATH"><I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN>.
    * 
    */
   public static double inverseF (double gamma, double delta,
                                  double xi, double lambda, double u) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      if (u > 1.0 || u < 0.0)
          throw new IllegalArgumentException ("u not in [0,1]");

      if (u >= 1.0)
         return Double.POSITIVE_INFINITY;
      if (u <= 0.0)
         return Double.NEGATIVE_INFINITY;

      double z = NormalDist.inverseF01 (u);
      double v = (z - gamma)/delta;
      if (v >= Num.LN2*Num.DBL_MAX_EXP)
         return Double.POSITIVE_INFINITY;
      if (v <= Num.LN2*Num.DBL_MIN_EXP)
         return Double.NEGATIVE_INFINITY;

      return xi + lambda * Math.sinh(v);
   }


   /**
    * Computes and returns the mean
    * of the Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN> distribution with parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, <SPAN CLASS="MATH"><I>&#958;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the mean of the Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN> distribution
    *     
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#958;</I> - <I>&#955;</I>exp<SUP>1/(2<I>&#948;</I><SUP>2</SUP>)</SUP><I>sinh</I>(<I>&#947;</I>/<I>&#948;</I>)</SPAN>
    * 
    */
   public static double getMean (double gamma, double delta,
                                 double xi, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");

      return (xi - (lambda * Math.exp(1.0 / (2.0 * delta * delta)) * 
                             Math.sinh(gamma / delta)));
   }


   /**
    * Computes and returns the variance
    * of the Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN> distribution with parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, <SPAN CLASS="MATH"><I>&#958;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the variance of the Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN> distribution
    *     
    * <SPAN CLASS="MATH">Var[<I>X</I>] = (<I>&#955;</I><SUP>2</SUP>/2)(exp<SUP>1/<I>&#948;</I><SUP>2</SUP></SUP> -1)(exp<SUP>1/<I>&#948;</I><SUP>2</SUP></SUP><I>cosh</I>(2<I>&#947;</I>/<I>&#948;</I>) + 1)</SPAN>
    * 
    */
   public static double getVariance (double gamma, double delta,
                                     double xi, double lambda) {
      double omega2;

      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0.0)
         throw new IllegalArgumentException ("delta <= 0");

      omega2 = Math.exp(1 / (delta * delta));

      return ((omega2 - 1) * (omega2 * Math.cosh(2 * gamma / delta) + 1) / 2 * lambda * lambda);
   }


   /**
    * Computes and returns the standard deviation of the Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN>
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, <SPAN CLASS="MATH"><I>&#958;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    * @return the standard deviation of the Johnson <SPAN CLASS="MATH"><I>S</I><SUB>U</SUB></SPAN> distribution
    * 
    */
   public static double getStandardDeviation (double gamma, double delta,
                                              double xi, double lambda) {
      return Math.sqrt (JohnsonSUDist.getVariance (gamma, delta, xi, lambda));
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#947;</I></SPAN> for this object.
    * 
    */
   public double getGamma() {
      return gamma;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#948;</I></SPAN> for this object.
    * 
    */
   public double getDelta() {
      return delta;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#958;</I></SPAN> for this object.
    * 
    */
   public double getXi() {
      return xi;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#955;</I></SPAN> for this object.
    * 
    */
   public double getLambda() {
      return lambda;
   }

      

   /**
    * Sets the value of the parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, <SPAN CLASS="MATH"><I>&#958;</I></SPAN> and
    *   <SPAN CLASS="MATH"><I>&#955;</I></SPAN> for this object.
    * 
    */
   public void setParams (double gamma, double delta,
                          double xi, double lambda) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      this.gamma = gamma;
      this.delta = delta;
      this.xi = xi;
      this.lambda = lambda;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>&#947;</I></SPAN>, <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, <SPAN CLASS="MATH"><I>&#958;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN>].
    * 
    * 
    */
   public double[] getParams () {
      double[] retour = {gamma, delta, xi, lambda};
      return retour;
   }


   public String toString () {
      return getClass().getSimpleName() + " : gamma = " + gamma + ", delta = " + delta + ", xi = " + xi + ", lambda = " + lambda;
   }

}
