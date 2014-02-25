

/*
 * Class:        FoldedNormalGen
 * Description:  generator of random variates from the folded normal distribution
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
 * <EM>folded normal</EM> distribution  with
 * parameters <SPAN CLASS="MATH"><I>&#956;</I>&nbsp;&gt;=&nbsp; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 * The density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#966;</I>((<I>x</I> - <I>&#956;</I>/)<I>&#963;</I>) + <I>&#966;</I>((- <I>x</I> - <I>&#956;</I>)/<I>&#963;</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;0,
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#966;</I></SPAN> denotes the density function of a standard normal distribution.
 * 
 */
public class FoldedNormalGen extends RandomVariateGen  {
    
   // Distribution parameters
   protected double mu;
   protected double sigma;



   /**
    * Creates a new <EM>folded normal</EM> generator with parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> 
    *      <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>, using stream <TT>s</TT>.
    * 
    */
   public FoldedNormalGen (RandomStream s, double mu, double sigma)  {
      super (s, new FoldedNormalDist (mu, sigma));
      setParams (mu, sigma);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public FoldedNormalGen (RandomStream s, FoldedNormalDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getSigma());
   }


   /**
    * Generates a variate from the <EM>folded normal</EM> distribution with
    *  parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>&nbsp;<TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN>&nbsp;<TT>sigma</TT>,
    *    using stream <TT>s</TT>.
    * 
    * @param s the random stream
    * 
    *    @param mu the parameter mu
    * 
    *    @param sigma the parameter sigma
    * 
    *    @return Generates a variate from the <EM>FoldedNormal</EM> distribution
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double sigma) {
      return FoldedNormalDist.inverseF (mu, sigma, s.nextDouble());
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
      if (mu < 0.0)
         throw new IllegalArgumentException ("mu < 0");
      this.mu = mu;
      this.sigma = sigma;
   }
}
