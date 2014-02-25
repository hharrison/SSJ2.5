

/*
 * Class:        RandomStart
 * Description:  Randomizes a sequence with a random starting point
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

package umontreal.iro.lecuyer.hups;

 import umontreal.iro.lecuyer.rng.RandomStream;
 import java.lang.IllegalArgumentException;


/**
 * This class implements a
 * {@link umontreal.iro.lecuyer.hups.PointSetRandomization PointSetRandomization}
 * that randomizes a sequence with a random starting point.
 * The point set must be an instance of 
 * {@link umontreal.iro.lecuyer.hups.HaltonSequence HaltonSequence} or an
 * {@link java.lang.IllegalArgumentException IllegalArgumentException} is thrown.
 * For now, only the Halton sequence is allowed, but there may be others
 * later.
 * 
 */
public class RandomStart implements PointSetRandomization  {

   protected RandomStream stream;



   /**
    * Empty constructor.
    * 
    */
   public RandomStart()  {
   }
   


   /**
    * Sets internal variable <TT>stream</TT> to the given
    *    <TT>stream</TT>.
    * 
    * @param stream stream to use in the randomization
    * 
    */
   public RandomStart (RandomStream stream)  {
       this.stream = stream;
   }
   


   /**
    * This method calls
    *    {@link umontreal.iro.lecuyer.hups.HaltonSequence#init(double[]) init}.
    *    If <TT>p</TT> is not a
    *    {@link umontreal.iro.lecuyer.hups.HaltonSequence HaltonSequence}, an
    * {@link java.lang.IllegalArgumentException IllegalArgumentException} is thrown.
    * 
    * @param p Point set to randomize
    * 
    * 
    */
   public void randomize (PointSet p)  {
      if (p instanceof HaltonSequence) {
         double[] x0 = new double[p.getDimension()];
         stream.nextArrayOfDouble(x0, 0, x0.length);
         ((HaltonSequence)p).setStart (x0);
      } else {
         throw new IllegalArgumentException("RandomStart" +
                     " can only randomize a HaltonSequence");
      }
   }
   


   /**
    * Sets the internal
    *    {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} to
    *    <TT>stream</TT>.
    * 
    * @param stream stream to use in the randomization
    * 
    * 
    */
   public void setStream (RandomStream stream)  {
      this.stream = stream;
   } 


   /**
    * Returns the internal
    *    {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}.
    * 
    * @return stream used in the randomization
    * 
    */
   public RandomStream getStream()  {
      return stream;
   } 

}
