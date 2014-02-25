

/*
 * Class:        IdentityMathFunction
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
 * Represents the identity function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) = <I>x</I></SPAN>.
 * 
 */
public class IdentityMathFunction implements MathFunction

,
      MathFunctionWithFirstDerivative, MathFunctionWithDerivative,
      MathFunctionWithIntegral {
   public double evaluate (double x) {
      return x;
   }
   
   public double derivative (double x) {
      return 1;
   }

   public double derivative (double x, int n) {
      return n > 1 ? 0 : 1;
   }

   public double integral (double a, double b) {
      return (b*b - a*a) / 2;
   }
}
