

/*
 * Class:        GlobalCPUTimeChrono
 * Description:  Compute the global CPU time used by the Java Virtual Machine
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

package umontreal.iro.lecuyer.util;


/**
 * Extends the {@link AbstractChrono}  class to compute the global CPU time used
 * by the Java Virtual Machine. This includes CPU time taken by any thread,
 * including the garbage collector, class loader, etc.
 * 
 * <P>
 * Part of this class is implemented in the C language and the 
 * implementation is unfortunately operating system-dependent.
 * The C functions for the current class have been compiled on a 32-bit machine
 * running Linux. For a <EM>platform-independent</EM> CPU timer (valid only with Java-1.5 or later),
 *  one should use the class {@link ThreadCPUTimeChrono} which is programmed 
 * directly in Java.
 * 
 */
public class GlobalCPUTimeChrono extends AbstractChrono {
   
   private static boolean ssjUtilLoaded = false;
   private static native void Heure (long[] tab);

   protected void getTime (long[] tab) {
      if (!ssjUtilLoaded) {
         // We cannot load the library in a static block
         // since the subclass ChronoSingleThread does not
         // need the native method.
         System.loadLibrary ("ssjutil");

         ssjUtilLoaded = true;
      }
      
      Heure (tab);
   }



   /**
    * Constructs a <TT>Chrono</TT> object and initializes it to zero.
    * 
    */
   public GlobalCPUTimeChrono() {
      init();
   }

}
