

/*
 * Class:        InverseGaussianGen
 * Description:  random variate generators for the inverse Gaussian distribution
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

package umontreal.iro.lecuyer.randvar;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;


/**
 * This class implements random variate generators for 
 * the <EM>inverse Gaussian</EM> distribution with location parameter
 * <SPAN CLASS="MATH"><I>&#956;</I> &gt; 0</SPAN> and scale parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * The density function of this distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (&lambda;)<SUP>1/2</SUP>/(2<I>&#960;x</I><SUP>3</SUP>) &nbsp;<I>e</I><SUP>-<I>&#955;</I>(x-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#956;</I><SUP>2</SUP>x)</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * 
 */
public class InverseGaussianGen extends RandomVariateGen  {
   protected double mu = -1.0;
   protected double lambda = -1.0;




   /**
    * Creates an <EM>inverse Gaussian</EM> random variate generator
    *  with parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT>,
    *   using stream <TT>s</TT>.
    * 
    */
   public InverseGaussianGen (RandomStream s, double mu, double lambda)  {
      super (s, new InverseGaussianDist(mu, lambda));
      setParams (mu, lambda);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public InverseGaussianGen (RandomStream s, InverseGaussianDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getLambda());
   }


   /**
    * Generates a variate from the inverse gaussian distribution
    *    with location parameter <SPAN CLASS="MATH"><I>&#956;</I> &gt; 0</SPAN> and scale parameter 
    * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s,
                                    double mu, double lambda) {
      return InverseGaussianDist.inverseF (mu, lambda, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double mu, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (mu <= 0.0)
         throw new IllegalArgumentException ("mu <= 0");
      this.mu = mu;
      this.lambda = lambda;
   }

}
