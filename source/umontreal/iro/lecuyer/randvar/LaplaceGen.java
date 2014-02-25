

/*
 * Class:        LaplaceGen
 * Description:  generator of random variates from the Laplace distribution
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
 * This class implements methods for generating random variates from the 
 * <EM>Laplace</EM> distribution. Its density is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = (1/(2<I>&#946;</I>))<I>e</I><SUP>-| x-<I>&#956;</I>|/<I>&#946;</I></SUP> for  - &#8734; &lt; <I>x</I> &lt; &#8734;
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#946;</I> &gt; 0</SPAN>.
 * 
 * <P>
 * The (non-static) <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution. 
 * 
 */
public class LaplaceGen extends RandomVariateGen  {
   private double mu;
   private double beta;


   /**
    * Creates a Laplace random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public LaplaceGen (RandomStream s, double mu, double beta)  {
      super (s, new LaplaceDist(mu, beta));
      setParams (mu, beta);
   }


   /**
    * Creates a Laplace random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#956;</I> = 0</SPAN> and <SPAN CLASS="MATH"><I>&#946;</I> = 1</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public LaplaceGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }

   
   /**
    * Creates a new generator for the Laplace distribution <TT>dist</TT>
    *    and stream <TT>s</TT>.
    * 
    */
   public LaplaceGen (RandomStream s, LaplaceDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getBeta());
   } 


   /**
    * Generates a new variate from the Laplace distribution with parameters 
    *    <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>&nbsp;<TT>mu</TT> and <SPAN CLASS="MATH"><I>&#946;</I> =</SPAN>&nbsp;<TT>beta</TT>, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double beta) {
      return LaplaceDist.inverseF (mu, beta, s.nextDouble());
   }

   
   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN>.
    * 
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double mu, double beta) {
     if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      this.mu = mu;
      this.beta = beta;
   }

}
