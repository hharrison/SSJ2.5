

/*
 * Class:        IIDMultivariateGen
 * Description:  vector of independent identically distributed random variables
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       David Munger
 * @since        January 2011

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

import umontreal.iro.lecuyer.randvar.RandomVariateGen;


/**
 * Extends {@link RandomMultivariateGen} for a vector of independent identically distributed
 * (i.i.d.) random variables.
 * 
 */
public class IIDMultivariateGen extends RandomMultivariateGen {


   /**
    * Constructs a generator for a <TT>d</TT>-dimensional vector of i.i.d. variates
    * with a common one-dimensional generator <TT>gen1</TT>.
    * 
    * @param gen1 the one-dimensional generator
    * 
    *    @param d dimension of the vector (number of i.i.d. variates).
    * 
    */
   public IIDMultivariateGen (RandomVariateGen gen1, int d)  {
      setGen1 (gen1);
      this.stream = gen1.getStream();
      dimension = d;
   } 


   /**
    * Changes the dimension of the vector to <TT>d</TT>.
    * 
    */
   public void setDimension (int d)  {
      dimension = d;
   } 


   /**
    * Generates a vector of i.i.d. variates.
    * 
    */
   public void nextPoint (double[] p)  {
      if (p.length != dimension)
         throw new IllegalArgumentException(String.format(
            "p's dimension (%d) does not mach dimension (%d)", p.length, dimension));

      for (int i = 0; i < dimension; i++)
         p[i] = gen1.nextDouble();
   } 


   /**
    * Sets the common one-dimensional generator to <TT>gen1</TT>.
    * 
    */
   public void setGen1 (RandomVariateGen gen1) {
      if (gen1 == null)
         throw new NullPointerException ("gen1 is null");
      this.gen1 = gen1;
   } 


   /**
    * Returns the common one-dimensional generator used in this class.
    * 
    */
   public RandomVariateGen getGen1() {
     return gen1;
   } 


   /**
    * Returns a string representation of the generator.
    * 
    */
   public String toString()  {
      return dimension + "-dimensional vector of i.i.d. " +
            gen1.getDistribution().toString();
   }
   

}
