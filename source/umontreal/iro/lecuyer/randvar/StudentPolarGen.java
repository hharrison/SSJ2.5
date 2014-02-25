

/*
 * Class:        StudentPolarGen
 * Description:  Student-t random variate generators using the polar method
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

package umontreal.iro.lecuyer.randvar;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;


/**
 * This class implements <EM>Student</EM> random variate generators using
 *  the <EM>polar</EM> method of.
 * The code is adapted from UNURAN.
 * 
 * <P>
 * The non-static <TT>nextDouble</TT> method generates two variates at a time
 * and the second one is saved for the next call.  
 * A pair of variates is generated every second call. 
 * In the static case, two variates are generated per
 * call but only the first one is returned and the second is discarded.
 * 
 */
public class StudentPolarGen extends StudentGen  {

    private boolean available = false;
    private double[] variates = new double[2];
    private static double[] staticVariates = new double[2];
    // Used by the polar method.


   /**
    * Creates a Student random variate generator with <SPAN CLASS="MATH"><I>n</I></SPAN>
    *   degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public StudentPolarGen (RandomStream s, int n)  {
      super (s, null);
      setN (n);
   }

 
   /**
    * Creates a new generator for the Student distribution <TT>dist</TT>
    *      and stream <TT>s</TT>.
    * 
    */
   public StudentPolarGen (RandomStream s, StudentDist dist)  {
      super (s, dist);
      if (dist != null)
         setN (dist.getN ());
   }
 

   public double nextDouble() {
       if (available) {
          available = false;
          return variates[1];
       }
       else {
          polar (stream, n, variates);
          available = true;
          return variates[0];
       }
   }

   public static double nextDouble (RandomStream s, int n) {
      polar (s, n, staticVariates);
      return staticVariates[0];
   }
   /**
    * Generates a new variate from the Student distribution
    *    with <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>n</TT> degrees of freedom, using stream <TT>s</TT>.
    * 
    */



//>>>>>>>>>>>>>>>>>>>>  P R I V A T E S    M E T H O D S   <<<<<<<<<<<<<<<<<<<<


/* ****************************************************************************
 *                                                                           *
 * Student's t Distribution: Polar Method                                    *
 *                                                                           *
 *****************************************************************************
 *                                                                           *
 * FUNCTION:   - samples a random number from Student's t distribution with  *
 *               parameters n > 0.                                          *
 *                                                                           *
 * REFERENCE:  - R.W. Bailey (1994): Polar generation of random variates     *
 *               with the t-distribution,                                    *
 *               Mathematics of Computation 62, 779-781.                     *
 *                                                                           *
 * Implemented by F. Niederl, 1994                                           *
 *****************************************************************************
 *                                                                           *
 * The polar method of Box/Muller for generating Normal variates is adapted  *
 * to the Student-t distribution. The two generated variates are not         *
 * independent and the expected no. of uniforms per variate is 2.5464.       *
 *                                                                           *
 *****************************************************************************
 *    WinRand (c) 1995 Ernst Stadlober, Institut fuer Statistitk, TU Graz    *
 *****************************************************************************
 * UNURAN (c) 2000  W. Hoermann & J. Leydold, Institut f. Statistik, WU Wien *
 ****************************************************************************/

   private static void polar (RandomStream stream, int n, double[] variates) {
      double u,v,w;
      do {
         u = 2. * stream.nextDouble() - 1.;
         v = 2. * stream.nextDouble() - 1.;
         w = u*u + v*v;
       } while (w > 1.);

      double temp = Math.sqrt (n*(Math.exp (-2./n*Math.log (w)) - 1.)/w);
      variates[0] = u*temp;
      variates[1] = v*temp;
   }


}
