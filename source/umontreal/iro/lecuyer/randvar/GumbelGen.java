

/*
 * Class:        GumbelGen
 * Description:  generator of random variates from the Gumbel distribution 
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
 * <EM>Gumbel</EM> distribution. Its density is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>e</I><SUP>-z</SUP><I>e</I><SUP>-e<SUP>-z</SUP></SUP>/| <I>&#946;</I>|,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;.
 * </DIV><P></P>
 * where we use the notation 
 * <SPAN CLASS="MATH"><I>z</I> = (<I>x</I> - <I>&#948;</I>)/<I>&#946;</I></SPAN>. The scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>
 * can be positive (for the Gumbel distribution) or negative (for the reverse
 * Gumbel distribution), but not 0.
 * 
 */
public class GumbelGen extends RandomVariateGen  {
   private double delta;
   private double beta;


   /**
    * Creates a Gumbel random number generator with
    *   <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#948;</I> = 0</SPAN> using stream <TT>s</TT>.
    * 
    */
   public GumbelGen (RandomStream s)  {
      this (s, 1.0, 0.0);
   }


   /**
    * Creates a Gumbel random number generator with parameters
    * <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT> using stream <TT>s</TT>.
    * 
    */
   public GumbelGen (RandomStream s, double beta, double delta)  {
      super (s, new GumbelDist(beta, delta));
      setParams (beta, delta);
   }


   /**
    * Creates a new generator for the Gumbel distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public GumbelGen (RandomStream s, GumbelDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getBeta(), dist.getDelta());
   } 


   /**
    * Generates a new variate from the Gumbel distribution with parameters
    *    <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT> and <SPAN CLASS="MATH"><I>&#948;</I> =</SPAN>&nbsp;<TT>delta</TT> using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double beta, double delta) {
      return GumbelDist.inverseF (beta, delta, s.nextDouble());
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
    * Sets the parameters  <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double beta, double delta) {
     if (beta == 0.0)
         throw new IllegalArgumentException ("beta = 0");
      this.delta = delta;
      this.beta = beta;
   }

}
