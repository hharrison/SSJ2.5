

/*
 * Class:        ChiGen
 * Description:  random variate generators for the chi distribution
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
 * <EM>chi</EM> distribution. It has  <SPAN CLASS="MATH"><I>&#957;</I> &gt; 0</SPAN> degrees of freedom and
 * its density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>e</I><SUP>-x<SUP>2</SUP>/2</SUP><I>x</I><SUP><I>&#957;</I>-1</SUP>/(2<SUP>(<I>&#957;</I>/2)-1</SUP><I>&#915;</I>(<I>&#957;</I>/2))&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined
 * in {@link GammaGen}.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution (slow).
 * 
 */
public class ChiGen extends RandomVariateGen  {
   protected int nu = -1;



   /**
    * Creates a <SPAN  CLASS="textit">chi</SPAN>  random variate generator with 
    *  <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN> <TT>nu</TT> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public ChiGen (RandomStream s, int nu)  {
      super (s, new ChiDist(nu));
      setParams (nu);
   }


   /**
    * Create a new generator for the distribution <TT>dist</TT>,
    *     using stream <TT>s</TT>.
    * 
    */
   public ChiGen (RandomStream s, ChiDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getNu ());
   }


   /**
    * Generates a random variate from the chi distribution with <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN>&nbsp;<TT>nu</TT>
    *    degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, int nu) {
      if (nu <= 0)
         throw new IllegalArgumentException ("nu <= 0");
      return ChiDist.inverseF (nu, s.nextDouble());
   }
 
     
   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#957;</I></SPAN> for this object.
    * 
    * 
    */
   public int getNu() {
      return nu;
   }

 
     
   protected void setParams (int nu) {
      if (nu <= 0)
         throw new IllegalArgumentException ("nu <= 0");
      this.nu = nu;
   }
}
