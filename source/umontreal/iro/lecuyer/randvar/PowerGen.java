

/*
 * Class:        PowerGen
 * Description:  random variate generators for the power distribution
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
 * <EM>power</EM> distribution with shape parameter
 * <SPAN CLASS="MATH"><I>c</I> &gt; 0</SPAN>, over the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
 * Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>c</I>(<I>x</I> - <I>a</I>)<SUP>c-1</SUP>/(<I>b</I> - <I>a</I>)<SUP>c</SUP>
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I></SPAN>, and 0 elsewhere.
 * 
 */
public class PowerGen extends RandomVariateGen  {
   private double a;
   private double b;
   private double c;


   /**
    * Creates a Power random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>a</I> =</SPAN> <TT>a</TT>, <SPAN CLASS="MATH"><I>b</I> =</SPAN> <TT>b</TT> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>,
    *  using stream <TT>s</TT>.
    * 
    */
   public PowerGen (RandomStream s, double a, double b, double c)  {
      super (s, new PowerDist(a, b, c));
      setParams (a,  b, c);
   }


   /**
    * Creates a Power random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>a</I> = 0</SPAN>, <SPAN CLASS="MATH"><I>b</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>, using stream <TT>s</TT>.
    * 
    */
   public PowerGen (RandomStream s, double c)  {
      super (s, new PowerDist(0.0, 1.0, c));
      setParams (0.0, 1.0, c);
   }


   /**
    * Creates a new generator for the power distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public PowerGen (RandomStream s, PowerDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getA(), dist.getB(), dist.getC());
   } 


   /**
    * Uses inversion to generate a new variate from the power
    *    distribution with parameters <SPAN CLASS="MATH"><I>a</I> =</SPAN>&nbsp;<TT>a</TT>, <SPAN CLASS="MATH"><I>b</I> =</SPAN>&nbsp;<TT>b</TT>, and
    *    <SPAN CLASS="MATH"><I>c</I> =</SPAN>&nbsp;<TT>c</TT>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double a, double b,
                                    double c)  {
       return PowerDist.inverseF (a, b, c, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * 
    */
   public double getB() {
      return b;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>c</I></SPAN>.
    * 
    */
   public double getC() {
      return c;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>c</I></SPAN> for this object.
    * 
    * 
    */
   public void setParams (double a, double b, double c)  {
      this.a  = a;
      this.b  = b;
      this.c  = c;
   }

}
