

/*
 * Class:        NormalInverseFromDensityGen
 * Description:  random variate generators using numerical inversion of
                 the normal density
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
 * This class implements <SPAN  CLASS="textit">normal</SPAN> random variate generators
 *  using numerical inversion of the <SPAN  CLASS="textit">normal</SPAN> density
 *  as described in.  It makes use of the class
 * {@link umontreal.iro.lecuyer.probdist.InverseDistFromDensity InverseDistFromDensity}.
 * A set of tables are precomputed to speed up the generation of normal random 
 * variables by numerical inversion. This will be useful if one 
 * wants to generate a large number of random variables.
 * 
 */
public class NormalInverseFromDensityGen extends NormalGen  {



   /**
    * Creates a normal random variate generator with parameters 
    * <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN> <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> = <TT>sigma</TT>, using stream <TT>stream</TT>.
    * It uses numerical inversion with precomputed tables.
    * The <SPAN CLASS="MATH"><I>u</I></SPAN>-resolution <TT>ueps</TT> is the desired absolute error in the 
    * <TT>cdf</TT>, and <TT>order</TT> is the degree of the Newton interpolating 
    * polynomial over each interval.
    * 
    */
   public NormalInverseFromDensityGen (RandomStream stream, double mu, 
                                       double sigma, double ueps, int order)  {
      // dist is the normal distribution
      super (stream, mu, sigma);
      double xc = mu;

      // member (NormalDist) dist is replaced by 
      // (InverseDistFromDensity) dist
      dist = new InverseDistFromDensity ((ContinuousDistribution) dist,
                                         xc, ueps, order);
    }


   /**
    * Similar to the first constructor, with the normal 
    *    distribution <TT>dist</TT>.
    * 
    */
   public NormalInverseFromDensityGen (RandomStream stream, NormalDist dist,
                                       double ueps, int order)  {
      super (stream, dist);
      double xc = mu;

      // member (NormalDist) dist is replaced by 
      // (InverseDistFromDensity) dist
      this.dist = new InverseDistFromDensity (dist, xc, ueps, order);
   } 


   /**
    * Creates a new normal generator using the <SPAN  CLASS="textit">normal</SPAN>
    *    distribution <TT>dist</TT> and stream <TT>stream</TT>. <TT>dist</TT>
    *    may be obtained by calling method {@link #getDistribution(()) getDistribution},
    *    after using one of the other constructors to create the 
    *    precomputed tables. This is useful when one needs many   generators
    *  using the same normal distribution.
    *  Precomputing tables for numerical inversion is
    *  costly; thus using only one set of tables for many generators 
    * is more efficient. The first {@link NormalInverseFromDensityGen} generator 
    *  using the other constructors creates the precomputed tables.
    * Then all other streams use this constructor with the same set of tables.
    * 
    */
   public NormalInverseFromDensityGen (RandomStream stream, 
                                       InverseDistFromDensity dist)  {
      super (stream, null);
      mu = dist.getXc();
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