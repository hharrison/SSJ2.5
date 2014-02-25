

/*
 * Class:        ExponentialGen
 * Description:  random variate generators for the exponential distribution
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
 * <EM>exponential</EM> distribution. The density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#955;e</I><SUP>-<I>&#955;</I>x</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I>&nbsp;&gt;=&nbsp;0,
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution. 
 * 
 */
public class ExponentialGen extends RandomVariateGen  {
   protected double lambda; 



   /**
    * Creates an exponential random variate generator with
    *  parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>, using stream <TT>s</TT>.
    * 
    */
   public ExponentialGen (RandomStream s, double lambda)  {
      super (s, new ExponentialDist(lambda));
      setParams (lambda);
   }


   /**
    * Creates a new generator for the exponential 
    *    distribution <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public ExponentialGen (RandomStream s, ExponentialDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getLambda());
   } 

   
   /**
    * Uses inversion to generate a new exponential variate
    *    with parameter <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>&nbsp;<TT>lambda</TT>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double lambda)  {
      return ExponentialDist.inverseF (lambda, s.nextDouble());
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#955;</I></SPAN> associated with this object.
    * 
    * 
    */
   public double getLambda() {
      return lambda;
   }



   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lam</TT> of this object.
    * 
    */
   protected void setParams (double lam) {
      if (lam <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      this.lambda = lam;
   }

}