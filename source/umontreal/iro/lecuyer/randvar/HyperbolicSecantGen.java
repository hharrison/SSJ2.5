

/*
 * Class:        HyperbolicSecantGen
 * Description:  random variate generators for the hyperbolic secant distribution
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
 * This class implements random variate generators for the 
 *  <EM>hyperbolic secant</EM> distribution with location
 *  parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
 * The density function of this distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 1/(2<I>&#963;</I>) sech(<I>&#960;</I>/2(<I>x</I> - <I>&#956;</I>)/<I>&#963;</I>),&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * 
 */
public class HyperbolicSecantGen extends RandomVariateGen  {
   protected double mu;
   protected double sigma;




   /**
    * Creates a <EM>hyperbolic secant</EM> random variate generator
    *   with parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>,
    *   using stream <TT>s</TT>.
    * 
    */
   public HyperbolicSecantGen (RandomStream s, double mu, double sigma)  {
      super (s, new HyperbolicSecantDist(mu, sigma));
      setParams (mu, sigma);
   }


   /**
    * Creates a <EM>hyperbolic secant</EM> random variate generator
    *   with parameters <SPAN CLASS="MATH"><I>&#956;</I> = 0</SPAN> and <SPAN CLASS="MATH"><I>&#963;</I> = 1</SPAN>,
    *   using stream <TT>s</TT>.
    * 
    */
   public HyperbolicSecantGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public HyperbolicSecantGen (RandomStream s, HyperbolicSecantDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getSigma());
   }


   /**
    * Generates a variate from the <EM>hyperbolic secant</EM> distribution with
    *    location parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and scale parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double sigma) {
      return HyperbolicSecantDist.inverseF (mu, sigma, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    * 
    * 
    */
   public double getSigma() {
      return sigma;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      this.mu = mu;
      this.sigma = sigma;
   }

}
