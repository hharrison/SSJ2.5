

/*
 * Class:        BetaSymmetricalGen
 * Description:  random variate generators for the symmetrical beta distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
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
 * This class implements random variate generators with the 
 * <SPAN  CLASS="textit">symmetrical beta</SPAN> distribution with shape parameters  
 * <SPAN CLASS="MATH"><I>&#945;</I> = <I>&#946;</I></SPAN>,
 * over the interval  <SPAN CLASS="MATH">(0, 1)</SPAN>. 
 * 
 */
public class BetaSymmetricalGen extends BetaGen  {



   /**
    * Creates a new symmetrical beta generator with parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> 
    *   <TT>alpha</TT>, over the interval <SPAN CLASS="MATH">(0, 1)</SPAN>, using stream <TT>s</TT>.
    * 
    */
   public BetaSymmetricalGen (RandomStream s, double alpha)  {
      this (s, new BetaSymmetricalDist (alpha));
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using stream <TT>s</TT>.
    * 
    */
   public BetaSymmetricalGen (RandomStream s, BetaSymmetricalDist dist)  {
      super (s, dist);
   }


   public static double nextDouble (RandomStream s, double alpha) {
      return BetaSymmetricalDist.inverseF (alpha, s.nextDouble());
   }

}
