

/*
 * Class:        FrechetGen
 * Description:  generator of random variates from the Fréchet distribution
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
 * <SPAN  CLASS="textit">Fr&#233;chet</SPAN> distribution, with location parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, scale
 *  parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>, and shape parameter 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>, where we use
 *  the notation 
 * <SPAN CLASS="MATH"><I>z</I> = (<I>x</I> - <I>&#948;</I>)/<I>&#946;</I></SPAN>. It has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#945;e</I><SUP>-z<SUP>-<I>&#945;</I></SUP></SUP>/(<I>&#946;z</I><SUP><I>&#945;</I>+1</SUP>),&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; <I>&#948;</I>
 * </DIV><P></P>
 * The density is  0 for 
 * <SPAN CLASS="MATH"><I>x</I>&nbsp;&lt;=&nbsp;<I>&#948;</I></SPAN>.
 * 
 */
public class FrechetGen extends RandomVariateGen  {
   private double delta;
   private double beta;
   private double alpha;


   /**
    * Creates a <SPAN  CLASS="textit">Fr&#233;chet</SPAN> random number generator with <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>
    *  <TT>alpha</TT>, <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#948;</I> = 0</SPAN> using stream <TT>s</TT>.
    * 
    */
   public FrechetGen (RandomStream s, double alpha)  {
      this (s, alpha, 1.0, 0.0);
   }


   /**
    * Creates a <SPAN  CLASS="textit">Fr&#233;chet</SPAN> random number generator with parameters
    *  <SPAN CLASS="MATH"><I>&#945;</I></SPAN> = <TT>alpha</TT>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT> using stream <TT>s</TT>.
    * 
    */
   public FrechetGen (RandomStream s, double alpha, double beta,
                      double delta)  {
      super (s, new FrechetDist (alpha, beta, delta));
      setParams (alpha, beta, delta);
   }


   /**
    * Creates a new generator for the <SPAN  CLASS="textit">Fr&#233;chet</SPAN> distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public FrechetGen (RandomStream s, FrechetDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getAlpha(), dist.getBeta(), dist.getDelta());
   } 


   /**
    * Generates a new variate from the <SPAN  CLASS="textit">Fr&#233;chet</SPAN> distribution with parameters
    * <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>,   <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT> and <SPAN CLASS="MATH"><I>&#948;</I> =</SPAN>&nbsp;<TT>delta</TT> using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha,
                                    double beta, double delta) {
      return FrechetDist.inverseF (alpha, beta, delta, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * 
    */
   public double getDelta() {
      return delta;
   }


   /**
    * Sets the parameters  <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double alpha, double beta, double delta) {
     if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      this.delta = delta;
      this.beta = beta;
      this.alpha = alpha;
   }

}
