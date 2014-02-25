

/*
 * Class:        UserRecord
 * Description:  A record object to store information related to the request
                 of a process for a Resource or for Bin tokens, or when a 
                 process waits for a Condition.
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

package umontreal.iro.lecuyer.simprocs;

import umontreal.iro.lecuyer.simprocs.SimProcess;


/**
 * This class represents a record object to store 
 * information related to the request of a process for a
 * {@link Resource} or for {@link Bin} tokens, 
 * or when a process waits for a {@link Condition}.
 * A {@link UserRecord} is created for each process request.
 * The record contains the number of units requested 
 * or used, the associated process, and the
 * simulation time when the request was made.
 * Lists of processes waiting for a {@link Resource},
 * {@link Bin}, or {@link Condition}, for example, contain
 * {@link UserRecord} objects.
 * 
 */
public class UserRecord  {
   // Nb. of units taken for this record.
   protected int numUnits;

   // Process associated to the record
   protected SimProcess process;

   // Priority of this process.
   // protected double priority;

   // Time of the record creation
   protected double requestTime;

   // Constructor.
   // We do not want the user to construct such objects.
   protected UserRecord (int n, SimProcess p, double requestTime) {
      numUnits = n;
      process = p;
      this.requestTime = requestTime;
   }

   /**
    * Returns the number of units requested or used
    *    by the associated process.
    * 
    * @return the number of requested or used units
    * 
    */
   public int getNumUnits() {
      return numUnits;
   }


   /**
    * Returns the process object associated with this record.
    * 
    * @return the process associated to this record
    * 
    */
   public SimProcess getProcess() {
      return process;
   }


   /**
    * Returns the time of creation of this record.  
    * 
    * @return the simulation time of the record creation
    */
   public double getRequestTime() {
      return requestTime;
   }
}
