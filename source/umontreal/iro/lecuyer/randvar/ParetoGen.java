

/*
 * Class:        ParetoGen
 * Description:  random variate generators for the Pareto distribution
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
 * This class implements random variate generators for one of the <EM>Pareto</EM>
 * distributions, with parameters <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * Its density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <I>&#945;&#946;</I><SUP><I>&#945;</I></SUP>&nbsp;/&nbsp;<I>x</I><SUP><I>&#945;</I>+1</SUP> for <I>x</I> &gt; <I>&#946;</I>, 0 otherwise.
 * </DIV><P></P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 */
public class ParetoGen extends RandomVariateGen  {
   protected double alpha;
   protected double beta;


   /**
    * Creates a Pareto random variate generator with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> 
    *    <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN> <TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public ParetoGen (RandomStream s, double alpha, double beta)  {
      super (s, new ParetoDist(alpha, beta));
      setParams(alpha, beta);
   }


   /**
    * Creates a Pareto random variate generator with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> 
    *    <TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public ParetoGen (RandomStream s, double alpha)  {
      this (s, alpha, 1.0);
   }


   /**
    * Creates a new generator for the Pareto distribution
    *    <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public ParetoGen (RandomStream s, ParetoDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams(dist.getAlpha(), dist.getBeta());
   }


   /**
    * Generates a new variate from the Pareto distribution
    *    with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN>&nbsp;<TT>alpha</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT>, 
    *    using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s,
                                    double alpha, double beta) {
      return ParetoDist.inverseF (alpha, beta, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#945;</I></SPAN> of this object.
    * 
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   protected void setParams (double alpha, double beta) {
      if (alpha <= 0.0)
         throw new IllegalArgumentException ("alpha <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      this.alpha = alpha;
      this.beta = beta;
   }
}
