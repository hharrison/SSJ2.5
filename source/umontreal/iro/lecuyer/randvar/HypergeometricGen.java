

/*
 * Class:        HypergeometricGen
 * Description:  random variate generators for the hypergeometric distribution
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
 * <EM>hypergeometric</EM> distribution. Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = nCr(<I>m</I>, <I>x</I>)nCr(<I>l</I> - <I>m</I>, <I>k</I> - <I>x</I>)/nCr(<I>l</I>, <I>k</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = max(0, <I>k</I> - <I>l</I> + <I>m</I>),..., min(<I>k</I>, <I>m</I>)
 * </DIV><P></P>
 * where nCr<SPAN CLASS="MATH">(<I>n</I>, <I>x</I>)</SPAN> is the number of possible combinations when choosing
 * <SPAN CLASS="MATH"><I>x</I></SPAN> elements among a set of <SPAN CLASS="MATH"><I>n</I></SPAN> elements,
 * <SPAN CLASS="MATH"><I>m</I></SPAN>, <SPAN CLASS="MATH"><I>l</I></SPAN> and <SPAN CLASS="MATH"><I>k</I></SPAN> are integers that satisfy <SPAN CLASS="MATH">0 &lt; <I>m</I>&nbsp;&lt;=&nbsp;<I>l</I></SPAN> and 
 * <SPAN CLASS="MATH">0 &lt; <I>k</I>&nbsp;&lt;=&nbsp;<I>l</I></SPAN>.
 * 
 * <P>
 * The generation method is inversion using the chop-down algorithm
 * 
 */
public class HypergeometricGen extends RandomVariateGenInt  {
   private int m;
   private int l;
   private int k;    



   /**
    * Creates a hypergeometric generator with
    *    parameters <SPAN CLASS="MATH"><I>m</I> =</SPAN>&nbsp;<TT>m</TT>, <SPAN CLASS="MATH"><I>l</I> =</SPAN>&nbsp;<TT>l</TT> and <SPAN CLASS="MATH"><I>k</I> =</SPAN>&nbsp;<TT>k</TT>,
    *    using stream <TT>s</TT>.
    * 
    */
   public HypergeometricGen (RandomStream s, int m, int l, int k)  {
      super (s, new HypergeometricDist (m, l, k));
      setParams (m, l, k);
   }


   /**
    * Creates a new generator for distribution <TT>dist</TT>,
    *     using stream <TT>s</TT>.
    * 
    */
   public HypergeometricGen (RandomStream s, HypergeometricDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getM(), dist.getL(), dist.getK());
   }


   /**
    * Generates a new variate from the <EM>hypergeometric</EM> distribution with
    *    parameters <SPAN CLASS="MATH"><I>m</I> =</SPAN>&nbsp;<TT>m</TT>, <SPAN CLASS="MATH"><I>l</I> =</SPAN>&nbsp;<TT>l</TT> and <SPAN CLASS="MATH"><I>k</I> =</SPAN>&nbsp;<TT>k</TT>,
    *    using stream <TT>s</TT>.
    * 
    */
   public static int nextInt (RandomStream s, int m, int l, int k) {
      return HypergeometricDist.inverseF (m, l, k, s.nextDouble());
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>m</I></SPAN> associated with this object.
    * 
    */
   public int getM() {
      return m;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>l</I></SPAN> associated with this object.
    * 
    */
   public int getL() {
      return l;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>k</I></SPAN> associated with this object.
    * 
    * 
    */
   public int getK() {
      return k;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   protected void setParams (int m, int l, int k) {
      if (l <= 0)
         throw new IllegalArgumentException ("l must be greater than 0");
      if (m <= 0 || m > l)
         throw new IllegalArgumentException ("m is invalid: 1<=m<l");
      if (k <= 0 || k > l)
         throw new IllegalArgumentException ("k is invalid: 1<=k<l");
      this.m = m;
      this.l = l;
      this.k = k;
   }

}
