

/*
 * Interface:    PointSetRandomization
 * Description:  Used to randomize a PointSet
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
 * This interface is used to randomize a
 * {@link umontreal.iro.lecuyer.hups.PointSet PointSet}. One can
 * implement method {@link #randomize(PointSet) randomize} in any way. This
 * method must use an internal
 * {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}. This
 * stream can be set in the constructor, but the methods
 * {@link #getStream getStream} and {@link #setStream(RandomStream) setStream} must be
 * implemented.
 * 
 * <P>
 * The method {@link #randomize(PointSet) randomize} must be implemented using
 * combinations of the randomization methods from the point set such
 * as
 * {@link umontreal.iro.lecuyer.hups.PointSet#addRandomShift addRandomShift},
 * {@link umontreal.iro.lecuyer.hups.DigitalNet#leftMatrixScramble leftMatrixScramble},
 * {@link umontreal.iro.lecuyer.hups.DigitalNet#stripedMatrixScramble stripedMatrixScramble},
 *  ...
 * 
 * <P>
 * If more than one {@link PointSetRandomization} is applied to the
 * same point set, the randomizations will concatenate if they are of
 * different types, but only the last of each type will remain.
 * 
 */
public interface PointSetRandomization  {



   /**
    * This method must randomize <TT>p</TT>.
    * 
    * @param p Point set to randomize
    * 
    * 
    */
   public void randomize (PointSet p);


   /**
    * Sets the internal
    *    {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} to
    *    <TT>stream</TT>.
    * 
    * @param stream stream to use in the randomization
    * 
    * 
    */
   public void setStream (RandomStream stream);


   /**
    * Returns the internal
    *    {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}.
    * 
    * @return stream used in the randomization
    * 
    */
   public RandomStream getStream();

}
