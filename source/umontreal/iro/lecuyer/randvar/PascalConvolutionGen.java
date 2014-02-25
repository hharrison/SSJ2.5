

/*
 * Class:        PascalConvolutionGen
 * Description:  Pascal random variate generators using the convolution method
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
 * Implements <SPAN  CLASS="textit">Pascal</SPAN> random variate generators by
 * the <SPAN  CLASS="textit">convolution</SPAN> method.
 * The method generates <SPAN CLASS="MATH"><I>n</I></SPAN> geometric variates with probability <SPAN CLASS="MATH"><I>p</I></SPAN>
 * and adds them up.
 * 
 * <P>
 * The algorithm is slow if <SPAN CLASS="MATH"><I>n</I></SPAN> is large.
 * 
 */
public class PascalConvolutionGen extends PascalGen  {



   /**
    * Creates a <SPAN  CLASS="textit">Pascal</SPAN> random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>, using stream <TT>s</TT>.
    * 
    */
   public PascalConvolutionGen (RandomStream s, int n, double p) {
      super (s, null);
      setParams (n, p);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>, using
    *   stream <TT>s</TT>.
    * 
    */
   public PascalConvolutionGen (RandomStream s, PascalDist dist) {
      super (s, dist);
   }
 
    
   public int nextInt() {
      int x = 0;
      for (int i = 0; i < n; i++)
         x += GeometricDist.inverseF (p, stream.nextDouble());
      return x;

   }
    
   public static int nextInt (RandomStream s, int n, double p) {
     return convolution (s, n, p);
   }
   /**
    * Generates a new variate from the <SPAN  CLASS="textit">Pascal</SPAN> distribution,
    *   with parameters <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>n</TT> and <SPAN CLASS="MATH"><I>p</I> =</SPAN>&nbsp;<TT>p</TT>, using the stream <TT>s</TT>.
    * 
    */


   private static int convolution (RandomStream stream, int n, double p) {
      int x = 0;
      for (int i = 0; i < n; i++)
         x += GeometricDist.inverseF (p, stream.nextDouble());
      return x;
   }
}
