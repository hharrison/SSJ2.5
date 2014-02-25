

/*
 * Class:        WeibullGen
 * Description:  Weibull random number generator
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
 * <EM>Weibull</EM> distribution. Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#945;&#955;</I><SUP><I>&#945;</I></SUP>(<I>x</I> - <I>&#948;</I>)<SUP><I>&#945;</I>-1</SUP>exp[- (<I>&#955;</I>(<I>x</I> - <I>&#948;</I>))<SUP><I>&#945;</I></SUP>]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &gt; <I>&#948;</I>,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> elsewhere, where 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>, and 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class WeibullGen extends RandomVariateGen  {
   private double alpha = -1.0;
   private double lambda = -1.0;
   private double delta = -1.0;


   /**
    * Creates a Weibull random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> =
    *   <TT>delta</TT>, using stream <TT>s</TT>.
    * 
    */
   public WeibullGen (RandomStream s, double alpha, double lambda,
                                      double delta)  {
      super (s, new WeibullDist(alpha, lambda, delta));
      setParams (alpha, lambda, delta);
   }


   /**
    * Creates a Weibull random variate generator with parameters
    *  <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>, 
    * <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#948;</I> = 0</SPAN>, using stream
    *  <TT>s</TT>.
    * 
    */
   public WeibullGen (RandomStream s, double alpha)  {
      this (s, alpha, 1.0, 0.0);
   }

   
   /**
    * Creates a new generator for the Weibull distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public WeibullGen (RandomStream s, WeibullDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getAlpha(), dist.getLambda(), dist.getDelta());
   } 

   
   /**
    * Uses inversion to generate a new variate from the Weibull
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>&nbsp;<TT>alpha</TT>,
    *    <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>&nbsp;<TT>lambda</TT>, and <SPAN CLASS="MATH"><I>&#948;</I> =</SPAN>&nbsp;<TT>delta</TT>, using
    *    stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha,
                                    double lambda, double delta)  {
       return WeibullDist.inverseF (alpha, lambda, delta, s.nextDouble());
   }
      

   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN>.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    * 
    */
   public double getDelta() {
      return delta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#955;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> for this
    *    object.
    * 
    */
   public void setParams (double alpha, double lambda, double delta) {
      if (alpha <= 0.0)
        throw new IllegalArgumentException ("alpha <= 0");
      if (lambda <= 0.0)
        throw new IllegalArgumentException ("lambda <= 0");
      this.alpha  = alpha;
      this.lambda = lambda;
      this.delta  = delta;
   }

}
