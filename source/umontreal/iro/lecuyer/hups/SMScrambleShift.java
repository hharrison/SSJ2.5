

/*
 * Class:        SMScrambleShift
 * Description:  Performs a striped matrix scrambling with a digital shift
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
 * that performs a striped matrix scrambling and adds a random
 * digital shift. Point set must be a
 * {@link umontreal.iro.lecuyer.hups.DigitalNet DigitalNet} or an
 * {@link java.lang.IllegalArgumentException IllegalArgumentException} is thrown.
 * 
 */
public class SMScrambleShift extends RandomShift  {



   /**
    * Empty constructor.
    * 
    */
   public SMScrambleShift()  {
   }
   


   /**
    * Sets internal variable <TT>stream</TT> to the given
    *    <TT>stream</TT>.
    * 
    * @param stream stream to use in the randomization
    * 
    */
   public SMScrambleShift (RandomStream stream)  {
       super(stream);
   }
   


   /**
    * This method calls
    *    {@link umontreal.iro.lecuyer.hups.DigitalNet#stripedMatrixScramble(RandomStream) stripedMatrixScramble},
    *    then
    *    {@link umontreal.iro.lecuyer.hups.DigitalNet#addRandomShift(RandomStream) addRandomShift}.
    *    If <TT>p</TT> is not a
    *    {@link umontreal.iro.lecuyer.hups.DigitalNet DigitalNet}, an
    * {@link java.lang.IllegalArgumentException IllegalArgumentException} is thrown.
    * 
    * @param p Point set to randomize
    * 
    * 
    */
   public void randomize (PointSet p)  {
      if(p instanceof DigitalNet){
         ((DigitalNet)p).stripedMatrixScramble (stream);
         ((DigitalNet)p).addRandomShift (stream);
      }else{
         throw new IllegalArgumentException("SMScrambleShift"+
                                            " can only randomize a DigitalNet");
      }
   }
   

}
