

/*
 * Class:        UniformGen
 * Description:  random variate generators for the uniform distribution over the reals
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
 * the (continuous) <EM>uniform</EM> distribution over the interval <SPAN CLASS="MATH">(<I>a</I>, <I>b</I>)</SPAN>,
 * where <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN> are real numbers with <SPAN CLASS="MATH"><I>a</I> &lt; <I>b</I></SPAN>.
 * The density is 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 1/(<I>b</I> - <I>a</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>.
 * </DIV><P></P>
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class UniformGen extends RandomVariateGen  {
   private double a;
   private double b;


   /**
    * Creates a uniform random variate generator over the interval
    *   <SPAN CLASS="MATH">(</SPAN><TT>a</TT>, <TT>b</TT><SPAN CLASS="MATH">)</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public UniformGen (RandomStream s, double a, double b)  {
      super (s, new UniformDist(a, b));
      setParams (a, b);
   }


   /**
    * Creates a uniform random variate generator over the interval
    *   <SPAN CLASS="MATH">(0, 1)</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public UniformGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }

 
   /**
    * Creates a new generator for the uniform distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public UniformGen (RandomStream s, UniformDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getA(), dist.getB());
   }


   /**
    * Generates a uniform random variate over the interval 
    *   <SPAN CLASS="MATH">(</SPAN><TT>a</TT>, <TT>b</TT><SPAN CLASS="MATH">)</SPAN> by inversion, using stream <TT>s</TT>.
    * 
    */
   static public double nextDouble (RandomStream s, double a, double b)  {
      return UniformDist.inverseF (a, b, s.nextDouble());
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>a</I></SPAN> for this object.
    * 
    */
   public double getA() {
      return a;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>b</I></SPAN> for this object.
    * 
    * 
    */
   public double getB() {
      return b;
   }
      

   /**
    * Sets the value of the parameters <SPAN CLASS="MATH"><I>a</I></SPAN> and <SPAN CLASS="MATH"><I>b</I></SPAN> for this object.
    * 
    */
   private void setParams (double a, double b) {
      if (b <= a)
         throw new IllegalArgumentException ("b <= a");
      this.a = a;
      this.b = b;
   }
      
}
