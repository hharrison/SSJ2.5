

/*
 * Class:        NakagamiGen
 * Description:  random variate generators for the Nakagami distribution.
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
 * This class implements random variate generators for the <EM>Nakagami</EM> 
 * distribution. See the definition in 
 * {@link umontreal.iro.lecuyer.probdist.NakagamiDist NakagamiDist} of package
 * <TT>probdist</TT>.
 * 
 */
public class NakagamiGen extends RandomVariateGen  {
   // Distribution parameters
   protected double a;
   protected double lambda;
   protected double c;



   /**
    * Creates a new Nakagami generator with parameters <SPAN CLASS="MATH"><I>a</I> =</SPAN> <TT>a</TT>, 
    *  <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>, using stream <TT>s</TT>.
    * 
    */
   public NakagamiGen (RandomStream s, double a, double lambda, double c)  {
      super (s, new NakagamiDist (a, lambda, c));
      setParams (a, lambda, c);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public NakagamiGen (RandomStream s, NakagamiDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getA(), dist.getLambda(), dist.getC());
   }


   /**
    * Generates a variate from the <EM>Nakagami</EM> distribution with
    *  parameters <SPAN CLASS="MATH"><I>a</I> =</SPAN> <TT>a</TT>, 
    *  <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN> <TT>lambda</TT> and <SPAN CLASS="MATH"><I>c</I> =</SPAN> <TT>c</TT>, using stream <TT>s</TT>.
    * 
    * @param s the random stream
    * 
    *    @param a the location parameter
    * 
    *    @param lambda the scale parameter
    * 
    *    @param c the shape parameter
    * 
    *    @return Generates a variate from the <EM>Nakagami</EM> distribution
    * 
    */
   public static double nextDouble (RandomStream s, double a, double lambda,
                                    double c) {
      return NakagamiDist.inverseF (a, lambda, c, s.nextDouble());
   }


   /**
    * Returns the location parameter <SPAN CLASS="MATH"><I>a</I></SPAN> of this object.
    *   
    * @return the location parameter mu
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the scale parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> of this object.
    *   
    * @return the scale parameter mu
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Returns the shape parameter <SPAN CLASS="MATH"><I>c</I></SPAN> of this object.
    *   
    * @return the shape parameter mu
    * 
    */
   public double getC() {
      return c;
   }


   protected void setParams (double a, double lambda, double c) {
      if (lambda <= 0.0)
         throw new IllegalArgumentException ("lambda <= 0");
      if (c <= 0.0)
         throw new IllegalArgumentException ("c <= 0");
      this.a = a;
      this.lambda = lambda;
      this.c = c;
   }
}
