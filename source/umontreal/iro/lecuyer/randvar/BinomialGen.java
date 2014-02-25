

/*
 * Class:        BinomialGen
 * Description:  random variate generators for the binomial distribution
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
import umontreal.iro.lecuyer.probdist.*;
import umontreal.iro.lecuyer.rng.*;

/**
 * This class implements random variate generators for the 
 * <EM>binomial</EM> distribution. It has parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> with
 *  mass function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = nCr(<I>n</I>, <I>x</I>)<I>p</I><SUP>x</SUP>(1 - <I>p</I>)<SUP>n-x</SUP> = <I>n</I>!/(<I>x</I>!(<I>n</I> - <I>x</I>)!) &nbsp;<I>p</I><SUP>x</SUP>(1 - <I>p</I>)<SUP>n-x</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = 0, 1, 2,..., <I>n</I>
 * </DIV><P></P>
 * where nCr<SPAN CLASS="MATH">(<I>n</I>, <I>x</I>)</SPAN> is the number of combinations of <SPAN CLASS="MATH"><I>x</I></SPAN> objects
 * among <SPAN CLASS="MATH"><I>n</I></SPAN>,
 * <SPAN CLASS="MATH"><I>n</I></SPAN> is a positive integer, and 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>p</I>&nbsp;&lt;=&nbsp;1</SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextInt</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class BinomialGen extends RandomVariateGenInt  {
   protected int    n = -1;
   protected double p = -1.0;    
    


   /**
    * Creates a binomial random variate generator with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>,
    *   using stream <TT>s</TT>.
    * 
    */
   public BinomialGen (RandomStream s, int n, double p) {
      super (s, new BinomialDist (n, p));
      setParams (n, p);
   }


   /**
    * Creates a random variate generator for the <EM>binomial</EM> 
    *     distribution <TT>dist</TT> and the random stream <TT>s</TT>.
    * 
    */
   public BinomialGen (RandomStream s, BinomialDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getN(), dist.getP());
   }


   /**
    * Generates a new integer from the <EM>binomial</EM> distribution with
    *   parameters
    *    <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>n</TT> and <SPAN CLASS="MATH"><I>p</I> =</SPAN>&nbsp;<TT>p</TT>, using the given stream <TT>s</TT>.
    * 
    */
   public static int nextInt (RandomStream s, int n, double p) {
      return BinomialDist.inverseF (n, p, s.nextDouble());
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
   public double getP() {
      return p;
   }
   


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   protected void setParams (int n, double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range [0, 1]");
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      this.p = p;
      this.n = n;
   }

}
