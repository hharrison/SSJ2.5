

/*
 * Class:        AntitheticPointSet
 * Description:  
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

import umontreal.iro.lecuyer.util.PrintfFormat;


/**
 * This container class provides antithetic points.
 * That is, 
 * <SPAN CLASS="MATH">1 - <I>u</I><SUB>i, j</SUB></SPAN> is returned in place of coordinate <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN>.
 * To generate regular and antithetic variates with a point
 * set <TT>p</TT>, e.g., for variance reduction, one can define an
 * {@link AntitheticPointSet} object <TT>pa</TT> that contains <TT>p</TT>,
 * and then generate the regular variates with <TT>p</TT> and the
 * antithetic variates with <TT>pa</TT>.
 * 
 */
public class AntitheticPointSet extends ContainerPointSet  {



   /**
    * Constructs an antithetic point set from the given point set <TT>P</TT>.
    *  
    * @param P point set for which we want antithetic version
    * 
    * 
    */
   public AntitheticPointSet (PointSet P)  {
      init (P);
   }



   public double getCoordinate (int i, int j) {
      return 1.0 - P.getCoordinate (i, j);
   }

   public PointSetIterator iterator(){
      return new AntitheticPointSetIterator();
   }

   public String toString() {
      return "Antithetic point set of: {" + PrintfFormat.NEWLINE +
              P.toString() + PrintfFormat.NEWLINE + "}";
   }


   // ***************************************************************

   protected class AntitheticPointSetIterator
                   extends ContainerPointSetIterator {

      public double nextCoordinate() {
         return 1.0 - innerIterator.nextCoordinate();
      }

      public double nextDouble() {
         return 1.0 - innerIterator.nextCoordinate();
      }

      public void nextCoordinates (double p[], int d)  {
         innerIterator.nextCoordinates (p, d);
         for (int j = 0; j < d; j++)
            p[j] = 1.0 - p[j];
      }

      public int nextPoint (double p[], int d)  {
         innerIterator.nextPoint (p, d);
         for (int j = 0; j < d; j++)
            p[j] = 1.0 - p[j];
         return getCurPointIndex();
      }

   }
}
