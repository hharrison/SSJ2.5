

/*
 * Class:        RandomShift
 * Description:  Applies a random shift on a point set
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


/**
 * This class implements a
 * {@link umontreal.iro.lecuyer.hups.PointSetRandomization PointSetRandomization}.
 * The {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} is
 * stored internally. The method {@link #randomize(PointSet) randomize} simply
 * calls
 * {@link umontreal.iro.lecuyer.hups.PointSet#addRandomShift(RandomStream) addRandomShift}<TT>(stream)</TT>.
 * 
 * <P>
 * This class can be used as a base class to implement a specific
 * randomization by overriding method {@link #randomize(PointSet) randomize}.
 * 
 */
public class RandomShift implements PointSetRandomization {
   protected RandomStream stream;



   /**
    * Empty constructor.
    * 
    */
   public RandomShift()  {
   }
   


   /**
    * Sets the internal
    *   {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} to  <TT>stream</TT>.
    * 
    * @param stream stream to use in the randomization
    * 
    */
   public RandomShift (RandomStream stream)  {
       this.stream = stream;
   }
   


   /**
    * This method calls
    *    {@link umontreal.iro.lecuyer.hups.PointSet#addRandomShift(RandomStream) addRandomShift}&nbsp;<TT>(stream)</TT>.
    * 
    * @param p Point set to randomize
    * 
    * 
    */
   public void randomize (PointSet p)  {
      p.addRandomShift(stream);
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
