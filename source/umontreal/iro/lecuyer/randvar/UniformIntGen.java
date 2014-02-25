

/*
 * Class:        UniformIntGen
 * Description:  random variate generator for the uniform distribution over integers
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
 * This class implements a random variate generator for the 
 * <EM>uniform</EM> distribution over integers, over the interval <SPAN CLASS="MATH">[<I>i</I>, <I>j</I>]</SPAN>.
 * Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = 1/(<I>j</I> - <I>i</I> + 1)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> = <I>i</I>, <I>i</I> + 1,&#8230;, <I>j</I>
 * </DIV><P></P>
 * and 0 elsewhere.
 * 
 */
public class UniformIntGen extends RandomVariateGenInt  {
   protected int left;     // the left limit of the interval
   protected int right;    // the right limit of the interval
    



   /**
    * Creates a uniform random variate generator over the integers
    *   in the closed interval <SPAN CLASS="MATH">[<I>i</I>, <I>j</I>]</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public UniformIntGen (RandomStream s, int i, int j)  {
      super (s, new UniformIntDist (i, j));
      setParams (i, j);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>, using 
    *     stream <TT>s</TT>.
    * 
    */
   public UniformIntGen (RandomStream s, UniformIntDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getI(), dist.getJ());
   }


   /**
    * Generates a new <EM>uniform</EM> random variate over the interval
    *    <SPAN CLASS="MATH">[<I>i</I>, <I>j</I>]</SPAN>, using stream <TT>s</TT>, by inversion.
    * 
    */
   public static int nextInt (RandomStream s, int i, int j)  {
      return UniformIntDist.inverseF (i, j, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>i</I></SPAN>.
    * 
    */
   public int getI() {
      return left;
   }
 

   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>j</I></SPAN>.
    * 
    * 
    */
   public int getJ() {
      return right;
   }


   protected  void setParams (int i, int j) {
      if (j < i)
        throw new IllegalArgumentException ("j < i");
      left = i;
      right = j;
   }
}