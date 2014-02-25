

/*
 * Class:        ChiSquareGen
 * Description:  random variate generators with the chi square distribution
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
 * This class implements random variate generators with the
 * <EM>chi square</EM> distribution with <SPAN CLASS="MATH"><I>n</I> &gt; 0</SPAN> degrees of freedom.
 * Its density function is 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>x</I><SUP>n/2-1</SUP><I>e</I><SUP>-x/2</SUP>/(2<SUP>n/2</SUP><I>&#915;</I>(<I>n</I>/2)) for <I>x</I> &gt; 0, 0 elsewhere
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined
 * in {@link GammaGen}.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class ChiSquareGen extends RandomVariateGen  {
   protected int n = -1;
    



   /**
    * Creates a <SPAN  CLASS="textit">chi square</SPAN>  random variate generator with 
    *  <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public ChiSquareGen (RandomStream s, int n)  {
      super (s, new ChiSquareDist(n));
      setParams (n);
   }

 
   /**
    * Create a new generator for the distribution <TT>dist</TT>
    *     and stream <TT>s</TT>.
    * 
    */
   public ChiSquareGen (RandomStream s, ChiSquareDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getN ());
   }


   /**
    * Generates a new variate from the chi square distribution 
    *    with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, int n)  {
      return ChiSquareDist.inverseF (n, s.nextDouble());
   }

 
     
   /**
    * Returns the value of <SPAN CLASS="MATH"><I>n</I></SPAN> for this object.
    * 
    * 
    */
   public int getN() {
      return n;
   }

 
     
   protected void setParams (int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      this.n = n;
   }
}
