

/*
 * Class:        FatigueLifeGen
 * Description:  random variate generators for the fatigue life distribution 
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
 * the <SPAN  CLASS="textit">fatigue life</SPAN> distribution with location
 * parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and shape
 * parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
 * The density function of this distribution is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = [(((<I>x</I> - <I>&#956;</I>)/<I>&#946;</I>)<SUP>1/2</SUP> + (<I>&#946;</I>/(<I>x</I> - <I>&#956;</I>))<SUP>1/2</SUP>)/(2<I>&#947;</I>(<I>x</I> - <I>&#956;</I>))]<I>&#966;</I>((((<I>x</I> - <I>&#956;</I>)/<I>&#946;</I>)<SUP>1/2</SUP> - (<I>&#946;</I>/(<I>x</I> - <I>&#956;</I>))<SUP>1/2</SUP>)/<I>&#947;</I>),&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<I>x</I> &gt; <I>&#956;</I>
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#966;</I></SPAN> is the probability density of the standard normal distribution.
 * 
 */
public class FatigueLifeGen extends RandomVariateGen  {
   protected double mu;
   protected double beta;
   protected double gamma;




   /**
    * Creates a <SPAN  CLASS="textit">fatigue life</SPAN> random variate generator with
    *   parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> <TT>mu</TT>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN> =
    *   <TT>gamma</TT>, using stream <TT>s</TT>.
    * 
    */
   public FatigueLifeGen (RandomStream s, double mu, double beta,
                                          double gamma)  {
      super (s, new FatigueLifeDist(mu, beta, gamma));
      setParams (mu, beta, gamma);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public FatigueLifeGen (RandomStream s, FatigueLifeDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getBeta(), dist.getGamma());
   }


   /**
    * Generates a variate from the <EM>fatigue life</EM> distribution
    *    with location parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, scale parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and shape parameter
    *    <SPAN CLASS="MATH"><I>&#947;</I></SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double beta,
                                    double gamma) {
      return FatigueLifeDist.inverseF (mu, beta, gamma, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#946;</I></SPAN> of this object.
    * 
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#947;</I></SPAN> of this object.
    * 
    */
   public double getGamma() {
      return gamma;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> and <SPAN CLASS="MATH"><I>&#947;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double mu, double beta, double gamma) {
      if (beta <= 0.0)
         throw new IllegalArgumentException ("beta <= 0");
      if (gamma <= 0.0)
         throw new IllegalArgumentException ("gamma <= 0");
      
      this.mu = mu;
      this.beta = beta;
      this.gamma = gamma;
   }

}
