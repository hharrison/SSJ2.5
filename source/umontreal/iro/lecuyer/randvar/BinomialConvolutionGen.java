

/*
 * Class:        BinomialConvolutionGen
 * Description:  binomial random variate generators using the convolution method
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
 * Implements binomial random variate generators using the 
 * convolution method.  
 * This method generates <SPAN CLASS="MATH"><I>n</I></SPAN> Bernouilli random variates with 
 * parameter <SPAN CLASS="MATH"><I>p</I></SPAN> and adds them up. 
 * Its advantages are that it requires
 * little computer memory and no setup time.
 * Its disadvantage is that it is very slow for large <SPAN CLASS="MATH"><I>n</I></SPAN>.
 * It makes sense only when <SPAN CLASS="MATH"><I>n</I></SPAN> is small.
 * 
 */
public class BinomialConvolutionGen extends BinomialGen  {
  


   /**
    * Creates a <SPAN  CLASS="textit">binomial</SPAN> random variate generator with
    *   parameters <SPAN CLASS="MATH"><I>n</I></SPAN> and <SPAN CLASS="MATH"><I>p</I></SPAN>, using stream <TT>s</TT>.
    * 
    */
   public BinomialConvolutionGen (RandomStream s, int n, double p) {
      super (s, null);
      setParams (n, p);
   }


   /**
    * Creates a random variate generator for the <SPAN  CLASS="textit">binomial</SPAN>
    *    distribution <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public BinomialConvolutionGen (RandomStream s, BinomialDist dist)  {
      super (s, dist);
   }


   public int nextInt() { 
      int x = 0;
      for (int i = 0; i < n; i++) {
         double unif = stream.nextDouble();
         if (unif <= p)
            x++;
      }
      return x;
   }

   public static int nextInt (RandomStream s, int n, double p) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      if (p < 0 || p > 1)
         throw new IllegalArgumentException ("p must be in [0,1]");
      return convolution (s, n, p);
   }
   /**
    * Generates a new integer from the binomial distribution with parameters
    *    <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>n</TT> and <SPAN CLASS="MATH"><I>p</I> =</SPAN>&nbsp;<TT>p</TT>, using the given stream <TT>s</TT>.
    * 
    */


   private static int convolution (RandomStream stream, int n, double p) {
      int x = 0;
      for (int i = 0; i < n; i++) {
         double unif = stream.nextDouble();
         if (unif <= p)
            x++;
      }
      return x;
   }
}
