

/*
 * Class:        ErlangGen
 * Description:  random variate generators for the Erlang distribution
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
 * This class implements random variate generators for the <EM>Erlang</EM> 
 * distribution with parameters <SPAN CLASS="MATH"><I>k</I> &gt; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * This Erlang random variable is the sum of <SPAN CLASS="MATH"><I>k</I></SPAN> exponentials with 
 * parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and has mean <SPAN CLASS="MATH"><I>k</I>/<I>&#955;</I></SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution. 
 * 
 */
public class ErlangGen extends GammaGen  {
   protected int    k = -1;



   /**
    * Creates an Erlang random variate generator with parameters
    *  <TT>k</TT> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>,
    *   using stream <TT>s</TT>.
    * 
    */
   public ErlangGen (RandomStream s, int k, double lambda)  {
      super (s, new ErlangDist(k, lambda));
      setParams (k, lambda);
   }


   /**
    * Creates an Erlang random variate generator with parameters
    *  <TT>k</TT> and 
    * <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public ErlangGen (RandomStream s, int k)  {
      this (s, k, 1.0);
   }

    
   /**
    * Creates a new generator for the distribution <TT>dist</TT>
    *     and stream <TT>s</TT>.
    * 
    */
   public ErlangGen (RandomStream s, ErlangDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getK(), dist.getLambda());
   }


   /**
    * Generates a new variate from the Erlang distribution with
    *    parameters <SPAN CLASS="MATH"><I>k</I> =</SPAN>&nbsp;<TT>k</TT> and <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>&nbsp;<TT>lambda</TT>,
    *    using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, int k, double lambda)  {
      return ErlangDist.inverseF (k, lambda, 15, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>k</I></SPAN> of this object.
    * 
    * 
    */
   public int getK() {
      return k;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>k</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   protected void setParams (int k, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (k <= 0)
         throw new IllegalArgumentException ("k <= 0");
      this.lambda = lambda;
      this.k = k;
   }

}