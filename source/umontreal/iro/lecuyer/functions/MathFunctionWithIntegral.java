

/*
 * Class:        MathFunctionWithIntegral
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Éric Buist
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

package umontreal.iro.lecuyer.functions;



/**
 * Represents a mathematical function whose
 * integral can be computed by the
 * {@link #integral(double, double) integral} method.
 * 
 */
public interface MathFunctionWithIntegral extends MathFunction {


   /**
    * Computes (or estimates) the integral of the 
    *    function over the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    * 
    * @param a the starting point of the interval.
    * 
    *    @param b the ending point of the interval.
    * 
    *    @return the value of the integral.
    */
   public double integral (double a, double b);
}
