

/*
 * Class:        TriangularGen
 * Description:  random variate generators for the triangular distribution
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
 * <EM>triangular</EM> distribution. Its  density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">2(<I>x</I> - <I>a</I>)/[(<I>b</I> - <I>a</I>)(<I>m</I> - <I>a</I>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>m</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">2(<I>b</I> - <I>x</I>)/[(<I>b</I> - <I>a</I>)(<I>b</I> - <I>m</I>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>m</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">0</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; elsewhere,</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>a</I>&nbsp;&lt;=&nbsp;<I>m</I>&nbsp;&lt;=&nbsp;<I>b</I></SPAN> (see, e.g.,).
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class TriangularGen extends RandomVariateGen  {
   private double a;
   private double b;
   private double m;


   /**
    * Creates a triangular random variate generator over the interval
    * (<TT>a</TT>, <TT>b</TT>), with parameter <TT>m</TT>, using stream <TT>s</TT>.
    * 
    */
   public TriangularGen (RandomStream s, double a, double b, double m)  {
      super (s, new TriangularDist(a, b, m));
      setParams (a, b, m);
   }


   /**
    * Creates a triangular random variate generator over the interval
    *   <SPAN CLASS="MATH">(0, 1)</SPAN>, with parameter <TT>m</TT>, using stream <TT>s</TT>.
    * 
    */
   public TriangularGen (RandomStream s, double m)  {
      this (s, 0.0, 1.0, m);
   }

 
   /**
    * Creates a new generator for the triangular distribution
    *  <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public TriangularGen (RandomStream s, TriangularDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getA(), dist.getB(), dist.getM());
   }

   
   /**
    * Generates a new variate from the triangular distribution with parameters
    *   <SPAN CLASS="MATH"><I>a</I> =</SPAN>&nbsp;<TT>a</TT>, <SPAN CLASS="MATH"><I>b</I> =</SPAN>&nbsp;<TT>b</TT> and <SPAN CLASS="MATH"><I>m</I> =</SPAN>&nbsp;<TT>m</TT> and stream <TT>s</TT>,
    *    using inversion.
    * 
    */
   public static double nextDouble (RandomStream s, double a, 
                                    double b, double m)  {
       // the code is taken and adapted from unuran
       // file /distributions/c_triangular_gen.c
       return TriangularDist.inverseF (a, b, m, s.nextDouble());
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
    */
   public double getB() {
      return b;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>m</I></SPAN> for this object.
    * 
    * 
    */
   public double getM() {
      return m;
   }
      

   /**
    * Sets the value of the parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> for this object.
    * 
    */
   private void setParams (double a, double b, double m) {
      if ((a == 0.0 && b == 1.0) && (m < 0 || m > 1))
         throw new IllegalArgumentException ("m is not in [0,1]");
      else if (a >= b)
         throw new IllegalArgumentException ("a >= b");
      else if (m < a || m > b) 
         throw new IllegalArgumentException ("m is not in [a,b]");
      this.a = a;
      this.b = b;
      this.m = m;
   }
      
}
