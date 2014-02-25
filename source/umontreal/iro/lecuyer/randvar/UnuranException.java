

/*
 * Class:        UnuranException
 * Description:  unchecked exception for errors inside the UNURAN package
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

/**
 * This type of unchecked exception is thrown when an error occurs
 * <SPAN  CLASS="textit">inside</SPAN> the UNURAN package.  
 * Usually, such an exception will come from the native side.
 * 
 */
public class UnuranException extends RuntimeException {

   /**
    * Constructs a new generic UNURAN exception.
    * 
    */
   public UnuranException() {
      super();
   }


   /**
    * Constructs a UNURAN exception with the error
    *    message <TT>message</TT>
    * 
    * @param message error message describing the problem that occurred
    * 
    */
   public UnuranException (String message) {
      super (message);
   }
}
