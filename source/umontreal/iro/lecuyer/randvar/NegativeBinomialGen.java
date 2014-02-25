

/*
 * Class:        NegativeBinomialGen
 * Description:  random variate generators for the negative binomial distribution
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
 * This class implements random variate generators having the 
 * <EM>negative binomial</EM> distribution. Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:fmass-negbin"></A>
 * <I>p</I>(<I>x</I>) = <I>&#915;</I>(<I>&#947;</I> + <I>x</I>)/(<I>x</I>!&nbsp;<I>&#915;</I>(<I>&#947;</I>))&nbsp;<I>p</I><SUP><I>&#947;</I></SUP>(1 - <I>p</I>)<SUP>x</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,&#8230;
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#915;</I></SPAN> is the gamma function, 
 * 
 * <SPAN CLASS="MATH"><I>&#947;</I> &gt; 0</SPAN> and 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>p</I>&nbsp;&lt;=&nbsp;1</SPAN>.
 * No local copy of the parameters <SPAN CLASS="MATH"><I>&#947;</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> is maintained in this class.
 * The (non-static) <TT>nextInt</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class NegativeBinomialGen extends RandomVariateGenInt  {
   protected double gamma;
   protected double p; 



   /**
    * Creates a negative binomial random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#947;</I> =</SPAN> <TT>gamma</TT> and <SPAN CLASS="MATH"><I>p</I></SPAN>,  using stream <TT>s</TT>.
    * 
    */
   public NegativeBinomialGen (RandomStream s, double gamma, double p)  {
      super (s, new NegativeBinomialDist (gamma, p));
      setParams (gamma, p);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>, using 
    *   stream <TT>s</TT>.
    * 
    */
   public NegativeBinomialGen (RandomStream s, NegativeBinomialDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getGamma(), dist.getP());
   }

    
   /**
    * Generates a new variate from the <EM>negative binomial</EM> distribution,
    *  with parameters <SPAN CLASS="MATH"><I>&#947;</I> =</SPAN>&nbsp;<TT>gamma</TT> and <SPAN CLASS="MATH"><I>p</I> =</SPAN>&nbsp;<TT>p</TT>,
    *  using stream <TT>s</TT>.
    * 
    */
   public static int nextInt (RandomStream s, double gamma, double p)  {
      return NegativeBinomialDist.inverseF (gamma, p, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN> of this object.
    * 
    */
   public double getGamma() {
      return gamma;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    * 
    */
   public double getP() {
      return p;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   protected void setParams (double gamma, double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in [0, 1]");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      this.p = p;
      this.gamma = gamma;
   }

}