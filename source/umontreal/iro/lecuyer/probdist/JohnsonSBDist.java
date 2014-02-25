

/*
 * Class:        JohnsonSBDist
 * Description:  Johnson S_Bdistribution
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
 * the <EM>Johnson <SPAN CLASS="MATH"><I>S</I><SUB>B</SUB></SPAN></EM> distribution
 * with shape parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN> and 
 * <SPAN CLASS="MATH"><I>&#948;</I> &gt; 0</SPAN>, location parameter <SPAN CLASS="MATH"><I>&#958;</I></SPAN>, 
 * and scale parameter <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * Denoting 
 * <SPAN CLASS="MATH"><I>y</I> = (<I>x</I> - <I>&#958;</I>)/<I>&#955;</I></SPAN>, the density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#948;</I>/(<I>&#955;y</I>(1-<I>y</I>)(2&pi;)<SUP>1/2</SUP>exp(- (1/2)[<I>&#947;</I> + <I>&#948;</I>ln(<I>y</I>/(1 - <I>y</I>))]<SUP>2</SUP>)) for <I>&#958;</I> &lt; <I>x</I> &lt; <I>&#958;</I> + <I>&#955;</I>,
 * </DIV><P></P>
 * and 0 elsewhere.  The distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>(<I>x</I>) = <I>&#934;</I>[<I>&#947;</I> + <I>&#948;</I>ln(<I>y</I>/(1 - <I>y</I>))], for <I>&#958;</I> &lt; <I>x</I> &lt; <I>&#958;</I> + <I>&#955;</I>,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#934;</I></SPAN> is the standard normal distribution function.
 * The inverse distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I><SUP>-1</SUP>(<I>u</I>) = <I>&#958;</I> + <I>&#955;</I>(1/(1 + <I>e</I><SUP>-v(u)</SUP>))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for 0&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;1,
 * </DIV><P></P>
 * where
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>v</I>(<I>u</I>) = [<I>&#934;</I><SUP>-1</SUP>(<I>u</I>) - <I>&#947;</I>]/<I>&#948;</I>.
 * </DIV><P></P>
 * 
 * <P>
 * This class relies on the methods  {@link NormalDist#cdf01 NormalDist.cdf01} and
 *   {@link NormalDist#inverseF01 NormalDist.inverseF01}
 * of {@link NormalDist} to approximate <SPAN CLASS="MATH"><I>&#934;</I></SPAN> and <SPAN CLASS="MATH"><I>&#934;</I><SUP>-1</SUP></SPAN>.
 * 
 */
public class JohnsonSBDist extends ContinuousDistribution {
   private double gamma;
   private double delta;
   private double xi;
   private double lambda;


   /**
    * Constructs a <TT>JohnsonSBDist</TT> object
    *    with shape parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>,
    *    location parameter <SPAN CLASS="MATH"><I>&#958;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    */
   public JohnsonSBDist (double gamma, double delta,
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


   /**
    * Computes the density function.
    * 
    */
   public static double density (double gamma, double delta,
                                 double xi, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      if (x <= xi || x >= (xi+lambda))
         return 0.0;
      double y = (x - xi)/lambda;
      double t1 = gamma + delta*Math.log (y/(1.0 - y));
      return delta/(lambda*y*(1.0 - y)*Math.sqrt (2.0*Math.PI))*
           Math.exp (-t1*t1/2.0);
   }
      

   /**
    * Computes the distribution function.
    * 
    */
   public static double cdf (double gamma, double delta,
                             double xi, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      if (x <= xi)
         return 0.0;
      if (x >= xi+lambda)
         return 1.0;
      double y = (x - xi)/lambda;
      return NormalDist.cdf01 (gamma + delta*Math.log (y/(1.0 - y)));
   }


   /**
    * Computes the complementary distribution.
    * 
    */
   public static double barF (double gamma, double delta,
                              double xi, double lambda, double x) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      if (x <= xi)
         return 1.0;
      if (x >= xi+lambda)
         return 0.0;
      double y = (x - xi)/lambda;
      return NormalDist.barF01 (gamma + delta*Math.log (y/(1.0 - y)));
   }


   /**
    * Computes the inverse of the distribution.
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

      double v, z;

      if (u >= 1.0)    // if u == 1, in fact
          return xi+lambda;
      if (u <= 0.0)    // if u == 0, in fact
          return xi;

      z = NormalDist.inverseF01 (u);
      v = (z - gamma)/delta;

      if ((z >= XBIG) || (v >= Num.DBL_MAX_EXP*Num.LN2))
            return xi + lambda;

        if ((z <= -XBIG) || (v <= -Num.DBL_MAX_EXP*Num.LN2))
            return xi;

        v = Math.exp (v);
        return (xi + (xi+lambda)*v)/(1.0 + v);
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
   public void setParams(double gamma, double delta, double xi,
                         double lambda) {
      if (lambda <= 0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (delta <= 0)
         throw new IllegalArgumentException ("delta <= 0");
      this.gamma = gamma;
      this.delta = delta;
      this.xi = xi;
      this.lambda = lambda;
      supportA = xi;
      supportB = xi + lambda;
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
