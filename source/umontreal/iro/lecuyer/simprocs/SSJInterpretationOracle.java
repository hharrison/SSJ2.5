

/*
 * Class:        SSJInterpretationOracle
 * Description:  Determines which classes should be interpreted by the DSOL
                 interpreter during process simulation
 * Environment:  Java
 * Software:     SSJ 
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

import java.lang.reflect.Method;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/*
 *
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/">
 * www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public
 * License (GPL) </a>, no warranty <br>
 *
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm"> Peter
 *         Jacobs </a>
 * @version $Revision$ $Date$
 * @since 1.2
 */ 


/**
 * Determines which classes should be interpreted by the DSOL
 * interpreter during process simulation.
 * 
 */
public class SSJInterpretationOracle implements InterpreterOracleInterface {
   public boolean shouldBeInterpreted (final Method method) {
      Class declClass = method.getDeclaringClass();
      if (declClass == null)
         // Should not happen
         return true;
      Package pack = declClass.getPackage();
      String packName = pack == null ? null : pack.getName();
      if (packName != null) {
         if (packName.startsWith
                ("umontreal.iro.lecuyer.simevents"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.stat"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.util"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.rng"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.hups"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.gof"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.probdist"))
            return false;
         if (packName.startsWith
                ("umontreal.iro.lecuyer.randvar"))
            return false;
         if (packName.startsWith
                ("java"))
            return false;
         if (packName.startsWith
                ("nl.tudelft.simulation"))
            return false;
      }
      if (declClass.equals (System.class))
         return false;
      return true;
   }
}
