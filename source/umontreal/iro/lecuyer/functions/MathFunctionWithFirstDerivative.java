

/*
 * Class:        MathFunctionWithFirstDerivative
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
 * derivative can be computed using
 * {@link #derivative(double) derivative}.
 * 
 */
public interface MathFunctionWithFirstDerivative extends MathFunction {


   /**
    * Computes (or estimates) the first derivative 
    *    of the function at point <TT>x</TT>.
    * 
    * @param x the point to evaluate the derivative to.
    * 
    *    @return the value of the derivative.
    */
   public double derivative (double x);
}
