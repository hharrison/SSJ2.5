

/*
 * Class:        ExponentialInverseFromDensityGen
 * Description:  exponential random variate generators using numerical
                 inversion of the exponential density
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
 * This class implements <SPAN  CLASS="textit">exponential</SPAN> random variate generators
 *  using numerical inversion of the <SPAN  CLASS="textit">exponential</SPAN> density
 * as described in. It makes use of the class
 * {@link umontreal.iro.lecuyer.probdist.InverseDistFromDensity InverseDistFromDensity}.
 * Generating exponential random variables by inversion usually requires
 * the computation of a logarithm for each generated random number.
 * Numerical inversion precomputes a set of tables that will speed up the
 * generation of random variables. This is useful if one 
 * wants to generate a large number of random variables.
 * 
 */
public class ExponentialInverseFromDensityGen extends ExponentialGen  {



   /**
    * Creates an exponential random variate generator with
    *  parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN> = <TT>lambda</TT>, using stream <TT>stream</TT>.
    * It uses numerical inversion with precomputed tables.
    * The <SPAN CLASS="MATH"><I>u</I></SPAN>-resolution <TT>ueps</TT> is the desired absolute error in the 
    * <TT>cdf</TT>, and <TT>order</TT> is the degree of the Newton interpolating 
    * polynomial over each interval.
    * 
    */
   public ExponentialInverseFromDensityGen (RandomStream stream,
                                            double lambda,
                                            double ueps, int order)  {
      // dist is the exponential distribution
      super (stream, lambda);
      double xc = Math.min(1.0, 0.5/lambda);

      // Member (ExponentialDist) dist is replaced by 
      // (InverseDistFromDensity) dist
      dist = new InverseDistFromDensity ((ContinuousDistribution) dist,
                                         xc, ueps, order);
    }


   /**
    * Similar to the above constructor, with the exponential 
    *    distribution <TT>dist</TT>.
    * 
    */
   public ExponentialInverseFromDensityGen (RandomStream stream, 
                                            ExponentialDist dist,
                                            double ueps, int order)  {
      super (stream, dist);
      double xc = Math.min(1.0, 0.5/lambda);

      // Member (ExponentialDist) dist is replaced by 
      // (InverseDistFromDensity) dist
      this.dist = new InverseDistFromDensity (dist, xc, ueps, order);
   } 


   /**
    * Creates a new exponential generator using the <SPAN  CLASS="textit">exponential</SPAN>
    *    distribution <TT>dist</TT> and stream <TT>stream</TT>. <TT>dist</TT>
    *    may be obtained by calling method {@link #getDistribution(()) getDistribution},
    *    after using one of the other constructors to create the 
    *    precomputed tables. This is useful when one needs many   generators 
    *  using the same exponential distribution
    * (same <SPAN CLASS="MATH"><I>&#955;</I></SPAN>). Precomputing tables for numerical inversion is
    *  costly; thus using only one set of tables for many generators 
    * is more efficient. The first {@link ExponentialInverseFromDensityGen} generator 
    *  using the other constructors creates the precomputed tables.
    * Then all other streams use this constructor with the same set of tables.
    * 
    */
   public ExponentialInverseFromDensityGen (RandomStream stream, 
                                            InverseDistFromDensity dist)  {
      super (stream, null);
      lambda = -1;   // don't know its explicit value; it is inside dist
      this.dist = dist;
   } 


   /**
    * Returns the <SPAN CLASS="MATH"><I>u</I></SPAN>-resolution <TT>ueps</TT>.
    * 
    */
   public double getUepsilon() {
      return ((InverseDistFromDensity)dist).getEpsilon();
   }



   /**
    * Returns the order of the interpolating polynomial.
    * 
    */
   public int getOrder() {
      return ((InverseDistFromDensity)dist).getOrder();
   }


}