

/*
 * Class:        RayleighGen
 * Description:  random variate generators for the Rayleigh distribution
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
 * <EM>Rayleigh</EM> distribution.
 *  Its density is
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * 
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>f</I> (<I>x</I>)</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP>(<I>x</I>-<I>a</I>)&nbsp;<I>e</I><SUP>-(x-a)<SUP>2</SUP>/(2<I>&#946;</I><SUP>2</SUP>)</SUP>/<I>&#946;</I><SUP>2</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I>&nbsp;&gt;=&nbsp;<I>a</I></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>f</I> (<I>x</I>)</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP>0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &lt; <I>a</I>,</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 * 
 * where <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * 
 */
public class RayleighGen extends RandomVariateGen  {
   private double a;
   private double beta;


   /**
    * Creates a Rayleigh random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>a</I> =</SPAN> <TT>a</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public RayleighGen (RandomStream s, double a, double beta)  {
      super (s, new RayleighDist(a, beta));
      setParams (a, beta);
   }


   /**
    * Creates a Rayleigh random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>a</I> = 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public RayleighGen (RandomStream s, double beta)  {
      this (s, 0.0, beta);
   }


   /**
    * Creates a new generator for the Rayleigh distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public RayleighGen (RandomStream s, RayleighDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getA(), dist.getSigma());
   } 


   /**
    * Uses inversion to generate a new variate from the Rayleigh
    *    distribution with parameters <SPAN CLASS="MATH"><I>a</I> =</SPAN>&nbsp;<TT>a</TT> and
    *    <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double a, double beta)  {
       return RayleighDist.inverseF (a, beta, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    */
   public double getSigma() {
      return beta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>a</I> =</SPAN>&nbsp;<TT>a</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT>
    *  for this object.
    * 
    */
   public void setParams (double a, double beta)  {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      this.a  = a;
      this.beta = beta;
   }

}

