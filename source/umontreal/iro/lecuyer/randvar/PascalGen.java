

/*
 * Class:        PascalGen
 * Description:  Pascal random variate generators
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
 * Implements Pascal random variate generators, which is a special case of the
 * negative binomial generator with parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN> equal to a positive integer.
 * See {@link NegativeBinomialGen} for a description.
 * 
 */
public class PascalGen extends RandomVariateGenInt  {
   protected int    n;
   protected double p;    



   /**
    * Creates a Pascal random variate generator with parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>,
    *   using stream <TT>s</TT>.
    * 
    */
   public PascalGen (RandomStream s, int n, double p) {
      super (s, new PascalDist (n, p));
      setParams (n, p);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>, using
    *   stream <TT>s</TT>.
    * 
    */
   public PascalGen (RandomStream s, PascalDist dist) {
      super (s, dist);
      if (dist != null)
         setParams (dist.getN1(), dist.getP());
   }

    
   /**
    * Generates a new variate from the <SPAN  CLASS="textit">Pascal</SPAN> distribution,
    *  with parameters <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>n</TT> and <SPAN CLASS="MATH"><I>p</I> =</SPAN>&nbsp;<TT>p</TT>, using stream <TT>s</TT>.
    * 
    */
   public static int nextInt (RandomStream s, int n, double p) {
      return PascalDist.inverseF (n, p, s.nextDouble());
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
         throw new IllegalArgumentException ("p not in [0, 1]");
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      this.p = p;
      this.n = n;
   }

}