

/*
 * Class:        ObservationListener
 * Description:  Observation listener
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

package umontreal.iro.lecuyer.stat;


/**
 * Represents an object that can listen to observations broadcast
 * by statistical probes.
 * 
 */
public interface ObservationListener {

   /**
    * Receives the new observation <TT>x</TT> broadcast by <TT>probe</TT>.
    * 
    * @param probe the statistical probe broadcasting the observation.
    * 
    *    @param x the observation being broadcast.
    * 
    * 
    */
   public void newObservation (StatProbe probe, double x);

}
