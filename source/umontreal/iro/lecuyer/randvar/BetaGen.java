

/*
 * Class:        BetaGen
 * Description:  random variate generators with the beta distribution
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
 * <EM>beta</EM> distribution with shape parameters 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>, over the interval <SPAN CLASS="MATH">(<I>a</I>, <I>b</I>)</SPAN>, where <SPAN CLASS="MATH"><I>a</I> &lt; <I>b</I></SPAN>.
 * The density function of this distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = [<I>&#915;</I>(<I>&#945;</I> + <I>&#946;</I>)/(<I>&#915;</I>(<I>&#945;</I>)<I>&#915;</I>(<I>&#946;</I>)(<I>b</I> - <I>a</I>)<SUP><I>&#945;</I>+<I>&#946;</I>-1</SUP>)](<I>x</I> - <I>a</I>)<SUP><I>&#945;</I>-1</SUP>(<I>b</I> - <I>x</I>)<SUP><I>&#946;</I>-1</SUP> for <I>a</I> &lt; <I>x</I> &lt; <I>b</I>,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> elsewhere,
 * where 
 * <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined
 * in
 * {@link GammaGen}.
 * Local copies of the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN>, <SPAN CLASS="MATH"><I>a</I></SPAN>, and <SPAN CLASS="MATH"><I>b</I></SPAN>
 * are maintained in this class.
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class BetaGen extends RandomVariateGen  {
    
   // Distribution parameters
   protected double p;
   protected double q;
   protected double a;
   protected double b;
   protected int gen;



   /**
    * Creates a new beta generator with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> 
    *      <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, over the interval
    *     <SPAN CLASS="MATH">(</SPAN><TT>a</TT><SPAN CLASS="MATH">,</SPAN>&nbsp;<TT>b</TT><SPAN CLASS="MATH">)</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public BetaGen (RandomStream s, double alpha, double beta,
                                   double a, double b)  {
      super (s, new BetaDist (alpha, beta, a, b));
      setParams (alpha, beta, a, b);
   }


   /**
    * Creates a new beta generator with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> 
    *      <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, over the interval <SPAN CLASS="MATH">(0, 1)</SPAN>,
    *    using stream <TT>s</TT>.
    * 
    */
   public BetaGen (RandomStream s, double alpha, double beta)  {
      this (s, alpha, beta, 0.0, 1.0);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public BetaGen (RandomStream s, BetaDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getAlpha(), dist.getBeta(), dist.getA(), dist.getB());
   }


   /**
    * Generates a variate from the <EM>beta</EM> distribution with
    *  parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>&nbsp;<TT>alpha</TT>, <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT>, over the
    *  interval <SPAN CLASS="MATH">(<I>a</I>, <I>b</I>)</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha,
                                    double beta, double a, double b) {
      return BetaDist.inverseF (alpha, beta, a, b, 15, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return p;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return q;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>a</I></SPAN> of this object.
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>b</I></SPAN> of this object.
    * 
    */
   public double getB() {
      return b;
   }


   protected void setParams (double alpha, double beta, double aa, double bb) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (aa >= bb)
         throw new IllegalArgumentException ("a >= b");
      p = alpha;
      q = beta;
      a = aa;
      b = bb;
   }
}
