

/*
 * Class:        ChiSquareNoncentralGamGen
 * Description:  noncentral chi-square random variate generators using the
                 additive property of the noncentral chi-square distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
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
 * This class implements <EM>noncentral chi square</EM> random variate generators 
 * using the additive property of the noncentral chi square distribution. It uses the following algorithm: generate a real
 *  
 * <SPAN CLASS="MATH"><I>X</I>&#8764;<I>N</I>((&lambda;)<SUP>1/2</SUP>, 1)</SPAN> from a normal distribution with variance 1, 
 * generate a real 
 * <SPAN CLASS="MATH"><I>Y</I>&#8764;<I>&#915;</I>((<I>&#957;</I> - 1)/2, 1/2)</SPAN> from a gamma distribution,
 * then return <SPAN CLASS="MATH"><I>X</I><SUP>2</SUP> + <I>Y</I></SPAN>. Here <SPAN CLASS="MATH"><I>&#957;</I></SPAN> is the number of degrees of freedom and 
 * <SPAN CLASS="MATH"><I>&#955;</I></SPAN> is the noncentrality parameter.
 * 
 * <P>
 * To generate the normal variates, one uses the fast 
 * <SPAN  CLASS="textit">acceptance-complement ratio</SPAN> method in
 * (see class {@link umontreal.iro.lecuyer.randvar.NormalACRGen NormalACRGen}).
 * To generate the gamma variates, one uses acceptance-rejection for <SPAN CLASS="MATH"><I>&#945;</I> &lt; 1</SPAN>,
 * and acceptance-complement for 
 * <SPAN CLASS="MATH"><I>&#945;</I>&nbsp;&gt;=&nbsp;1</SPAN>, as proposed in
 * (see class {@link umontreal.iro.lecuyer.randvar.GammaAcceptanceRejectionGen GammaAcceptanceRejectionGen}).
 * 
 * <P>
 * This noncentral chi square generator is faster than the generator 
 * {@link umontreal.iro.lecuyer.randvar.ChiSquareNoncentralPoisGen ChiSquareNoncentralPoisGen} .
 * For small <SPAN CLASS="MATH"><I>&#955;</I></SPAN>, it is nearly twice as fast. As <SPAN CLASS="MATH"><I>&#955;</I></SPAN> increases, 
 * it is still faster but not as much.
 * 
 */
public class ChiSquareNoncentralGamGen extends ChiSquareNoncentralGen  {
   private double racLam = -1.0;



   /**
    * Creates a noncentral chi square random variate generator with 
    *  with <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN> <TT>nu</TT> degrees of freedom and noncentrality parameter
    * <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT> using stream <TT>stream</TT>, as described above.
    * 
    */
   public ChiSquareNoncentralGamGen (RandomStream stream,
                                     double nu, double lambda)  {
      super (stream, null);
      setParams (nu, lambda);
      racLam = Math.sqrt(lambda);
   }
 
  
   public double nextDouble() {
      return gamGen (stream, nu, racLam);
   }

   /**
    * Generates a variate from the noncentral chi square
    *    distribution with parameters <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN>&nbsp;<TT>nu</TT> and <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>&nbsp;<TT>lambda</TT> using stream <TT>stream</TT>, as described above.
    * 
    */
   public static double nextDouble (RandomStream stream,
                                    double nu, double lambda)  {
      double racLam = Math.sqrt(lambda);
      return gamGen (stream, nu, racLam);
   }


//>>>>>>>>>>>>>>>>>>>>  P R I V A T E    M E T H O D S   <<<<<<<<<<<<<<<<<<<< 

   private static double gamGen (RandomStream s, double nu, double racLam) {
      // racLam = sqrt(lambda)
      double x = NormalACRGen.nextDouble (s, racLam, 1.0);
      double y = GammaAcceptanceRejectionGen.nextDouble(s, 0.5*(nu - 1.0), 0.5);
      return x*x + y;
   }

}
