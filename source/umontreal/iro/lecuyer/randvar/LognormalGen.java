

/*
 * Class:        LognormalGen
 * Description:  random variate generator for the lognormal distribution
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
 * <EM>lognormal</EM> distribution. Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (1/((2&pi;)<SUP>1/2</SUP><I>&#963;x</I>)<I>e</I><SUP>-(ln(x)-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#963;</I><SUP>2</SUP>)</SUP> for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * lognormal distribution object.
 * One can also generate a lognormal random variate <SPAN CLASS="MATH"><I>X</I></SPAN> via 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TT>X = Math.exp (NormalGen.nextDouble (s, mu, sigma))</TT>,
 * </DIV><P></P>
 * in which
 * <TT>NormalGen</TT> can actually be replaced by any subclass of <TT>NormalGen</TT>.
 * 
 */
public class LognormalGen extends RandomVariateGen  {
   private double mu;
   private double sigma = -1.0;



   /**
    * Creates a lognormal random variate generator with parameters
    *    <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>, using stream <TT>s</TT>.
    * 
    */
   public LognormalGen (RandomStream s, double mu, double sigma)  {
      this (s, new LognormalDist(mu, sigma));
      setParams (mu, sigma);
   }


   /**
    * Creates a lognormal random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>&#956;</I> = 0</SPAN> and  
    * <SPAN CLASS="MATH"><I>&#963;</I> = 1</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public LognormalGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }


   /**
    * Create a random variate generator for the lognormal 
    *    distribution <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public LognormalGen (RandomStream s, LognormalDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getSigma());
   }


   /**
    * Generates a new variate from the <EM>lognormal</EM>
    *     distribution with parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>&nbsp;<TT>mu</TT> and
    *     <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN>&nbsp;<TT>sigma</TT>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double sigma) {
      return LognormalDist.inverseF (mu, sigma, s.nextDouble());
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
      if (sigma <= 0)
         throw new IllegalArgumentException ("sigma <= 0");
      this.mu = mu;
      this.sigma = sigma;
   }

}
