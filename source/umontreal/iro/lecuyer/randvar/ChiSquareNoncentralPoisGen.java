

/*
 * Class:        ChiSquareNoncentralPoisGen
 * Description:  noncentral chi square random variate generators using Poisson
                 and central chi square generators
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
 * using Poisson and central chi square generators. It uses the following algorithm:
 * generate a random integer 
 * <SPAN CLASS="MATH"><I>J</I>&#8764;Poisson(<I>&#955;</I>/2)</SPAN> from a Poisson 
 * distribution, generate a random real 
 * <SPAN CLASS="MATH"><I>X</I>&#8764;<I>&#915;</I>(<I>j</I> + <I>&#957;</I>/2, 1/2)</SPAN> from a 
 * gamma distribution, then return <SPAN CLASS="MATH"><I>X</I></SPAN>. 
 * Here <SPAN CLASS="MATH"><I>&#957;</I></SPAN> is the number of degrees of freedom and 
 * <SPAN CLASS="MATH"><I>&#955;</I></SPAN> is the noncentrality parameter.
 * 
 * <P>
 * To generate the Poisson variates, one 
 * uses tabulated inversion for 
 * <SPAN CLASS="MATH"><I>&#955;</I> &lt; 10</SPAN>, and the acceptance complement 
 * method for 
 * <SPAN CLASS="MATH"><I>&#955;</I>&nbsp;&gt;=&nbsp;10</SPAN>, as in
 * (see class {@link umontreal.iro.lecuyer.randvar.PoissonTIACGen PoissonTIACGen}).
 * To generate the gamma variates, one 
 * uses acceptance-rejection for <SPAN CLASS="MATH"><I>&#945;</I> &lt; 1</SPAN>, and acceptance-complement
 * for 
 * <SPAN CLASS="MATH"><I>&#945;</I>&nbsp;&gt;=&nbsp;1</SPAN>, as proposed  in
 * (see class {@link umontreal.iro.lecuyer.randvar.GammaAcceptanceRejectionGen GammaAcceptanceRejectionGen}).
 * 
 */
public class ChiSquareNoncentralPoisGen extends ChiSquareNoncentralGen  {
//   protected RandomStream aux; 



   /**
    * Creates a noncentral chi square random variate generator 
    * with <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN> <TT>nu</TT> degrees of freedom and noncentrality parameter
    * <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT> using stream <TT>stream</TT>, as described above.
    * 
    */
   public ChiSquareNoncentralPoisGen (RandomStream stream,
                                      double nu, double lambda)  {
      super (stream, null);
      setParams (nu, lambda);
   }
 
  
   public double nextDouble() {
      return poisGenere (stream, nu, lambda);
   }

   /**
    * Generates a variate from the noncentral chi square
    *    distribution with
    *    parameters <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN>&nbsp;<TT>nu</TT> and <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>&nbsp;<TT>lambda</TT> using
    *    stream <TT>stream</TT>, as described above.
    * 
    */
   public static double nextDouble (RandomStream stream,
                                    double nu, double lambda)  {
      return poisGenere (stream, nu, lambda);
   }


//>>>>>>>>>>>>>>>>>>>>  P R I V A T E S    M E T H O D S   <<<<<<<<<<<<<<<<<<<< 

   private static double poisGenere (RandomStream s, double nu, double lambda) {
      int j = PoissonTIACGen.nextInt (s, 0.5*lambda);
      return GammaAcceptanceRejectionGen.nextDouble (s, 0.5*nu + j, 0.5);
   }

}
