

/*
 * Class:        ShiftedMathFunction
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
 * Represents a function computing 
 * <SPAN CLASS="MATH"><I>f</I> (<I>x</I>) - <I>&#948;</I></SPAN> for a user-defined function
 * <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> and shift <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
 * 
 */
public class ShiftedMathFunction implements MathFunction

,
      MathFunctionWithFirstDerivative, MathFunctionWithDerivative,
      MathFunctionWithIntegral {
   MathFunction func;
   double delta;


   /**
    * Constructs a new function shifting the function <TT>func</TT> by
    *  a shift <TT>delta</TT>.
    * 
    * @param func the function.
    * 
    *    @param delta the shift.
    * 
    * 
    */
   public ShiftedMathFunction (MathFunction func, double delta) {
      if (func == null)
         throw new NullPointerException ();
      this.func = func;
      this.delta = delta;
   }


   /**
    * Returns the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    * @return the function.
    * 
    */
   public MathFunction getFunction () {
      return func;
   }


   /**
    * Returns the shift <SPAN CLASS="MATH"><I>&#948;</I></SPAN> = <TT>delta</TT>.
    * 
    * @return the shift.
    * 
    */
   public double getDelta () {
      return delta;
   }


   public double evaluate (double x) {
      return func.evaluate (x) - delta;
   }

   public double derivative (double x) {
      return MathFunctionUtil.derivative (func, x);
   }

   public double derivative (double x, int n) {
      return MathFunctionUtil.derivative (func, x, n);
   }

   public double integral (double a, double b) {
      return MathFunctionUtil.integral (func, a, b) - (b - a)*getDelta();
   }
}
