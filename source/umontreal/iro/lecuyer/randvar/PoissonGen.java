

/*
 * Class:        PoissonGen
 * Description:  random variate generators having the Poisson distribution
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
 * This class implements random variate generators having the <EM>Poisson</EM> 
 * distribution.  Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = <I>e</I><SUP>-<I>&#955;</I></SUP><I>&#955;</I><SUP>x</SUP>/(<I>x</I>!) for <I>x</I> = 0, 1,...
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN> is a real valued parameter equal to the mean.
 * 
 * <P>
 * No local copy of the parameter <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT>
 * is maintained in this class.
 * The (non-static) <TT>nextInt</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class PoissonGen extends RandomVariateGenInt  {
   protected double lambda; 



   /**
    * Creates a Poisson random variate generator with 
    *   parameter <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT>, using stream <TT>s</TT>.
    * 
    */
   public PoissonGen (RandomStream s, double lambda)  {
      super (s, new PoissonDist (lambda));
      setParams (lambda);
   }


   /**
    * Creates a new random variate generator using the Poisson 
    *     distribution <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public PoissonGen (RandomStream s, PoissonDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getLambda());
   }


   /**
    * A static method for generating a random variate from a 
    *   <EM>Poisson</EM> distribution with parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>.
    * 
    */
   public static int nextInt (RandomStream s, double lambda)  {
      return PoissonDist.inverseF (lambda, s.nextDouble());
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