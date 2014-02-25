

/*
 * Class:        HalfNormalGen
 * Description:  generator of random variates from the half-normal distribution
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
 * This class implements methods for generating random variates from the 
 * <EM>half-normal</EM> distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and 
 * <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = ((2/&pi;)<SUP>1/2</SUP>/<I>&#963;</I>)<I>e</I><SUP>-(x-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#963;</I><SUP>2</SUP>)</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; = <I>&#956;</I>,
 * </DIV><P></P>
 * 
 */
public class HalfNormalGen extends RandomVariateGen  {
    
   // Distribution parameters
   protected double mu;
   protected double sigma;



   /**
    * Creates a new <EM>half-normal</EM> generator with parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> 
    *      <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>, using stream <TT>s</TT>.
    * 
    */
   public HalfNormalGen (RandomStream s, double mu, double sigma)  {
      super (s, new HalfNormalDist (mu, sigma));
      setParams (mu, sigma);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public HalfNormalGen (RandomStream s, HalfNormalDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getSigma());
   }


   /**
    * Generates a variate from the <EM>half-normal</EM> distribution with
    *  parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>&nbsp;<TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN>&nbsp;<TT>sigma</TT>, 
    * using stream <TT>s</TT>.
    * 
    * @param s the random stream
    * 
    *    @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return Generates a variate from the <EM>HalfNormal</EM> distribution
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double sigma) {
      return HalfNormalDist.inverseF (mu, sigma, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    *   
    * @return the parameter mu
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    *   
    * @return the parameter mu
    * 
    */
   public double getSigma() {
      return sigma;
   }


   protected void setParams (double mu, double sigma) {
      if (sigma <= 0.0)
         throw new IllegalArgumentException ("sigma <= 0");
      this.mu = mu;
      this.sigma = sigma;
   }
}
