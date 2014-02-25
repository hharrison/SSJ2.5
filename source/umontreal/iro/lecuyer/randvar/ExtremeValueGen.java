

/*
 * Class:        ExtremeValueGen
 * Description:  random variate generators for the Gumbel distribution
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


@Deprecated
/**
 * <SPAN  CLASS="textbf">This class has been replaced by {@link GumbelGen}</SPAN>.
 * 
 * <P>
 * This class implements random variate generators for the <SPAN  CLASS="textit">Gumbel</SPAN>
 * (or <EM>extreme value</EM>) distribution. Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#955;e</I><SUP>-e<SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP>-<I>&#955;</I>(x-<I>&#945;</I>)</SUP>
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class ExtremeValueGen extends RandomVariateGen  {
   protected double alpha = -1.0;
   protected double lambda = -1.0;




   /**
    * Creates an <SPAN  CLASS="textit">extreme value</SPAN> random variate generator with
    *  parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>,
    *  using stream <TT>s</TT>.
    * 
    */
   public ExtremeValueGen (RandomStream s, double alpha, double lambda)  {
      super (s, new ExtremeValueDist(alpha, lambda));
      setParams (alpha, lambda);
   }


   /**
    * Creates an <SPAN  CLASS="textit">extreme value</SPAN> random variate generator with
    *  parameters 
    * <SPAN CLASS="MATH"><I>&#945;</I> = 0</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#955;</I> = 1</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public ExtremeValueGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }


   /**
    * Creates a new generator object for distribution <TT>dist</TT> and
    *    stream <TT>s</TT>.
    * 
    */
   public ExtremeValueGen (RandomStream s, ExtremeValueDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getAlpha(), dist.getLambda());
   }


   /**
    * Uses inversion to generate a new variate from the extreme value
    *   distribution with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>&nbsp;<TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>&nbsp;<TT>lambda</TT>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha,
                                    double lambda)  {
      return ExtremeValueDist.inverseF (alpha, lambda, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double alpha, double lambda) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      this.lambda = lambda;
      this.alpha = alpha;
   }

}
