

/*
 * Class:        FisherFGen
 * Description:  random variate generators for the Fisher F distribution
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
 * <SPAN  CLASS="textit">Fisher F</SPAN> distribution with <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN>
 * degrees of freedom, where <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> are positive integers.
 * The density function of this distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#915;</I>((<I>n</I> + <I>m</I>)/2)<I>n</I><SUP>n/2</SUP><I>m</I><SUP>m/2</SUP>/[<I>&#915;</I>(<I>n</I>/2)<I>&#915;</I>(<I>m</I>/2)]<I>x</I><SUP>(n-2)/2</SUP>/(<I>m</I> + <I>nx</I>)<SUP>(n+m)/2</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &gt; 0.
 * </DIV><P></P>
 * 
 */
public class FisherFGen extends RandomVariateGen  {
   protected int    n = -1;
   protected int    m = -1;




   /**
    * Creates a <SPAN  CLASS="textit">Fisher F</SPAN> random variate generator with 
    *  <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public FisherFGen (RandomStream s, int n, int m)  {
      super (s, new FisherFDist(n, m));
      setParams (n, m);
      }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public FisherFGen (RandomStream s, FisherFDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getN(), dist.getM());
   }

 
   /**
    * Generates a variate from the <SPAN  CLASS="textit">Fisher F</SPAN> distribution with
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, int n, int m) {
      return FisherFDist.inverseF (n, m, 15, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> of this object.
    * 
    */
   public int getN() {
      return n;
   }
   


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    * 
    */
   public int getM() {
      return m;
   }
   


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> of this object.
    * 
    */
   protected void setParams (int n, int m) {
      if (m <= 0)
         throw new IllegalArgumentException ("m <= 0");
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      this.m = m;
      this.n = n;
   }

}