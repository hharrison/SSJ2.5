

/*
 * Interface:    Randomization
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
import umontreal.iro.lecuyer.rng.*;


@Deprecated
/**
 * Each type of <SPAN  CLASS="textit">randomization</SPAN> that can be applied to a general 
 * point set should be defined in a class that implements this interface.
 * 
 */
public interface Randomization  {



   /**
    * Applies this randomization to the point set using 
    *    stream <TT>stream</TT>.
    * 
    * @param stream random number stream used to generate needed uniforms
    * 
    */
   public void apply (RandomStream stream) ;
     // public double randCoordintate(int i, int j);
}
