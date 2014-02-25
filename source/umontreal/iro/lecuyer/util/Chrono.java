

/*
 * Class:        Chrono
 * Description:  computes the CPU time for the current thread only
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Éric Buist
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

package umontreal.iro.lecuyer.util;


/**
 * The {@link Chrono} class extends the 
 * {@link umontreal.iro.lecuyer.util.AbstractChrono AbstractChrono} 
 * class and computes the CPU time for the current thread only.
 * This is the simplest way to use chronos. Classes <TT>AbstractChrono</TT>,
 * {@link umontreal.iro.lecuyer.util.SystemTimeChrono SystemTimeChrono}, 
 * {@link umontreal.iro.lecuyer.util.GlobalCPUTimeChrono GlobalCPUTimeChrono} and 
 * {@link umontreal.iro.lecuyer.util.ThreadCPUTimeChrono ThreadCPUTimeChrono} 
 * provide different chronos implementations.
 * See these classes to learn more about SSJ chronos, if problems appear with
 * class <TT>Chrono</TT>.
 * 
 */
public class Chrono extends AbstractChrono  {
   private ThreadCPUTimeChrono chrono = new ThreadCPUTimeChrono();

   protected void getTime (long[] tab) {
         chrono.getTime(tab);
   }


   /**
    * Constructs a <TT>Chrono</TT> object and
    *     initializes it to zero.
    * 
    */
   public Chrono() {
      chrono.init();
      init();
   }


   /**
    * Creates a <TT>Chrono</TT> instance adapted for a program
    *    using a single thread.  Under Java 1.5, this method returns
    *    an instance of {@link ChronoSingleThread} which can
    *    measure CPU time for one thread.  Under Java versions prior to
    *    1.5, this returns an instance of this class.
    *    This method must not be used to create a timer for a
    *    multi-threaded program, because the obtained CPU times
    *    will differ depending on the used Java version.
    * 
    * @return the constructed timer.
    * 
    */
   public static Chrono createForSingleThread () {
         return new Chrono();
   }


}
