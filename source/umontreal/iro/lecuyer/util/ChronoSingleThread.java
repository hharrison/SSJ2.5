

/*
 * Class:        ChronoSingleThread
 * Description:  deprecated
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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;



@Deprecated
/**
 * <SPAN  CLASS="textbf">This class is deprecated</SPAN> but kept for
 * compatibility with older versions of SSJ.
 * {@link Chrono} should be used instead of {@link ChronoSingleThread}.
 * The {@link ChronoSingleThread} class extends the {@link AbstractChrono} 
 * class and computes the CPU time for the current thread only.
 * This is the simplest way to use chronos. Classes {@link AbstractChrono},
 * {@link SystemTimeChrono}, {@link GlobalCPUTimeChrono} and 
 * {@link ThreadCPUTimeChrono} provide different chronos implementations
 * (see these classes to learn more about SSJ chronos).
 * 
 */
public class ChronoSingleThread  extends AbstractChrono {

   private ThreadCPUTimeChrono chrono = new ThreadCPUTimeChrono();

   protected void getTime (long[] tab) {
         chrono.getTime(tab);
   }


   /**
    * Constructs a <TT>ChronoSingleThread</TT> object and
    *     initializes it to zero.
    * 
    */
   public ChronoSingleThread() {
      chrono.init();
      init();
   }


}
