

/*
 * Class:        RandomMultivariateGen
 * Description:  base class for multidimensional random variate generators
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

package umontreal.iro.lecuyer.randvarmulti;

import umontreal.iro.lecuyer.probdistmulti.ContinuousDistributionMulti;
import umontreal.iro.lecuyer.rng.RandomStream;
import umontreal.iro.lecuyer.randvar.RandomVariateGen;



/**
 * This class is the multivariate counterpart of
 * {@link umontreal.iro.lecuyer.randvar.RandomVariateGen RandomVariateGen}.
 * It is the base class for general random variate generators over
 * the <SPAN CLASS="MATH"><I>d</I></SPAN>-dimensional real space <SPAN CLASS="MATH"><B>R</B><SUP>d</SUP></SPAN>.
 * It specifies the signature of the {@link #nextPoint nextPoint} method, which is
 * normally called to generate a random vector from a given distribution.
 * Contrary to univariate distributions and generators, here the inversion method
 * is not well defined, so we cannot construct a multivariate generator simply
 * by passing a multivariate distribution and a stream; we must specify a
 * generating method as well.  For this reason, this class is abstract.
 * Generators can be constructed only by invoking the constructor of a subclass.
 * This is an important difference with
 * {@link umontreal.iro.lecuyer.randvar.RandomVariateGen RandomVariateGen}.
 * 
 */
public abstract class RandomMultivariateGen  {
   protected int dimension;
   // Careful here: there is also a RandomStream inside gen1. But only one
   // of these two is used in a given class.
   protected RandomStream stream;  // stream used to generate random numbers
   protected RandomVariateGen gen1; // 1-dim generator used to generate random variates

// This constructor is needed for subclasses with no associated distribution.
//   protected RandomMultivariateGen() {}


   /**
    * Generates a random point <SPAN CLASS="MATH"><I>p</I></SPAN> using the
    *   the stream contained in this object.
    * 
    */
   abstract public void nextPoint (double[] p);


   /**
    * Generates <SPAN CLASS="MATH"><I>n</I></SPAN> random points. These points are stored in
    *    the array <TT>v</TT>, starting at index <TT>start</TT>. Thus
    *    <TT>v[start][i]</TT> contains
    *    coordinate <SPAN CLASS="MATH"><I>i</I></SPAN> of the first generated point.
    *    By default, this method calls {@link #nextPoint nextPoint} <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    times, but one can override it in subclasses for better efficiency.
    *    The array argument <TT>v[][d]</TT> must have <SPAN CLASS="MATH"><I>d</I></SPAN> elements reserved
    *    for each generated point before calling this method.
    *  
    * @param v array in which the variates will be stored
    * 
    *    @param start starting index, in <TT>v</TT>, of the new variates
    * 
    *    @param n number of variates to generate
    * 
    * 
    */
   public void nextArrayOfPoints (double[][] v, int start, int n)  {
      if (n <= 0)
         throw new IllegalArgumentException ("n must be positive.");
      for (int i = 0; i < n; i++)
         nextPoint(v[start + i]);
   }


   /**
    * Returns the dimension of this multivariate generator
    *   (the dimension of the random points).
    * 
    */
   public int getDimension() {
      return dimension;
   }


   /**
    * Returns the {@link RandomStream} used by this object.
    *  
    * @return the stream associated to this object
    * 
    */
   public RandomStream getStream()  {
      if (null != gen1)
         return gen1.getStream();
      return stream;
   }


   /**
    * Sets the {@link RandomStream} used by this object to <TT>stream</TT>.
    * 
    */
   public void setStream (RandomStream stream) {
      if (null != gen1)
         gen1.setStream(stream);
      else
         this.stream = stream;
   }

}
