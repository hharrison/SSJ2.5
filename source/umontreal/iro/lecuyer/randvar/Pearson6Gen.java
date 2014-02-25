

/*
 * Class:        Pearson6Gen
 * Description:  random variate generators for the Pearson type VI distribution
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
 * This class implements random variate generators for 
 * the <EM>Pearson type VI</EM> distribution with shape parameters
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB> &gt; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB> &gt; 0</SPAN>, and scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * The density function of this distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (<I>x</I>/<I>&#946;</I>)<SUP><I>&#945;</I><SUB>1</SUB>-1</SUP>/(<I>&#946;B</I>(<I>&#945;</I><SUB>1</SUB>, <I>&#945;</I><SUB>2</SUB>)[1 + <I>x</I>/<I>&#946;</I>]<SUP><I>&#945;</I><SUB>1</SUB>+<I>&#945;</I><SUB>2</SUB></SUP>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> &gt; 0,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = 0</SPAN> otherwise,
 * where <SPAN CLASS="MATH"><I>B</I></SPAN> is the beta function.
 * 
 */
public class Pearson6Gen extends RandomVariateGen  {
   protected double alpha1;
   protected double alpha2;
   protected double beta;




   /**
    * Creates a Pearson6 random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> = <TT>alpha1</TT>, <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> = <TT>alpha2</TT> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> =
    *   <TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public Pearson6Gen (RandomStream s, double alpha1, double alpha2,
                                       double beta)  {
      super (s, new Pearson6Dist(alpha1, alpha2, beta));
      setParams (alpha1, alpha2, beta);
   }


   /**
    * Creates a Pearson6 random variate generator with parameters
    *   
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB> =</SPAN> <TT>alpha1</TT>, <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> = <TT>alpha2</TT> and <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN>,
    *     using stream <TT>s</TT>.
    * 
    */
   public Pearson6Gen (RandomStream s, double alpha1, double alpha2)  {
      this (s, alpha1, alpha2, 1.0);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public Pearson6Gen (RandomStream s, Pearson6Dist dist)  {
      super (s, dist);
      if (dist != null)
         setParams(dist.getAlpha1(), dist.getAlpha2(), dist.getBeta());
   }


   /**
    * Generates a variate from the Pearson VI distribution
    *    with shape parameters 
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB> &gt; 0</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB> &gt; 0</SPAN>, and
    *    scale parameter <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha1,
                                    double alpha2, double beta) {
      return Pearson6Dist.inverseF (alpha1, alpha2, beta, s.nextDouble());
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN> parameter of this object.
    * 
    */
   public double getAlpha1() {
      return alpha1;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> parameter of this object.
    * 
    */
   public double getAlpha2() {
      return alpha2;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#946;</I></SPAN> parameter of this object.
    * 
    * 
    */
   public double getBeta() {
      return beta;
   }

   
   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB></SPAN>, <SPAN CLASS="MATH"><I>&#945;</I><SUB>2</SUB></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public void setParams (double alpha1, double alpha2, double beta) {
      if (alpha1 <= 0.0)
         throw new IllegalArgumentException("alpha1 <= 0");
      if (alpha2 <= 0.0)
         throw new IllegalArgumentException("alpha2 <= 0");
      if (beta <= 0.0)
         throw new IllegalArgumentException("beta <= 0");
      this.alpha1 = alpha1;
      this.alpha2 = alpha2;
      this.beta = beta;
   }

}
