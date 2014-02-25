

/*
 * Class:        NormalInverseGaussianGen
 * Description:  random variate generators for the normal inverse gaussian distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        June 2008

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

package umontreal.iro.lecuyer.randvar;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;


/**
 * This class implements random variate generators for 
 * the <SPAN  CLASS="textit">normal inverse gaussian</SPAN> (<SPAN CLASS="MATH"><I>NIG</I></SPAN>) distribution. See the definition of
 * {@link umontreal.iro.lecuyer.probdist.NormalInverseGaussianDist NormalInverseGaussianDist}
 * 
 */
public class NormalInverseGaussianGen extends RandomVariateGen  {
   protected double mu;
   protected double delta = -1.0;
   protected double alpha = -1.0;
   protected double beta = -2.0;
   protected double gamma = -1.0;



   /**
    * Creates an <SPAN  CLASS="textit">normal inverse gaussian</SPAN> random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>,  <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT>
    *  and  <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT>, using stream <TT>s</TT>.
    * 
    */
   public NormalInverseGaussianGen (RandomStream s, double alpha,
                                    double beta, double mu, double delta)  {
      super (s, new NormalInverseGaussianDist(alpha, beta, mu, delta));
      setParams (alpha, beta, mu, delta);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public NormalInverseGaussianGen (RandomStream s,
                                    NormalInverseGaussianDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getAlpha(), dist.getBeta(), dist.getMu(),
                    dist.getDelta());
   }


   /**
    * NOT IMPLEMENTED. Use the daughter classes.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha,
                                    double beta, double mu, double delta) {
      return NormalInverseGaussianDist.inverseF (alpha, beta, mu, delta,
                                                 s.nextDouble());
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
    * Sets the parameters  <SPAN CLASS="MATH"><I>&#945;</I></SPAN>,  <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
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
 
}

